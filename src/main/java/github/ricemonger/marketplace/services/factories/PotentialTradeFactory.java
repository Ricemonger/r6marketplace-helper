package github.ricemonger.marketplace.services.factories;

import github.ricemonger.marketplace.services.PotentialTradeStatsService;
import github.ricemonger.utils.DTOs.personal.PersonalItem;
import github.ricemonger.utils.DTOs.personal.PotentialPersonalBuyTrade;
import github.ricemonger.utils.DTOs.personal.PotentialPersonalSellTrade;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.enums.TradeOperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static github.ricemonger.utils.enums.TradeCategory.Sell;

@Component
@RequiredArgsConstructor
public class PotentialTradeFactory {

    private final PotentialTradeStatsService potentialTradeStatsService;

    public List<PotentialPersonalSellTrade> getResultingPersonalSellTrades(Set<PersonalItem> personalItems, Collection<? extends ItemResaleLock> resaleLocks, Integer soldIn24h, int sellSlots, int sellLimit) {

        List<PotentialPersonalSellTrade> filteredPersonalSellTrades = getFilteredPotentialSellTradesForUser(personalItems, resaleLocks);

        List<PotentialPersonalSellTrade> resultingPersonalSellTrades = new LinkedList<>();
        int slotsLeft = sellSlots;
        int limitLeft = sellLimit - soldIn24h;

        for (PotentialPersonalSellTrade sellTrade : filteredPersonalSellTrades.stream().sorted().toList()) {

            if (slotsLeft <= 0) {
                break;
            }

            boolean resultHaveSameItemTrade = resultingPersonalSellTrades.stream().anyMatch(trade -> trade.getItemId().equals(sellTrade.getItemId()));

            if (resultHaveSameItemTrade || (limitLeft <= 0 && !sellTrade.tradeForItemAlreadyExists())) {
                continue;
            } else if (sellTrade.tradeForItemAlreadyExists()) {
                slotsLeft--;
                resultingPersonalSellTrades.add(sellTrade);
            } else if (limitLeft > 0) {
                slotsLeft--;
                limitLeft--;
                resultingPersonalSellTrades.add(sellTrade);
            } else {
                continue;
            }
        }

        return resultingPersonalSellTrades;
    }

    private List<PotentialPersonalSellTrade> getFilteredPotentialSellTradesForUser(Set<PersonalItem> personalItems, Collection<? extends ItemResaleLock> resaleLocks) {
        List<PotentialPersonalSellTrade> potentialPersonalSellTrades = new ArrayList<>();
        for (PersonalItem personalItem : personalItems) {

            if (personalItem.getTradeAlreadyExists() && personalItem.getExistingTrade().getCategory() == Sell) {

                PotentialTradeStats potentialTradeStats = potentialTradeStatsService.calculatePotentialSellTradeStatsForExistingTrade(personalItem.getItem(), personalItem.getExistingTrade());

                if (potentialTradeStats.getPrice() != null && potentialTradeStats.getTradePriority() != null && potentialTradeStats.getPrognosedTradeSuccessMinutes() != null) {
                    potentialPersonalSellTrades.add(new PotentialPersonalSellTrade(personalItem, potentialTradeStats));
                }
            }

            boolean itemResaleNotLocked = resaleLocks.stream().noneMatch(resaleLock -> resaleLock.getItemId().equals(personalItem.getItemId()));
            boolean itemIsOwned = personalItem.getIsOwned();
            boolean properTradeOperationType = personalItem.getTradeOperationType() == TradeOperationType.SELL || personalItem.getTradeOperationType() == TradeOperationType.BUY_AND_SELL;

            if (itemResaleNotLocked && itemIsOwned && properTradeOperationType) {
                List<PotentialTradeStats> potentialSellTradesStats = potentialTradeStatsService.getPotentialSellTradesStats(personalItem.getItem());
                for (PotentialTradeStats potentialTradeStats : potentialSellTradesStats) {
                    boolean sellBoundaryPriceIsNotExceeded = personalItem.getSellBoundaryPrice() == null || potentialTradeStats.getPrice() >= personalItem.getSellBoundaryPrice();

                    int medianPriceDifference;
                    int monthMedianPrice;
                    if (personalItem.getMonthMedianPrice() == null) {
                        monthMedianPrice = 0;
                        medianPriceDifference = potentialTradeStats.getPrice();
                    } else {
                        monthMedianPrice = personalItem.getMonthMedianPrice();
                        medianPriceDifference = (potentialTradeStats.getPrice() - monthMedianPrice);
                    }

                    if (monthMedianPrice == 0) {
                        monthMedianPrice = 1;
                    }

                    boolean minMedianPriceDifferenceIsNotExceeded =
                            personalItem.getMinMedianPriceDifference() == null || medianPriceDifference >= personalItem.getMinMedianPriceDifference();

                    boolean minMedianPriceDifferencePercentIsNotExceeded =
                            personalItem.getMinMedianPriceDifferencePercent() == null || (medianPriceDifference * 100) / monthMedianPrice >= personalItem.getMinMedianPriceDifferencePercent();

                    if (sellBoundaryPriceIsNotExceeded && minMedianPriceDifferenceIsNotExceeded && minMedianPriceDifferencePercentIsNotExceeded) {
                        potentialPersonalSellTrades.add(new PotentialPersonalSellTrade(personalItem, potentialTradeStats));
                    }
                }
            }
        }

        return potentialPersonalSellTrades;
    }

