package github.ricemonger.item_trade_stats_calculator.postgres.repositories;


import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemRecalculationRequiredFieldsProjection;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomItemPostgresRepository extends JpaRepository<ItemEntity, String> {
    @Transactional
    default void updateAllItemsHistoryFields(List<ItemHistoryFieldsProjection> projections) {
        for (ItemHistoryFieldsProjection projection : projections) {
            updateHistoryFields(projection);
        }
    }

    @Transactional
    @Modifying
    @Query(value = "UPDATE ItemEntity i SET " +
                   "i.monthAveragePrice = :#{#projection.monthAveragePrice}, " +
                   "i.monthMedianPrice = :#{#projection.monthMedianPrice}, " +
                   "i.monthMaxPrice = :#{#projection.monthMaxPrice}, " +
                   "i.monthMinPrice = :#{#projection.monthMinPrice}, " +
                   "i.monthSalesPerDay = :#{#projection.monthSalesPerDay}, " +
                   "i.monthSales = :#{#projection.monthSales}, " +
                   "i.dayAveragePrice = :#{#projection.dayAveragePrice}, " +
                   "i.dayMedianPrice = :#{#projection.dayMedianPrice}, " +
                   "i.dayMaxPrice = :#{#projection.dayMaxPrice}, " +
                   "i.dayMinPrice = :#{#projection.dayMinPrice}, " +
                   "i.daySales = :#{#projection.daySales}, " +
                   "i.priorityToSellByMaxBuyPrice = :#{#projection.priorityToSellByMaxBuyPrice}, " +
                   "i.priorityToSellByNextFancySellPrice = :#{#projection.priorityToSellByNextFancySellPrice}, " +
                   "i.priorityToBuyByMinSellPrice = :#{#projection.priorityToBuyByMinSellPrice}, " +
                   "i.priorityToBuyIn1Hour = :#{#projection.priorityToBuyIn1Hour}, " +
                   "i.priorityToBuyIn6Hours = :#{#projection.priorityToBuyIn6Hours}, " +
                   "i.priorityToBuyIn24Hours = :#{#projection.priorityToBuyIn24Hours}, " +
                   "i.priorityToBuyIn168Hours = :#{#projection.priorityToBuyIn168Hours}, " +
                   "i.priorityToBuyIn720Hours = :#{#projection.priorityToBuyIn720Hours}, " +
                   "i.priceToBuyIn1Hour = :#{#projection.priceToBuyIn1Hour}, " +
                   "i.priceToBuyIn6Hours = :#{#projection.priceToBuyIn6Hours}, " +
                   "i.priceToBuyIn24Hours = :#{#projection.priceToBuyIn24Hours}, " +
                   "i.priceToBuyIn168Hours = :#{#projection.priceToBuyIn168Hours}, " +
                   "i.priceToBuyIn720Hours = :#{#projection.priceToBuyIn720Hours} " +
                   "WHERE i.itemId = :#{#projection.itemId}")
    void updateHistoryFields(ItemHistoryFieldsProjection projection);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemRecalculationRequiredFieldsProjection(" +
           "i.itemId, i.rarity, i.maxBuyPrice, i.buyOrdersCount, i.minSellPrice, i.sellOrdersCount) " +
           "FROM ItemEntity i")
    List<ItemRecalculationRequiredFieldsProjection> findAllItemsRecalculationRequiredFields();

    @Transactional
    default void updateAllItemsCurrentPricesHistoryFields(List<ItemCurrentPricesHistoryFieldsProjection> projections) {
        for (ItemCurrentPricesHistoryFieldsProjection projection : projections) {
            updateItemCurrentPricesHistoryFields(projection);
        }
    }

    @Transactional
    @Modifying
    @Query(value = "UPDATE ItemEntity i SET " +
                   "i.priorityToSellByMaxBuyPrice = :#{#projection.priorityToSellByMaxBuyPrice}, " +
                   "i.priorityToSellByNextFancySellPrice = :#{#projection.priorityToSellByNextFancySellPrice}, " +
                   "i.priorityToBuyByMinSellPrice = :#{#projection.priorityToBuyByMinSellPrice} " +
                   "WHERE i.itemId = :#{#projection.itemId}")
    void updateItemCurrentPricesHistoryFields(ItemCurrentPricesHistoryFieldsProjection projection);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesRecalculationRequiredFieldsProjection(" +
           "i.itemId, i.rarity, i.maxBuyPrice, i.minSellPrice, i.sellOrdersCount,i.monthMedianPrice,i.monthSalesPerDay,i.monthSales) " +
           "FROM ItemEntity i")
    List<ItemCurrentPricesRecalculationRequiredFieldsProjection> findAllItemsCurrentPricesRecalculationRequiredFields();
}
