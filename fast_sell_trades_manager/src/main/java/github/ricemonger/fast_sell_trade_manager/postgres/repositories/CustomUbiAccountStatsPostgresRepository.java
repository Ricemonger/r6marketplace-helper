package github.ricemonger.fast_sell_trade_manager.postgres.repositories;

import github.ricemonger.fast_sell_trade_manager.postgres.dto_projections.ItemMedianPriceAndRarityProjectionI;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomUbiAccountStatsPostgresRepository extends JpaRepository<UbiAccountStatsEntity, String> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT i.item_id AS itemId, i.rarity AS rarity, i.month_median_price AS monthMedianPrice " +
                   "FROM item i " +
                   "JOIN ubi_account_owned_items uoi ON i.item_id = uoi.item_id " +
                   "WHERE uoi.ubi_profile_id = :ubiProfileId", nativeQuery = true)
    List<ItemMedianPriceAndRarityProjectionI> findOwnedItemsMedianPriceAndRarity(String ubiProfileId);

    @Transactional(readOnly = true)
    @Query("SELECT r.item.itemId FROM ItemResaleLockEntity r WHERE r.ubiAccount.ubiProfileId = :ubiProfileId")
    List<String> findAllUserResaleLocksItemIds(String ubiProfileId);
}
