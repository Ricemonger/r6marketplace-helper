package github.ricemonger.item_trade_stats_calculator.postgres.dto_projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCurrentPricesHistoryFieldsProjection {
    private String itemId;
    private Long priorityToSellByMaxBuyPrice;
    private Long priorityToSellByNextFancySellPrice;
    private Long priorityToBuyByMinSellPrice;
}
