package github.ricemonger.marketplace.databases.postgres.services.entity_mappers.item;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.full_entities.item.TagEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ItemEntityMapperTest {
    @Autowired
    private ItemEntityMapper itemEntityMapper;

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
}