package github.ricemonger.marketplace.graphQl.dtos.personal_query_one_item.game.marketableItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLimitations {
    private int minPrice;
    private int maxPrice;
}
