package github.ricemonger.notifications_service.postgres.repositories;

import github.ricemonger.notifications_service.postgres.dto_projections.ToBeNotifiedUserProjection;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomUserPostgresRepository extends JpaRepository<UserEntity, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.notifications_service.postgres.dto_projections.ToBeNotifiedUserProjection(" +
           "u.telegramUser.chatId," +
           "u.privateNotificationsEnabledFlag," +
           "u.publicNotificationsEnabledFlag," +
           "u.ubiStatsUpdatedNotificationsEnabledFlag," +
           "u.tradeManagerNotificationsEnabledFlag," +
           "u.authorizationNotificationsEnabledFlag)" +
           "FROM UserEntity u WHERE u.id = :id")
    Optional<ToBeNotifiedUserProjection> findToBeNotifiedUserIdById(Long id);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.notifications_service.postgres.dto_projections.ToBeNotifiedUserProjection(" +
           "u.telegramUser.chatId," +
           "u.privateNotificationsEnabledFlag," +
           "u.publicNotificationsEnabledFlag," +
           "u.ubiStatsUpdatedNotificationsEnabledFlag," +
           "u.tradeManagerNotificationsEnabledFlag," +
           "u.authorizationNotificationsEnabledFlag)" +
           "FROM UserEntity u")
    List<ToBeNotifiedUserProjection> findAllToBeNotifiedUsers();
}
