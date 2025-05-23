package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.PrioritizedPotentialTradeStats;
import github.ricemonger.utils.enums.TradeCategory;

public class PotentialPersonalSellTrade extends PotentialTrade {

    public PotentialPersonalSellTrade(PersonalItem personalItem, PrioritizedPotentialTradeStats prioritizedPotentialTradeStats) {
        super(personalItem, prioritizedPotentialTradeStats);
    }

    public TradeCategory getTradeCategory() {
        return TradeCategory.Sell;
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

                return other.getNewPrice().compareTo(this.getNewPrice());
            }
        }
    }
}
