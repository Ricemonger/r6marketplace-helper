package github.ricemonger.trades_manager.services.factories;

import github.ricemonger.trades_manager.services.DTOs.PersonalItem;
import github.ricemonger.trades_manager.services.DTOs.Trade;
import github.ricemonger.trades_manager.services.DTOs.TradeByFiltersManager;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PersonalItemFactory {

    public Set<PersonalItem> getPersonalItemsForUser(Collection<TradeByFiltersManager> tradeByFiltersManagers,
                                                     Collection<TradeByItemIdManager> tradeByItemIdManagers,
                                                     Collection<Trade> existingSellTrades,
                                                     Collection<Trade> existingBuyTrades,
                                                     Collection<String> ownedItemsId,
                                                     Collection<Item> existingItems) {
        HashSet<PersonalItem> personalItems = new HashSet<>();
        personalItems.addAll(getPersonalItemsFromTradeByItemIdManagersByPriority(
                tradeByItemIdManagers,
                existingSellTrades,
                existingBuyTrades,
                ownedItemsId,
                existingItems));
        personalItems.addAll(getPersonalItemsFromTradeByFiltersManagersByPriority(
                tradeByFiltersManagers,
                existingSellTrades,
                existingBuyTrades,
                ownedItemsId,
                existingItems));

        return personalItems;
    }

    public Set<PersonalItem> getPersonalItemsFromTradeByFiltersManagersByPriority(Collection<TradeByFiltersManager> tradeByFiltersManagers,
                                                                                  Collection<Trade> existingSellTrades,
                                                                                  Collection<Trade> existingBuyTrades,
                                                                                  Collection<String> ownedItemsIds,
                                                                                  Collection<Item> existingItems) {
        Set<PersonalItem> personalItems = new HashSet<>();

        if (tradeByFiltersManagers == null || tradeByFiltersManagers.isEmpty() || existingItems == null || existingItems.isEmpty()) {
            return personalItems;
        }

        List<TradeByFiltersManager> sortedTradeByFiltersManagers =
                tradeByFiltersManagers.stream().filter(m -> m.getPriorityMultiplier() != null).sorted(Comparator.comparingInt(TradeByFiltersManager::getPriorityMultiplier).reversed()).toList();

        for (TradeByFiltersManager tradeByFiltersManager : sortedTradeByFiltersManagers) {
            personalItems.addAll(getPersonalItemsFromTradeByFiltersManager(
                    tradeByFiltersManager,
                    existingSellTrades,
                    existingBuyTrades,
                    ownedItemsIds,
                    existingItems));
        }
        return personalItems;
    }

    public Set<PersonalItem> getPersonalItemsFromTradeByFiltersManager(TradeByFiltersManager tradeByFiltersManager,
                                                                       Collection<Trade> existingSellTrades,
                                                                       Collection<Trade> existingBuyTrades,
                                                                       Collection<String> ownedItemsIds,
                                                                       Collection<Item> existingItems) {
        if (tradeByFiltersManager.getEnabled() == null ||
            !tradeByFiltersManager.getEnabled() ||
            existingItems == null ||
            existingItems.isEmpty() ||
            tradeByFiltersManager.getAppliedFilters() == null ||
            tradeByFiltersManager.getAppliedFilters().isEmpty()) {
            return new HashSet<>();
        } else {
            return new HashSet<>(ItemFilter.filterItems(existingItems, tradeByFiltersManager.getAppliedFilters())
                    .stream()
                    .map(item -> {
                                int sellBoundaryPrice = Integer.MIN_VALUE;
                                int buyBoundaryPrice = Integer.MAX_VALUE;

                                boolean isOwned = ownedItemsIds.contains(item.getItemId());
                                Trade existingTrade;

                                if (isOwned) {
                                    existingTrade = existingSellTrades.stream().filter(t -> t.getItemId().equals(item.getItemId())).findFirst().orElse(null);
                                } else {
                                    existingTrade = existingBuyTrades.stream().filter(t -> t.getItemId().equals(item.getItemId())).findFirst().orElse(null);
                                }

                                return new PersonalItem(
                                        item,
                                        sellBoundaryPrice,
                                        buyBoundaryPrice,
                                        tradeByFiltersManager.getMinDifferenceFromMedianPrice(),
                                        tradeByFiltersManager.getMinDifferenceFromMedianPricePercent(),
                                        tradeByFiltersManager.getTradeOperationType(),
                                        tradeByFiltersManager.getPriorityMultiplier(),
                                        isOwned,
                                        existingTrade != null,
                                        existingTrade
                                );
                            }
                    )
                    .toList());
        }
    }

    public Set<PersonalItem> getPersonalItemsFromTradeByItemIdManagersByPriority(Collection<TradeByItemIdManager> tradeByItemIdManagers,
                                                                                 Collection<Trade> existingSellTrades,
                                                                                 Collection<Trade> existingBuyTrades,
                                                                                 Collection<String> ownedItemsIds,
                                                                                 Collection<Item> existingItems) {
        Set<PersonalItem> personalItems = new HashSet<>();

        if (tradeByItemIdManagers == null || tradeByItemIdManagers.isEmpty() || existingItems == null || existingItems.isEmpty()) {
            return personalItems;
        }

        List<TradeByItemIdManager> sortedTradeByItemIdManagers =
                tradeByItemIdManagers.stream().filter(m -> m.getPriorityMultiplier() != null).sorted(Comparator.comparingInt(TradeByItemIdManager::getPriorityMultiplier).reversed()).toList();

        for (TradeByItemIdManager tradeByItemIdManager : sortedTradeByItemIdManagers) {
            PersonalItem personalItem = getPersonalItemFromTradeByItemIdManagerOrNull(
                    tradeByItemIdManager,
                    existingSellTrades,
                    existingBuyTrades,
                    ownedItemsIds,
                    existingItems);
            if (personalItem != null) {
                personalItems.add(personalItem);
            }
        }
        return personalItems;
    }

    public PersonalItem getPersonalItemFromTradeByItemIdManagerOrNull(TradeByItemIdManager tradeByItemIdManager,
                                                                      Collection<Trade> existingSellTrades,
                                                                      Collection<Trade> existingBuyTrades,
                                                                      Collection<String> ownedItemsIds,
                                                                      Collection<Item> existingItems) {

        if (tradeByItemIdManager.getEnabled() == null || !tradeByItemIdManager.getEnabled() || existingItems == null || existingItems.isEmpty()) {
            return null;
        }

        Item item = existingItems.stream().filter(i -> i.getItemId().equals(tradeByItemIdManager.getItemId())).findFirst().orElse(null);
        if (item != null) {
            int minMedianPriceDifference = Integer.MIN_VALUE;
            int minMedianPriceDifferencePercent = Integer.MIN_VALUE;

            boolean isOwned = ownedItemsIds.contains(item.getItemId());
            Trade existingTrade;

            if (isOwned) {
                existingTrade = existingSellTrades.stream().filter(t -> t.getItemId().equals(item.getItemId())).findFirst().orElse(null);
            } else {
                existingTrade = existingBuyTrades.stream().filter(t -> t.getItemId().equals(item.getItemId())).findFirst().orElse(null);
            }

            return new PersonalItem(
                    item,
                    tradeByItemIdManager.getSellBoundaryPrice(),
                    tradeByItemIdManager.getBuyBoundaryPrice(),
                    minMedianPriceDifference,
                    minMedianPriceDifferencePercent,
                    tradeByItemIdManager.getTradeOperationType(),
                    tradeByItemIdManager.getPriorityMultiplier(),
                    ownedItemsIds.contains(item.getItemId()),
                    existingTrade != null,
                    existingTrade
            );
        } else {
            return null;
        }
    }
}
