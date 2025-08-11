package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.CommonValuesService;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.PotentialTradeItem;
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

    public List<PotentialTradeItem> createPotentialTradeItemsForUser(Collection<ItemCurrentPrices> ownedItemsCurrentPrices,
                                                                     Collection<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity,
                                                                     Integer minMedianPriceDifference,
                                                                     Integer minMedianPriceDifferencePercentage) {
        List<PotentialTradeItem> potentialTradeItems = new ArrayList<>();

        for (ItemCurrentPrices itemPrices : ownedItemsCurrentPrices) {
            ItemMedianPriceAndRarity itemMedianPriceAndRarity = itemsMedianPriceAndRarity.stream()
                    .filter(item -> item.getItemId().equals(itemPrices.getItemId()))
                    .findFirst()
                    .orElse(null);

            if (itemMedianPriceAndRarity != null && itemMedianPriceAndRarity.getMonthMedianPrice() != null) {
                PotentialTradeItem trade = createPotentialTradeItemsForUserOrNull(itemPrices, itemMedianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);
                if (trade != null) {
                    potentialTradeItems.add(trade);
                }
            }
        }

        if (!potentialTradeItems.isEmpty()) {
            log.debug("Potential trade items for user: {}", potentialTradeItems);
        }

        return potentialTradeItems;
    }

    public PotentialTradeItem createPotentialTradeItemsForUserOrNull(@NonNull ItemCurrentPrices itemsCurrentPrices,
                                                                     @NonNull ItemMedianPriceAndRarity itemMedianPriceAndRarity,
                                                                     @NonNull Integer minMedianPriceDifference,
                                                                     @NonNull Integer minMedianPriceDifferencePercentage) {

        if (itemsCurrentPrices.getMaxBuyPrice() != null) {
            Integer buyPriceMedianPriceDifference = itemsCurrentPrices.getMaxBuyPrice() - itemMedianPriceAndRarity.getMonthMedianPrice();
            Integer buyPriceMedianPriceDifferencePercentage = (buyPriceMedianPriceDifference * 100) / itemMedianPriceAndRarity.getMonthMedianPrice();

            if (buyPriceMedianPriceDifference >= minMedianPriceDifference && buyPriceMedianPriceDifferencePercentage >= minMedianPriceDifferencePercentage) {
                return new PotentialTradeItem(
                        itemsCurrentPrices.getItemId(),
                        itemsCurrentPrices.getMaxBuyPrice() - 1,
                        buyPriceMedianPriceDifference,
                        buyPriceMedianPriceDifferencePercentage,
                        true
                );
            }
        }
        int sellPrice = itemsCurrentPrices.getMinSellPrice() == null ? commonValuesService.getMaximumPriceByRarity(itemMedianPriceAndRarity.getRarity()) : itemsCurrentPrices.getMinSellPrice();

        Integer sellPriceMedianPriceDifference = sellPrice - itemMedianPriceAndRarity.getMonthMedianPrice();
        Integer sellPriceMedianPriceDifferencePercentage = (sellPriceMedianPriceDifference * 100) / itemMedianPriceAndRarity.getMonthMedianPrice();

        if (sellPriceMedianPriceDifference >= minMedianPriceDifference && sellPriceMedianPriceDifferencePercentage >= minMedianPriceDifferencePercentage) {
            return new PotentialTradeItem(
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
