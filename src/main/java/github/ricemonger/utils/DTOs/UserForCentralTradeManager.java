package github.ricemonger.utils.DTOs;

import github.ricemonger.utils.DTOs.items.ItemResaleLockWithUbiAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForCentralTradeManager {
    private Long id;

    private UbiAccountStats ubiAccountStats;

    private String ubiSessionId;
    private String ubiSpaceId;
    private String ubiAuthTicket;
    private String ubiTwoFactorAuthTicket;
    private String ubiRememberDeviceTicket;
    private String ubiRememberMeTicket;

    private String chatId;
    private Boolean privateNotificationsEnabledFlag;

    private List<TradeByFiltersManager> tradeByFiltersManagers = new ArrayList<>();

    private List<TradeByItemIdManager> tradeByItemIdManagers = new ArrayList<>();

    public String getUbiProfileId() {
        return ubiAccountStats.getUbiProfileId();
    }

    public Integer getSoldIn24h() {
        return ubiAccountStats.getSoldIn24h();
    }

    public Integer getBoughtIn24h() {
        return ubiAccountStats.getBoughtIn24h();
    }

    public Integer getCreditAmount() {
        return ubiAccountStats.getCreditAmount();
    }

    public List<String> getOwnedItemsIds() {
        return ubiAccountStats.getOwnedItemsIds();
    }

    public List<ItemResaleLockWithUbiAccount> getResaleLocks() {
        return ubiAccountStats.getResaleLocks();
    }

    public List<UbiTrade> getCurrentBuyTrades() {
        return ubiAccountStats.getCurrentBuyTrades();
    }

    public List<UbiTrade> getCurrentSellTrades() {
        return ubiAccountStats.getCurrentSellTrades();
    }
}
