package github.ricemonger.item_trade_stats_calculator.postgres.repositories;

import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemRecalculationRequiredFieldsProjection;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ItemPostgresRepositoryTest {
    @Autowired
    private ItemPostgresRepository itemPostgresRepository;

    @BeforeEach
    void setUp() {
        itemPostgresRepository.deleteAll();
    }

    @Test
    public void updateAllHistoryFields_should_update_by_projectionsItems() {
        ItemEntity existingItem1 = new ItemEntity();
        existingItem1.setItemId("itemId1");
        existingItem1.setAssetUrl("assetUrl1");
        existingItem1.setName("name1");
        existingItem1.setTags(List.of());
        existingItem1.setRarity(ItemRarity.RARE);
        existingItem1.setType(ItemType.WeaponSkin);
        existingItem1.setMaxBuyPrice(101);
        existingItem1.setBuyOrdersCount(102);
        existingItem1.setMinSellPrice(103);
        existingItem1.setSellOrdersCount(104);
        existingItem1.setMonthAveragePrice(105);
        existingItem1.setMonthMedianPrice(106);
        existingItem1.setMonthMaxPrice(107);
        existingItem1.setMonthMinPrice(108);
        existingItem1.setMonthSalesPerDay(109);
        existingItem1.setMonthSales(110);
        existingItem1.setDayAveragePrice(111);
        existingItem1.setDayMedianPrice(112);
        existingItem1.setDayMaxPrice(113);
        existingItem1.setDayMinPrice(114);
        existingItem1.setDaySales(115);
        existingItem1.setPriorityToSellByMaxBuyPrice(116L);
        existingItem1.setPriorityToSellByNextFancySellPrice(117L);
        existingItem1.setPriorityToBuyByMinSellPrice(118L);
        existingItem1.setPriorityToBuyIn1Hour(119L);
        existingItem1.setPriorityToBuyIn6Hours(120L);
        existingItem1.setPriorityToBuyIn24Hours(121L);
        existingItem1.setPriorityToBuyIn168Hours(122L);
        existingItem1.setPriorityToBuyIn720Hours(123L);
        existingItem1.setPriceToBuyIn1Hour(124);
        existingItem1.setPriceToBuyIn6Hours(125);
        existingItem1.setPriceToBuyIn24Hours(126);
        existingItem1.setPriceToBuyIn168Hours(127);
        existingItem1.setPriceToBuyIn720Hours(128);

        ItemEntity existingItem2 = new ItemEntity();
        existingItem2.setItemId("itemId2");
        existingItem2.setAssetUrl("assetUrl2");
        existingItem2.setName("name2");
        existingItem2.setTags(List.of());
        existingItem2.setRarity(ItemRarity.EPIC);
        existingItem2.setType(ItemType.Charm);
        existingItem2.setMaxBuyPrice(201);
        existingItem2.setBuyOrdersCount(202);
        existingItem2.setMinSellPrice(203);
        existingItem2.setSellOrdersCount(204);
        existingItem2.setMonthAveragePrice(205);
        existingItem2.setMonthMedianPrice(206);
        existingItem2.setMonthMaxPrice(207);
        existingItem2.setMonthMinPrice(208);
        existingItem2.setMonthSalesPerDay(209);
        existingItem2.setMonthSales(210);
        existingItem2.setDayAveragePrice(211);
        existingItem2.setDayMedianPrice(212);
        existingItem2.setDayMaxPrice(213);
        existingItem2.setDayMinPrice(214);
        existingItem2.setDaySales(215);
        existingItem2.setPriorityToSellByMaxBuyPrice(216L);
        existingItem2.setPriorityToSellByNextFancySellPrice(217L);
        existingItem2.setPriorityToBuyByMinSellPrice(218L);
        existingItem2.setPriorityToBuyIn1Hour(219L);
        existingItem2.setPriorityToBuyIn6Hours(220L);
        existingItem2.setPriorityToBuyIn24Hours(221L);
        existingItem2.setPriorityToBuyIn168Hours(222L);
        existingItem2.setPriorityToBuyIn720Hours(223L);
        existingItem2.setPriceToBuyIn1Hour(224);
        existingItem2.setPriceToBuyIn6Hours(225);
        existingItem2.setPriceToBuyIn24Hours(226);
        existingItem2.setPriceToBuyIn168Hours(227);
        existingItem2.setPriceToBuyIn720Hours(228);

        ItemEntity existingItem3 = new ItemEntity();
        existingItem3.setItemId("itemId3");
        existingItem3.setAssetUrl("assetUrl3");
        existingItem3.setName("name3");
        existingItem3.setTags(List.of());
        existingItem3.setRarity(ItemRarity.LEGENDARY);
        existingItem3.setType(ItemType.WeaponSkin);
        existingItem3.setMaxBuyPrice(301);
        existingItem3.setBuyOrdersCount(302);
        existingItem3.setMinSellPrice(303);
        existingItem3.setSellOrdersCount(304);
        existingItem3.setMonthAveragePrice(305);
        existingItem3.setMonthMedianPrice(306);
        existingItem3.setMonthMaxPrice(307);
        existingItem3.setMonthMinPrice(308);
        existingItem3.setMonthSalesPerDay(309);
        existingItem3.setMonthSales(310);
        existingItem3.setDayAveragePrice(311);
        existingItem3.setDayMedianPrice(312);
        existingItem3.setDayMaxPrice(313);
        existingItem3.setDayMinPrice(314);
        existingItem3.setDaySales(315);
        existingItem3.setPriorityToSellByMaxBuyPrice(316L);
        existingItem3.setPriorityToSellByNextFancySellPrice(317L);
        existingItem3.setPriorityToBuyByMinSellPrice(318L);
        existingItem3.setPriorityToBuyIn1Hour(319L);
        existingItem3.setPriorityToBuyIn6Hours(320L);
        existingItem3.setPriorityToBuyIn24Hours(321L);
        existingItem3.setPriorityToBuyIn168Hours(322L);
        existingItem3.setPriorityToBuyIn720Hours(323L);
        existingItem3.setPriceToBuyIn1Hour(324);
        existingItem3.setPriceToBuyIn6Hours(325);
        existingItem3.setPriceToBuyIn24Hours(326);
        existingItem3.setPriceToBuyIn168Hours(327);
        existingItem3.setPriceToBuyIn720Hours(328);

        itemPostgresRepository.save(existingItem1);
        itemPostgresRepository.save(existingItem2);
        itemPostgresRepository.save(existingItem3);

        ItemHistoryFieldsProjection projection1 = new ItemHistoryFieldsProjection();
        projection1.setItemId("itemId1");
        projection1.setMonthAveragePrice(1001);
        projection1.setMonthMedianPrice(1002);
        projection1.setMonthMaxPrice(1003);
        projection1.setMonthMinPrice(1004);
        projection1.setMonthSalesPerDay(1005);
        projection1.setMonthSales(1006);
        projection1.setDayAveragePrice(1007);
        projection1.setDayMedianPrice(1008);
        projection1.setDayMaxPrice(1009);
        projection1.setDayMinPrice(1010);
        projection1.setDaySales(1011);
        projection1.setPriorityToSellByMaxBuyPrice(1012L);
        projection1.setPriorityToSellByNextFancySellPrice(1013L);
        projection1.setPriorityToBuyByMinSellPrice(1014L);
        projection1.setPriorityToBuyIn1Hour(1015L);
        projection1.setPriorityToBuyIn6Hours(1016L);
        projection1.setPriorityToBuyIn24Hours(1017L);
        projection1.setPriorityToBuyIn168Hours(1018L);
        projection1.setPriorityToBuyIn720Hours(1019L);
        projection1.setPriceToBuyIn1Hour(1020);
        projection1.setPriceToBuyIn6Hours(1021);
        projection1.setPriceToBuyIn24Hours(1022);
        projection1.setPriceToBuyIn168Hours(1023);
        projection1.setPriceToBuyIn720Hours(1024);

        ItemHistoryFieldsProjection projection2 = new ItemHistoryFieldsProjection();
        projection2.setItemId("itemId2");
        projection2.setMonthAveragePrice(2001);
        projection2.setMonthMedianPrice(2002);
        projection2.setMonthMaxPrice(2003);
        projection2.setMonthMinPrice(2004);
        projection2.setMonthSalesPerDay(2005);
        projection2.setMonthSales(2006);
        projection2.setDayAveragePrice(2007);
        projection2.setDayMedianPrice(2008);
        projection2.setDayMaxPrice(2009);
        projection2.setDayMinPrice(2010);
        projection2.setDaySales(2011);
        projection2.setPriorityToSellByMaxBuyPrice(2012L);
        projection2.setPriorityToSellByNextFancySellPrice(2013L);
        projection2.setPriorityToBuyByMinSellPrice(2014L);
        projection2.setPriorityToBuyIn1Hour(2015L);
        projection2.setPriorityToBuyIn6Hours(2016L);
        projection2.setPriorityToBuyIn24Hours(2017L);
        projection2.setPriorityToBuyIn168Hours(2018L);
        projection2.setPriorityToBuyIn720Hours(2019L);
        projection2.setPriceToBuyIn1Hour(2020);
        projection2.setPriceToBuyIn6Hours(2021);
        projection2.setPriceToBuyIn24Hours(2022);
        projection2.setPriceToBuyIn168Hours(2023);
        projection2.setPriceToBuyIn720Hours(2024);

        itemPostgresRepository.updateAllItemsHistoryFields(List.of(projection1, projection2));

        List<ItemEntity> result = itemPostgresRepository.findAll();

        ItemEntity expectedItem1 = new ItemEntity();
        expectedItem1.setItemId("itemId1");
        expectedItem1.setAssetUrl("assetUrl1");
        expectedItem1.setName("name1");
        expectedItem1.setTags(List.of());
        expectedItem1.setRarity(ItemRarity.RARE);
        expectedItem1.setType(ItemType.WeaponSkin);
        expectedItem1.setMaxBuyPrice(101);
        expectedItem1.setBuyOrdersCount(102);
        expectedItem1.setMinSellPrice(103);
        expectedItem1.setSellOrdersCount(104);
        expectedItem1.setMonthAveragePrice(1001);
        expectedItem1.setMonthMedianPrice(1002);
        expectedItem1.setMonthMaxPrice(1003);
        expectedItem1.setMonthMinPrice(1004);
        expectedItem1.setMonthSalesPerDay(1005);
        expectedItem1.setMonthSales(1006);
        expectedItem1.setDayAveragePrice(1007);
        expectedItem1.setDayMedianPrice(1008);
        expectedItem1.setDayMaxPrice(1009);
        expectedItem1.setDayMinPrice(1010);
        expectedItem1.setDaySales(1011);
        expectedItem1.setPriorityToSellByMaxBuyPrice(1012L);
        expectedItem1.setPriorityToSellByNextFancySellPrice(1013L);
        expectedItem1.setPriorityToBuyByMinSellPrice(1014L);
        expectedItem1.setPriorityToBuyIn1Hour(1015L);
        expectedItem1.setPriorityToBuyIn6Hours(1016L);
        expectedItem1.setPriorityToBuyIn24Hours(1017L);
        expectedItem1.setPriorityToBuyIn168Hours(1018L);
        expectedItem1.setPriorityToBuyIn720Hours(1019L);
        expectedItem1.setPriceToBuyIn1Hour(1020);
        expectedItem1.setPriceToBuyIn6Hours(1021);
        expectedItem1.setPriceToBuyIn24Hours(1022);
        expectedItem1.setPriceToBuyIn168Hours(1023);
        expectedItem1.setPriceToBuyIn720Hours(1024);

        ItemEntity expectedItem2 = new ItemEntity();
        expectedItem2.setItemId("itemId2");
        expectedItem2.setAssetUrl("assetUrl2");
        expectedItem2.setName("name2");
        expectedItem2.setTags(List.of());
        expectedItem2.setRarity(ItemRarity.EPIC);
        expectedItem2.setType(ItemType.Charm);
        expectedItem2.setMaxBuyPrice(201);
        expectedItem2.setBuyOrdersCount(202);
        expectedItem2.setMinSellPrice(203);
        expectedItem2.setSellOrdersCount(204);
        expectedItem2.setMonthAveragePrice(2001);
        expectedItem2.setMonthMedianPrice(2002);
        expectedItem2.setMonthMaxPrice(2003);
        expectedItem2.setMonthMinPrice(2004);
        expectedItem2.setMonthSalesPerDay(2005);
        expectedItem2.setMonthSales(2006);
        expectedItem2.setDayAveragePrice(2007);
        expectedItem2.setDayMedianPrice(2008);
        expectedItem2.setDayMaxPrice(2009);
        expectedItem2.setDayMinPrice(2010);
        expectedItem2.setDaySales(2011);
        expectedItem2.setPriorityToSellByMaxBuyPrice(2012L);
        expectedItem2.setPriorityToSellByNextFancySellPrice(2013L);
        expectedItem2.setPriorityToBuyByMinSellPrice(2014L);
        expectedItem2.setPriorityToBuyIn1Hour(2015L);
        expectedItem2.setPriorityToBuyIn6Hours(2016L);
        expectedItem2.setPriorityToBuyIn24Hours(2017L);
        expectedItem2.setPriorityToBuyIn168Hours(2018L);
        expectedItem2.setPriorityToBuyIn720Hours(2019L);
        expectedItem2.setPriceToBuyIn1Hour(2020);
        expectedItem2.setPriceToBuyIn6Hours(2021);
        expectedItem2.setPriceToBuyIn24Hours(2022);
        expectedItem2.setPriceToBuyIn168Hours(2023);
        expectedItem2.setPriceToBuyIn720Hours(2024);

        ItemEntity expectedItem3 = existingItem3;

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(res -> res.isFullyEqual(expectedItem1)));
        assertTrue(result.stream().anyMatch(res -> res.isFullyEqual(expectedItem2)));
        assertTrue(result.stream().anyMatch(res -> res.isFullyEqual(expectedItem3)));
    }

    @Test
    public void updateHistoryFields_should_update_by_projection() {
        ItemEntity existingItem1 = new ItemEntity();
        existingItem1.setItemId("itemId1");
        existingItem1.setAssetUrl("assetUrl1");
        existingItem1.setName("name1");
        existingItem1.setTags(List.of());
        existingItem1.setRarity(ItemRarity.RARE);
        existingItem1.setType(ItemType.WeaponSkin);
        existingItem1.setMaxBuyPrice(101);
        existingItem1.setBuyOrdersCount(102);
        existingItem1.setMinSellPrice(103);
        existingItem1.setSellOrdersCount(104);
        existingItem1.setMonthAveragePrice(105);
        existingItem1.setMonthMedianPrice(106);
        existingItem1.setMonthMaxPrice(107);
        existingItem1.setMonthMinPrice(108);
        existingItem1.setMonthSalesPerDay(109);
        existingItem1.setMonthSales(110);
        existingItem1.setDayAveragePrice(111);
        existingItem1.setDayMedianPrice(112);
        existingItem1.setDayMaxPrice(113);
        existingItem1.setDayMinPrice(114);
        existingItem1.setDaySales(115);
        existingItem1.setPriorityToSellByMaxBuyPrice(116L);
        existingItem1.setPriorityToSellByNextFancySellPrice(117L);
        existingItem1.setPriorityToBuyByMinSellPrice(118L);
        existingItem1.setPriorityToBuyIn1Hour(119L);
        existingItem1.setPriorityToBuyIn6Hours(120L);
        existingItem1.setPriorityToBuyIn24Hours(121L);
        existingItem1.setPriorityToBuyIn168Hours(122L);
        existingItem1.setPriorityToBuyIn720Hours(123L);
        existingItem1.setPriceToBuyIn1Hour(124);
        existingItem1.setPriceToBuyIn6Hours(125);
        existingItem1.setPriceToBuyIn24Hours(126);
        existingItem1.setPriceToBuyIn168Hours(127);
        existingItem1.setPriceToBuyIn720Hours(128);

        ItemEntity existingItem2 = new ItemEntity();
        existingItem2.setItemId("itemId2");
        existingItem2.setAssetUrl("assetUrl2");
        existingItem2.setName("name2");
        existingItem2.setTags(List.of());
        existingItem2.setRarity(ItemRarity.EPIC);
        existingItem2.setType(ItemType.Charm);
        existingItem2.setMaxBuyPrice(201);
        existingItem2.setBuyOrdersCount(202);
        existingItem2.setMinSellPrice(203);
        existingItem2.setSellOrdersCount(204);
        existingItem2.setMonthAveragePrice(205);
        existingItem2.setMonthMedianPrice(206);
        existingItem2.setMonthMaxPrice(207);
        existingItem2.setMonthMinPrice(208);
        existingItem2.setMonthSalesPerDay(209);
        existingItem2.setMonthSales(210);
        existingItem2.setDayAveragePrice(211);
        existingItem2.setDayMedianPrice(212);
        existingItem2.setDayMaxPrice(213);
        existingItem2.setDayMinPrice(214);
        existingItem2.setDaySales(215);
        existingItem2.setPriorityToSellByMaxBuyPrice(216L);
        existingItem2.setPriorityToSellByNextFancySellPrice(217L);
        existingItem2.setPriorityToBuyByMinSellPrice(218L);
        existingItem2.setPriorityToBuyIn1Hour(219L);
        existingItem2.setPriorityToBuyIn6Hours(220L);
        existingItem2.setPriorityToBuyIn24Hours(221L);
        existingItem2.setPriorityToBuyIn168Hours(222L);
        existingItem2.setPriorityToBuyIn720Hours(223L);
        existingItem2.setPriceToBuyIn1Hour(224);
        existingItem2.setPriceToBuyIn6Hours(225);
        existingItem2.setPriceToBuyIn24Hours(226);
        existingItem2.setPriceToBuyIn168Hours(227);
        existingItem2.setPriceToBuyIn720Hours(228);

        itemPostgresRepository.save(existingItem1);
        itemPostgresRepository.save(existingItem2);

        ItemHistoryFieldsProjection projection1 = new ItemHistoryFieldsProjection();
        projection1.setItemId("itemId1");
        projection1.setMonthAveragePrice(1001);
        projection1.setMonthMedianPrice(1002);
        projection1.setMonthMaxPrice(1003);
        projection1.setMonthMinPrice(1004);
        projection1.setMonthSalesPerDay(1005);
        projection1.setMonthSales(1006);
        projection1.setDayAveragePrice(1007);
        projection1.setDayMedianPrice(1008);
        projection1.setDayMaxPrice(1009);
        projection1.setDayMinPrice(1010);
        projection1.setDaySales(1011);
        projection1.setPriorityToSellByMaxBuyPrice(1012L);
        projection1.setPriorityToSellByNextFancySellPrice(1013L);
        projection1.setPriorityToBuyByMinSellPrice(1014L);
        projection1.setPriorityToBuyIn1Hour(1015L);
        projection1.setPriorityToBuyIn6Hours(1016L);
        projection1.setPriorityToBuyIn24Hours(1017L);
        projection1.setPriorityToBuyIn168Hours(1018L);
        projection1.setPriorityToBuyIn720Hours(1019L);
        projection1.setPriceToBuyIn1Hour(1020);
        projection1.setPriceToBuyIn6Hours(1021);
        projection1.setPriceToBuyIn24Hours(1022);
        projection1.setPriceToBuyIn168Hours(1023);
        projection1.setPriceToBuyIn720Hours(1024);

        itemPostgresRepository.updateHistoryFields(projection1);

        List<ItemEntity> result = itemPostgresRepository.findAll();

        ItemEntity expectedItem1 = new ItemEntity();
        expectedItem1.setItemId("itemId1");
        expectedItem1.setAssetUrl("assetUrl1");
        expectedItem1.setName("name1");
        expectedItem1.setTags(List.of());
        expectedItem1.setRarity(ItemRarity.RARE);
        expectedItem1.setType(ItemType.WeaponSkin);
        expectedItem1.setMaxBuyPrice(101);
        expectedItem1.setBuyOrdersCount(102);
        expectedItem1.setMinSellPrice(103);
        expectedItem1.setSellOrdersCount(104);
        expectedItem1.setMonthAveragePrice(1001);
        expectedItem1.setMonthMedianPrice(1002);
        expectedItem1.setMonthMaxPrice(1003);
        expectedItem1.setMonthMinPrice(1004);
        expectedItem1.setMonthSalesPerDay(1005);
        expectedItem1.setMonthSales(1006);
        expectedItem1.setDayAveragePrice(1007);
        expectedItem1.setDayMedianPrice(1008);
        expectedItem1.setDayMaxPrice(1009);
        expectedItem1.setDayMinPrice(1010);
        expectedItem1.setDaySales(1011);
        expectedItem1.setPriorityToSellByMaxBuyPrice(1012L);
        expectedItem1.setPriorityToSellByNextFancySellPrice(1013L);
        expectedItem1.setPriorityToBuyByMinSellPrice(1014L);
        expectedItem1.setPriorityToBuyIn1Hour(1015L);
        expectedItem1.setPriorityToBuyIn6Hours(1016L);
        expectedItem1.setPriorityToBuyIn24Hours(1017L);
        expectedItem1.setPriorityToBuyIn168Hours(1018L);
        expectedItem1.setPriorityToBuyIn720Hours(1019L);
        expectedItem1.setPriceToBuyIn1Hour(1020);
        expectedItem1.setPriceToBuyIn6Hours(1021);
        expectedItem1.setPriceToBuyIn24Hours(1022);
        expectedItem1.setPriceToBuyIn168Hours(1023);
        expectedItem1.setPriceToBuyIn720Hours(1024);

        ItemEntity expectedItem2 = existingItem2;

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(res -> res.isFullyEqual(expectedItem1)));
        assertTrue(result.stream().anyMatch(res -> res.isFullyEqual(expectedItem2)));
    }

    @Test
    public void findAllItemRecalculationRequiredFields_should_return_all_fields() {
        ItemEntity existingItem1 = new ItemEntity();
        existingItem1.setItemId("itemId1");
        existingItem1.setAssetUrl("assetUrl1");
        existingItem1.setName("name1");
        existingItem1.setTags(List.of());
        existingItem1.setRarity(ItemRarity.RARE);
        existingItem1.setType(ItemType.WeaponSkin);
        existingItem1.setMaxBuyPrice(101);
        existingItem1.setBuyOrdersCount(102);
        existingItem1.setMinSellPrice(103);
        existingItem1.setSellOrdersCount(104);

        ItemEntity existingItem2 = new ItemEntity();
        existingItem2.setItemId("itemId2");
        existingItem2.setAssetUrl("assetUrl2");
        existingItem2.setName("name2");
        existingItem2.setTags(List.of());
        existingItem2.setRarity(ItemRarity.EPIC);
        existingItem2.setType(ItemType.Charm);
        existingItem2.setMaxBuyPrice(201);
        existingItem2.setBuyOrdersCount(202);
        existingItem2.setMinSellPrice(203);
        existingItem2.setSellOrdersCount(204);

        ItemEntity existingItem3 = new ItemEntity();
        existingItem3.setItemId("itemId3");
        existingItem3.setAssetUrl("assetUrl3");
        existingItem3.setName("name3");
        existingItem3.setTags(List.of());
        existingItem3.setRarity(ItemRarity.LEGENDARY);
        existingItem3.setType(ItemType.WeaponSkin);
        existingItem3.setMaxBuyPrice(301);
        existingItem3.setBuyOrdersCount(302);
        existingItem3.setMinSellPrice(303);
        existingItem3.setSellOrdersCount(304);

        itemPostgresRepository.save(existingItem1);
        itemPostgresRepository.save(existingItem2);
        itemPostgresRepository.save(existingItem3);

        List<ItemRecalculationRequiredFieldsProjection> result = itemPostgresRepository.findAllItemsRecalculationRequiredFields();

        ItemRecalculationRequiredFieldsProjection expectedItem1 = new ItemRecalculationRequiredFieldsProjection();
        expectedItem1.setItemId("itemId1");
        expectedItem1.setRarity(ItemRarity.RARE);
        expectedItem1.setMaxBuyPrice(101);
        expectedItem1.setBuyOrdersCount(102);
        expectedItem1.setMinSellPrice(103);
        expectedItem1.setSellOrdersCount(104);

        ItemRecalculationRequiredFieldsProjection expectedItem2 = new ItemRecalculationRequiredFieldsProjection();
        expectedItem2.setItemId("itemId2");
        expectedItem2.setRarity(ItemRarity.EPIC);
        expectedItem2.setMaxBuyPrice(201);
        expectedItem2.setBuyOrdersCount(202);
        expectedItem2.setMinSellPrice(203);
        expectedItem2.setSellOrdersCount(204);

        ItemRecalculationRequiredFieldsProjection expectedItem3 = new ItemRecalculationRequiredFieldsProjection();
        expectedItem3.setItemId("itemId3");
        expectedItem3.setRarity(ItemRarity.LEGENDARY);
        expectedItem3.setMaxBuyPrice(301);
        expectedItem3.setBuyOrdersCount(302);
        expectedItem3.setMinSellPrice(303);
        expectedItem3.setSellOrdersCount(304);

        assertEquals(3, result.size());
        assertTrue(result.contains(expectedItem1));
        assertTrue(result.contains(expectedItem2));
        assertTrue(result.contains(expectedItem3));
    }
}