package github.ricemonger.ubi_users_stats_fetcher.postgres.repositories;


import github.ricemonger.ubi_users_stats_fetcher.postgres.dto_projections.UserAuthorizedUbiAccountProjection;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountEntryEntity;
import github.ricemonger.utilspostgresschema.ids.user.UbiAccountEntryEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomUserUbiAccountEntryPostgresRepository extends JpaRepository<UbiAccountEntryEntity, UbiAccountEntryEntityId> {
    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.ubi_users_stats_fetcher.postgres.dto_projections.UserAuthorizedUbiAccountProjection(" +
           "u.user.id," +
           " u.ubiAccountStats.ubiProfileId," +
           " u.ubiAccountStats.creditAmount," +
           "size(u.ubiAccountStats.ownedItems)," +
           " u.ubiAuthTicket," +
           " u.ubiSpaceId," +
           " u.ubiSessionId," +
           " u.ubiRememberDeviceTicket," +
           " u.ubiRememberMeTicket) " +
           "FROM UbiAccountEntryEntity u WHERE u.ubiAccountStats IS NOT NULL")
    List<UserAuthorizedUbiAccountProjection> findAllUserAuthorizedUbiAccounts();
}
