package github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.node.marketData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyStats {

    private Integer highestPrice;

    private Integer activeCount;
}
