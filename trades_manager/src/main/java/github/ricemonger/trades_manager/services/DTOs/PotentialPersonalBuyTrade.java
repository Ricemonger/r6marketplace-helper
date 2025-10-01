package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.enums.TradeCategory;

public class PotentialPersonalBuyTrade extends PotentialTrade {
    public PotentialPersonalBuyTrade(PersonalItem personalItem, PotentialTradeStats potentialTradeStats) {
        super(personalItem, potentialTradeStats);
    }

    public TradeCategory getTradeCategory() {
        return TradeCategory.Buy;
    }

    @Override
    public int compareTo(PotentialTrade other) {
        int thisPriorityMultiplier = this.getPriorityMultiplier() == null || this.getPriorityMultiplier() < 1 ? 1 : this.getPriorityMultiplier();
        int otherPriorityMultiplier = other.getPriorityMultiplier() == null || other.getPriorityMultiplier() < 1 ? 1 : other.getPriorityMultiplier();

        Long thisPriority = this.getTradePriority() > 0 ? this.getTradePriority() * thisPriorityMultiplier : this.getTradePriority() / thisPriorityMultiplier;

        Long otherPriority = other.getTradePriority() > 0 ? other.getTradePriority() * otherPriorityMultiplier : other.getTradePriority() / otherPriorityMultiplier;

        int priorityComparison = otherPriority.compareTo(thisPriority);

        if (priorityComparison != 0) {
            return priorityComparison;
        } else {
            int itemIdComparison = this.getItemId().compareTo(other.getItemId());

            if (itemIdComparison != 0) {
                return itemIdComparison;
            } else {
                return this.getNewPrice().compareTo(other.getNewPrice());
            }
        }
    }
}
