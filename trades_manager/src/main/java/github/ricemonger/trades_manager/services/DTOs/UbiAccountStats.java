package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbiAccountStats {
    private String ubiProfileId;

    private Integer creditAmount;

    private Integer soldIn24h;

    private Integer boughtIn24h;

    private List<String> ownedItemsIds = new ArrayList<>();

    private List<ItemResaleLock> resaleLocks = new ArrayList<>();

    private List<Trade> currentSellTrades = new ArrayList<>();

    private List<Trade> currentBuyTrades = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hashCode(ubiProfileId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UbiAccountStats ubiAccountStats)) {
            return false;
        }
        return Objects.equals(ubiAccountStats.ubiProfileId, ubiProfileId);
    }

    public boolean isFullyEqual(Object o) {
        if (this.equals(o) && o instanceof UbiAccountStats ubiAccountStats) {

            boolean ownedItemsIdsEqual = ownedItemsIds == null && ubiAccountStats.ownedItemsIds == null || (
                    ownedItemsIds != null && ubiAccountStats.ownedItemsIds != null &&
                    ownedItemsIds.size() == ubiAccountStats.ownedItemsIds.size() &&
                    new HashSet<>(ownedItemsIds).containsAll(ubiAccountStats.ownedItemsIds));

            boolean resaleLocksEqual = resaleLocks == null && ubiAccountStats.resaleLocks == null || (
                    resaleLocks != null && ubiAccountStats.resaleLocks != null &&
                    resaleLocks.size() == ubiAccountStats.resaleLocks.size() &&
                    new HashSet<>(resaleLocks).containsAll(ubiAccountStats.resaleLocks));

            boolean currentSellTradesEqual = currentSellTrades == null && ubiAccountStats.currentSellTrades == null || (
                    currentSellTrades != null && ubiAccountStats.currentSellTrades != null &&
                    currentSellTrades.size() == ubiAccountStats.currentSellTrades.size() &&
                    new HashSet<>(currentSellTrades).containsAll(ubiAccountStats.currentSellTrades));

            boolean currentBuyTradesEqual = currentBuyTrades == null && ubiAccountStats.currentBuyTrades == null || (
                    currentBuyTrades != null && ubiAccountStats.currentBuyTrades != null &&
                    currentBuyTrades.size() == ubiAccountStats.currentBuyTrades.size() &&
                    new HashSet<>(currentBuyTrades).containsAll(ubiAccountStats.currentBuyTrades));

            return Objects.equals(ubiProfileId, ubiAccountStats.ubiProfileId) &&
                   Objects.equals(soldIn24h, ubiAccountStats.soldIn24h) &&
                   Objects.equals(boughtIn24h, ubiAccountStats.boughtIn24h) &&
                   Objects.equals(creditAmount, ubiAccountStats.creditAmount) &&
                   ownedItemsIdsEqual &&
                   resaleLocksEqual &&
                   currentSellTradesEqual &&
                   currentBuyTradesEqual;
        } else {
            return false;
        }
    }
}
