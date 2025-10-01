package github.ricemonger.item_trade_stats_calculator.postgres.services.entity_mappers.item;

import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemCurrentPricesRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemHistoryFields;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemRecalculationRequiredFields;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemHistoryFieldsI;
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
class ItemEntitiesMapperTest {

    @Autowired
    private ItemEntitiesMapper itemEntitiesMapper;

    @Test
    public void createRecalculationRequiredFieldsDTO_should_return_expected_result() {
        ItemRecalculationRequiredFieldsProjection projection = new ItemRecalculationRequiredFieldsProjection();
        projection.setItemId("itemId");
        projection.setRarity(ItemRarity.RARE);
        projection.setMaxBuyPrice(1);
        projection.setBuyOrdersCount(2);
        projection.setMinSellPrice(3);
        projection.setSellOrdersCount(4);

        ItemRecalculationRequiredFields result = itemEntitiesMapper.createRecalculationRequiredFieldsDTO(projection);

        assertEquals(projection.getItemId(), result.getItemId());
        assertEquals(projection.getRarity(), result.getRarity());
        assertEquals(projection.getMaxBuyPrice(), result.getMaxBuyPrice());
        assertEquals(projection.getBuyOrdersCount(), result.getBuyOrdersCount());
        assertEquals(projection.getMinSellPrice(), result.getMinSellPrice());
        assertEquals(projection.getSellOrdersCount(), result.getSellOrdersCount());
    }

    @Test
    public void createHistoryFieldsDtoProjection_should_return_expected_result() {
        ItemHistoryFieldsI dto = new ItemHistoryFields();

        dto.setItemId("itemId");
        dto.setMonthAveragePrice(1);
        dto.setMonthMedianPrice(2);
        dto.setMonthMaxPrice(3);
        dto.setMonthMinPrice(4);
        dto.setMonthSalesPerDay(5);
        dto.setMonthSales(6);
        dto.setDayAveragePrice(7);
        dto.setDayMedianPrice(8);
        dto.setDayMaxPrice(9);
        dto.setDayMinPrice(10);
        dto.setDaySales(11);
        dto.setPriorityToSellByMaxBuyPrice(12L);
        dto.setPriorityToSellByNextFancySellPrice(13L);
        dto.setPriorityToBuyByMinSellPrice(14L);
        dto.setPriorityToBuyIn1Hour(15L);
        dto.setPriorityToBuyIn6Hours(16L);
        dto.setPriorityToBuyIn24Hours(17L);
        dto.setPriorityToBuyIn168Hours(18L);
        dto.setPriorityToBuyIn720Hours(19L);
        dto.setPriceToBuyIn1Hour(20);
        dto.setPriceToBuyIn6Hours(21);
        dto.setPriceToBuyIn24Hours(22);
        dto.setPriceToBuyIn168Hours(23);
        dto.setPriceToBuyIn720Hours(24);

        ItemHistoryFieldsProjection result = itemEntitiesMapper.createHistoryFieldsDtoProjection(dto);

        assertEquals(dto.getItemId(), result.getItemId());
        assertEquals(dto.getMonthAveragePrice(), result.getMonthAveragePrice());
        assertEquals(dto.getMonthMedianPrice(), result.getMonthMedianPrice());
        assertEquals(dto.getMonthMaxPrice(), result.getMonthMaxPrice());
        assertEquals(dto.getMonthMinPrice(), result.getMonthMinPrice());
        assertEquals(dto.getMonthSalesPerDay(), result.getMonthSalesPerDay());
        assertEquals(dto.getMonthSales(), result.getMonthSales());
        assertEquals(dto.getDayAveragePrice(), result.getDayAveragePrice());
        assertEquals(dto.getDayMedianPrice(), result.getDayMedianPrice());
        assertEquals(dto.getDayMaxPrice(), result.getDayMaxPrice());
        assertEquals(dto.getDayMinPrice(), result.getDayMinPrice());
        assertEquals(dto.getDaySales(), result.getDaySales());
        assertEquals(dto.getPriorityToSellByMaxBuyPrice(), result.getPriorityToSellByMaxBuyPrice());
        assertEquals(dto.getPriorityToSellByNextFancySellPrice(), result.getPriorityToSellByNextFancySellPrice());
        assertEquals(dto.getPriorityToBuyByMinSellPrice(), result.getPriorityToBuyByMinSellPrice());
        assertEquals(dto.getPriorityToBuyIn1Hour(), result.getPriorityToBuyIn1Hour());
        assertEquals(dto.getPriorityToBuyIn6Hours(), result.getPriorityToBuyIn6Hours());
        assertEquals(dto.getPriorityToBuyIn24Hours(), result.getPriorityToBuyIn24Hours());
        assertEquals(dto.getPriorityToBuyIn168Hours(), result.getPriorityToBuyIn168Hours());
        assertEquals(dto.getPriorityToBuyIn720Hours(), result.getPriorityToBuyIn720Hours());
        assertEquals(dto.getPriceToBuyIn1Hour(), result.getPriceToBuyIn1Hour());
        assertEquals(dto.getPriceToBuyIn6Hours(), result.getPriceToBuyIn6Hours());
        assertEquals(dto.getPriceToBuyIn24Hours(), result.getPriceToBuyIn24Hours());
        assertEquals(dto.getPriceToBuyIn168Hours(), result.getPriceToBuyIn168Hours());
        assertEquals(dto.getPriceToBuyIn720Hours(), result.getPriceToBuyIn720Hours());
    }

