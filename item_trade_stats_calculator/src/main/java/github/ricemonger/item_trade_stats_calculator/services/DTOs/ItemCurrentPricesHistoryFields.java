package github.ricemonger.item_trade_stats_calculator.services.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCurrentPricesHistoryFields {
    private String itemId;
    private Long priorityToSellByMaxBuyPrice;
    private Long priorityToSellByNextFancySellPrice;
    private Long priorityToBuyByMinSellPrice;
}
