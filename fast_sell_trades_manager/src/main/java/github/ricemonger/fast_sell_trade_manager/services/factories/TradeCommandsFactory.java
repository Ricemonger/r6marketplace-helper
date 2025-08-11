package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.*;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeCommandsFactory {

    public List<FastTradeCommand> createTradeCommandsForUser(ManagedUser user, List<SellTrade> currentSellTrades, List<ItemCurrentPrices> currentPrices, List<ItemMedianPriceAndRarity> medianPricesAndRarities, List<PotentialTradeItem> potentialTradeItems, int sellLimit, int sellSlots) {

        List<FastTradeCommand> commands = new LinkedList<>();

        int freeSlots = sellSlots - currentSellTrades.size();
        List<String> leaveUntouchedTradesIds = new ArrayList<>();

        for (PotentialTradeItem potential : potentialTradeItems.stream().sorted().toList()) {

            if (user.getResaleLocks().contains(potential.getItemId()) || commands.stream().anyMatch(command -> command.getItemId().equals(potential.getItemId()))) {
                continue;
            }
            SellTrade sellTrade = currentSellTrades.stream().filter(trade -> trade.getItemId().equals(potential.getItemId())).findFirst().orElse(null);
            if (sellTrade != null && sellTrade.getPrice() <= potential.getPrice() + 1) {
                leaveUntouchedTradesIds.add(sellTrade.getTradeId());
            } else if (sellTrade != null && sellTrade.getPrice() > potential.getPrice() + 1) {
                commands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_UPDATE, potential.getItemId(), sellTrade.getTradeId(), potential.getPrice()));
            } else if (sellTrade == null && sellLimit > user.getSoldIn24h()) {
                if (freeSlots > 0) {
                    commands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, potential.getItemId(), potential.getPrice()));
                    freeSlots--;
                } else {
                    commands.addAll(createPairOfCancelCreateCommandsOrEmpty(user, currentSellTrades, leaveUntouchedTradesIds, commands, currentPrices,
                            medianPricesAndRarities, potential));
                }
            }
        }

        return commands;
    }

    public List<FastTradeCommand> createTradeCommandsToKeepUnusedSlotForUser(ManagedUser user,
                                                                             Collection<SellTrade> currentSellTrades,
                                                                             Collection<ItemCurrentPrices> itemsCurrentPrices,
                                                                             List<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                             int sellLimit,
                                                                             int sellSlots) {
        List<FastTradeCommand> commands = new ArrayList<>();

        if (user.getSoldIn24h() >= sellLimit || currentSellTrades.size() < sellSlots) {
            log.info("User has reached the sell limit for 24h or not all slots are used, skipping commands for slot cleaning");
            return commands;
        }

        List<SellTrade> sortedTrades = getCurrentSellTradesWithBiggerThenMinSellPriceByPriorityAsc(currentSellTrades, itemsCurrentPrices, medianPriceAndRarities);

        if (!sortedTrades.isEmpty()) {
            commands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, sortedTrades.get(0).getItemId(), sortedTrades.get(0).getTradeId()));
        }

        return commands;
    }

    private List<FastTradeCommand> createPairOfCancelCreateCommandsOrEmpty(ManagedUser user,
                                                                           Collection<SellTrade> currentSellTrades,
                                                                           Collection<String> higherPriorityExistingTrades,
                                                                           Collection<FastTradeCommand> higherPriorityExistingCommands,
                                                                           Collection<ItemCurrentPrices> currentPrices,
                                                                           Collection<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                           PotentialTradeItem item) {

        List<FastTradeCommand> pairCommands = new ArrayList<>();

        List<SellTrade> sortedNotUpdatedTrades =
                getCurrentSellTradesByPriorityAsc(currentSellTrades, currentPrices, medianPriceAndRarities).stream().filter(trade ->
                        higherPriorityExistingTrades.stream().noneMatch(id -> id.equals(trade.getTradeId()))
                                && higherPriorityExistingCommands.stream().noneMatch(c -> trade.getTradeId().equals(c.getTradeId()))).toList();

        if (sortedNotUpdatedTrades.isEmpty()) {
            return pairCommands;
        } else {
            pairCommands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, sortedNotUpdatedTrades.get(0).getItemId(), sortedNotUpdatedTrades.get(0).getTradeId()));
            pairCommands.add(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, item.getItemId(), item.getPrice()));
        }

        return pairCommands;
    }

    private List<SellTrade> getCurrentSellTradesWithBiggerThenMinSellPriceByPriorityAsc(Collection<SellTrade> currentSellTrades,
                                                                                        Collection<ItemCurrentPrices> ownedItemsCurrentPrices,
                                                                                        Collection<ItemMedianPriceAndRarity> medianPriceAndRarities) {

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

    private List<SellTrade> getCurrentSellTradesByPriorityAsc(Collection<SellTrade> currentSellTrades,
                                                              Collection<ItemCurrentPrices> itemsCurrentPrices,
                                                              Collection<ItemMedianPriceAndRarity> medianPriceAndRarities) {

        return currentSellTrades.stream().sorted(new Comparator<SellTrade>() {
            @Override
            public int compare(SellTrade o1, SellTrade o2) {
                Integer minSellPrice1 = itemsCurrentPrices.stream().filter(item -> item.getItemId().equals(o1.getItemId())).findFirst().orElse(new ItemCurrentPrices()).getMinSellPrice();
                Integer minSellPrice2 = itemsCurrentPrices.stream().filter(item -> item.getItemId().equals(o2.getItemId())).findFirst().orElse(new ItemCurrentPrices()).getMinSellPrice();

                minSellPrice1 = minSellPrice1 == null ? 0 : minSellPrice1;
                minSellPrice2 = minSellPrice2 == null ? 0 : minSellPrice2;

                int price1 = o1.getPrice() == null ? Integer.MAX_VALUE : o1.getPrice();
                int price2 = o2.getPrice() == null ? Integer.MAX_VALUE : o2.getPrice();

                if (minSellPrice1 >= price1 && minSellPrice2 < price2) {
                    return 1;
                } else if (minSellPrice1 < price1 && minSellPrice2 >= price2) {
                    return -1;
                } else {
                    Integer medianPrice1 = medianPriceAndRarities.stream().filter(item -> item.getItemId().equals(o1.getItemId())).findFirst().orElse(new ItemMedianPriceAndRarity()).getMonthMedianPrice();
                    Integer medianPrice2 = medianPriceAndRarities.stream().filter(item -> item.getItemId().equals(o2.getItemId())).findFirst().orElse(new ItemMedianPriceAndRarity()).getMonthMedianPrice();

                    medianPrice1 = medianPrice1 == null ? price1 : medianPrice1;
                    medianPrice2 = medianPrice2 == null ? price2 : medianPrice2;

                    Integer medianPriceDiff1 = (price1 - medianPrice1) * (price1 - medianPrice1) / medianPrice1;
                    Integer medianPriceDiff2 = (price2 - medianPrice2) * (price2 - medianPrice2) / medianPrice2;

                    return medianPriceDiff1.compareTo(medianPriceDiff2);
                }
            }
        }).toList();
    }
}
