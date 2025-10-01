package github.ricemonger.item_trade_stats_calculator.services;

import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemCurrentPricesRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.abstractions.ItemDatabaseService;
import github.ricemonger.item_trade_stats_calculator.services.abstractions.ItemSaleDatabaseService;
import github.ricemonger.item_trade_stats_calculator.services.abstractions.ItemSaleUbiStatsDatabaseService;
import github.ricemonger.utils.DTOs.common.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemDatabaseService itemDatabaseService;

    private final ItemSaleDatabaseService saleDatabaseService;

    private final ItemSaleUbiStatsDatabaseService itemSaleUbiStatsDatabaseService;

    private final PotentialTradeStatsService potentialTradeStatsService;

    public void recalculateAndSaveAllItemsHistoryFields() {
        Set<Item> itemWithRequiredFields = itemDatabaseService.findAllItemsRecalculationRequiredFields().stream().map(ItemRecalculationRequiredFields::toItem).collect(Collectors.toSet());

        Collection<ItemSale> lastMonthSales = saleDatabaseService.findAllForLastMonth();
        Collection<ItemDaySalesUbiStats> lastMonthSalesUbiStats = itemSaleUbiStatsDatabaseService.findAllForLastMonth();

        for (Item item : itemWithRequiredFields) {
            Collection<ItemSale> saleStats = getItemSalesForItem(lastMonthSales, item.getItemId());
            Collection<ItemDaySalesUbiStats> ubiSaleStats = getItemDaySalesUbiStatsForItem(lastMonthSalesUbiStats, item.getItemId());
            List<ItemDaySalesStatsByItemId> resultingPerDayStats = getResultingSaleStatsByPeriodForItem(saleStats, ubiSaleStats, item,
                    LocalDate.now().minusDays(30), LocalDate.now());

            TodayPriceStats todayStats = recalculateTodayItemPriceStats(resultingPerDayStats.get(30));
            item.setDaySales(todayStats.quantity());
            item.setDayAveragePrice(todayStats.averagePrice());
            item.setDayMaxPrice(todayStats.maxPrice());
            item.setDayMinPrice(todayStats.minPrice());
            item.setDayMedianPrice(todayStats.medianPrice());

            LastMonthPriceStats lastMonthStats = recalculateLastMonthItemPriceStats(resultingPerDayStats);
            item.setMonthSales(lastMonthStats.sales());
            item.setMonthSalesPerDay(lastMonthStats.salesPerDay());
            item.setMonthAveragePrice(lastMonthStats.averagePrice());
            item.setMonthMaxPrice(lastMonthStats.maxPrice());
            item.setMonthMinPrice(lastMonthStats.minPrice());
            item.setMonthMedianPrice(lastMonthStats.medianPrice());

            PotentialTradeStats potentialTradeToBuyIn1Hour = potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(item, resultingPerDayStats, 60);
            PotentialTradeStats potentialTradeToBuyIn6Hours = potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(item, resultingPerDayStats, 360);
            PotentialTradeStats potentialTradeToBuyIn24Hours = potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(item, resultingPerDayStats, 1440);
            PotentialTradeStats potentialTradeToBuyIn168Hours = potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(item, resultingPerDayStats, 10080);
            PotentialTradeStats potentialTradeToBuyIn720Hours = potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(item, resultingPerDayStats, 43200);

            PotentialTradeStats potentialTradeToBuyInstantlyByMinSellPrice = potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(item);

            PotentialTradeStats potentialTradeToSellInstantlyByMaxBuyPrice = potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(item);
            PotentialTradeStats potentialTradeToSellByNextFancySellPrice = potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(item);

            item.setPriorityToSellByMaxBuyPrice(potentialTradeToSellInstantlyByMaxBuyPrice.getTradePriority());

            item.setPriorityToSellByNextFancySellPrice(potentialTradeToSellByNextFancySellPrice.getTradePriority());
            item.setPriorityToBuyByMinSellPrice(potentialTradeToBuyInstantlyByMinSellPrice.getTradePriority());

            item.setPriorityToBuyIn1Hour(potentialTradeToBuyIn1Hour.getTradePriority());
            item.setPriorityToBuyIn6Hours(potentialTradeToBuyIn6Hours.getTradePriority());
            item.setPriorityToBuyIn24Hours(potentialTradeToBuyIn24Hours.getTradePriority());
            item.setPriorityToBuyIn168Hours(potentialTradeToBuyIn168Hours.getTradePriority());
            item.setPriorityToBuyIn720Hours(potentialTradeToBuyIn720Hours.getTradePriority());

            item.setPriceToBuyIn1Hour(potentialTradeToBuyIn1Hour.getPrice());
            item.setPriceToBuyIn6Hours(potentialTradeToBuyIn6Hours.getPrice());
            item.setPriceToBuyIn24Hours(potentialTradeToBuyIn24Hours.getPrice());
            item.setPriceToBuyIn168Hours(potentialTradeToBuyIn168Hours.getPrice());
            item.setPriceToBuyIn720Hours(potentialTradeToBuyIn720Hours.getPrice());
        }
        itemDatabaseService.updateAllItemsHistoryFields(itemWithRequiredFields);
    }

    public void recalculateAndSaveAllItemsPotentialTradeStatsByCurrentPrices() {
        Set<Item> itemWithRequiredFields = itemDatabaseService.findAllItemsCurrentPricesRecalculationRequiredFields().stream().map(ItemCurrentPricesRecalculationRequiredFields::toItem).collect(Collectors.toSet());

        for (Item item : itemWithRequiredFields) {
            PotentialTradeStats potentialTradeToBuyInstantlyByMinSellPrice = potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(item);

            PotentialTradeStats potentialTradeToSellInstantlyByMaxBuyPrice = potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(item);
            PotentialTradeStats potentialTradeToSellByNextFancySellPrice = potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(item);

            item.setPriorityToBuyByMinSellPrice(potentialTradeToBuyInstantlyByMinSellPrice.getTradePriority());

            item.setPriorityToSellByMaxBuyPrice(potentialTradeToSellInstantlyByMaxBuyPrice.getTradePriority());
            item.setPriorityToSellByNextFancySellPrice(potentialTradeToSellByNextFancySellPrice.getTradePriority());
        }
        itemDatabaseService.updateAllItemsCurrentPricesHistoryFields(itemWithRequiredFields);
    }

    private TodayPriceStats recalculateTodayItemPriceStats(ItemDaySalesStatsByItemId todayStats) {
        int todaySalesQuantity = todayStats.getQuantity();
        int todaySumSales = todayStats.getPriceAndQuantity().entrySet().stream().mapToInt(entry -> entry.getKey() * entry.getValue()).sum();
        int todayMedianPrice = 0;

        TreeMap<Integer, Integer> sortedTodayPrices = new TreeMap<>(todayStats.getPriceAndQuantity());

        int currentQuantity = 0;
        for (Map.Entry<Integer, Integer> entry : sortedTodayPrices.entrySet()) {
            currentQuantity += entry.getValue();
            if (currentQuantity >= (float) todaySalesQuantity / 2) {
                todayMedianPrice = entry.getKey();
                break;
            }
        }


        return new TodayPriceStats(todaySalesQuantity,
                todaySalesQuantity == 0 ? 0 : todaySumSales / todaySalesQuantity,
                todayStats.getPriceAndQuantity().keySet().stream().max(Integer::compareTo).orElse(0),
                todayStats.getPriceAndQuantity().keySet().stream().min(Integer::compareTo).orElse(0),
                todayMedianPrice);
    }

    private LastMonthPriceStats recalculateLastMonthItemPriceStats(Collection<ItemDaySalesStatsByItemId> monthStats) {
        int monthSalesQuantity = monthStats.stream().mapToInt(ItemDaySalesStatsByItemId::getQuantity).sum();
        int monthSumSales = 0;
        int monthMaxPrice = 0;
        int monthMinPrice = Integer.MAX_VALUE;
        int monthMedianPrice = 0;
        int currentQuantity = 0;
        TreeMap<Integer, Integer> monthDayMedianPricesAndQuantities = new TreeMap<>();

        for (ItemDaySalesStatsByItemId dayStats : monthStats) {
            for (Map.Entry<Integer, Integer> priceAndQuantity : dayStats.getPriceAndQuantity().entrySet()) {
                if (priceAndQuantity.getKey() > monthMaxPrice) {
                    monthMaxPrice = priceAndQuantity.getKey();
                }
                if (priceAndQuantity.getKey() < monthMinPrice) {
                    monthMinPrice = priceAndQuantity.getKey();
                }
                monthSumSales += priceAndQuantity.getValue() * priceAndQuantity.getKey();
            }

            TreeMap<Integer, Integer> sortedPrices = new TreeMap<>(dayStats.getPriceAndQuantity());
            int daySalesQuantity = dayStats.getQuantity();
            currentQuantity = 0;
            for (Map.Entry<Integer, Integer> entry : sortedPrices.entrySet()) {
                currentQuantity += entry.getValue();
                if (currentQuantity >= (float) daySalesQuantity / 2) {
                    monthDayMedianPricesAndQuantities.put(entry.getKey(),
                            monthDayMedianPricesAndQuantities.getOrDefault(entry.getKey(), 0) + daySalesQuantity);
                    break;
                }
            }
        }
        currentQuantity = 0;
        for (Map.Entry<Integer, Integer> entry : monthDayMedianPricesAndQuantities.entrySet()) {
            currentQuantity += entry.getValue();
            if (currentQuantity >= monthSalesQuantity / 2) {
                monthMedianPrice = entry.getKey();
                break;
            }
        }
        if (monthMinPrice == Integer.MAX_VALUE) {
            monthMinPrice = 0;
        }


        return new LastMonthPriceStats(monthSalesQuantity, monthSalesQuantity / 31, monthSalesQuantity == 0 ? 0 : monthSumSales / monthSalesQuantity
                , monthMaxPrice,
                monthMinPrice,
                monthMedianPrice);
    }

    private List<ItemDaySalesStatsByItemId> getResultingSaleStatsByPeriodForItem(Collection<ItemSale> saleStats,
                                                                                 Collection<ItemDaySalesUbiStats> ubiSaleStats, Item item, LocalDate startDate, LocalDate endDate) {
        List<ItemDaySalesStatsByItemId> resultingPerDayStats = new ArrayList<>();

        for (LocalDate day = startDate; day.isBefore(endDate.plusDays(1)); day = day.plusDays(1)) {
            ItemDaySalesStatsByItemId daySaleStats = new ItemDaySalesStatsByItemId(item.getItemId(), day, saleStats);
            ItemDaySalesStatsByItemId ubiDayStats = new ItemDaySalesStatsByItemId(day, item.getItemId(), ubiSaleStats);

            if (daySaleStats.getQuantity() < (int) (ubiDayStats.getQuantity() * 0.8)) {
                resultingPerDayStats.add(ubiDayStats);
            } else {
                resultingPerDayStats.add(daySaleStats);
            }
        }
        return resultingPerDayStats;
    }

    private Collection<ItemSale> getItemSalesForItem(Collection<ItemSale> itemSales, String itemId) {
        return itemSales.stream().filter(itemSale -> itemSale.getItemId().equals(itemId)).toList();
    }

    private Collection<ItemDaySalesUbiStats> getItemDaySalesUbiStatsForItem(Collection<ItemDaySalesUbiStats> itemDaySalesUbiStatEntityDTOS, String itemId) {
        return itemDaySalesUbiStatEntityDTOS.stream().filter(ubiStats -> ubiStats.getItemId().equals(itemId)).toList();
    }

    private record LastMonthPriceStats(int sales, int salesPerDay, int averagePrice, int maxPrice, int minPrice, int medianPrice) {
    }

    private record TodayPriceStats(int quantity, int averagePrice, int maxPrice, int minPrice, int medianPrice) {
    }
}
