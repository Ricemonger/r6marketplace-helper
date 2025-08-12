package github.ricemonger.trades_manager.postgres.dto_projections;

import github.ricemonger.utilspostgresschema.full_entities.user.TradeByFiltersManagerEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.TradeByItemIdManagerEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountStatsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageableUserProjection {
    private Long id;

    private UbiAccountStatsEntity ubiAccountStats;

    private String ubiAuthTicket;
    private String ubiSpaceId;
    private String ubiSessionId;
    private String ubiRememberDeviceTicket;
    private String ubiRememberMeTicket;

    private List<TradeByFiltersManagerEntity> tradeByFiltersManagers;
    private List<TradeByItemIdManagerEntity> tradeByItemIdManagers;

    private Boolean sellTradesManagingEnabledFlag;
    private String sellTradePriorityExpression;

    private Boolean buyTradesManagingEnabledFlag;
    private String buyTradePriorityExpression;
}
