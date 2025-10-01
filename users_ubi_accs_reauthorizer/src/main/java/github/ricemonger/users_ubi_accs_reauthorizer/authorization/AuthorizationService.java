package github.ricemonger.users_ubi_accs_reauthorizer.authorization;

import github.ricemonger.users_ubi_accs_reauthorizer.services.CommonValuesService;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.exceptions.client.UbiUserAuthorizationClientErrorException;
import github.ricemonger.utils.exceptions.server.UbiUserAuthorizationServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final static String AUTH_TICKET_PREFIX = "ubi_v1 t=";

    private final CommonValuesService commonValuesService;

    private final AesPasswordEncoder AESPasswordEncoder;

    public AuthorizationDTO reauthorizeAndGet2FaAuthorizedDTOWithAuthorizeTicket(String ticket) throws UbiUserAuthorizationClientErrorException,
            UbiUserAuthorizationServerErrorException {
        String ticketWithPrefix = ticket;
        if (!ticketWithPrefix.startsWith(AUTH_TICKET_PREFIX)) {
            ticketWithPrefix = AUTH_TICKET_PREFIX + ticket;
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(commonValuesService.getAuthorizationUrl())
                .defaultHeader("Content-Type", commonValuesService.getContentType())
                .defaultHeader("User-Agent", commonValuesService.getUserAgent())
                .defaultHeader("Authorization", ticketWithPrefix)
                .defaultHeader("Ubi-Appid", commonValuesService.getUbiTwoFaAppId())
                .build();

        AuthorizationDTO dto = webClient
                .post()
                .bodyValue(new TwoFaAuthorizationBodyValue(true, new TwoFaAuthorizationBodyValueTrustedDevice(commonValuesService.getTrustedDeviceId(), commonValuesService.getTrustedDeviceFriendlyName())))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(new Function<String, Throwable>() {
                            @Override
                            public Throwable apply(String s) {
                                log.error("Client error during ubi 2fa reauthorization for ticket {} : {}", ticket, s);
                                return new UbiUserAuthorizationClientErrorException(s);
                            }
                        }))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(new Function<String, Throwable>() {
                            @Override
                            public Throwable apply(String s) {
                                log.error("Server error during ubi 2fa reauthorization for ticket {} : {}", ticket, s);
                                return new UbiUserAuthorizationServerErrorException(s);
                            }
                        }))
                .bodyToMono(AuthorizationDTO.class)
                .block();

        if (dto != null) {
            dto.setTicket(AUTH_TICKET_PREFIX + dto.getTicket());
        }

        return dto;
    }

    public AuthorizationDTO reauthorizeAndGet2FaAuthorizedDtoForEncodedPasswordWithRememberDeviceTicket(String email, String encodedPassword,
                                                                                                        String rememberDeviceTicket) throws UbiUserAuthorizationClientErrorException,
            UbiUserAuthorizationServerErrorException {

        WebClient webClient = WebClient.builder()
                .baseUrl(commonValuesService.getAuthorizationUrl())
                .defaultHeader("Content-Type", commonValuesService.getContentType())
                .defaultHeader("Authorization", createBasicTokenForCredentials(email, AESPasswordEncoder.decode(encodedPassword)))
                .defaultHeader("Ubi-Appid", commonValuesService.getUbiTwoFaAppId())
                .defaultHeader("Ubi-RememberDeviceTicket", rememberDeviceTicket)
                .build();

        AuthorizationDTO dto = webClient
                .post()
                .bodyValue(new TwoFaAuthorizationBodyValue(true, new TwoFaAuthorizationBodyValueTrustedDevice(commonValuesService.getTrustedDeviceId(), commonValuesService.getTrustedDeviceFriendlyName())))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(new Function<String, Throwable>() {
                            @Override
                            public Throwable apply(String s) {
                                log.error("Client error during ubi 2fa reauthorization for email {}, rememberDeviceTicket: {} : {}", email,
                                        rememberDeviceTicket, s);
                                return new UbiUserAuthorizationClientErrorException(s);
                            }
                        }))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(new Function<String, Throwable>() {
                            @Override
                            public Throwable apply(String s) {
                                log.error("Server error during ubi 2fa reauthorization for email {}, rememberDeviceTicket: {} : {}", email, rememberDeviceTicket, s);
                                return new UbiUserAuthorizationServerErrorException(s);
                            }
                        }))
                .bodyToMono(AuthorizationDTO.class)
                .block();

        if (dto != null) {
            dto.setTicket(AUTH_TICKET_PREFIX + dto.getTicket());
        }
        return dto;
    }

    private String createBasicTokenForCredentials(String email, String password) {

        String token = Base64.getEncoder().encodeToString(String.format("%s:%s", email, password).getBytes());

        return "Basic " + token;
    }

    private record TwoFaAuthorizationBodyValue(boolean rememberMe, TwoFaAuthorizationBodyValueTrustedDevice trustedDevice) {
    }

    private record TwoFaAuthorizationBodyValueTrustedDevice(String id, String friendlyName) {
    }
}
