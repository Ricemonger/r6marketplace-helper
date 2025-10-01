package github.ricemonger.item_trade_stats_calculator.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemCurrentPricesRecalculationRequiredFieldsTest {

    @Test
    public void toItem_should_return_expected_result() {
        ItemCurrentPricesRecalculationRequiredFields itemCurrentPricesRecalculationRequiredFields = new ItemCurrentPricesRecalculationRequiredFields();
        itemCurrentPricesRecalculationRequiredFields.setItemId("itemId");
        itemCurrentPricesRecalculationRequiredFields.setRarity(ItemRarity.RARE);
        itemCurrentPricesRecalculationRequiredFields.setMinSellPrice(1);
        itemCurrentPricesRecalculationRequiredFields.setSellOrdersCount(2);
        itemCurrentPricesRecalculationRequiredFields.setMaxBuyPrice(3);
        itemCurrentPricesRecalculationRequiredFields.setMonthMedianPrice(4);
        itemCurrentPricesRecalculationRequiredFields.setMonthSalesPerDay(5);
        itemCurrentPricesRecalculationRequiredFields.setMonthSales(6);

        Item result = itemCurrentPricesRecalculationRequiredFields.toItem();

        assertEquals("itemId", result.getItemId());
        assertEquals(ItemRarity.RARE, result.getRarity());
        assertEquals(1, result.getMinSellPrice());
        assertEquals(2, result.getSellOrdersCount());
        assertEquals(3, result.getMaxBuyPrice());
        assertEquals(4, result.getMonthMedianPrice());
        assertEquals(5, result.getMonthSalesPerDay());
        assertEquals(6, result.getMonthSales());
    }
}