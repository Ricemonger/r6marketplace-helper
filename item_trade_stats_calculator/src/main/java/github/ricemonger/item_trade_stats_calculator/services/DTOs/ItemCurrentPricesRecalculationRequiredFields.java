package github.ricemonger.item_trade_stats_calculator.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCurrentPricesRecalculationRequiredFields {
    private String itemId;
    private ItemRarity rarity;
    private Integer maxBuyPrice;
    private Integer minSellPrice;
    private Integer sellOrdersCount;
    private Integer monthMedianPrice;
    private Integer monthSalesPerDay;
    private Integer monthSales;

    public Item toItem() {
        Item item = new Item();
        item.setItemId(itemId);
        item.setRarity(rarity);
        item.setMaxBuyPrice(maxBuyPrice);
        item.setMinSellPrice(minSellPrice);
        item.setSellOrdersCount(sellOrdersCount);
        item.setMonthMedianPrice(monthMedianPrice);
        item.setMonthSalesPerDay(monthSalesPerDay);
        item.setMonthSales(monthSales);
        return item;
    }
}
