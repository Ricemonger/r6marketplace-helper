package github.ricemonger.item_trade_stats_calculator.services;

import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemCurrentPricesRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.abstractions.ItemDatabaseService;
import github.ricemonger.item_trade_stats_calculator.services.abstractions.ItemSaleDatabaseService;
import github.ricemonger.item_trade_stats_calculator.services.abstractions.ItemSaleUbiStatsDatabaseService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemDaySalesUbiStats;
import github.ricemonger.utils.DTOs.common.ItemSale;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @MockBean
    private ItemDatabaseService itemDatabaseService;
    @MockBean
    private ItemSaleDatabaseService itemSaleDatabaseService;
    @MockBean
    private ItemSaleUbiStatsDatabaseService itemSaleUbiStatsService;
    @MockBean
    private PotentialTradeStatsService potentialTradeStatsService;

    @Test
    public void recalculateAndSaveAllItemsHistoryFields_should_recalculate_only_history_fields_and_save_all_items() {
        ItemRecalculationRequiredFields existingItemToRecalculate1 = new ItemRecalculationRequiredFields();
        existingItemToRecalculate1.setItemId("itemId1");
        existingItemToRecalculate1.setRarity(ItemRarity.RARE);
        existingItemToRecalculate1.setMaxBuyPrice(1);
        existingItemToRecalculate1.setBuyOrdersCount(2);
        existingItemToRecalculate1.setMinSellPrice(3);
        existingItemToRecalculate1.setSellOrdersCount(4);

        ItemRecalculationRequiredFields existingItemToRecalculate2 = new ItemRecalculationRequiredFields();
        existingItemToRecalculate2.setItemId("itemId2");
        existingItemToRecalculate2.setRarity(ItemRarity.EPIC);
        existingItemToRecalculate2.setMaxBuyPrice(2);
        existingItemToRecalculate2.setBuyOrdersCount(3);
        existingItemToRecalculate2.setMinSellPrice(4);
        existingItemToRecalculate2.setSellOrdersCount(5);

        ItemRecalculationRequiredFields existingItemNoSales = new ItemRecalculationRequiredFields();
        existingItemNoSales.setItemId("itemId3");
        existingItemNoSales.setRarity(ItemRarity.UNKNOWN);
        existingItemNoSales.setMaxBuyPrice(1);
        existingItemNoSales.setBuyOrdersCount(2);
        existingItemNoSales.setMinSellPrice(3);
        existingItemNoSales.setSellOrdersCount(4);

        List<ItemRecalculationRequiredFields> existingItems = List.of(existingItemToRecalculate1, existingItemToRecalculate2, existingItemNoSales);

        when(itemDatabaseService.findAllItemsRecalculationRequiredFields()).thenReturn(existingItems);

        ItemSale item1TodaySale1 = new ItemSale("itemId1", LocalDateTime.now().withMinute(1).withNano(0), 100);
        ItemSale item1TodaySale2 = new ItemSale("itemId1", LocalDateTime.now().withMinute(2).withNano(0), 200);
        ItemSale item1TodaySale3 = new ItemSale("itemId1", LocalDateTime.now().withMinute(3).withNano(0), 600);

        ItemSale item1Day7Sale1 = new ItemSale("itemId1", LocalDateTime.now().minusDays(7).withMinute(1).withNano(0), 200);
        ItemSale item1Day7Sale2 = new ItemSale("itemId1", LocalDateTime.now().minusDays(7).withMinute(2).withNano(0), 400);
        ItemSale item1Day7Sale3 = new ItemSale("itemId1", LocalDateTime.now().minusDays(7).withMinute(3).withNano(0), 1200);

        ItemSale item1Day30Sale1 = new ItemSale("itemId1", LocalDateTime.now().minusDays(30).withMinute(1).withNano(0), 400);
        ItemSale item1Day30Sale2 = new ItemSale("itemId1", LocalDateTime.now().minusDays(30).withMinute(2).withNano(0), 800);
        ItemSale item1Day30Sale3 = new ItemSale("itemId1", LocalDateTime.now().minusDays(30).withMinute(3).withNano(0), 2400);

        ItemSale item1Day31Sale1 = new ItemSale("itemId1", LocalDateTime.now().minusDays(31).withMinute(1).withNano(0), 800);
        ItemSale item1Day31Sale2 = new ItemSale("itemId1", LocalDateTime.now().minusDays(31).withMinute(2).withNano(0), 1600);
        ItemSale item1Day31Sale3 = new ItemSale("itemId1", LocalDateTime.now().minusDays(31).withMinute(3).withNano(0), 4800);

        ItemSale item2Day7Sale1 = new ItemSale("itemId2", LocalDateTime.now().minusDays(7).withMinute(1).withNano(0), 200);
        ItemSale item2Day7Sale2 = new ItemSale("itemId2", LocalDateTime.now().minusDays(7).withMinute(2).withNano(0), 800);
        ItemSale item2Day7Sale3 = new ItemSale("itemId2", LocalDateTime.now().minusDays(7).withMinute(3).withNano(0), 5000);

        List<ItemSale> existingItemSales = List.of(item1TodaySale1, item1TodaySale2, item1TodaySale3, item1Day7Sale1, item1Day7Sale2, item1Day7Sale3, item1Day30Sale1, item1Day30Sale2, item1Day30Sale3, item1Day31Sale1, item1Day31Sale2, item1Day31Sale3, item2Day7Sale1, item2Day7Sale2, item2Day7Sale3);
        when(itemSaleDatabaseService.findAllForLastMonth()).thenReturn(existingItemSales);

        //  4-200, 1-1200
        ItemDaySalesUbiStats item1Day7Stats = new ItemDaySalesUbiStats("itemId1", LocalDate.now().minusDays(7), 200, 400, 1200, 5);

        ItemDaySalesUbiStats item1Day30Stats = new ItemDaySalesUbiStats("itemId1", LocalDate.now().minusDays(30), 400, 2000, 3200, 4);

        ItemDaySalesUbiStats item1Day32Stats = new ItemDaySalesUbiStats("itemId1", LocalDate.now().minusDays(32), 800, 1600, 48000, 3);

        //1 - 100, 26-300,  1 - 500
        ItemDaySalesUbiStats item2Day15Stats = new ItemDaySalesUbiStats("itemId2", LocalDate.now().minusDays(15), 100, 300, 500, 28);

        List<ItemDaySalesUbiStats> existingItemDaySalesUbiStatEntityDTOS = List.of(item1Day7Stats, item1Day30Stats, item1Day32Stats, item2Day15Stats);
        when(itemSaleUbiStatsService.findAllForLastMonth()).thenReturn(existingItemDaySalesUbiStatEntityDTOS);

        Item expectedRecalculatedItem1 = new Item();
        expectedRecalculatedItem1.setItemId("itemId1");
        expectedRecalculatedItem1.setAssetUrl("assetUrl1");
        expectedRecalculatedItem1.setName("name1");
        expectedRecalculatedItem1.setTags(List.of());
        expectedRecalculatedItem1.setRarity(ItemRarity.RARE);
        expectedRecalculatedItem1.setType(ItemType.WeaponSkin);
        expectedRecalculatedItem1.setMaxBuyPrice(1);
        expectedRecalculatedItem1.setBuyOrdersCount(2);
        expectedRecalculatedItem1.setMinSellPrice(3);
        expectedRecalculatedItem1.setSellOrdersCount(4);
        expectedRecalculatedItem1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        expectedRecalculatedItem1.setLastSoldPrice(5);
        expectedRecalculatedItem1.setMonthAveragePrice(590);
        expectedRecalculatedItem1.setMonthMedianPrice(200);
        expectedRecalculatedItem1.setMonthMaxPrice(2400);
        expectedRecalculatedItem1.setMonthMinPrice(100);
        expectedRecalculatedItem1.setMonthSalesPerDay(0);
        expectedRecalculatedItem1.setMonthSales(11);
        expectedRecalculatedItem1.setDayAveragePrice(300);
        expectedRecalculatedItem1.setDayMedianPrice(200);
        expectedRecalculatedItem1.setDayMaxPrice(600);
        expectedRecalculatedItem1.setDayMinPrice(100);
        expectedRecalculatedItem1.setDaySales(3);
        PotentialTradeStats item1PotentialTradeStatsSellByMaxBuyPrice = new PotentialTradeStats(1, 1, 12L);
        PotentialTradeStats item1PotentialTradeStatsSellByNextFancySellPrice = new PotentialTradeStats(2, 1, 13L);
        PotentialTradeStats item1PotentialTradeStatsBuyByMinSellPrice = new PotentialTradeStats(3, 1, 14L);
        PotentialTradeStats item1PotentialTradeStatsBuyIn1Hour = new PotentialTradeStats(100, 1, 1100L);
        PotentialTradeStats item1PotentialTradeStatsBuyIn6Hours = new PotentialTradeStats(200, 1, 1200L);
        PotentialTradeStats item1PotentialTradeStatsBuyIn24Hours = new PotentialTradeStats(300, 1, 1300L);
        PotentialTradeStats item1PotentialTradeStatsBuyIn168Hours = new PotentialTradeStats(400, 1, 1400L);
        PotentialTradeStats item1PotentialTradeStatsBuyIn720Hours = new PotentialTradeStats(500, 1, 1500L);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(new Item("itemId1"))).thenReturn(item1PotentialTradeStatsSellByMaxBuyPrice);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(new Item("itemId1"))).thenReturn(item1PotentialTradeStatsSellByNextFancySellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(new Item("itemId1"))).thenReturn(item1PotentialTradeStatsBuyByMinSellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId1")), any(), eq(60))).thenReturn(item1PotentialTradeStatsBuyIn1Hour);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId1")), any(), eq(360))).thenReturn(item1PotentialTradeStatsBuyIn6Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId1")), any(), eq(1440))).thenReturn(item1PotentialTradeStatsBuyIn24Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId1")), any(), eq(10080))).thenReturn(item1PotentialTradeStatsBuyIn168Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId1")), any(), eq(43200))).thenReturn(item1PotentialTradeStatsBuyIn720Hours);
        expectedRecalculatedItem1.setPriorityToSellByMaxBuyPrice(12L);
        expectedRecalculatedItem1.setPriorityToSellByNextFancySellPrice(13L);
        expectedRecalculatedItem1.setPriorityToBuyByMinSellPrice(14L);
        expectedRecalculatedItem1.setPriorityToBuyIn1Hour(1100L);
        expectedRecalculatedItem1.setPriorityToBuyIn6Hours(1200L);
        expectedRecalculatedItem1.setPriorityToBuyIn24Hours(1300L);
        expectedRecalculatedItem1.setPriorityToBuyIn168Hours(1400L);
        expectedRecalculatedItem1.setPriorityToBuyIn720Hours(1500L);
        expectedRecalculatedItem1.setPriceToBuyIn1Hour(100);
        expectedRecalculatedItem1.setPriceToBuyIn6Hours(200);
        expectedRecalculatedItem1.setPriceToBuyIn24Hours(300);
        expectedRecalculatedItem1.setPriceToBuyIn168Hours(400);
        expectedRecalculatedItem1.setPriceToBuyIn720Hours(500);

        Item expectedRecalculatedItem2 = new Item();
        expectedRecalculatedItem2.setItemId("itemId2");
        expectedRecalculatedItem2.setAssetUrl("assetUrl2");
        expectedRecalculatedItem2.setName("name2");
        expectedRecalculatedItem2.setTags(List.of());
        expectedRecalculatedItem2.setRarity(ItemRarity.EPIC);
        expectedRecalculatedItem2.setType(ItemType.CharacterHeadgear);
        expectedRecalculatedItem2.setMaxBuyPrice(2);
        expectedRecalculatedItem2.setBuyOrdersCount(3);
        expectedRecalculatedItem2.setMinSellPrice(4);
        expectedRecalculatedItem2.setSellOrdersCount(5);
        expectedRecalculatedItem2.setLastSoldAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        expectedRecalculatedItem2.setLastSoldPrice(6);
        expectedRecalculatedItem2.setMonthAveragePrice(464);
        expectedRecalculatedItem2.setMonthMedianPrice(300);
        expectedRecalculatedItem2.setMonthMaxPrice(5000);
        expectedRecalculatedItem2.setMonthMinPrice(100);
        expectedRecalculatedItem2.setMonthSalesPerDay(1);
        expectedRecalculatedItem2.setMonthSales(31);
        expectedRecalculatedItem2.setDayAveragePrice(0);
        expectedRecalculatedItem2.setDayMedianPrice(0);
        expectedRecalculatedItem2.setDayMaxPrice(0);
        expectedRecalculatedItem2.setDayMinPrice(0);
        expectedRecalculatedItem2.setDaySales(0);
        PotentialTradeStats item2PotentialTradeStatsSellByMaxBuyPrice = new PotentialTradeStats(2, 1, 13L);
        PotentialTradeStats item2PotentialTradeStatsSellByNextFancySellPrice = new PotentialTradeStats(3, 1, 14L);
        PotentialTradeStats item2PotentialTradeStatsBuyByMinSellPrice = new PotentialTradeStats(4, 1, 15L);
        PotentialTradeStats item2PotentialTradeStatsBuyIn1Hour = new PotentialTradeStats(200, 1, 1200L);
        PotentialTradeStats item2PotentialTradeStatsBuyIn6Hours = new PotentialTradeStats(300, 1, 1300L);
        PotentialTradeStats item2PotentialTradeStatsBuyIn24Hours = new PotentialTradeStats(400, 1, 1400L);
        PotentialTradeStats item2PotentialTradeStatsBuyIn168Hours = new PotentialTradeStats(500, 1, 1500L);
        PotentialTradeStats item2PotentialTradeStatsBuyIn720Hours = new PotentialTradeStats(600, 1, 1600L);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(new Item("itemId2"))).thenReturn(item2PotentialTradeStatsSellByMaxBuyPrice);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(new Item("itemId2"))).thenReturn(item2PotentialTradeStatsSellByNextFancySellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(new Item("itemId2"))).thenReturn(item2PotentialTradeStatsBuyByMinSellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId2")), any(), eq(60))).thenReturn(item2PotentialTradeStatsBuyIn1Hour);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId2")), any(), eq(360))).thenReturn(item2PotentialTradeStatsBuyIn6Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId2")), any(), eq(1440))).thenReturn(item2PotentialTradeStatsBuyIn24Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId2")), any(), eq(10080))).thenReturn(item2PotentialTradeStatsBuyIn168Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId2")), any(), eq(43200))).thenReturn(item2PotentialTradeStatsBuyIn720Hours);
        expectedRecalculatedItem2.setPriorityToSellByMaxBuyPrice(13L);
        expectedRecalculatedItem2.setPriorityToSellByNextFancySellPrice(14L);
        expectedRecalculatedItem2.setPriorityToBuyByMinSellPrice(15L);
        expectedRecalculatedItem2.setPriorityToBuyIn1Hour(1200L);
        expectedRecalculatedItem2.setPriorityToBuyIn6Hours(1300L);
        expectedRecalculatedItem2.setPriorityToBuyIn24Hours(1400L);
        expectedRecalculatedItem2.setPriorityToBuyIn168Hours(1500L);
        expectedRecalculatedItem2.setPriorityToBuyIn720Hours(1600L);
        expectedRecalculatedItem2.setPriceToBuyIn1Hour(200);
        expectedRecalculatedItem2.setPriceToBuyIn6Hours(300);
        expectedRecalculatedItem2.setPriceToBuyIn24Hours(400);
        expectedRecalculatedItem2.setPriceToBuyIn168Hours(500);
        expectedRecalculatedItem2.setPriceToBuyIn720Hours(600);

        Item expectedItemNoSales = new Item();
        expectedItemNoSales.setItemId("itemId3");
        expectedItemNoSales.setAssetUrl("assetUrl3");
        expectedItemNoSales.setName("name3");
        expectedItemNoSales.setTags(List.of());
        expectedItemNoSales.setRarity(ItemRarity.UNKNOWN);
        expectedItemNoSales.setType(ItemType.Unknown);
        expectedItemNoSales.setMaxBuyPrice(1);
        expectedItemNoSales.setBuyOrdersCount(2);
        expectedItemNoSales.setMinSellPrice(3);
        expectedItemNoSales.setSellOrdersCount(4);
        expectedItemNoSales.setLastSoldAt(LocalDateTime.of(2023, 1, 1, 0, 0));
        expectedItemNoSales.setLastSoldPrice(5);
        expectedItemNoSales.setMonthAveragePrice(0);
        expectedItemNoSales.setMonthMedianPrice(0);
        expectedItemNoSales.setMonthMaxPrice(0);
        expectedItemNoSales.setMonthMinPrice(0);
        expectedItemNoSales.setMonthSalesPerDay(0);
        expectedItemNoSales.setMonthSales(0);
        expectedItemNoSales.setDayAveragePrice(0);
        expectedItemNoSales.setDayMedianPrice(0);
        expectedItemNoSales.setDayMaxPrice(0);
        expectedItemNoSales.setDayMinPrice(0);
        expectedItemNoSales.setDaySales(0);
        PotentialTradeStats item3PotentialTradeStatsSellByMaxBuyPrice = new PotentialTradeStats(2, 1, null);
        PotentialTradeStats item3PotentialTradeStatsSellByNextFancySellPrice = new PotentialTradeStats(3, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyByMinSellPrice = new PotentialTradeStats(4, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyIn1Hour = new PotentialTradeStats(null, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyIn6Hours = new PotentialTradeStats(null, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyIn24Hours = new PotentialTradeStats(null, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyIn168Hours = new PotentialTradeStats(null, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyIn720Hours = new PotentialTradeStats(null, 1, null);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(new Item("itemId3"))).thenReturn(item3PotentialTradeStatsSellByMaxBuyPrice);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(new Item("itemId3"))).thenReturn(item3PotentialTradeStatsSellByNextFancySellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(new Item("itemId3"))).thenReturn(item3PotentialTradeStatsBuyByMinSellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId3")), any(), eq(60))).thenReturn(item3PotentialTradeStatsBuyIn1Hour);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId3")), any(), eq(360))).thenReturn(item3PotentialTradeStatsBuyIn6Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId3")), any(), eq(1440))).thenReturn(item3PotentialTradeStatsBuyIn24Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId3")), any(), eq(10080))).thenReturn(item3PotentialTradeStatsBuyIn168Hours);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(eq(new Item("itemId3")), any(), eq(43200))).thenReturn(item3PotentialTradeStatsBuyIn720Hours);
        expectedItemNoSales.setPriorityToSellByMaxBuyPrice(null);
        expectedItemNoSales.setPriorityToSellByNextFancySellPrice(null);
        expectedItemNoSales.setPriorityToBuyByMinSellPrice(null);
        expectedItemNoSales.setPriorityToBuyIn1Hour(null);
        expectedItemNoSales.setPriorityToBuyIn6Hours(null);
        expectedItemNoSales.setPriorityToBuyIn24Hours(null);
        expectedItemNoSales.setPriorityToBuyIn168Hours(null);
        expectedItemNoSales.setPriorityToBuyIn720Hours(null);
        expectedItemNoSales.setPriceToBuyIn1Hour(null);
        expectedItemNoSales.setPriceToBuyIn6Hours(null);
        expectedItemNoSales.setPriceToBuyIn24Hours(null);
        expectedItemNoSales.setPriceToBuyIn168Hours(null);
        expectedItemNoSales.setPriceToBuyIn720Hours(null);


        List<Item> expectedResult = List.of(expectedRecalculatedItem1, expectedRecalculatedItem2, expectedItemNoSales);

        itemService.recalculateAndSaveAllItemsHistoryFields();

        verify(itemDatabaseService).updateAllItemsHistoryFields(argThat(arg ->
                arg.size() == expectedResult.size() &&
                expectedResult.stream().allMatch(expectedItem ->
                        arg.stream().anyMatch(actualItem ->
                                actualItem.itemHistoryFieldsAreEqual(expectedItem)
                        )
                )
        ));
    }

    @Test
    public void recalculateAndSaveAllItemsPotentialTradeStatsByCurrentPrices_should_recalculate_only_current_prices_history_fields_and_save_all_items() {
        ItemCurrentPricesRecalculationRequiredFields existingItemToRecalculate1 = new ItemCurrentPricesRecalculationRequiredFields();
        existingItemToRecalculate1.setItemId("itemId1");
        existingItemToRecalculate1.setRarity(ItemRarity.RARE);
        existingItemToRecalculate1.setMaxBuyPrice(101);
        existingItemToRecalculate1.setMinSellPrice(102);
        existingItemToRecalculate1.setSellOrdersCount(103);
        existingItemToRecalculate1.setMonthMedianPrice(104);
        existingItemToRecalculate1.setMonthSalesPerDay(105);
        existingItemToRecalculate1.setMonthSales(106);

        ItemCurrentPricesRecalculationRequiredFields existingItemToRecalculate2 = new ItemCurrentPricesRecalculationRequiredFields();
        existingItemToRecalculate2.setItemId("itemId2");
        existingItemToRecalculate2.setRarity(ItemRarity.EPIC);
        existingItemToRecalculate2.setMaxBuyPrice(201);
        existingItemToRecalculate2.setMinSellPrice(202);
        existingItemToRecalculate2.setSellOrdersCount(203);
        existingItemToRecalculate2.setMonthMedianPrice(204);
        existingItemToRecalculate2.setMonthSalesPerDay(205);
        existingItemToRecalculate2.setMonthSales(206);

        ItemCurrentPricesRecalculationRequiredFields existingItemNoSales = new ItemCurrentPricesRecalculationRequiredFields();
        existingItemNoSales.setItemId("itemId3");
        existingItemNoSales.setRarity(ItemRarity.UNKNOWN);
        existingItemNoSales.setMaxBuyPrice(301);
        existingItemNoSales.setMinSellPrice(302);
        existingItemNoSales.setSellOrdersCount(303);
        existingItemNoSales.setMonthMedianPrice(304);
        existingItemNoSales.setMonthSalesPerDay(305);
        existingItemNoSales.setMonthSales(306);

        List<ItemCurrentPricesRecalculationRequiredFields> existingItems = List.of(existingItemToRecalculate1, existingItemToRecalculate2, existingItemNoSales);

        when(itemDatabaseService.findAllItemsCurrentPricesRecalculationRequiredFields()).thenReturn(existingItems);

        Item expectedRecalculatedItem1 = new Item();
        expectedRecalculatedItem1.setItemId("itemId1");
        expectedRecalculatedItem1.setRarity(ItemRarity.RARE);
        expectedRecalculatedItem1.setMaxBuyPrice(101);
        expectedRecalculatedItem1.setMinSellPrice(102);
        expectedRecalculatedItem1.setSellOrdersCount(103);
        expectedRecalculatedItem1.setMonthMedianPrice(104);
        expectedRecalculatedItem1.setMonthSalesPerDay(105);
        expectedRecalculatedItem1.setMonthSales(106);
        PotentialTradeStats item1PotentialTradeStatsSellByMaxBuyPrice = new PotentialTradeStats(1, 1, 12L);
        PotentialTradeStats item1PotentialTradeStatsSellByNextFancySellPrice = new PotentialTradeStats(2, 1, 13L);
        PotentialTradeStats item1PotentialTradeStatsBuyByMinSellPrice = new PotentialTradeStats(3, 1, 14L);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(new Item("itemId1"))).thenReturn(item1PotentialTradeStatsSellByMaxBuyPrice);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(new Item("itemId1"))).thenReturn(item1PotentialTradeStatsSellByNextFancySellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(new Item("itemId1"))).thenReturn(item1PotentialTradeStatsBuyByMinSellPrice);
        expectedRecalculatedItem1.setPriorityToSellByMaxBuyPrice(12L);
        expectedRecalculatedItem1.setPriorityToSellByNextFancySellPrice(13L);
        expectedRecalculatedItem1.setPriorityToBuyByMinSellPrice(14L);

        Item expectedRecalculatedItem2 = new Item();
        expectedRecalculatedItem2.setItemId("itemId2");
        expectedRecalculatedItem2.setRarity(ItemRarity.EPIC);
        expectedRecalculatedItem2.setMaxBuyPrice(201);
        expectedRecalculatedItem2.setMinSellPrice(202);
        expectedRecalculatedItem2.setSellOrdersCount(203);
        expectedRecalculatedItem2.setMonthMedianPrice(204);
        expectedRecalculatedItem2.setMonthSalesPerDay(205);
        expectedRecalculatedItem2.setMonthSales(206);
        PotentialTradeStats item2PotentialTradeStatsSellByMaxBuyPrice = new PotentialTradeStats(2, 1, 13L);
        PotentialTradeStats item2PotentialTradeStatsSellByNextFancySellPrice = new PotentialTradeStats(3, 1, 14L);
        PotentialTradeStats item2PotentialTradeStatsBuyByMinSellPrice = new PotentialTradeStats(4, 1, 15L);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(new Item("itemId2"))).thenReturn(item2PotentialTradeStatsSellByMaxBuyPrice);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(new Item("itemId2"))).thenReturn(item2PotentialTradeStatsSellByNextFancySellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(new Item("itemId2"))).thenReturn(item2PotentialTradeStatsBuyByMinSellPrice);
        expectedRecalculatedItem2.setPriorityToSellByMaxBuyPrice(13L);
        expectedRecalculatedItem2.setPriorityToSellByNextFancySellPrice(14L);
        expectedRecalculatedItem2.setPriorityToBuyByMinSellPrice(15L);

        Item expectedItemNoSales = new Item();
        expectedItemNoSales.setItemId("itemId3");
        expectedItemNoSales.setRarity(ItemRarity.UNKNOWN);
        expectedItemNoSales.setMaxBuyPrice(301);
        expectedItemNoSales.setMinSellPrice(302);
        expectedItemNoSales.setSellOrdersCount(303);
        expectedItemNoSales.setMonthMedianPrice(304);
        expectedItemNoSales.setMonthSalesPerDay(305);
        expectedItemNoSales.setMonthSales(306);
        PotentialTradeStats item3PotentialTradeStatsSellByMaxBuyPrice = new PotentialTradeStats(2, 1, null);
        PotentialTradeStats item3PotentialTradeStatsSellByNextFancySellPrice = new PotentialTradeStats(3, 1, null);
        PotentialTradeStats item3PotentialTradeStatsBuyByMinSellPrice = new PotentialTradeStats(4, 1, null);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(new Item("itemId3"))).thenReturn(item3PotentialTradeStatsSellByMaxBuyPrice);
        when(potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(new Item("itemId3"))).thenReturn(item3PotentialTradeStatsSellByNextFancySellPrice);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(new Item("itemId3"))).thenReturn(item3PotentialTradeStatsBuyByMinSellPrice);
        expectedItemNoSales.setPriorityToSellByMaxBuyPrice(null);
        expectedItemNoSales.setPriorityToSellByNextFancySellPrice(null);
        expectedItemNoSales.setPriorityToBuyByMinSellPrice(null);

        List<Item> expectedResult = List.of(expectedRecalculatedItem1, expectedRecalculatedItem2, expectedItemNoSales);

        itemService.recalculateAndSaveAllItemsPotentialTradeStatsByCurrentPrices();

        verify(itemDatabaseService).updateAllItemsCurrentPricesHistoryFields(argThat(arg -> {
            arg.forEach(System.out::println);
            return arg.size() == expectedResult.size() &&
                   expectedResult.stream().allMatch(expectedItem ->
                           arg.stream().anyMatch(actualItem ->
                                   actualItem.itemHistoryFieldsAreEqual(expectedItem)
                           )
                   );
        }));
    }

}