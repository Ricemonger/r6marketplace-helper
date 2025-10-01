package github.ricemonger.trades_manager.postgres.custom_entities.items;

import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomItemEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomItemEntity entity = new CustomItemEntity();
        assertEquals(entity, entity);
    }

    @Test
    public void equals_should_return_true_if_equal_id() {
        CustomItemEntity entity1 = new CustomItemEntity();
        entity1.setItemId("itemId");
        entity1.setAssetUrl("url");
        entity1.setName("name");
        entity1.setTags(Collections.emptyList());
        entity1.setRarity(ItemRarity.RARE);
        entity1.setType(ItemType.WeaponSkin);
        entity1.setMaxBuyPrice(100);
        entity1.setBuyOrdersCount(10);
        entity1.setMinSellPrice(200);
        entity1.setSellOrdersCount(20);
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setLastSoldPrice(150);
        entity1.setMonthAveragePrice(120);
        entity1.setMonthMedianPrice(130);
        entity1.setMonthMaxPrice(140);
        entity1.setMonthMinPrice(110);
        entity1.setMonthSalesPerDay(5);
        entity1.setMonthSales(50);
        entity1.setDayAveragePrice(125);
        entity1.setDayMedianPrice(135);
        entity1.setDayMaxPrice(145);
        entity1.setDayMinPrice(115);
        entity1.setPriorityToSellByMaxBuyPrice(1L);
        entity1.setPriorityToSellByNextFancySellPrice(2L);
        entity1.setPriorityToBuyByMinSellPrice(3L);
        entity1.setPriorityToBuyIn1Hour(4L);
        entity1.setPriorityToBuyIn6Hours(5L);
        entity1.setPriorityToBuyIn24Hours(6L);
        entity1.setPriorityToBuyIn168Hours(7L);
        entity1.setPriorityToBuyIn720Hours(8L);
        entity1.setPriceToBuyIn1Hour(9);
        entity1.setPriceToBuyIn6Hours(10);
        entity1.setPriceToBuyIn24Hours(11);
        entity1.setPriceToBuyIn168Hours(12);
        entity1.setPriceToBuyIn720Hours(13);

        CustomItemEntity entity2 = new CustomItemEntity();
        entity2.setItemId("itemId");

        assertEquals(entity1, entity2);
    }

    @Test
    public void equals_should_return_false_for_null() {
        CustomItemEntity entity = new CustomItemEntity();
        assertNotEquals(null, entity);
    }


    @Test
    public void isFullyEqual_should_return_true_if_same() {
        CustomItemEntity entity = new CustomItemEntity();
        assertTrue(entity.isFullyEqual(entity));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal() {
        CustomItemEntity entity1 = new CustomItemEntity();
        entity1.setItemId("itemId");
        entity1.setAssetUrl("url");
        entity1.setName("name");
        entity1.setTags(Collections.emptyList());
        entity1.setRarity(ItemRarity.RARE);
        entity1.setType(ItemType.WeaponSkin);
        entity1.setMaxBuyPrice(100);
        entity1.setBuyOrdersCount(10);
        entity1.setMinSellPrice(200);
        entity1.setSellOrdersCount(20);
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setLastSoldPrice(150);
        entity1.setMonthAveragePrice(120);
        entity1.setMonthMedianPrice(130);
        entity1.setMonthMaxPrice(140);
        entity1.setMonthMinPrice(110);
        entity1.setMonthSalesPerDay(5);
        entity1.setMonthSales(50);
        entity1.setDayAveragePrice(125);
        entity1.setDayMedianPrice(135);
        entity1.setDayMaxPrice(145);
        entity1.setDayMinPrice(115);
        entity1.setDaySales(15);
        entity1.setPriorityToSellByMaxBuyPrice(1L);
        entity1.setPriorityToSellByNextFancySellPrice(2L);
        entity1.setPriorityToBuyByMinSellPrice(3L);
        entity1.setPriorityToBuyIn1Hour(4L);
        entity1.setPriorityToBuyIn6Hours(5L);
        entity1.setPriorityToBuyIn24Hours(6L);
        entity1.setPriorityToBuyIn168Hours(7L);
        entity1.setPriorityToBuyIn720Hours(8L);
        entity1.setPriceToBuyIn1Hour(9);
        entity1.setPriceToBuyIn6Hours(10);
        entity1.setPriceToBuyIn24Hours(11);
        entity1.setPriceToBuyIn168Hours(12);
        entity1.setPriceToBuyIn720Hours(13);

        CustomItemEntity entity2 = new CustomItemEntity();
        entity2.setItemId("itemId");
        entity2.setAssetUrl("url");
        entity2.setName("name");
        entity2.setTags(Collections.emptyList());
        entity2.setRarity(ItemRarity.RARE);
        entity2.setType(ItemType.WeaponSkin);
        entity2.setMaxBuyPrice(100);
        entity2.setBuyOrdersCount(10);
        entity2.setMinSellPrice(200);
        entity2.setSellOrdersCount(20);
        entity2.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity2.setLastSoldPrice(150);
        entity2.setMonthAveragePrice(120);
        entity2.setMonthMedianPrice(130);
        entity2.setMonthMaxPrice(140);
        entity2.setMonthMinPrice(110);
        entity2.setMonthSalesPerDay(5);
        entity2.setMonthSales(50);
        entity2.setDayAveragePrice(125);
        entity2.setDayMedianPrice(135);
        entity2.setDayMaxPrice(145);
        entity2.setDayMinPrice(115);
        entity2.setDaySales(15);
        entity2.setPriorityToSellByMaxBuyPrice(1L);
        entity2.setPriorityToSellByNextFancySellPrice(2L);
        entity2.setPriorityToBuyByMinSellPrice(3L);
        entity2.setPriorityToBuyIn1Hour(4L);
        entity2.setPriorityToBuyIn6Hours(5L);
        entity2.setPriorityToBuyIn24Hours(6L);
        entity2.setPriorityToBuyIn168Hours(7L);
        entity2.setPriorityToBuyIn720Hours(8L);
        entity2.setPriceToBuyIn1Hour(9);
        entity2.setPriceToBuyIn6Hours(10);
        entity2.setPriceToBuyIn24Hours(11);
        entity2.setPriceToBuyIn168Hours(12);
        entity2.setPriceToBuyIn720Hours(13);

        assertTrue(entity1.isFullyEqual(entity2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_equal() {
        CustomItemEntity entity1 = new CustomItemEntity();
        entity1.setItemId("itemId1");
        entity1.setAssetUrl("url1");
        entity1.setName("name1");
        entity1.setTags(Collections.emptyList());
        entity1.setRarity(ItemRarity.RARE);
        entity1.setType(ItemType.WeaponSkin);
        entity1.setMaxBuyPrice(100);
        entity1.setBuyOrdersCount(10);
        entity1.setMinSellPrice(200);
        entity1.setSellOrdersCount(20);
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setLastSoldPrice(150);
        entity1.setMonthAveragePrice(120);
        entity1.setMonthMedianPrice(130);
        entity1.setMonthMaxPrice(140);
        entity1.setMonthMinPrice(110);
        entity1.setMonthSalesPerDay(5);
        entity1.setMonthSales(50);
        entity1.setDayAveragePrice(125);
        entity1.setDayMedianPrice(135);
        entity1.setDayMaxPrice(145);
        entity1.setDayMinPrice(115);
        entity1.setDaySales(15);
        entity1.setPriorityToSellByMaxBuyPrice(1L);
        entity1.setPriorityToSellByNextFancySellPrice(2L);
        entity1.setPriorityToBuyByMinSellPrice(3L);
        entity1.setPriorityToBuyIn1Hour(4L);
        entity1.setPriorityToBuyIn6Hours(5L);
        entity1.setPriorityToBuyIn24Hours(6L);
        entity1.setPriorityToBuyIn168Hours(7L);
        entity1.setPriorityToBuyIn720Hours(8L);
        entity1.setPriceToBuyIn1Hour(9);
        entity1.setPriceToBuyIn6Hours(10);
        entity1.setPriceToBuyIn24Hours(11);
        entity1.setPriceToBuyIn168Hours(12);
        entity1.setPriceToBuyIn720Hours(13);

        CustomItemEntity entity2 = new CustomItemEntity();
        entity2.setItemId("itemId");
        entity2.setAssetUrl("url");
        entity2.setName("name");
        entity2.setTags(Collections.emptyList());
        entity2.setRarity(ItemRarity.RARE);
        entity2.setType(ItemType.WeaponSkin);
        entity2.setMaxBuyPrice(100);
        entity2.setBuyOrdersCount(10);
        entity2.setMinSellPrice(200);
        entity2.setSellOrdersCount(20);
        entity2.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity2.setLastSoldPrice(150);
        entity2.setMonthAveragePrice(120);
        entity2.setMonthMedianPrice(130);
        entity2.setMonthMaxPrice(140);
        entity2.setMonthMinPrice(110);
        entity2.setMonthSalesPerDay(5);
        entity2.setMonthSales(50);
        entity2.setDayAveragePrice(125);
        entity2.setDayMedianPrice(135);
        entity2.setDayMaxPrice(145);
        entity2.setDayMinPrice(115);
        entity2.setDaySales(15);
        entity2.setPriorityToSellByMaxBuyPrice(1L);
        entity2.setPriorityToSellByNextFancySellPrice(2L);
        entity2.setPriorityToBuyByMinSellPrice(3L);
        entity2.setPriorityToBuyIn1Hour(4L);
        entity2.setPriorityToBuyIn6Hours(5L);
        entity2.setPriorityToBuyIn24Hours(6L);
        entity2.setPriorityToBuyIn168Hours(7L);
        entity2.setPriorityToBuyIn720Hours(8L);
        entity2.setPriceToBuyIn1Hour(9);
        entity2.setPriceToBuyIn6Hours(10);
        entity2.setPriceToBuyIn24Hours(11);
        entity2.setPriceToBuyIn168Hours(12);
        entity2.setPriceToBuyIn720Hours(13);

        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setItemId("itemId");
        entity1.setAssetUrl("url1");
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setAssetUrl("url");
        entity1.setName("name1");
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setName("name");
        entity1.setTags(null);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setTags(List.of(new CustomTagValueEntity()));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setTags(Collections.emptyList());
        entity1.setRarity(ItemRarity.UNCOMMON);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setRarity(ItemRarity.RARE);
        entity1.setType(ItemType.WeaponAttachmentSkinSet);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setType(ItemType.WeaponSkin);
        entity1.setMaxBuyPrice(101);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMaxBuyPrice(100);
        entity1.setBuyOrdersCount(11);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setBuyOrdersCount(10);
        entity1.setMinSellPrice(201);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMinSellPrice(200);
        entity1.setSellOrdersCount(21);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setSellOrdersCount(20);
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 2, 0, 0));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setLastSoldPrice(151);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setLastSoldPrice(150);
        entity1.setMonthAveragePrice(121);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMonthAveragePrice(120);
        entity1.setMonthMedianPrice(131);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMonthMedianPrice(130);
        entity1.setMonthMaxPrice(141);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMonthMaxPrice(140);
        entity1.setMonthMinPrice(111);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMonthMinPrice(110);
        entity1.setMonthSalesPerDay(6);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMonthSalesPerDay(5);
        entity1.setMonthSales(51);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setMonthSales(50);
        entity1.setDayAveragePrice(126);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setDayAveragePrice(125);
        entity1.setDayMedianPrice(136);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setDayMedianPrice(135);
        entity1.setDayMaxPrice(146);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setDayMaxPrice(145);
        entity1.setDayMinPrice(116);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setDayMinPrice(115);
        entity1.setDaySales(16);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setDaySales(15);
        entity1.setPriorityToSellByMaxBuyPrice(2L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToSellByMaxBuyPrice(1L);
        entity1.setPriorityToSellByNextFancySellPrice(3L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToSellByNextFancySellPrice(2L);
        entity1.setPriorityToBuyByMinSellPrice(4L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToBuyByMinSellPrice(3L);
        entity1.setPriorityToBuyIn1Hour(5L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToBuyIn1Hour(4L);
        entity1.setPriorityToBuyIn6Hours(6L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToBuyIn6Hours(5L);
        entity1.setPriorityToBuyIn24Hours(7L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToBuyIn24Hours(6L);
        entity1.setPriorityToBuyIn168Hours(8L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToBuyIn168Hours(7L);
        entity1.setPriorityToBuyIn720Hours(9L);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriorityToBuyIn720Hours(8L);
        entity1.setPriceToBuyIn1Hour(10);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriceToBuyIn1Hour(9);
        entity1.setPriceToBuyIn6Hours(11);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriceToBuyIn6Hours(10);
        entity1.setPriceToBuyIn24Hours(12);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriceToBuyIn24Hours(11);
        entity1.setPriceToBuyIn168Hours(13);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriceToBuyIn168Hours(12);
        entity1.setPriceToBuyIn720Hours(14);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setPriceToBuyIn720Hours(13);
    }
}