package github.ricemonger.marketplace.graphQl.personal_query_locked_items.DTO.tradeLimitations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buy {
    private Integer resolvedTransactionCount;
    private Integer activeTransactionCount;
}