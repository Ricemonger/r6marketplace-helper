package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.UbiTradeI;
import github.ricemonger.utils.services.calculators.ItemTradeStatsCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class PotentialTradeStatsService {

    private final ItemTradeStatsCalculator itemTradeStatsCalculator;

    public PotentialTradeStats calculatePotentialBuyTradeStatsForExistingTrade(UbiTradeI ubiTrade) {
        return itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForExistingTrade(ubiTrade);
    }

    public PotentialTradeStats calculatePotentialSellTradeStatsForExistingTrade(UbiTradeI ubiTrade) {
        return itemTradeStatsCalculator.calculatePotentialSellTradeStatsForExistingTrade(ubiTrade);
    }
}
