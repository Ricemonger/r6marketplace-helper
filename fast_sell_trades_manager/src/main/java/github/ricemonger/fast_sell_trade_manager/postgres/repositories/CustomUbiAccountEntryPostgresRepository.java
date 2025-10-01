package github.ricemonger.fast_sell_trade_manager.postgres.repositories;

import github.ricemonger.fast_sell_trade_manager.postgres.dto_projections.FastSellManagedUserProjection;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CustomUbiAccountEntryPostgresRepository extends JpaRepository<UbiAccountEntryEntity, String> {
    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.fast_sell_trade_manager.postgres.dto_projections.FastSellManagedUserProjection(" +
           "u.ubiAuthTicket, " +
           "u.ubiAccountStats.ubiProfileId, " +
           "u.ubiSpaceId, " +
           "u.ubiSessionId, " +
           "u.ubiRememberDeviceTicket, " +
           "u.ubiRememberMeTicket, " +
           "u.ubiAccountStats.soldIn24h) " +
           "FROM UbiAccountEntryEntity u WHERE u.user.id = :userId AND u.email = :email")
    Optional<FastSellManagedUserProjection> findFastSellManagedUserById(Long userId, String email);
}
