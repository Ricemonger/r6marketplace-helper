package github.ricemonger.marketplace.graphQl.personal_query_one_item.DTO.game.viewer;

import github.ricemonger.marketplace.graphQl.personal_query_one_item.DTO.game.viewer.meta.Trades;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    private Trades trades;
}
