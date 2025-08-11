package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.*;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import github.ricemonger.utils.DTOs.personal.SellTradeWithPriceDifferences;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeCommandsFactory {

    public List<FastTradeCommand> createTradeCommandsForUser(ManagedUser user, List<SellTrade> currentSellTrades, List<ItemCurrentPrices> currentPrices, List<ItemMedianPriceAndRarity> medianPricesAndRarities, List<PotentialTrade> potentialTrades, Collection<String> alreadyManagedItems, int sellLimit, int sellSlots) {

        List<FastTradeCommand> commands = new LinkedList<>();

        int freeSlots = sellSlots - currentSellTrades.size();
        List<String> leaveUntouchedTradesIds = new ArrayList<>();

        for (PotentialTrade potential : potentialTrades.stream().sorted().toList()) {

            if (user.getResaleLocks().contains(potential.getItemId()) || commands.stream().anyMatch(command -> command.getItemId().equals(potential.getItemId()))) {
                continue;
            }
            SellTrade sellTrade = currentSellTrades.stream().filter(trade -> trade.getItemId().equals(potential.getItemId())).findFirst().orElse(null);
            if (sellTrade != null && sellTrade.getPrice() <= potential.getPrice() + 1) {
                leaveUntouchedTradesIds.add(sellTrade.getTradeId());
                alreadyManagedItems.add(potential.getItemId());
            } else if (sellTrade != null && sellTrade.getPrice() > potential.getPrice() + 1) {
                commands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_UPDATE, potential.getItemId(), sellTrade.getTradeId(), potential.getPrice()));
                alreadyManagedItems.add(potential.getItemId());
            } else if (sellTrade == null && sellLimit > user.getSoldIn24h()) {
                if (freeSlots > 0) {
                    commands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, potential.getItemId(), potential.getPrice()));
                    freeSlots--;
                } else {
                    List<FastTradeCommand> pairedCancelCreateCommands = createPairOfCancelCreateCommandsOrEmpty(user, currentSellTrades, leaveUntouchedTradesIds, commands, currentPrices,
                            medianPricesAndRarities, alreadyManagedItems, potential);

                    if (!pairedCancelCreateCommands.isEmpty()) {
                        commands.addAll(pairedCancelCreateCommands);
                        alreadyManagedItems.add(potential.getItemId());
                    }
                }
            }
        }

        return commands;
    }

    private List<FastTradeCommand> createPairOfCancelCreateCommandsOrEmpty(ManagedUser user,
                                                                           Collection<SellTrade> currentSellTrades,
                                                                           Collection<String> higherPriorityExistingTrades,
                                                                           Collection<FastTradeCommand> higherPriorityExistingCommands,
                                                                           Collection<ItemCurrentPrices> currentPrices,
                                                                           Collection<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                           Collection<String> alreadyManagedItems,
                                                                           PotentialTrade potentialTrade) {

        List<FastTradeCommand> pairCommands = new ArrayList<>();

        List<SellTradeWithPriceDifferences> sortedNotUpdatedTrades =
                getCurrentSellTradesByPriorityAsc(currentSellTrades, currentPrices, medianPriceAndRarities, alreadyManagedItems).stream().filter(trade ->
                        higherPriorityExistingTrades.stream().noneMatch(id -> id.equals(trade.getTradeId()))
                                && higherPriorityExistingCommands.stream().noneMatch(c -> trade.getTradeId().equals(c.getTradeId()))).toList();

        if (sortedNotUpdatedTrades.isEmpty()) {
            return pairCommands;
        } else if (potentialTrade.isSellByMaxBuyPrice() || potentialTrade.getMonthMedianPriceDifference() * potentialTrade.getMonthMedianPriceDifferencePercentage() > sortedNotUpdatedTrades.get(0).getMonthMedianPriceDifference() * sortedNotUpdatedTrades.get(0).getMonthMedianPriceDifferencePercentage()) {
            pairCommands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, sortedNotUpdatedTrades.get(0).getItemId(), sortedNotUpdatedTrades.get(0).getTradeId()));
            pairCommands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, potentialTrade.getItemId(), potentialTrade.getPrice()));
        }
        return pairCommands;
    }

    private List<SellTradeWithPriceDifferences> getCurrentSellTradesByPriorityAsc(Collection<SellTrade> currentSellTrades,
                                                                                  Collection<ItemCurrentPrices> itemsCurrentPrices,
                                                                                  Collection<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                                  Collection<String> alreadyManagedItems) {

        return currentSellTrades.stream().map(trade -> {
                    int price = trade.getPrice() == null ? Integer.MAX_VALUE : trade.getPrice();

                    Integer minSellPrice = itemsCurrentPrices.stream().filter(item -> item.getItemId().equals(trade.getItemId())).findFirst().orElse(new ItemCurrentPrices()).getMinSellPrice();
                    minSellPrice = minSellPrice == null ? 0 : minSellPrice;
                    minSellPrice = minSellPrice == 0 && alreadyManagedItems.contains(trade.getItemId()) ? price : minSellPrice;

                    Integer medianPrice = medianPriceAndRarities.stream().filter(item -> item.getItemId().equals(trade.getItemId())).findFirst().orElse(new ItemMedianPriceAndRarity()).getMonthMedianPrice();
                    medianPrice = medianPrice == null ? 1 : medianPrice;

                    Integer medianPriceDifference = price - medianPrice;
                    Integer medianPriceDifferencePercentage = (100 * medianPriceDifference) / medianPrice;

                    return new SellTradeWithPriceDifferences(trade.getTradeId(), trade.getItemId(), trade.getPrice(), minSellPrice, medianPriceDifference, medianPriceDifferencePercentage);
                }
        ).sorted(new Comparator<SellTradeWithPriceDifferences>() {
            @Override
            public int compare(SellTradeWithPriceDifferences o1, SellTradeWithPriceDifferences o2) {

                if (o1.getMinSellPrice() >= o1.getPrice() && o2.getMinSellPrice() < o2.getPrice()) {
                    return 1;
                } else if (o1.getMinSellPrice() < o1.getPrice() && o2.getMinSellPrice() >= o2.getPrice()) {
                    return -1;
                } else {
                    Integer medianPriceCoef1 = o1.getMonthMedianPriceDifference() * o2.getMonthMedianPriceDifferencePercentage();
                    Integer medianPriceCoef2 = o2.getMonthMedianPriceDifference() * o2.getMonthMedianPriceDifferencePercentage();

                    return medianPriceCoef1.compareTo(medianPriceCoef2);
                }
            }
        }).toList();
    }

    public List<FastTradeCommand> createTradeCommandsToKeepUnusedSlotForUser(ManagedUser user,
                                                                             Collection<SellTrade> currentSellTrades,
                                                                             Collection<ItemCurrentPrices> itemsCurrentPrices,
                                                                             int sellLimit,
                                                                             int sellSlots) {
        List<FastTradeCommand> commands = new ArrayList<>();

        if (user.getSoldIn24h() >= sellLimit || currentSellTrades.size() < sellSlots) {
            log.info("User has reached the sell limit for 24h or not all slots are used, skipping commands for slot cleaning");
            return commands;
        }

        List<SellTrade> filteredAndSortedTrades = getCurrentSellTradesWithBiggerThenMinSellPriceByPriorityAsc(currentSellTrades, itemsCurrentPrices);

        if (!filteredAndSortedTrades.isEmpty()) {
            commands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, filteredAndSortedTrades.get(0).getItemId(), filteredAndSortedTrades.get(0).getTradeId()));
        }

        return commands;
    }

    private List<SellTrade> getCurrentSellTradesWithBiggerThenMinSellPriceByPriorityAsc(Collection<SellTrade> currentSellTrades,
                                                                                        Collection<ItemCurrentPrices> ownedItemsCurrentPrices) {

        return currentSellTrades.stream().filter(
                trade -> ownedItemsCurrentPrices.stream().filter(item -> item.getItemId().equals(trade.getItemId())).findFirst().orElse(new ItemCurrentPrices("", 1, 1)).getMinSellPrice() < trade.getPrice()
        ).sorted(new Comparator<SellTrade>() {
            @Override
            public int compare(SellTrade o1, SellTrade o2) {
                Integer minSellPrice1 = ownedItemsCurrentPrices.stream().filter(item -> item.getItemId().equals(o1.getItemId())).findFirst().orElse(new ItemCurrentPrices()).getMinSellPrice();
                Integer minSellPrice2 = ownedItemsCurrentPrices.stream().filter(item -> item.getItemId().equals(o2.getItemId())).findFirst().orElse(new ItemCurrentPrices()).getMinSellPrice();

                minSellPrice1 = minSellPrice1 == null ? 1 : minSellPrice1;
                minSellPrice2 = minSellPrice2 == null ? 1 : minSellPrice2;

                int price1 = o1.getPrice() == null ? Integer.MAX_VALUE : o1.getPrice();
                int price2 = o2.getPrice() == null ? Integer.MAX_VALUE : o2.getPrice();

                if (price1 / minSellPrice1 < price2 / minSellPrice2) {
                    return 1;
                } else if (price1 / minSellPrice1 > price2 / minSellPrice2) {
                    return -1;
                } else {
                    return price1 - price2;
                }
            }
        }).toList();
    }
}
