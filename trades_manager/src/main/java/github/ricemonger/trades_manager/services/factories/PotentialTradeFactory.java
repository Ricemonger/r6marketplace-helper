package github.ricemonger.trades_manager.services.factories;

import github.ricemonger.trades_manager.services.DTOs.PersonalItem;
import github.ricemonger.trades_manager.services.DTOs.PotentialPersonalBuyTrade;
import github.ricemonger.trades_manager.services.DTOs.PotentialPersonalSellTrade;
import github.ricemonger.trades_manager.services.PotentialTradeStatsService;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.enums.TradeOperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static github.ricemonger.utils.enums.TradeCategory.Buy;
import static github.ricemonger.utils.enums.TradeCategory.Sell;

@Component
@RequiredArgsConstructor
public class PotentialTradeFactory {

    private final PotentialTradeStatsService potentialTradeStatsService;

    public List<PotentialPersonalSellTrade> getResultingPersonalSellTrades(Collection<PersonalItem> personalItems, Collection<? extends ItemResaleLock> resaleLocks, Integer soldIn24h, int sellSlots, int sellLimit) {

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

    public List<PotentialPersonalSellTrade> getFilteredPotentialSellTradesForUser(Collection<PersonalItem> personalItems,
                                                                                  Collection<? extends ItemResaleLock> resaleLocks) {
        List<PotentialPersonalSellTrade> potentialPersonalSellTrades = new ArrayList<>();
        for (PersonalItem personalItem : personalItems) {
            boolean itemIsNotOwned = !personalItem.getIsOwned();
            if (itemIsNotOwned) {
                continue;
            }
            boolean itemResaleIsLocked = resaleLocks.stream().anyMatch(resaleLock -> resaleLock.getItemId().equals(personalItem.getItemId()));
            if (itemResaleIsLocked) {
                continue;
            }
            boolean wrongTradeOperationType = personalItem.getTradeOperationType() == null || personalItem.getTradeOperationType() == TradeOperationType.BUY;
            if (wrongTradeOperationType) {
                continue;
            }

            List<PotentialTradeStats> potentialSellTradesStats = potentialTradeStatsService.getPotentialSellTradesStatsOfItem(personalItem.getItem());
            for (PotentialTradeStats potentialTradeStats : potentialSellTradesStats) {
                if (!potentialTradeStats.isValid()) {
                    continue;
                }
                boolean sellBoundaryPriceIsExceeded = personalItem.getSellBoundaryPrice() != null && potentialTradeStats.getPrice() < personalItem.getSellBoundaryPrice();
                if (sellBoundaryPriceIsExceeded) {
                    continue;
                }

                int medianPriceDifference;
                int monthMedianPrice;
                if (personalItem.getMonthMedianPrice() == null || personalItem.getMonthMedianPrice() <= 0) {
                    monthMedianPrice = 1;
                    medianPriceDifference = potentialTradeStats.getPrice();
                } else {
                    monthMedianPrice = personalItem.getMonthMedianPrice();
                    medianPriceDifference = (potentialTradeStats.getPrice() - monthMedianPrice);
                }

                boolean minMedianPriceDifferenceIsExceeded = personalItem.getMinMedianPriceDifference() != null && medianPriceDifference < personalItem.getMinMedianPriceDifference();
                if (minMedianPriceDifferenceIsExceeded) {
                    continue;
                }

                boolean minMedianPriceDifferencePercentIsExceeded = personalItem.getMinMedianPriceDifferencePercent() != null && (medianPriceDifference * 100) / monthMedianPrice < personalItem.getMinMedianPriceDifferencePercent();
                if (minMedianPriceDifferencePercentIsExceeded) {
                    continue;
                }

                potentialPersonalSellTrades.add(new PotentialPersonalSellTrade(personalItem, potentialTradeStats));
            }

            if (personalItem.getTradeAlreadyExists() && personalItem.getExistingTrade().getCategory() == Sell) {
                PotentialTradeStats potentialTradeStats = new PotentialTradeStats(personalItem.getExistingTrade().getProposedPaymentPrice(), personalItem.getExistingTrade().getMinutesToTrade(), personalItem.getExistingTrade().getTradePriority());
                if (potentialTradeStats.isValid()) {
                    potentialPersonalSellTrades.add(new PotentialPersonalSellTrade(personalItem, potentialTradeStats));
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

    public List<PotentialPersonalBuyTrade> getFilteredPotentialBuyTradesForUser(Collection<PersonalItem> personalItems) {
        List<PotentialPersonalBuyTrade> potentialPersonalBuyTrades = new ArrayList<>();
        for (PersonalItem personalItem : personalItems) {
            boolean itemIsOwned = personalItem.getIsOwned();
            if (itemIsOwned) {
                continue;
            }
            boolean wrongTradeOperationType = personalItem.getTradeOperationType() == null || personalItem.getTradeOperationType() == TradeOperationType.SELL;
            if (wrongTradeOperationType) {
                continue;
            }

            List<PotentialTradeStats> potentialBuyTradesStats = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(personalItem.getItem());
            for (PotentialTradeStats potentialTradeStats : potentialBuyTradesStats) {
                if (!potentialTradeStats.isValid()) {
                    continue;
                }
                if (personalItem.getBuyBoundaryPrice() != null && potentialTradeStats.getPrice() > personalItem.getBuyBoundaryPrice()) {
                    continue;
                }

                int medianPriceDifference;
                int monthMedianPrice;
                if (personalItem.getMonthMedianPrice() == null || personalItem.getMonthMedianPrice() <= 0) {
                    monthMedianPrice = 1;
                    medianPriceDifference = potentialTradeStats.getPrice();
                } else {
                    monthMedianPrice = personalItem.getMonthMedianPrice();
                    medianPriceDifference = (monthMedianPrice - potentialTradeStats.getPrice());
                }

                if (personalItem.getMinMedianPriceDifference() != null && medianPriceDifference < personalItem.getMinMedianPriceDifference()) {
                    continue;
                }
                if (personalItem.getMinMedianPriceDifferencePercent() != null && (medianPriceDifference * 100) / monthMedianPrice < personalItem.getMinMedianPriceDifferencePercent()) {
                    continue;
                }

                potentialPersonalBuyTrades.add(new PotentialPersonalBuyTrade(personalItem, potentialTradeStats));
            }

            if (personalItem.getTradeAlreadyExists() && personalItem.getExistingTrade().getCategory() == Buy) {
                PotentialTradeStats potentialTradeStats = new PotentialTradeStats(personalItem.getExistingTrade().getProposedPaymentPrice(), personalItem.getExistingTrade().getMinutesToTrade(), personalItem.getExistingTrade().getTradePriority());
                if (potentialTradeStats.isValid()) {
                    potentialPersonalBuyTrades.add(new PotentialPersonalBuyTrade(personalItem, potentialTradeStats));
                }
            }
        }

        return potentialPersonalBuyTrades;
    }
}
