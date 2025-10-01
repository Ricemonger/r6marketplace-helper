package github.ricemonger.item_trade_stats_calculator.postgres.repositories;


import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemDaySalesUbiStatsProjectionI;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemDaySalesUbiStatsEntity;
import github.ricemonger.utilspostgresschema.ids.item.ItemDaySalesUbiStatsEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomItemDaySalesUbiStatsPostgresRepository extends JpaRepository<ItemDaySalesUbiStatsEntity, ItemDaySalesUbiStatsEntityId> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT s.item_id AS itemId, s.date AS date, s.lowest_price AS lowestPrice, " +
                   "s.average_price AS averagePrice, s.highest_price AS highestPrice, s.items_count AS itemsCount " +
                   "FROM item_day_sales_ubi_stats s WHERE s.date >= CURRENT_DATE-30", nativeQuery = true)
    List<ItemDaySalesUbiStatsProjectionI> findAllForLastMonth();
}


