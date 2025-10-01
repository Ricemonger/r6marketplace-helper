package github.ricemonger.item_trade_stats_calculator.postgres.repositories;

import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemSaleProjectionI;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemSaleEntity;
import github.ricemonger.utilspostgresschema.ids.item.ItemSaleEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomItemSalePostgresRepository extends JpaRepository<ItemSaleEntity, ItemSaleEntityId> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT s.item_id AS itemId, s.sold_at AS soldAt, s.price AS price " +
                   "FROM item_sale s WHERE s.sold_at >= CURRENT_DATE - 30", nativeQuery = true)
    List<ItemSaleProjectionI> findAllForLastMonth();
}
