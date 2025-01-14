package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.*;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeManagementCommandsFactory {

    public List<FastTradeManagerCommand> createFastSellTradeManagerCommandsForUser(FastSellManagedUser user,
                                                                                   List<SellTrade> currentSellTrades,
                                                                                   List<PotentialTrade> items,
                                                                                   List<ItemMedianPriceAndRarity> medianPriceAndRarities,
                                                                                   ConfigTrades configTrades) {
        List<FastTradeManagerCommand> commands = new LinkedList<>();
        int createdOrders = 0;

        int sellLimit = configTrades.getSellLimit();
        int sellSlots = configTrades.getSellSlots();

        if (user.getSoldIn24h() > sellLimit) {
            log.info("User has reached the sell limit for 24h, skipping commands creation");
            return new ArrayList<>();
        }

        int availableSlots = sellSlots - currentSellTrades.size();

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
            if (sellTrade != null) {
                commands.add(new FastTradeManagerCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_UPDATE, potential.getItemId(), sellTrade.getTradeId(), potential.getPrice()));
            } else {
                commands.add(new FastTradeManagerCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CREATE, potential.getItemId(), potential.getPrice()));
                createdOrders++;
            }
        }

        if (availableSlots >= createdOrders) {
            return commands;
        } else {
            List<SellTrade> sortedTrades = currentSellTrades.stream().sorted(new Comparator<SellTrade>() {
                @Override
                public int compare(SellTrade o1, SellTrade o2) {
                    int price1 = o1.getPrice();
                    int price2 = o2.getPrice();

                    Integer medianPrice1 = medianPriceAndRarities.stream().filter(item -> item.getItemId().equals(o1.getItemId())).findFirst().orElse(new ItemMedianPriceAndRarity()).getMonthMedianPrice();
                    Integer medianPrice2 = medianPriceAndRarities.stream().filter(item -> item.getItemId().equals(o2.getItemId())).findFirst().orElse(new ItemMedianPriceAndRarity()).getMonthMedianPrice();

                    medianPrice1 = medianPrice1 == null ? price1 : medianPrice1;
                    medianPrice2 = medianPrice2 == null ? price2 : medianPrice2;

                    Integer medianPriceDiff1 = (medianPrice1 - price1) * (medianPrice1 - price1) / price1;
                    Integer medianPriceDiff2 = (medianPrice2 - price2) * (medianPrice2 - price2) / price2;

                    return medianPriceDiff1.compareTo(medianPriceDiff2);
                }
            }).toList();

            for (int i = 0; i < createdOrders - availableSlots; i++) {
                commands.add(new FastTradeManagerCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, sortedTrades.get(i).getItemId(), sortedTrades.get(i).getTradeId()));
            }
            return commands;
        }
    }
}