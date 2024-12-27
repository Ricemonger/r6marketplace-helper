package github.ricemonger.item_trade_stats_calculator.postgres.repositories;

import github.ricemonger.item_trade_stats_calculator.postgres.entities.item.ItemRecalculationRequiredFieldsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRecalculationRequiredFieldsPostgresRepository extends JpaRepository<ItemRecalculationRequiredFieldsEntity, String> {
}
