package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.Trade;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final PotentialTradeStatsService potentialTradeStatsService;

    private final ItemDatabaseService itemDatabaseService;

    public List<Trade> calculateTradeStatsForUbiTrades(Collection<UbiTrade> ubiTrades) {
        List<String> itemIds = ubiTrades.stream().map(UbiTrade::getItemId).toList();

        List<Item> existingItems = itemDatabaseService.findItemsByIds(itemIds);

        List<Trade> trades = ubiTrades.stream().map(Trade::new).filter(trade -> existingItems.stream().anyMatch(item -> item.getItemId().equals(trade.getItem().getItemId()))).toList();

        for (Trade trade : trades) {
            Item item = existingItems.stream().filter(existingItem -> existingItem.equals(trade.getItem())).findFirst().get();

            trade.setItem(item);
            PotentialTradeStats potentialTradeStats;

            if (trade.getCategory() == TradeCategory.Sell) {
                potentialTradeStats = potentialTradeStatsService.calculatePotentialSellTradeStatsForExistingTrade(trade);
            } else {
                potentialTradeStats = potentialTradeStatsService.calculatePotentialBuyTradeStatsForExistingTrade(trade);
            }

            trade.setMinutesToTrade(potentialTradeStats.getPrognosedTradeSuccessMinutes());
            trade.setTradePriority(potentialTradeStats.getTradePriority());
        }

        return trades;
    }
}
