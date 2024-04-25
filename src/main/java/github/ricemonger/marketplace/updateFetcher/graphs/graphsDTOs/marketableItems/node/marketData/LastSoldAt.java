package github.ricemonger.marketplace.updateFetcher.graphs.graphsDTOs.marketableItems.node.marketData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastSoldAt {

    private String id;

    private int price;

    private String performedAt;
}