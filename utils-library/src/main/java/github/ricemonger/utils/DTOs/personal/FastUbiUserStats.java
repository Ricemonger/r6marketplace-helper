package github.ricemonger.utils.DTOs.personal;

import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FastUbiUserStats {
    private List<ItemCurrentPrices> ownedItemsCurrentPrices;
    private List<SellTrade> currentSellOrders;
}
