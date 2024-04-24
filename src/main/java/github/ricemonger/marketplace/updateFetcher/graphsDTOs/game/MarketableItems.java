package github.ricemonger.marketplace.updateFetcher.graphsDTOs.game;

import github.ricemonger.marketplace.updateFetcher.graphsDTOs.game.marketableItems.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketableItems {

    private List<Node> nodes;

    private int totalCount;
}
