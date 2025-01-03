package github.ricemonger.marketplace.databases.postgres.services.entity_mappers.item;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.entities.item.TagEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.TagPostgresRepository;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemEntityMapperTest {
    @Autowired
    private ItemEntityMapper itemEntityMapper;
    @MockBean
    private TagPostgresRepository tagRepository;

    @Test
    public void createDTO_should_properly_map_entity_to_dto() {
        ItemEntity entity = new ItemEntity();
        entity.setItemId("itemId");
        entity.setAssetUrl("assetUrl");
        entity.setName("name");
        TagEntity tagEntity1 = new TagEntity();
        tagEntity1.setValue("tag1");
        TagEntity tagEntity2 = new TagEntity();
        tagEntity2.setValue("tag2");
        entity.setTags(List.of(tagEntity1, tagEntity2));
        entity.setRarity(ItemRarity.RARE);
        entity.setType(ItemType.WeaponSkin);
        entity.setMaxBuyPrice(1);
        entity.setBuyOrdersCount(2);
        entity.setMinSellPrice(3);
        entity.setSellOrdersCount(4);
        entity.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        entity.setLastSoldPrice(5);
        entity.setMonthAveragePrice(6);
        entity.setMonthMedianPrice(7);
        entity.setMonthMaxPrice(8);
        entity.setMonthMinPrice(9);
        entity.setMonthSalesPerDay(10);
        entity.setMonthSales(11);
        entity.setDayAveragePrice(12);
        entity.setDayMedianPrice(13);
        entity.setDayMaxPrice(14);
        entity.setDayMinPrice(15);
        entity.setDaySales(16);
        entity.setPriorityToSellByMaxBuyPrice(17L);
        entity.setPriorityToSellByNextFancySellPrice(18L);
        entity.setPriorityToBuyByMinSellPrice(19L);
        entity.setPriorityToBuyIn1Hour(20L);
        entity.setPriorityToBuyIn6Hours(21L);
        entity.setPriorityToBuyIn24Hours(22L);
        entity.setPriorityToBuyIn168Hours(23L);
        entity.setPriorityToBuyIn720Hours(24L);
        entity.setPriceToBuyIn1Hour(25);
        entity.setPriceToBuyIn6Hours(26);
        entity.setPriceToBuyIn24Hours(27);
        entity.setPriceToBuyIn168Hours(28);
        entity.setPriceToBuyIn720Hours(29);

        Item item = itemEntityMapper.createDTO(entity);

        assertEquals(entity.getItemId(), item.getItemId());
        assertEquals(entity.getAssetUrl(), item.getAssetUrl());
        assertEquals(entity.getName(), item.getName());
        assertTrue(item.getTags().contains("tag1") && item.getTags().contains("tag2"));
        assertEquals(entity.getRarity(), item.getRarity());
        assertEquals(entity.getType(), item.getType());
        assertEquals(entity.getMaxBuyPrice(), item.getMaxBuyPrice());
        assertEquals(entity.getBuyOrdersCount(), item.getBuyOrdersCount());
        assertEquals(entity.getMinSellPrice(), item.getMinSellPrice());
        assertEquals(entity.getSellOrdersCount(), item.getSellOrdersCount());
        assertEquals(entity.getLastSoldAt(), item.getLastSoldAt());
        assertEquals(entity.getLastSoldPrice(), item.getLastSoldPrice());
        assertEquals(entity.getMonthAveragePrice(), item.getMonthAveragePrice());
        assertEquals(entity.getMonthMedianPrice(), item.getMonthMedianPrice());
        assertEquals(entity.getMonthMaxPrice(), item.getMonthMaxPrice());
        assertEquals(entity.getMonthMinPrice(), item.getMonthMinPrice());
        assertEquals(entity.getMonthSalesPerDay(), item.getMonthSalesPerDay());
        assertEquals(entity.getMonthSales(), item.getMonthSales());
        assertEquals(entity.getDayAveragePrice(), item.getDayAveragePrice());
        assertEquals(entity.getDayMedianPrice(), item.getDayMedianPrice());
        assertEquals(entity.getDayMaxPrice(), item.getDayMaxPrice());
        assertEquals(entity.getDayMinPrice(), item.getDayMinPrice());
        assertEquals(entity.getDaySales(), item.getDaySales());
        assertEquals(entity.getPriorityToSellByMaxBuyPrice(), item.getPriorityToSellByMaxBuyPrice());
        assertEquals(entity.getPriorityToSellByNextFancySellPrice(), item.getPriorityToSellByNextFancySellPrice());
        assertEquals(entity.getPriorityToBuyByMinSellPrice(), item.getPriorityToBuyByMinSellPrice());
        assertEquals(entity.getPriorityToBuyIn1Hour(), item.getPriorityToBuyIn1Hour());
        assertEquals(entity.getPriorityToBuyIn6Hours(), item.getPriorityToBuyIn6Hours());
        assertEquals(entity.getPriorityToBuyIn24Hours(), item.getPriorityToBuyIn24Hours());
        assertEquals(entity.getPriorityToBuyIn168Hours(), item.getPriorityToBuyIn168Hours());
        assertEquals(entity.getPriorityToBuyIn720Hours(), item.getPriorityToBuyIn720Hours());
        assertEquals(entity.getPriceToBuyIn1Hour(), item.getPriceToBuyIn1Hour());
        assertEquals(entity.getPriceToBuyIn6Hours(), item.getPriceToBuyIn6Hours());
        assertEquals(entity.getPriceToBuyIn24Hours(), item.getPriceToBuyIn24Hours());
        assertEquals(entity.getPriceToBuyIn168Hours(), item.getPriceToBuyIn168Hours());
        assertEquals(entity.getPriceToBuyIn720Hours(), item.getPriceToBuyIn720Hours());
    }

    @Test
    public void createEntities_should_properly_map_entities() {
        Item item1 = new Item();
        item1.setItemId("itemId");
        item1.setAssetUrl("assetUrl");
        item1.setName("name");
        item1.setTags(List.of("tag"));
        item1.setRarity(ItemRarity.RARE);
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
        item2.setItemId("itemId1");
        item2.setAssetUrl("assetUrl1");
        item2.setName("name1");
        item2.setTags(List.of("tag1"));
        item2.setRarity(ItemRarity.EPIC);
        item2.setType(ItemType.Charm);
        item2.setMaxBuyPrice(2);
        item2.setBuyOrdersCount(3);
        item2.setMinSellPrice(4);
        item2.setSellOrdersCount(5);
        item2.setLastSoldAt(LocalDateTime.of(2022, 1, 1, 1, 1));
        item2.setLastSoldPrice(6);
        item2.setMonthAveragePrice(7);
        item2.setMonthMedianPrice(8);
        item2.setMonthMaxPrice(9);
        item2.setMonthMinPrice(10);
        item2.setMonthSalesPerDay(11);
        item2.setMonthSales(12);
        item2.setDayAveragePrice(13);
        item2.setDayMedianPrice(14);
        item2.setDayMaxPrice(15);
        item2.setDayMinPrice(16);
        item2.setDaySales(17);
        item2.setPriorityToSellByMaxBuyPrice(18L);
        item2.setPriorityToSellByNextFancySellPrice(19L);
        item2.setPriorityToBuyByMinSellPrice(20L);
        item2.setPriorityToBuyIn1Hour(21L);
        item2.setPriorityToBuyIn6Hours(22L);
        item2.setPriorityToBuyIn24Hours(23L);
        item2.setPriorityToBuyIn168Hours(24L);
        item2.setPriorityToBuyIn720Hours(25L);
        item2.setPriceToBuyIn1Hour(26);
        item2.setPriceToBuyIn6Hours(27);
        item2.setPriceToBuyIn24Hours(28);
        item2.setPriceToBuyIn168Hours(29);
        item2.setPriceToBuyIn720Hours(30);

        TagEntity tagEntity1 = new TagEntity();
        tagEntity1.setValue("tag");
        TagEntity tagEntity2 = new TagEntity();
        tagEntity2.setValue("tag1");

        when(tagRepository.findAll()).thenReturn(List.of(tagEntity1, tagEntity2));

        ItemEntity entity1 = new ItemEntity();
        entity1.setItemId("itemId");
        entity1.setAssetUrl("assetUrl");
        entity1.setName("name");
        entity1.setTags(List.of(tagEntity1));
        entity1.setRarity(ItemRarity.RARE);
        entity1.setType(ItemType.WeaponSkin);
        entity1.setMaxBuyPrice(1);
        entity1.setBuyOrdersCount(2);
        entity1.setMinSellPrice(3);
        entity1.setSellOrdersCount(4);
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        entity1.setLastSoldPrice(5);
        entity1.setMonthAveragePrice(6);
        entity1.setMonthMedianPrice(7);
        entity1.setMonthMaxPrice(8);
        entity1.setMonthMinPrice(9);
        entity1.setMonthSalesPerDay(10);
        entity1.setMonthSales(11);
        entity1.setDayAveragePrice(12);
        entity1.setDayMedianPrice(13);
        entity1.setDayMaxPrice(14);
        entity1.setDayMinPrice(15);
        entity1.setDaySales(16);
        entity1.setPriorityToSellByMaxBuyPrice(17L);
        entity1.setPriorityToSellByNextFancySellPrice(18L);
        entity1.setPriorityToBuyByMinSellPrice(19L);
        entity1.setPriorityToBuyIn1Hour(20L);
        entity1.setPriorityToBuyIn6Hours(21L);
        entity1.setPriorityToBuyIn24Hours(22L);
        entity1.setPriorityToBuyIn168Hours(23L);
        entity1.setPriorityToBuyIn720Hours(24L);
        entity1.setPriceToBuyIn1Hour(25);
        entity1.setPriceToBuyIn6Hours(26);
        entity1.setPriceToBuyIn24Hours(27);
        entity1.setPriceToBuyIn168Hours(28);
        entity1.setPriceToBuyIn720Hours(29);

        ItemEntity entity2 = new ItemEntity();
        entity2.setItemId("itemId1");
        entity2.setAssetUrl("assetUrl1");
        entity2.setName("name1");
        entity2.setTags(List.of(tagEntity2));
        entity2.setRarity(ItemRarity.EPIC);
        entity2.setType(ItemType.Charm);
        entity2.setMaxBuyPrice(2);
        entity2.setBuyOrdersCount(3);
        entity2.setMinSellPrice(4);
        entity2.setSellOrdersCount(5);
        entity2.setLastSoldAt(LocalDateTime.of(2022, 1, 1, 1, 1));
        entity2.setLastSoldPrice(6);
        entity2.setMonthAveragePrice(7);
        entity2.setMonthMedianPrice(8);
        entity2.setMonthMaxPrice(9);
        entity2.setMonthMinPrice(10);
        entity2.setMonthSalesPerDay(11);
        entity2.setMonthSales(12);
        entity2.setDayAveragePrice(13);
        entity2.setDayMedianPrice(14);
        entity2.setDayMaxPrice(15);
        entity2.setDayMinPrice(16);
        entity2.setDaySales(17);
        entity2.setPriorityToSellByMaxBuyPrice(18L);
        entity2.setPriorityToSellByNextFancySellPrice(19L);
        entity2.setPriorityToBuyByMinSellPrice(20L);
        entity2.setPriorityToBuyIn1Hour(21L);
        entity2.setPriorityToBuyIn6Hours(22L);
        entity2.setPriorityToBuyIn24Hours(23L);
        entity2.setPriorityToBuyIn168Hours(24L);
        entity2.setPriorityToBuyIn720Hours(25L);
        entity2.setPriceToBuyIn1Hour(26);
        entity2.setPriceToBuyIn6Hours(27);
        entity2.setPriceToBuyIn24Hours(28);
        entity2.setPriceToBuyIn168Hours(29);
        entity2.setPriceToBuyIn720Hours(30);

        List<ItemEntity> expected = List.of(entity1, entity2);

        List<ItemEntity> actual = itemEntityMapper.createEntities(List.of(item1, item2));

        assertTrue(expected.stream().allMatch(ex -> actual.stream().anyMatch(ac -> ac.isFullyEqual(ex))) && expected.size() == actual.size());
    }
}