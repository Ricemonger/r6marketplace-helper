package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.marketplace.databases.postgres.dto_projections.UbiAccountAuthorizationEntryProjection;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountEntryEntity;
import github.ricemonger.utilspostgresschema.ids.user.UbiAccountEntryEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CustomUbiAccountEntryPostgresRepository extends JpaRepository<UbiAccountEntryEntity, UbiAccountEntryEntityId> {
    @Transactional
    void deleteByUserTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    Optional<UbiAccountEntryEntity> findByUserTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.databases.postgres.dto_projections.UbiAccountAuthorizationEntryProjection(" +
            "u.ubiAccountStats.ubiProfileId," +
            "u.email," +
            "u.encodedPassword," +
            "u.ubiSessionId," +
            "u.ubiSpaceId," +
            "u.ubiAuthTicket," +
            "u.ubiRememberDeviceTicket," +
            "u.ubiRememberMeTicket) " +
            "FROM UbiAccountEntryEntity u WHERE u.user.telegramUser.chatId = :chatId")
    Optional<UbiAccountAuthorizationEntryProjection> findUbiAccountAuthorizationEntryByUserTelegramUserChatId(String chatId);
}
