package github.ricemonger.marketplace.graphs.graphsDTOs.marketableItems.node.marketData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyStats {

    private String id;

    private int lowestPrice;

    private int highestPrice;

    private int activeCount;
}