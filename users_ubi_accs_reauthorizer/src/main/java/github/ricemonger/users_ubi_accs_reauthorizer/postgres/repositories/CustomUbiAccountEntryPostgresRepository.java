package github.ricemonger.users_ubi_accs_reauthorizer.postgres.repositories;


import github.ricemonger.users_ubi_accs_reauthorizer.postgres.dto_projections.UserUbiAccountAuthorizedProjection;
import github.ricemonger.users_ubi_accs_reauthorizer.postgres.dto_projections.UserUbiAccountCredentialsProjection;
import github.ricemonger.users_ubi_accs_reauthorizer.postgres.dto_projections.UserUnauthorizedUbiAccountProjection;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountEntryEntity;
import github.ricemonger.utilspostgresschema.ids.user.UbiAccountEntryEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomUbiAccountEntryPostgresRepository extends JpaRepository<UbiAccountEntryEntity, UbiAccountEntryEntityId> {
    @Transactional
    @Modifying
    @Query("UPDATE UbiAccountEntryEntity u SET" +
           " u.ubiSessionId = :#{#userUbiCredentials.ubiSessionId}," +
           " u.ubiSpaceId = :#{#userUbiCredentials.ubiSpaceId}," +
           " u.ubiAuthTicket = :#{#userUbiCredentials.ubiAuthTicket}," +
           " u.ubiRememberMeTicket = :#{#userUbiCredentials.ubiRememberMeTicket}," +
           " u.ubiRememberDeviceTicket = :#{#userUbiCredentials.ubiRememberDeviceTicket}" +
           " WHERE u.user.id = :#{#userUbiCredentials.userId} AND u.email = :#{#userUbiCredentials.email}")
    void updateUserUbiCredentials(UserUbiAccountAuthorizedProjection userUbiCredentials);

    @Transactional
    @Modifying
    @Query(value = "UPDATE ubi_account_authorization_entry SET ubi_profile_id = :profileId WHERE user_id = :userId AND email = :email", nativeQuery = true)
    void linkUbiAccountStatsByUserIdAndEmail(Long userId, String email, String profileId);

    @Transactional
    default void unlinkAllUbiAccountStatsForUnauthorizedUsers(Collection<UserUnauthorizedUbiAccountProjection> projections) {
        for (UserUnauthorizedUbiAccountProjection projection : projections) {
            unlinkUbiAccountStatsForUnauthorizedUser(projection);
        }
    }

    @Transactional
    @Modifying
    @Query("UPDATE UbiAccountEntryEntity u SET u.ubiAccountStats = NULL WHERE u.user.id = :#{#projection.id} AND u.email = :#{#projection.email}")
    void unlinkUbiAccountStatsForUnauthorizedUser(UserUnauthorizedUbiAccountProjection projection);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.users_ubi_accs_reauthorizer.postgres.dto_projections.UserUbiAccountCredentialsProjection(" +
           "u.user.id, " +
           "u.email, " +
           "u.encodedPassword, " +
           "u.ubiAuthTicket, " +
           "u.ubiRememberDeviceTicket) " +
           "FROM UbiAccountEntryEntity u")
    List<UserUbiAccountCredentialsProjection> findAllUsersUbiCredentials();

    @Transactional(readOnly = true)
    @Query("SELECT u.ubiAccountStats.ubiProfileId FROM UbiAccountEntryEntity u WHERE u.user.id = :userId AND u.email = :email")
    Optional<String> findUbiAccountStatsProfileIdByUserIdAndEmail(Long userId, String email);
}
