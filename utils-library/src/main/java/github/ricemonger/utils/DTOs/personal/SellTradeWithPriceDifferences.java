package github.ricemonger.utils.DTOs.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellTradeWithPriceDifferences {
    private String tradeId;
    private String itemId;
    private Integer price;

    private Integer minSellPrice;

    private Integer monthMedianPriceDifference;
    private Integer monthMedianPriceDifferencePercentage;
}
