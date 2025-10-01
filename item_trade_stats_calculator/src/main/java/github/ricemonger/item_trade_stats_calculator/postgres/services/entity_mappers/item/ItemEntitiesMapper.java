package github.ricemonger.item_trade_stats_calculator.postgres.services.entity_mappers.item;

import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemCurrentPricesRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemRecalculationRequiredFields;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemHistoryFieldsI;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.full_entities.item.TagEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEntitiesMapper {

    public ItemRecalculationRequiredFields createRecalculationRequiredFieldsDTO(ItemRecalculationRequiredFieldsProjection projection) {
        return new ItemRecalculationRequiredFields(
                projection.getItemId(),
                projection.getRarity(),
                projection.getMaxBuyPrice(),
                projection.getBuyOrdersCount(),
                projection.getMinSellPrice(),
                projection.getSellOrdersCount());
    }

    public ItemHistoryFieldsProjection createHistoryFieldsDtoProjection(ItemHistoryFieldsI item) {
        return new ItemHistoryFieldsProjection(
                item.getItemId(),
                item.getMonthAveragePrice(),
                item.getMonthMedianPrice(),
                item.getMonthMaxPrice(),
                item.getMonthMinPrice(),
                item.getMonthSalesPerDay(),
                item.getMonthSales(),
                item.getDayAveragePrice(),
                item.getDayMedianPrice(),
                item.getDayMaxPrice(),
                item.getDayMinPrice(),
                item.getDaySales(),
                item.getPriorityToSellByMaxBuyPrice(),
                item.getPriorityToSellByNextFancySellPrice(),
                item.getPriorityToBuyByMinSellPrice(),
                item.getPriorityToBuyIn1Hour(),
                item.getPriorityToBuyIn6Hours(),
                item.getPriorityToBuyIn24Hours(),
                item.getPriorityToBuyIn168Hours(),
                item.getPriorityToBuyIn720Hours(),
                item.getPriceToBuyIn1Hour(),
                item.getPriceToBuyIn6Hours(),
                item.getPriceToBuyIn24Hours(),
                item.getPriceToBuyIn168Hours(),
                item.getPriceToBuyIn720Hours());
    }

    public Item createItem(ItemEntity itemEntity) {
        List<String> tags = new ArrayList<>();
        if (itemEntity.getTags() != null && !itemEntity.getTags().isEmpty()) {
            tags = itemEntity.getTags().stream().map(TagEntity::getValue).toList();
        }
        return new Item(
                itemEntity.getItemId(),
                itemEntity.getAssetUrl(),
                itemEntity.getName(),
                tags,
                itemEntity.getRarity(),
                itemEntity.getType(),
                itemEntity.getMaxBuyPrice(),
                itemEntity.getBuyOrdersCount(),
                itemEntity.getMinSellPrice(),
                itemEntity.getSellOrdersCount(),
                itemEntity.getLastSoldAt(),
                itemEntity.getLastSoldPrice(),
                itemEntity.getMonthAveragePrice(),
                itemEntity.getMonthMedianPrice(),
                itemEntity.getMonthMaxPrice(),
                itemEntity.getMonthMinPrice(),
                itemEntity.getMonthSalesPerDay(),
                itemEntity.getMonthSales(),
                itemEntity.getDayAveragePrice(),
                itemEntity.getDayMedianPrice(),
                itemEntity.getDayMaxPrice(),
                itemEntity.getDayMinPrice(),
                itemEntity.getDaySales(),
                itemEntity.getPriorityToSellByMaxBuyPrice(),
                itemEntity.getPriorityToSellByNextFancySellPrice(),
                itemEntity.getPriorityToBuyByMinSellPrice(),
                itemEntity.getPriorityToBuyIn1Hour(),
                itemEntity.getPriorityToBuyIn6Hours(),
                itemEntity.getPriorityToBuyIn24Hours(),
                itemEntity.getPriorityToBuyIn168Hours(),
                itemEntity.getPriorityToBuyIn720Hours(),
                itemEntity.getPriceToBuyIn1Hour(),
                itemEntity.getPriceToBuyIn6Hours(),
                itemEntity.getPriceToBuyIn24Hours(),
                itemEntity.getPriceToBuyIn168Hours(),
                itemEntity.getPriceToBuyIn720Hours());
    }

    public ItemCurrentPricesHistoryFieldsProjection createCurrentPricesHistoryFieldsProjection(ItemHistoryFieldsI itemHistoryFieldsI) {
        return new ItemCurrentPricesHistoryFieldsProjection(
                itemHistoryFieldsI.getItemId(),
                itemHistoryFieldsI.getPriorityToSellByMaxBuyPrice(),
                itemHistoryFieldsI.getPriorityToSellByNextFancySellPrice(),
                itemHistoryFieldsI.getPriorityToBuyByMinSellPrice());
    }

    public ItemCurrentPricesRecalculationRequiredFields createCurrentPricesRecalculationRequiredFields(ItemCurrentPricesRecalculationRequiredFieldsProjection projection) {
        return new ItemCurrentPricesRecalculationRequiredFields(
                projection.getItemId(),
                projection.getRarity(),
                projection.getMaxBuyPrice(),
                projection.getMinSellPrice(),
                projection.getSellOrdersCount(),
                projection.getMonthMedianPrice(),
                projection.getMonthSalesPerDay(),
                projection.getMonthSales()
        );
    }
}