    public List<PotentialPersonalBuyTrade> getResultingPersonalBuyTrades(Collection<PersonalItem> personalItems, Integer creditAmount, Integer boughtIn24h, int buySlots, int buyLimit) {
        List<PotentialPersonalBuyTrade> resultingPersonalBuyTrades = new LinkedList<>();
        int slotsLeft = buySlots;
        int limitLeft = buyLimit - boughtIn24h;
        int creditLeft = creditAmount;

        List<PotentialPersonalBuyTrade> filteredPersonalBuyTrades = getFilteredPotentialBuyTradesForUser(personalItems);

        for (PotentialPersonalBuyTrade buyTrade : filteredPersonalBuyTrades.stream().sorted().toList()) {

            if (slotsLeft <= 0) {
                break;
            }

            boolean resultHaveSameItemTrade = resultingPersonalBuyTrades.stream().anyMatch(trade -> trade.getItemId().equals(buyTrade.getItemId()));

            if (resultHaveSameItemTrade || (limitLeft <= 0 && !buyTrade.tradeForItemAlreadyExists())) {
                continue;
            } else if (buyTrade.tradeForItemAlreadyExists()) {
                int newPrice = buyTrade.getNewPrice();
                int oldPrice = buyTrade.getOldPrice();
                int prognosedCreditsAmount = creditLeft - newPrice + oldPrice;

                if (prognosedCreditsAmount >= 0) {
                    slotsLeft--;
                    creditLeft = prognosedCreditsAmount;
                    resultingPersonalBuyTrades.add(buyTrade);
                } else {
                    continue;
                }
            } else if (limitLeft > 0 && creditLeft - buyTrade.getNewPrice() >= 0) {
                slotsLeft--;
                limitLeft--;
                creditLeft -= buyTrade.getNewPrice();
                resultingPersonalBuyTrades.add(buyTrade);
            } else {
                continue;
            }
        }

        return resultingPersonalBuyTrades;
    }

    private List<PotentialPersonalBuyTrade> getFilteredPotentialBuyTradesForUser(Collection<PersonalItem> personalItems) {
        List<PotentialPersonalBuyTrade> potentialPersonalBuyTrades = new ArrayList<>();
        for (PersonalItem personalItem : personalItems) {

            if (personalItem.getTradeAlreadyExists() && personalItem.getExistingTrade().getCategory() == Sell) {

                PotentialTradeStats potentialTradeStats = potentialTradeStatsService.calculatePotentialBuyTradeStatsForExistingTrade(personalItem.getItem(), personalItem.getExistingTrade());

                if (potentialTradeStats.getPrice() != null && potentialTradeStats.getTradePriority() != null && potentialTradeStats.getPrognosedTradeSuccessMinutes() != null) {
                    potentialPersonalBuyTrades.add(new PotentialPersonalBuyTrade(personalItem, potentialTradeStats));
                }
            }

            boolean itemIsNotOwned = !personalItem.getIsOwned();
            boolean properTradeOperationType = personalItem.getTradeOperationType() == TradeOperationType.BUY || personalItem.getTradeOperationType() == TradeOperationType.BUY_AND_SELL;

            if (itemIsNotOwned && properTradeOperationType) {
                List<PotentialTradeStats> potentialBuyTradesStats = potentialTradeStatsService.getPotentialBuyTradesStats(personalItem.getItem());
                for (PotentialTradeStats potentialTradeStats : potentialBuyTradesStats) {
                    boolean buyBoundaryPriceIsNotExceeded = personalItem.getBuyBoundaryPrice() == null || potentialTradeStats.getPrice() <= personalItem.getBuyBoundaryPrice();

                    int medianPriceDifference;
                    int monthMedianPrice;
                    if (personalItem.getMonthMedianPrice() == null) {
                        monthMedianPrice = 0;
                        medianPriceDifference = potentialTradeStats.getPrice();
                    } else {
                        monthMedianPrice = personalItem.getMonthMedianPrice();
                        medianPriceDifference = (monthMedianPrice - potentialTradeStats.getPrice());
                    }

                    if (monthMedianPrice == 0) {
                        monthMedianPrice = 1;
                    }

                    boolean minMedianPriceDifferenceIsNotExceeded =
                            personalItem.getMinMedianPriceDifference() == null || medianPriceDifference >= personalItem.getMinMedianPriceDifference();

                    boolean minMedianPriceDifferencePercentIsNotExceeded = personalItem.getMinMedianPriceDifferencePercent() == null || (medianPriceDifference * 100) / monthMedianPrice >= personalItem.getMinMedianPriceDifferencePercent();

                    if (buyBoundaryPriceIsNotExceeded && minMedianPriceDifferenceIsNotExceeded && minMedianPriceDifferencePercentIsNotExceeded) {
                        potentialPersonalBuyTrades.add(new PotentialPersonalBuyTrade(personalItem, potentialTradeStats));
                    }
                }
            }
        }

        return potentialPersonalBuyTrades;
    }
}
