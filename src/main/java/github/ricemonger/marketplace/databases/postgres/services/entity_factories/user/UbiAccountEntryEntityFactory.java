package github.ricemonger.marketplace.databases.postgres.services.entity_factories.user;

import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountEntryEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountStatsEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UserEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.UbiAccountStatsEntityPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UserPostgresRepository;
import github.ricemonger.utils.DTOs.UbiAccountAuthorizationEntry;
import github.ricemonger.utils.DTOs.UbiAccountAuthorizationEntryWithTelegram;
import github.ricemonger.utils.DTOs.UbiAccountEntry;
import github.ricemonger.utils.DTOs.UbiAccountEntryWithTelegram;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UbiAccountEntryEntityFactory {

    private final UserPostgresRepository userPostgresRepository;

    private final UbiAccountStatsEntityPostgresRepository ubiAccountStatsEntityPostgresRepository;

    private final UbiAccountStatsEntityFactory ubiAccountStatsEntityFactory;

    public UbiAccountEntryEntity createEntityForTelegramUser(String chatId, UbiAccountAuthorizationEntry account) {
        UserEntity user = userPostgresRepository.findByTelegramUserChatId(chatId);
        UbiAccountStatsEntity ubiAccountStatsEntity =
                ubiAccountStatsEntityPostgresRepository.findById(account.getUbiProfileId()).orElse(new UbiAccountStatsEntity(account.getUbiProfileId()));

        return new UbiAccountEntryEntity(
                user,
                account.getEmail(),
                account.getEncodedPassword(),
                account.getUbiSessionId(),
                account.getUbiSpaceId(),
                account.getUbiAuthTicket(),
                account.getUbiRememberDeviceTicket(),
                account.getUbiRememberMeTicket(),
                ubiAccountStatsEntity);
    }

    public UbiAccountAuthorizationEntryWithTelegram createUbiAccountAuthorizationEntryWithTelegram(UbiAccountEntryEntity entity) {
        return new UbiAccountAuthorizationEntryWithTelegram(
                entity.getUser().getTelegramUser().getChatId(),
                entity.getUser().getPrivateNotificationsEnabledFlag(),
                createUbiAccountAuthorizationEntry(entity));
    }

    public UbiAccountEntryWithTelegram createUbiAccountEntryWithTelegram(UbiAccountEntryEntity entity) {
        UbiAccountEntry ubiAccountEntry = new UbiAccountEntry(
                createUbiAccountAuthorizationEntry(entity),
                ubiAccountStatsEntityFactory.createDTO(entity.getUbiAccountStats())
        );
        return new UbiAccountEntryWithTelegram(
                entity.getUser().getTelegramUser().getChatId(),
                entity.getUser().getPrivateNotificationsEnabledFlag(),
                ubiAccountEntry);
    }

    public UbiAccountAuthorizationEntry createUbiAccountAuthorizationEntry(UbiAccountEntryEntity entity) {
        return new UbiAccountAuthorizationEntry(
                entity.getProfileId(),
                entity.getEmail(),
                entity.getEncodedPassword(),
                entity.getUbiSessionId(),
                entity.getUbiSpaceId(),
                entity.getUbiAuthTicket(),
                entity.getUbiRememberDeviceTicket(),
                entity.getUbiRememberMeTicket());
    }
}
