package github.ricemonger.marketplace.graphQl.dtos.config_query_marketplace.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tags {
    private String value;
    private String displayName;
}
