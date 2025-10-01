package github.ricemonger.trades_manager.postgres.repositories;

import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomManageableUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomManageableUserPostgresRepository extends JpaRepository<CustomManageableUserEntity, String> {
    @Query(value = "SELECT u FROM manageable_user u WHERE (SIZE(u.tradeByItemIdManagers) > 0 OR SIZE(u.tradeByFiltersManagers) > 0) AND u" +
                   ".managingEnabledFlag = true AND u.ubiAccountEntry IS NOT NULL AND u.ubiAccountEntry.ubiAccountStats IS NOT NULL")
    List<CustomManageableUserEntity> findAllManageableUsers();
}
