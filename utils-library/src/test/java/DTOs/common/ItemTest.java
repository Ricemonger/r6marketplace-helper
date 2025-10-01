package DTOs.common;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemMainFieldsI;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    public void constructor_should_copy_all_fields_different_link_for_mutable() {
        Item toCopy = new Item();
        toCopy.setItemId("1");
        toCopy.setAssetUrl("url1");
        toCopy.setName("name1");
        toCopy.setTags(List.of("tag1"));
        toCopy.setRarity(ItemRarity.UNCOMMON);
        toCopy.setType(ItemType.WeaponSkin);
        toCopy.setMaxBuyPrice(1);
        toCopy.setBuyOrdersCount(2);
        toCopy.setMinSellPrice(3);
        toCopy.setSellOrdersCount(4);
        toCopy.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        toCopy.setLastSoldPrice(5);
        toCopy.setMonthAveragePrice(6);
        toCopy.setMonthMedianPrice(7);
        toCopy.setMonthMaxPrice(8);
        toCopy.setMonthMinPrice(9);
        toCopy.setMonthSalesPerDay(10);
        toCopy.setMonthSales(11);
        toCopy.setDayAveragePrice(12);
        toCopy.setDayMedianPrice(13);
        toCopy.setDayMaxPrice(14);
        toCopy.setDayMinPrice(15);
        toCopy.setDaySales(16);
        toCopy.setPriorityToSellByMaxBuyPrice(17L);
        toCopy.setPriorityToSellByNextFancySellPrice(18L);
        toCopy.setPriorityToBuyByMinSellPrice(19L);
        toCopy.setPriorityToBuyIn1Hour(20L);
        toCopy.setPriorityToBuyIn6Hours(21L);
        toCopy.setPriorityToBuyIn24Hours(22L);
        toCopy.setPriorityToBuyIn168Hours(23L);
        toCopy.setPriorityToBuyIn720Hours(24L);
        toCopy.setPriceToBuyIn1Hour(25);
        toCopy.setPriceToBuyIn6Hours(26);
        toCopy.setPriceToBuyIn24Hours(27);
        toCopy.setPriceToBuyIn168Hours(28);
        toCopy.setPriceToBuyIn720Hours(29);

        Item item = new Item(toCopy);

        assertTrue(toCopy.isFullyEquals(item));
        assertNotSame(toCopy, item);
        assertNotSame(toCopy.getTags(), item.getTags());
    }

    @Test
    public void constructor_should_copy_all_fields_if_null() {
        Item toCopy = new Item();

        Item item = new Item(toCopy);

        assertTrue(toCopy.isFullyEquals(item));
    }

    @Test
    public void setRarityByTags_should_set_rarity_to_unknown_when_tags_are_null() {
        Item item = new Item();
        item.setTags(null);
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.UNKNOWN, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_rarity_to_unknown_when_tags_contains_no_rarity() {
        Item item = new Item();
        item.setTags(List.of("unknown"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.UNKNOWN, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_rarity_to_uncommon_when_tags_contains_uncommon() {
        Item item = new Item();
        item.setTags(List.of("uncommon"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.UNCOMMON, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_rarity_to_rare_when_tags_contains_rare() {
        Item item = new Item();
        item.setTags(List.of("rare"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.RARE, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_rarity_to_epic_when_tags_contains_epic() {
        Item item = new Item();
        item.setTags(List.of("epic"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.EPIC, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_rarity_to_legendary_when_tags_contains_legendary() {
        Item item = new Item();
        item.setTags(List.of("legendary"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.LEGENDARY, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_highest_rarity_if_contains_few_rare() {
        Item item = new Item();
        item.setTags(List.of("uncommon", "rare"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.RARE, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_highest_rarity_if_contains_few_epic() {
        Item item = new Item();
        item.setTags(List.of("uncommon", "rare", "epic"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.EPIC, item.getRarity());
    }

    @Test
    public void setRarityByTags_should_set_highest_rarity_if_contains_few_legendary() {
        Item item = new Item();
        item.setTags(List.of("uncommon", "rare", "epic", "legendary"));
        item.setRarityByTags("uncommon", "rare", "epic", "legendary");
        assertEquals(ItemRarity.LEGENDARY, item.getRarity());
    }

    @Test
    public void setMainFields_should_alter_main_fields() {
        Item item = new Item();

        ItemMainFieldsI mainFields = new Item();
        mainFields.setItemId("1");
        mainFields.setAssetUrl("url");
        mainFields.setName("name");
        mainFields.setTags(List.of("tag"));
        mainFields.setRarity(ItemRarity.UNCOMMON);
        mainFields.setType(ItemType.WeaponSkin);
        mainFields.setMaxBuyPrice(1);
        mainFields.setBuyOrdersCount(2);
        mainFields.setMinSellPrice(3);
        mainFields.setSellOrdersCount(4);
        mainFields.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        mainFields.setLastSoldPrice(5);
        item.setMainFields(mainFields);

        assertEquals("1", item.getItemId());
        assertEquals("url", item.getAssetUrl());
        assertEquals("name", item.getName());
        assertEquals(List.of("tag"), item.getTags());
        assertEquals(ItemRarity.UNCOMMON, item.getRarity());
        assertEquals(ItemType.WeaponSkin, item.getType());
        assertEquals(1, item.getMaxBuyPrice());
        assertEquals(2, item.getBuyOrdersCount());
        assertEquals(3, item.getMinSellPrice());
        assertEquals(4, item.getSellOrdersCount());
        assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), item.getLastSoldAt());
        assertEquals(5, item.getLastSoldPrice());
    }

    @Test
    public void updateCurrentPricesPriorities_should_update_fields() {
        Item item = new Item();
        item.updateCurrentPricesPriorities(1L, 2L, 3L);
        assertEquals(1L, item.getPriorityToSellByMaxBuyPrice());
        assertEquals(2L, item.getPriorityToSellByNextFancySellPrice());
        assertEquals(3L, item.getPriorityToBuyByMinSellPrice());
    }

    @Test
    public void isFullyEquals_should_return_true_for_same_objects() {
        Item item1 = new Item();
        item1.setItemId("1");
        item1.setAssetUrl("url1");
        item1.setName("name1");
        item1.setTags(List.of("tag1"));
        item1.setRarity(ItemRarity.UNCOMMON);
        item1.setType(ItemType.WeaponSkin);
        item1.setMaxBuyPrice(1);
        item1.setBuyOrdersCount(2);
        item1.setMinSellPrice(3);
        item1.setSellOrdersCount(4);
        item1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item1.setLastSoldPrice(5);
        item1.setMonthAveragePrice(6);
        item1.setMonthMedianPrice(7);
        item1.setMonthMaxPrice(8);
        item1.setMonthMinPrice(9);
        item1.setMonthSalesPerDay(10);
        item1.setMonthSales(11);
        item1.setDayAveragePrice(12);
        item1.setDayMedianPrice(13);
        item1.setDayMaxPrice(14);
        item1.setDayMinPrice(15);
        item1.setDaySales(16);
        item1.setPriorityToSellByMaxBuyPrice(17L);
        item1.setPriorityToSellByNextFancySellPrice(18L);
        item1.setPriorityToBuyByMinSellPrice(19L);
        item1.setPriorityToBuyIn1Hour(20L);
        item1.setPriorityToBuyIn6Hours(21L);
        item1.setPriorityToBuyIn24Hours(22L);
        item1.setPriorityToBuyIn168Hours(23L);
        item1.setPriorityToBuyIn720Hours(24L);
        item1.setPriceToBuyIn1Hour(25);
        item1.setPriceToBuyIn6Hours(26);
        item1.setPriceToBuyIn24Hours(27);
        item1.setPriceToBuyIn168Hours(28);
        item1.setPriceToBuyIn720Hours(29);

        Item item2 = new Item();
        item2.setItemId("1");
        item2.setAssetUrl("url1");
        item2.setName("name1");
        item2.setTags(List.of("tag1"));
        item2.setRarity(ItemRarity.UNCOMMON);
        item2.setType(ItemType.WeaponSkin);
        item2.setMaxBuyPrice(1);
        item2.setBuyOrdersCount(2);
        item2.setMinSellPrice(3);
        item2.setSellOrdersCount(4);
        item2.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item2.setLastSoldPrice(5);
        item2.setMonthAveragePrice(6);
        item2.setMonthMedianPrice(7);
        item2.setMonthMaxPrice(8);
        item2.setMonthMinPrice(9);
        item2.setMonthSalesPerDay(10);
        item2.setMonthSales(11);
        item2.setDayAveragePrice(12);
        item2.setDayMedianPrice(13);
        item2.setDayMaxPrice(14);
        item2.setDayMinPrice(15);
        item2.setDaySales(16);
        item2.setPriorityToSellByMaxBuyPrice(17L);
        item2.setPriorityToSellByNextFancySellPrice(18L);
        item2.setPriorityToBuyByMinSellPrice(19L);
        item2.setPriorityToBuyIn1Hour(20L);
        item2.setPriorityToBuyIn6Hours(21L);
        item2.setPriorityToBuyIn24Hours(22L);
        item2.setPriorityToBuyIn168Hours(23L);
        item2.setPriorityToBuyIn720Hours(24L);
        item2.setPriceToBuyIn1Hour(25);
        item2.setPriceToBuyIn6Hours(26);
        item2.setPriceToBuyIn24Hours(27);
        item2.setPriceToBuyIn168Hours(28);
        item2.setPriceToBuyIn720Hours(29);

        assertTrue(item1.isFullyEquals(item2));
    }

    @Test
    public void isFullyEquals_should_return_false_for_different_objects() {
        Item item1 = new Item();
        item1.setItemId("1");
        item1.setAssetUrl("url1");
        item1.setName("name1");
        item1.setTags(List.of("tag1"));
        item1.setRarity(ItemRarity.UNCOMMON);
        item1.setType(ItemType.WeaponSkin);
        item1.setMaxBuyPrice(1);
        item1.setBuyOrdersCount(2);
        item1.setMinSellPrice(3);
        item1.setSellOrdersCount(4);
        item1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item1.setLastSoldPrice(5);
        item1.setMonthAveragePrice(6);
        item1.setMonthMedianPrice(7);
        item1.setMonthMaxPrice(8);
        item1.setMonthMinPrice(9);
        item1.setMonthSalesPerDay(10);
        item1.setMonthSales(11);
        item1.setDayAveragePrice(12);
        item1.setDayMedianPrice(13);
        item1.setDayMaxPrice(14);
        item1.setDayMinPrice(15);
        item1.setDaySales(16);
        item1.setPriorityToSellByMaxBuyPrice(17L);
        item1.setPriorityToSellByNextFancySellPrice(18L);
        item1.setPriorityToBuyByMinSellPrice(19L);
        item1.setPriorityToBuyIn1Hour(20L);
        item1.setPriorityToBuyIn6Hours(21L);
        item1.setPriorityToBuyIn24Hours(22L);
        item1.setPriorityToBuyIn168Hours(23L);
        item1.setPriorityToBuyIn720Hours(24L);
        item1.setPriceToBuyIn1Hour(25);
        item1.setPriceToBuyIn6Hours(26);
        item1.setPriceToBuyIn24Hours(27);
        item1.setPriceToBuyIn168Hours(28);
        item1.setPriceToBuyIn720Hours(29);

        Item item2 = new Item();
        item2.setItemId("1");
        item2.setAssetUrl("url1");
        item2.setName("name1");
        item2.setTags(List.of("tag1"));
        item2.setRarity(ItemRarity.UNCOMMON);
        item2.setType(ItemType.WeaponSkin);
        item2.setMaxBuyPrice(1);
        item2.setBuyOrdersCount(2);
        item2.setMinSellPrice(3);
        item2.setSellOrdersCount(4);
        item2.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item2.setLastSoldPrice(5);
        item2.setMonthAveragePrice(6);
        item2.setMonthMedianPrice(7);
        item2.setMonthMaxPrice(8);
        item2.setMonthMinPrice(9);
        item2.setMonthSalesPerDay(10);
        item2.setMonthSales(11);
        item2.setDayAveragePrice(12);
        item2.setDayMedianPrice(13);
        item2.setDayMaxPrice(14);
        item2.setDayMinPrice(15);
        item2.setDaySales(16);
        item2.setPriorityToSellByMaxBuyPrice(17L);
        item2.setPriorityToSellByNextFancySellPrice(18L);
        item2.setPriorityToBuyByMinSellPrice(19L);
        item2.setPriorityToBuyIn1Hour(20L);
        item2.setPriorityToBuyIn6Hours(21L);
        item2.setPriorityToBuyIn24Hours(22L);
        item2.setPriorityToBuyIn168Hours(23L);
        item2.setPriorityToBuyIn720Hours(24L);
        item2.setPriceToBuyIn1Hour(25);
        item2.setPriceToBuyIn6Hours(26);
        item2.setPriceToBuyIn24Hours(27);
        item2.setPriceToBuyIn168Hours(28);
        item2.setPriceToBuyIn720Hours(29);

        item1.setItemId("2");
        assertFalse(item1.isFullyEquals(item2));
        item1.setItemId("1");
        item1.setAssetUrl("url2");
        assertFalse(item1.isFullyEquals(item2));
        item1.setAssetUrl("url1");
        item1.setName("name2");
        assertFalse(item1.isFullyEquals(item2));
        item1.setName("name1");
        item1.setTags(List.of("tag2"));
        assertFalse(item1.isFullyEquals(item2));
        item1.setTags(List.of("tag1"));
        item1.setRarity(ItemRarity.RARE);
        assertFalse(item1.isFullyEquals(item2));
        item1.setRarity(ItemRarity.UNCOMMON);
        item1.setType(ItemType.Charm);
        assertFalse(item1.isFullyEquals(item2));
        item1.setType(ItemType.WeaponSkin);
        item1.setMaxBuyPrice(10);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMaxBuyPrice(1);
        item1.setBuyOrdersCount(20);
        assertFalse(item1.isFullyEquals(item2));
        item1.setBuyOrdersCount(2);
        item1.setMinSellPrice(30);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMinSellPrice(3);
        item1.setSellOrdersCount(40);
        assertFalse(item1.isFullyEquals(item2));
        item1.setSellOrdersCount(4);
        item1.setLastSoldAt(LocalDateTime.of(2020, 1, 1, 1, 1));
        assertFalse(item1.isFullyEquals(item2));
        item1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item1.setLastSoldPrice(50);
        assertFalse(item1.isFullyEquals(item2));
        item1.setLastSoldPrice(5);
        item1.setMonthAveragePrice(60);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMonthAveragePrice(6);
        item1.setMonthMedianPrice(70);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMonthMedianPrice(7);
        item1.setMonthMaxPrice(80);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMonthMaxPrice(8);
        item1.setMonthMinPrice(90);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMonthMinPrice(9);
        item1.setMonthSalesPerDay(100);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMonthSalesPerDay(10);
        item1.setMonthSales(110);
        assertFalse(item1.isFullyEquals(item2));
        item1.setMonthSales(11);
        item1.setDayAveragePrice(120);
        assertFalse(item1.isFullyEquals(item2));
        item1.setDayAveragePrice(12);
        item1.setDayMedianPrice(130);
        assertFalse(item1.isFullyEquals(item2));
        item1.setDayMedianPrice(13);
        item1.setDayMaxPrice(140);
        assertFalse(item1.isFullyEquals(item2));
        item1.setDayMaxPrice(14);
        item1.setDayMinPrice(150);
        assertFalse(item1.isFullyEquals(item2));
        item1.setDayMinPrice(15);
        item1.setDaySales(160);
        assertFalse(item1.isFullyEquals(item2));
        item1.setDaySales(16);
        item1.setPriorityToSellByMaxBuyPrice(170L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToSellByMaxBuyPrice(17L);
        item1.setPriorityToSellByNextFancySellPrice(180L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToSellByNextFancySellPrice(18L);
        item1.setPriorityToBuyByMinSellPrice(190L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToBuyByMinSellPrice(19L);
        item1.setPriorityToBuyIn1Hour(200L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToBuyIn1Hour(20L);
        item1.setPriorityToBuyIn6Hours(210L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToBuyIn6Hours(21L);
        item1.setPriorityToBuyIn24Hours(220L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToBuyIn24Hours(22L);
        item1.setPriorityToBuyIn168Hours(230L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToBuyIn168Hours(23L);
        item1.setPriorityToBuyIn720Hours(240L);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriorityToBuyIn720Hours(24L);
        item1.setPriceToBuyIn1Hour(250);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriceToBuyIn1Hour(25);
        item1.setPriceToBuyIn6Hours(260);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriceToBuyIn6Hours(26);
        item1.setPriceToBuyIn24Hours(270);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriceToBuyIn24Hours(27);
        item1.setPriceToBuyIn168Hours(280);
        assertFalse(item1.isFullyEquals(item2));
        item1.setPriceToBuyIn168Hours(28);
        item1.setPriceToBuyIn720Hours(290);
        assertFalse(item1.isFullyEquals(item2));
    }

    @Test
    public void hashCode_should_be_same_for_different_objects_same_itemId() {
        Item item1 = new Item();
        item1.setItemId("1");
        item1.setAssetUrl("url1");
        item1.setName("name1");
        item1.setTags(List.of("tag1"));
        item1.setRarity(ItemRarity.UNCOMMON);
        item1.setType(ItemType.WeaponSkin);
        item1.setMaxBuyPrice(1);
        item1.setBuyOrdersCount(2);
        item1.setMinSellPrice(3);
        item1.setSellOrdersCount(4);
        item1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item1.setLastSoldPrice(5);
        item1.setMonthAveragePrice(6);
        item1.setMonthMedianPrice(7);
        item1.setMonthMaxPrice(8);
        item1.setMonthMinPrice(9);
        item1.setMonthSalesPerDay(10);
        item1.setMonthSales(11);
        item1.setDayAveragePrice(12);
        item1.setDayMedianPrice(13);
        item1.setDayMaxPrice(14);
        item1.setDayMinPrice(15);
        item1.setDaySales(16);
        item1.setPriorityToSellByMaxBuyPrice(17L);
        item1.setPriorityToSellByNextFancySellPrice(18L);
        item1.setPriorityToBuyByMinSellPrice(19L);
        item1.setPriorityToBuyIn1Hour(20L);
        item1.setPriorityToBuyIn6Hours(21L);
        item1.setPriorityToBuyIn24Hours(22L);
        item1.setPriorityToBuyIn168Hours(23L);
        item1.setPriorityToBuyIn720Hours(24L);
        item1.setPriceToBuyIn1Hour(25);
        item1.setPriceToBuyIn6Hours(26);
        item1.setPriceToBuyIn24Hours(27);
        item1.setPriceToBuyIn168Hours(28);
        item1.setPriceToBuyIn720Hours(29);

        Item item2 = new Item();
        item2.setItemId("1");
        item2.setAssetUrl("url2");
        item2.setName("name2");
        item2.setTags(List.of("tag2"));
        item2.setRarity(ItemRarity.RARE);
        item2.setType(ItemType.Charm);
        item2.setMaxBuyPrice(10);
        item2.setBuyOrdersCount(20);
        item2.setMinSellPrice(30);
        item2.setSellOrdersCount(40);
        item2.setLastSoldAt(LocalDateTime.of(2020, 1, 1, 1, 1));
        item2.setLastSoldPrice(50);
        item2.setMonthAveragePrice(60);
        item2.setMonthMedianPrice(70);
        item2.setMonthMaxPrice(80);
        item2.setMonthMinPrice(90);
        item2.setMonthSalesPerDay(100);
        item2.setMonthSales(110);
        item2.setDayAveragePrice(120);
        item2.setDayMedianPrice(130);
        item2.setDayMaxPrice(140);
        item2.setDayMinPrice(150);
        item2.setDaySales(160);
        item2.setPriorityToSellByMaxBuyPrice(170L);
        item2.setPriorityToSellByNextFancySellPrice(180L);
        item2.setPriorityToBuyByMinSellPrice(190L);
        item2.setPriorityToBuyIn1Hour(200L);
        item2.setPriorityToBuyIn6Hours(210L);
        item2.setPriorityToBuyIn24Hours(220L);
        item2.setPriorityToBuyIn168Hours(230L);
        item2.setPriorityToBuyIn720Hours(240L);
        item2.setPriceToBuyIn1Hour(250);
        item2.setPriceToBuyIn6Hours(260);
        item2.setPriceToBuyIn24Hours(270);
        item2.setPriceToBuyIn168Hours(280);
        item2.setPriceToBuyIn720Hours(290);

        assertEquals(item2.hashCode(), item2.hashCode());
    }

    @Test
    public void hashCode_should_be_different_for_different_itemId() {
        Item item1 = new Item();
        item1.setItemId("1");

        Item item2 = new Item();
        item2.setItemId("2");

        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    public void equals_should_be_true_for_different_objects_same_itemId() {
        Item item1 = new Item();
        item1.setItemId("1");
        item1.setAssetUrl("url1");
        item1.setName("name1");
        item1.setTags(List.of("tag1"));
        item1.setRarity(ItemRarity.UNCOMMON);
        item1.setType(ItemType.WeaponSkin);
        item1.setMaxBuyPrice(1);
        item1.setBuyOrdersCount(2);
        item1.setMinSellPrice(3);
        item1.setSellOrdersCount(4);
        item1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item1.setLastSoldPrice(5);
        item1.setMonthAveragePrice(6);
        item1.setMonthMedianPrice(7);
        item1.setMonthMaxPrice(8);
        item1.setMonthMinPrice(9);
        item1.setMonthSalesPerDay(10);
        item1.setMonthSales(11);
        item1.setDayAveragePrice(12);
        item1.setDayMedianPrice(13);
        item1.setDayMaxPrice(14);
        item1.setDayMinPrice(15);
        item1.setDaySales(16);
        item1.setPriorityToSellByMaxBuyPrice(17L);
        item1.setPriorityToSellByNextFancySellPrice(18L);
        item1.setPriorityToBuyByMinSellPrice(19L);
        item1.setPriorityToBuyIn1Hour(20L);
        item1.setPriorityToBuyIn6Hours(21L);
        item1.setPriorityToBuyIn24Hours(22L);
        item1.setPriorityToBuyIn168Hours(23L);
        item1.setPriorityToBuyIn720Hours(24L);
        item1.setPriceToBuyIn1Hour(25);
        item1.setPriceToBuyIn6Hours(26);
        item1.setPriceToBuyIn24Hours(27);
        item1.setPriceToBuyIn168Hours(28);
        item1.setPriceToBuyIn720Hours(29);

        Item item2 = new Item();
        item2.setItemId("1");
        item2.setAssetUrl("url2");
        item2.setName("name2");
        item2.setTags(List.of("tag2"));
        item2.setRarity(ItemRarity.RARE);
        item2.setType(ItemType.Charm);
        item2.setMaxBuyPrice(10);
        item2.setBuyOrdersCount(20);
        item2.setMinSellPrice(30);
        item2.setSellOrdersCount(40);
        item2.setLastSoldAt(LocalDateTime.of(2020, 1, 1, 1, 1));
        item2.setLastSoldPrice(50);
        item2.setMonthAveragePrice(60);
        item2.setMonthMedianPrice(70);
        item2.setMonthMaxPrice(80);
        item2.setMonthMinPrice(90);
        item2.setMonthSalesPerDay(100);
        item2.setMonthSales(110);
        item2.setDayAveragePrice(120);
        item2.setDayMedianPrice(130);
        item2.setDayMaxPrice(140);
        item2.setDayMinPrice(150);
        item2.setDaySales(160);
        item2.setPriorityToSellByMaxBuyPrice(170L);
        item2.setPriorityToSellByNextFancySellPrice(180L);
        item2.setPriorityToBuyByMinSellPrice(190L);
        item2.setPriorityToBuyIn1Hour(200L);
        item2.setPriorityToBuyIn6Hours(210L);
        item2.setPriorityToBuyIn24Hours(220L);
        item2.setPriorityToBuyIn168Hours(230L);
        item2.setPriorityToBuyIn720Hours(240L);
        item2.setPriceToBuyIn1Hour(250);
        item2.setPriceToBuyIn6Hours(260);
        item2.setPriceToBuyIn24Hours(270);
        item2.setPriceToBuyIn168Hours(280);
        item2.setPriceToBuyIn720Hours(290);

        assertEquals(item2, item1);
    }

    @Test
    public void equals_should_be_false_for_different_itemId() {
        Item item1 = new Item();
        item1.setItemId("1");

        Item item2 = new Item();
        item2.setItemId("2");

        assertNotEquals(item1, item2);
    }

    @Test
    public void equals_should_be_false_for_null() {
        Item item1 = new Item();
        item1.setItemId("1");

        Item item2 = null;

        assertNotEquals(item2, item1);
    }

    @Test
    public void equals_should_be_false_for_object_of_another_class() {
        Item item1 = new Item();
        item1.setItemId("1");

        Item item2 = new Item();

        assertNotEquals(item2, item1);
    }
}