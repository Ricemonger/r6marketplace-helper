package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.enums.TradeCategory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public abstract class PotentialTrade implements Comparable<PotentialTrade> {
    private final PersonalItem personalItem;

    private final PotentialTradeStats potentialTradeStats;

    public abstract TradeCategory getTradeCategory();

    public Integer getPriorityMultiplier() {
        return personalItem == null ? null : personalItem.getPriorityMultiplier();
    }

    public Long getTradePriority() {
        return potentialTradeStats == null ? null : potentialTradeStats.getTradePriority();
    }

    public Boolean tradeForItemAlreadyExists() {
        return personalItem == null ? null : personalItem.getTradeAlreadyExists();
    }

    public String getItemId() {
        return personalItem == null ? null : personalItem.getItemId();
    }

    public Integer getNewPrice() {
        return potentialTradeStats == null ? null : potentialTradeStats.getPrice();
    }

    public Integer getOldPrice() {
        return personalItem == null ? null : personalItem.getProposedPaymentPrice();
    }

    public String getItemName() {
        return personalItem == null ? null : personalItem.getName();
    }

    public String getTradeId() {
        return personalItem == null ? null : personalItem.getTradeId();
    }
}
