package github.ricemonger.marketplace.graphQl.personal_query_one_item.DTO.game.viewer.meta;

import github.ricemonger.marketplace.graphQl.personal_query_one_item.DTO.game.viewer.meta.trades.Nodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trades {
    private List<Nodes> nodes;
}
