package github.ricemonger.utils.DTOs;

import github.ricemonger.utils.DTOs.items.ItemFilter;
import github.ricemonger.utils.enums.TradeOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeByFiltersManager {
    private String name;
    private boolean enabled;
    private TradeOperationType tradeOperationType;
    private List<ItemFilter> appliedFilters;
    private Integer minBuySellProfit;
    private Integer minProfitPercent;
    private Integer priority;

    public String toString() {
        String sb = "Trade By Item Filter Manager: \n" +
                    "Name: " + name + "\n" +
                    "Enabled: " + enabled + "\n" +
                    "Trade type: " + tradeOperationType + "\n";
        if (appliedFilters != null) {
            sb = sb + "Applied filters' names: " + appliedFilters.stream().map(ItemFilter::getName).reduce((a, b) -> a + ", " + b).orElse("") + "\n";
        } else {
            sb = sb + "Applied filters: null\n";
        }
        sb = sb + "Min profit: " + minBuySellProfit + "\n" +
             "Min profit percent: " + minProfitPercent + "\n" +
             "Priority: " + priority + "\n";
        return sb;
    }
}
