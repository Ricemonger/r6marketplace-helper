package github.ricemonger.utils.dtos;

import github.ricemonger.utils.enums.PlannedTradeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlannedOneItemTrade {
    private String chatId;
    private PlannedTradeType tradeType;
    private String itemId;
    private Integer sellStartingPrice;
    private Integer sellBoundaryPrice;
    private Integer buyStartingPrice;
    private Integer buyBoundaryPrice;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Planned one item trade: \n");
        sb.append("Trade type: ").append(tradeType).append("\n");
        sb.append("Item id: ").append(itemId).append("\n");

        if (tradeType == PlannedTradeType.SELL) {
            sb.append("Starting price: ").append(sellStartingPrice).append("\n");
            sb.append("Boundary price: ").append(sellBoundaryPrice).append("\n");
        } else if (tradeType == PlannedTradeType.BUY) {
            sb.append("Starting price: ").append(buyStartingPrice).append("\n");
            sb.append("Boundary price: ").append(buyBoundaryPrice).append("\n");
        } else if (tradeType == PlannedTradeType.BUY_AND_SELL || tradeType == null) {
            sb.append("Starting Sell price: ").append(sellStartingPrice).append("\n");
            sb.append("Boundary Sell price: ").append(sellBoundaryPrice).append("\n");
            sb.append("Starting Buy price: ").append(buyStartingPrice).append("\n");
            sb.append("Boundary Buy price: ").append(buyBoundaryPrice).append("\n");
        }

        return sb.toString();
    }
}
