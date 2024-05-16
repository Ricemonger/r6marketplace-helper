package github.ricemonger.marketplace.databases.neo4j.services;

import github.ricemonger.marketplace.authorization.AuthorizationDTO;
import github.ricemonger.marketplace.authorization.AuthorizationService;
import github.ricemonger.marketplace.databases.neo4j.entities.TelegramLinkedUserEntity;
import github.ricemonger.marketplace.databases.neo4j.entities.UbiUserEntity;
import github.ricemonger.marketplace.databases.neo4j.repositories.UbiUserRepository;
import github.ricemonger.utils.exceptions.AesPasswordEncoder;
import github.ricemonger.utils.exceptions.UbiUserAuthorizationClientErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UbiUserService {

    private final UbiUserRepository ubiUserRepository;

    private final AesPasswordEncoder AESPasswordEncoder;

    private final AuthorizationService authorizationService;

    public void deleteByLinkedTelegramUserChatIdAndEmail(String chatId, String emailToRemove) {
        ubiUserRepository.deleteByLinkedTelegramUserChatIdAndEmail(chatId, emailToRemove);
    }

    public void deleteAllByLinkedTelegramUserChatId(String chatId) {
        ubiUserRepository.deleteAllByLinkedTelegramUserChatId(chatId);
    }

    public List<UbiUserEntity> reauthorizeAllUbiUsersAndGetUnauthorizedList(){
        List<UbiUserEntity> entities = ubiUserRepository.findAll();

        List<UbiUserEntity> notifyList = new ArrayList<>();

        for(UbiUserEntity entity: entities){
            try {
                reauthorizeUserOrThrow(entity);
            }
            catch(UbiUserAuthorizationClientErrorException e){
                notifyList.add(entity);
            }
        }

        return notifyList;
    }

    public void reauthorizeUserOrThrow(UbiUserEntity entity) {
            AuthorizationDTO authorizationDTO = authorizationService.getUserAuthorizationDTO(entity.getEmail(), AESPasswordEncoder.decode(entity.getPassword()));

            entity.setUbiProfileId(authorizationDTO.getProfileId());
            entity.setUbiSessionId(authorizationDTO.getSessionId());
            entity.setUbiAuthTicket(authorizationDTO.getTicket());
            entity.setUbiSpaceId(authorizationDTO.getSpaceId());
            entity.setUbiRememberMeTicket(authorizationDTO.getRememberMeTicket());
            entity.setUbiRememberDeviceTicket(authorizationDTO.getRememberDeviceTicket());
            entity.setUbiTwoFactorAuthTicket(authorizationDTO.getTwoFactorAuthenticationTicket());

            ubiUserRepository.save(entity);
    }

    public void createAndAuthorizeOrThrowForTelegramUser(TelegramLinkedUserEntity telegramLinkedUserEntity, String email, String password) throws UbiUserAuthorizationClientErrorException {
        UbiUserEntity ubiUserEntity = authorizeAndGetUbiUser(email, password);

        ubiUserEntity.setLinkedTelegramUser(telegramLinkedUserEntity);

        ubiUserRepository.save(ubiUserEntity);
    }

    private UbiUserEntity authorizeAndGetUbiUser(String email, String password) throws UbiUserAuthorizationClientErrorException {
        UbiUserEntity ubiUserEntity = new UbiUserEntity();

        AuthorizationDTO userAuthorizationDTO = authorizationService.getUserAuthorizationDTO(email, password);
        ubiUserEntity.setUbiProfileId(userAuthorizationDTO.getProfileId());
        ubiUserEntity.setUbiSessionId(userAuthorizationDTO.getSessionId());
        ubiUserEntity.setUbiAuthTicket(userAuthorizationDTO.getTicket());
        ubiUserEntity.setUbiSpaceId(userAuthorizationDTO.getSpaceId());
        ubiUserEntity.setUbiRememberMeTicket(userAuthorizationDTO.getRememberMeTicket());
        ubiUserEntity.setUbiRememberDeviceTicket(userAuthorizationDTO.getRememberDeviceTicket());
        ubiUserEntity.setUbiTwoFactorAuthTicket(userAuthorizationDTO.getTwoFactorAuthenticationTicket());

        ubiUserEntity.setEmail(email);
        ubiUserEntity.setPassword(AESPasswordEncoder.encode(password));

        return ubiUserEntity;
    }
}