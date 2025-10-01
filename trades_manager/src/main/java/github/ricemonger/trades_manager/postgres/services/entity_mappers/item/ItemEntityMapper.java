package github.ricemonger.trades_manager.postgres.services.entity_mappers.item;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomTagValueEntity;
import github.ricemonger.utils.DTOs.common.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemEntityMapper {

    public Item createDTO(CustomItemEntity itemEntity) {
        List<String> tags = new ArrayList<>();
        if (itemEntity.getTags() != null && !itemEntity.getTags().isEmpty()) {
            tags = itemEntity.getTags().stream().map(CustomTagValueEntity::getValue).toList();
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
}
