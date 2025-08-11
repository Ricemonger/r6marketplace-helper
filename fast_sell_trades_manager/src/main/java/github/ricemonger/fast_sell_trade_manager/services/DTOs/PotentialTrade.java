package github.ricemonger.fast_sell_trade_manager.services.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PotentialTrade implements Comparable<PotentialTrade> {
    private String itemId;
    private Integer price;
    private Integer monthMedianPriceDifference;
    private Integer monthMedianPriceDifferencePercentage;
    private boolean sellByMaxBuyPrice;

    @Override
    public int compareTo(PotentialTrade other) {
        boolean byMaxBuyPrice1 = this.isSellByMaxBuyPrice();
        boolean byMaxBuyPrice2 = other.isSellByMaxBuyPrice();

        if (byMaxBuyPrice1 && !byMaxBuyPrice2) {
            return -1;
        } else if (!byMaxBuyPrice1 && byMaxBuyPrice2) {
            return 1;
        } else {

            Integer diff1 = this.getMonthMedianPriceDifference() * this.getMonthMedianPriceDifferencePercentage();
            Integer diff2 = other.getMonthMedianPriceDifference() * other.getMonthMedianPriceDifferencePercentage();

            return -diff1.compareTo(diff2);
        }
    }
}