    @Test
    public void createItem_should_properly_map_entity_to_dto() {
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

        Item item = itemEntitiesMapper.createItem(entity);

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
    public void createCurrentPricesHistoryFieldsProjection_should_return_expected_result() {
        ItemHistoryFieldsI item = new Item();
        item.setItemId("itemId");
        item.setPriorityToSellByMaxBuyPrice(12L);
        item.setPriorityToSellByMaxBuyPrice(13L);
        item.setPriorityToBuyByMinSellPrice(14L);

        ItemCurrentPricesHistoryFieldsProjection result = itemEntitiesMapper.createCurrentPricesHistoryFieldsProjection(item);

        assertEquals(item.getItemId(), result.getItemId());
        assertEquals(item.getPriorityToSellByMaxBuyPrice(), result.getPriorityToSellByMaxBuyPrice());
        assertEquals(item.getPriorityToSellByNextFancySellPrice(), result.getPriorityToSellByNextFancySellPrice());
    }

    @Test
    public void createCurrentPricesRecalculationRequiredFields_should_return_expected_result() {
        ItemCurrentPricesRecalculationRequiredFieldsProjection projection = new ItemCurrentPricesRecalculationRequiredFieldsProjection();
        projection.setItemId("itemId");
        projection.setRarity(ItemRarity.RARE);
        projection.setMaxBuyPrice(1);
        projection.setMinSellPrice(3);
        projection.setSellOrdersCount(4);
        projection.setMonthMedianPrice(7);
        projection.setMonthSalesPerDay(10);
        projection.setMonthSales(11);

        ItemCurrentPricesRecalculationRequiredFields result = itemEntitiesMapper.createCurrentPricesRecalculationRequiredFields(projection);

        assertEquals(projection.getItemId(), result.getItemId());
        assertEquals(projection.getRarity(), result.getRarity());
        assertEquals(projection.getMaxBuyPrice(), result.getMaxBuyPrice());
        assertEquals(projection.getMinSellPrice(), result.getMinSellPrice());
        assertEquals(projection.getSellOrdersCount(), result.getSellOrdersCount());
        assertEquals(projection.getMonthMedianPrice(), result.getMonthMedianPrice());
        assertEquals(projection.getMonthSalesPerDay(), result.getMonthSalesPerDay());
        assertEquals(projection.getMonthSales(), result.getMonthSales());
    }

}