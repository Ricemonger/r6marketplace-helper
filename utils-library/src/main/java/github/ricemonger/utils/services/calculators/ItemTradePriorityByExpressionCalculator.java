package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemTradePriorityByExpressionCalculator {

    public boolean isValidExpression(String tradePriorityExpression) {
        return false;
    }

    public Long calculateTradePriority(String tradePriorityExpression, Item item, Integer price, Integer time) {
        return null;
    }
}
