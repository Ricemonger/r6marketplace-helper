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
public class TradeManagementCommandsFactory {

    public List<FastSellCommand> createFastSellCommandsForUser(FastSellManagedUser user, List<SellTrade> currentSellTrades, List<ItemCurrentPrices> currentPrices, List<ItemMedianPriceAndRarity> medianPricesAndRarities, List<PotentialTrade> items, int sellLimit, int sellSlots) {

        List<FastSellCommand> commands = new LinkedList<>();

        int freeSlots = sellSlots - currentSellTrades.size();
        List<String> leaveUntouchedTradesIds = new ArrayList<>();

        for (PotentialTrade potential : items.stream().sorted(new Comparator<PotentialTrade>() {
            @Override
            public int compare(PotentialTrade o1, PotentialTrade o2) {
                boolean byMaxBuyPrice1 = o1.isSellByMaxBuyPrice();
                boolean byMaxBuyPrice2 = o2.isSellByMaxBuyPrice();

                if (byMaxBuyPrice1 && !byMaxBuyPrice2) {
                    return -1;
                } else if (!byMaxBuyPrice1 && byMaxBuyPrice2) {
                    return 1;
                } else {

                    Integer diff1 = o1.getMonthMedianPriceDifference() * o1.getMonthMedianPriceDifferencePercentage();
                    Integer diff2 = o2.getMonthMedianPriceDifference() * o2.getMonthMedianPriceDifferencePercentage();

                    return -diff1.compareTo(diff2);
                }
            }
        }).toList()) {

            if (user.getResaleLocks().contains(potential.getItemId()) || commands.stream().anyMatch(command -> command.getItemId().equals(potential.getItemId()))) {
                continue;
            }
            SellTrade sellTrade = currentSellTrades.stream().filter(trade -> trade.getItemId().equals(potential.getItemId())).findFirst().orElse(null);
            if (sellTrade != null && sellTrade.getPrice() <= potential.getPrice() + 1) {
                leaveUntouchedTradesIds.add(sellTrade.getTradeId());
            } else if (sellTrade != null && sellTrade.getPrice() > potential.getPrice() + 1) {
                commands.add(new FastSellCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_UPDATE, potential.getItemId(), sellTrade.getTradeId(), potential.getPrice()));
            } else if (sellTrade == null && sellLimit > user.getSoldIn24h()) {
                if (freeSlots > 0) {
                    commands.add(new FastSellCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, potential.getItemId(), potential.getPrice()));
                    freeSlots--;
                } else {
                    commands.addAll(createCancelCreatePairCommandsOrEmpty(user, currentSellTrades, leaveUntouchedTradesIds, commands, currentPrices,
                            medianPricesAndRarities, potential));
                }
            }
        }

        return commands;
    }

    public List<FastSellCommand> createKeepUnusedSlotCommandForUser(FastSellManagedUser user,
                                                                    Collection<SellTrade> currentSellTrades,
                                                                    Collection<ItemCurrentPrices> itemsCurrentPrices,
                                                                    List<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                    int sellLimit,
                                                                    int sellSlots) {
        List<FastSellCommand> commands = new ArrayList<>();

        if (user.getSoldIn24h() >= sellLimit || currentSellTrades.size() < sellSlots) {
            log.info("User has reached the sell limit for 24h or not all slots are used, skipping commands for slot cleaning");
            return commands;
        }

        List<SellTrade> sortedTrades = getCurrentSellTradesByPriorityAsc(currentSellTrades, itemsCurrentPrices, medianPriceAndRarities);

        if (!sortedTrades.isEmpty()) {
            commands.add(new FastSellCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, sortedTrades.get(0).getItemId(), sortedTrades.get(0).getTradeId()));
        }

        return commands;
    }

    private List<FastSellCommand> createCancelCreatePairCommandsOrEmpty(FastSellManagedUser user,
                                                                        Collection<SellTrade> currentSellTrades,
                                                                        Collection<String> higherPriorityExistingTrades,
                                                                        Collection<FastSellCommand> higherPriorityExistingCommands,
                                                                        Collection<ItemCurrentPrices> currentPrices,
                                                                        Collection<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                        PotentialTrade item) {

        List<FastSellCommand> pairCommands = new ArrayList<>();

        List<SellTrade> sortedNotUpdatedTrades =
                getCurrentSellTradesByPriorityAsc(currentSellTrades, currentPrices, medianPriceAndRarities).stream().filter(trade ->
                        higherPriorityExistingTrades.stream().noneMatch(id -> id.equals(trade.getTradeId()))
                        && higherPriorityExistingCommands.stream().noneMatch(c -> trade.getTradeId().equals(c.getTradeId()))).toList();

        if (sortedNotUpdatedTrades.isEmpty()) {
            return pairCommands;
        } else {
            pairCommands.add(new FastSellCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, sortedNotUpdatedTrades.get(0).getItemId(), sortedNotUpdatedTrades.get(0).getTradeId()));
            pairCommands.add(new FastSellCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, item.getItemId(), item.getPrice()));
        }

        return pairCommands;
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
