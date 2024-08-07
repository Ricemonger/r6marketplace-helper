package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.authorization.AuthorizationService;
import github.ricemonger.marketplace.services.abstractions.TelegramUserUbiAccountEntryDatabaseService;
import github.ricemonger.utils.dtos.AuthorizationDTO;
import github.ricemonger.utils.dtos.UbiAccountEntry;
import github.ricemonger.utils.dtos.UbiAccountWithTelegram;
import github.ricemonger.utils.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramUserUbiAccountEntryService {

    private final AuthorizationService authorizationService;

    private final TelegramUserUbiAccountEntryDatabaseService telegramUserUbiAccountEntryDatabaseService;

    public void authorizeAndSaveUser(String chatId, String email, String password) throws
            TelegramUserDoesntExistException,
            UserAlreadyHasAnotherUbiAccountEntryException,
            UbiUserAuthorizationClientErrorException,
            UbiUserAuthorizationServerErrorException {
        AuthorizationDTO userAuthorizationDTO = authorizationService.authorizeAndGetDTO(email, password);

        telegramUserUbiAccountEntryDatabaseService.save(chatId, buildUbiAccount(email, authorizationService.getEncodedPassword(password), userAuthorizationDTO));
    }

    public void deleteByChatId(String chatId) throws TelegramUserDoesntExistException {
        telegramUserUbiAccountEntryDatabaseService.deleteByChatId(chatId);
    }

    public List<UbiAccountWithTelegram> reauthorizeAllUbiUsersAndGetUnauthorizedList() {
        List<UbiAccountWithTelegram> users = new ArrayList<>(telegramUserUbiAccountEntryDatabaseService.findAll());

        List<UbiAccountWithTelegram> unauthorizedUsers = new ArrayList<>();

        for (UbiAccountWithTelegram user : users) {
            try {
                reauthorizeAndSaveUser(user.getChatId(), user.getEmail(), user.getEncodedPassword());
            } catch (UbiUserAuthorizationClientErrorException | UbiUserAuthorizationServerErrorException e) {
                unauthorizedUsers.add(user);
            }
        }

        return unauthorizedUsers;
    }

    public UbiAccountEntry findByChatId(String chatId) throws TelegramUserDoesntExistException, UbiAccountEntryDoesntExistException {
        return telegramUserUbiAccountEntryDatabaseService.findByChatId(chatId);
    }

    private void reauthorizeAndSaveUser(String chatId, String email, String encodedPassword) throws UbiUserAuthorizationClientErrorException, UbiUserAuthorizationServerErrorException {
        AuthorizationDTO dto = authorizationService.authorizeAndGetDtoForEncodedPassword(email, encodedPassword);

        try {
            telegramUserUbiAccountEntryDatabaseService.save(chatId, buildUbiAccount(email, encodedPassword, dto));
        } catch (TelegramUserDoesntExistException e) {
            log.error("Telegram user with chatId {} doesn't exist, but reauthorize ubi user was called fir him with authorizationDto-{}", chatId, dto);
        } catch (UserAlreadyHasAnotherUbiAccountEntryException e) {
            log.error("User with chatId {} already has another Ubi account, but reauthorize ubi user was called for him with authorizationDto-{}", chatId, dto);
        }
    }

    private UbiAccountEntry buildUbiAccount(String email, String encodedPassword, AuthorizationDTO authorizationDTO) {
        UbiAccountEntry user = new UbiAccountEntry();
        user.setEmail(email);
        user.setEncodedPassword(encodedPassword);

        user.setUbiProfileId(authorizationDTO.getProfileId());
        user.setUbiSessionId(authorizationDTO.getSessionId());
        user.setUbiAuthTicket(authorizationDTO.getTicket());
        user.setUbiSpaceId(authorizationDTO.getSpaceId());
        user.setUbiRememberMeTicket(authorizationDTO.getRememberMeTicket());
        user.setUbiRememberDeviceTicket(authorizationDTO.getRememberDeviceTicket());
        user.setUbiTwoFactorAuthTicket(authorizationDTO.getTwoFactorAuthenticationTicket());
        return user;
    }
}
