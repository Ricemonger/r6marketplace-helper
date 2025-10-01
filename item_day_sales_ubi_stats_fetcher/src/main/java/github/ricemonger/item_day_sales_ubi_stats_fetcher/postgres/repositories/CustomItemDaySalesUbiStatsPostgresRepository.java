package github.ricemonger.item_day_sales_ubi_stats_fetcher.postgres.repositories;


import github.ricemonger.item_day_sales_ubi_stats_fetcher.postgres.custom_entities.ItemDaySalesUbiStatsEntity;
import github.ricemonger.item_day_sales_ubi_stats_fetcher.postgres.custom_entities.ItemDaySalesUbiStatsEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomItemDaySalesUbiStatsPostgresRepository extends JpaRepository<ItemDaySalesUbiStatsEntity, ItemDaySalesUbiStatsEntityId> {
    @Transactional
    default void insertAll(List<ItemDaySalesUbiStatsEntity> entities) {
        for (ItemDaySalesUbiStatsEntity entity : entities) {
            insert(entity);
        }
    }

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO item_day_sales_ubi_stats (item_id, date, lowest_price, average_price, highest_price, items_count) VALUES " +
                   "(:#{#entity.item.itemId}, :#{#entity.date}, :#{#entity.lowestPrice}, :#{#entity.averagePrice}, :#{#entity.highestPrice}, " +
                   ":#{#entity.itemsCount}) ON CONFLICT (item_id, date) DO NOTHING ", nativeQuery = true)
    void insert(ItemDaySalesUbiStatsEntity entity);
}


