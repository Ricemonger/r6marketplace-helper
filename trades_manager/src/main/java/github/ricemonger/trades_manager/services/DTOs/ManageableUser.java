package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageableUser {
    private Long id;

    private UbiAccountStats ubiAccountStats;

    private String ubiAuthTicket;
    private String ubiSpaceId;
    private String ubiSessionId;
    private String ubiRememberDeviceTicket;
    private String ubiRememberMeTicket;

    private List<TradeByFiltersManager> tradeByFiltersManagers = new ArrayList<>();

    private List<TradeByItemIdManager> tradeByItemIdManagers = new ArrayList<>();

    private Boolean sellTradesManagingEnabledFlag;

    private Boolean buyTradesManagingEnabledFlag;

    public String getUbiProfileId() {
        return ubiAccountStats == null ? null : ubiAccountStats.getUbiProfileId();
    }

    public Integer getSoldIn24h() {
        return ubiAccountStats == null ? null : ubiAccountStats.getSoldIn24h();
    }

    public Integer getBoughtIn24h() {
        return ubiAccountStats == null ? null : ubiAccountStats.getBoughtIn24h();
    }

    public Integer getCreditAmount() {
        return ubiAccountStats == null ? null : ubiAccountStats.getCreditAmount();
    }

    public List<String> getOwnedItemsIds() {
        return ubiAccountStats == null ? null : ubiAccountStats.getOwnedItemsIds();
    }

    public List<ItemResaleLock> getResaleLocks() {
        return ubiAccountStats == null ? null : ubiAccountStats.getResaleLocks();
    }

    public List<Trade> getCurrentSellTrades() {
        return ubiAccountStats == null ? null : ubiAccountStats.getCurrentSellTrades();
    }

    public List<Trade> getCurrentBuyTrades() {
        return ubiAccountStats == null ? null : ubiAccountStats.getCurrentBuyTrades();
    }

    public AuthorizationDTO toAuthorizationDTO() {
        return new AuthorizationDTO(ubiAuthTicket, getUbiProfileId(), ubiSpaceId, ubiSessionId, ubiRememberDeviceTicket, ubiRememberMeTicket);
    }
}
