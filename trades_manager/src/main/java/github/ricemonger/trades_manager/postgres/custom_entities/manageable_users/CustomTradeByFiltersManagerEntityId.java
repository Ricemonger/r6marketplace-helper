package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomTradeByFiltersManagerEntityId {
    private CustomManageableUserEntity user;
    private String name;
}
