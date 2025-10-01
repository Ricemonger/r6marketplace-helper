package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.CommonValuesService;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.PotentialTrade;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PotentialTradeItemsFactory {

    private final CommonValuesService commonValuesService;

    public List<PotentialTrade> createPotentialTradeItemsForUser(Collection<ItemCurrentPrices> ownedItemsCurrentPrices,
                                                                 Collection<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity,
                                                                 Integer minMedianPriceDifference,
                                                                 Integer minMedianPriceDifferencePercentage) {
        List<PotentialTrade> potentialTrades = new ArrayList<>();

        for (ItemCurrentPrices itemPrices : ownedItemsCurrentPrices) {
            ItemMedianPriceAndRarity itemMedianPriceAndRarity = itemsMedianPriceAndRarity.stream()
                    .filter(item -> item.getItemId().equals(itemPrices.getItemId()))
                    .findFirst()
                    .orElse(null);

            if (itemMedianPriceAndRarity != null && itemMedianPriceAndRarity.getMonthMedianPrice() != null) {
                PotentialTrade trade = createPotentialTradeItemsForUserOrNull(itemPrices, itemMedianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);
                if (trade != null) {
                    potentialTrades.add(trade);
                }
            }
        }

        if (!potentialTrades.isEmpty()) {
            log.debug("Potential trade items for user: {}", potentialTrades);
        }

        return potentialTrades;
    }

    public PotentialTrade createPotentialTradeItemsForUserOrNull(@NonNull ItemCurrentPrices itemsCurrentPrices,
                                                                 @NonNull ItemMedianPriceAndRarity itemMedianPriceAndRarity,
                                                                 @NonNull Integer minMedianPriceDifference,
                                                                 @NonNull Integer minMedianPriceDifferencePercentage) {

        int sellPrice = itemsCurrentPrices.getMinSellPrice() == null ? commonValuesService.getMaximumPriceByRarity(itemMedianPriceAndRarity.getRarity()) : itemsCurrentPrices.getMinSellPrice();
        int itemMonthMedianPrice = itemMedianPriceAndRarity.getMonthMedianPrice() == 0 ? commonValuesService.getMinimumPriceByRarity(itemMedianPriceAndRarity.getRarity()) : itemMedianPriceAndRarity.getMonthMedianPrice();

        if (itemsCurrentPrices.getMaxBuyPrice() != null) {
            Integer buyPriceMedianPriceDifference = itemsCurrentPrices.getMaxBuyPrice() - itemMonthMedianPrice;
            Integer buyPriceMedianPriceDifferencePercentage = (buyPriceMedianPriceDifference * 100) / itemMonthMedianPrice;

            if (buyPriceMedianPriceDifference >= minMedianPriceDifference && buyPriceMedianPriceDifferencePercentage >= minMedianPriceDifferencePercentage) {
                return new PotentialTrade(
                        itemsCurrentPrices.getItemId(),
                        itemsCurrentPrices.getMaxBuyPrice() - 1,
                        buyPriceMedianPriceDifference,
                        buyPriceMedianPriceDifferencePercentage,
                        true
                );
            }
        }

        Integer sellPriceMedianPriceDifference = sellPrice - itemMonthMedianPrice;
        Integer sellPriceMedianPriceDifferencePercentage = (sellPriceMedianPriceDifference * 100) / itemMonthMedianPrice;

        if (sellPriceMedianPriceDifference >= minMedianPriceDifference && sellPriceMedianPriceDifferencePercentage >= minMedianPriceDifferencePercentage) {
            return new PotentialTrade(
                    itemsCurrentPrices.getItemId(),
                    sellPrice - 1,
                    sellPriceMedianPriceDifference,
                    sellPriceMedianPriceDifferencePercentage,
                    false
            );
        }

        return null;
    }
}
