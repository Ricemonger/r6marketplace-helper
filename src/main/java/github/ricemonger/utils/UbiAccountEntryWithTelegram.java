package github.ricemonger.utils;

import github.ricemonger.utils.DTOs.UbiTrade;
import github.ricemonger.utils.DTOs.items.ItemResaleLockWithUbiAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbiAccountEntryWithTelegram {
    private String chatId;

    private Boolean privateNotificationsEnabledFlag;

    private UbiAccountEntry ubiAccountEntry;

    public String getEmail() {
        return ubiAccountEntry.getEmail();
    }

    public String getEncodedPassword() {
        return ubiAccountEntry.getEncodedPassword();
    }

    public String getAuthorizationEntryProfileId() {
        return ubiAccountEntry.getAuthorizationEntryProfileId();
    }

    public String getUbiSessionId() {
        return ubiAccountEntry.getUbiSessionId();
    }

    public String getUbiSpaceId() {
        return ubiAccountEntry.getUbiSpaceId();
    }

    public String getUbiAuthTicket() {
        return ubiAccountEntry.getUbiAuthTicket();
    }

    public String getUbiTwoFactorAuthTicket() {
        return ubiAccountEntry.getUbiTwoFactorAuthTicket();
    }

    public String getUbiRememberDeviceTicket() {
        return ubiAccountEntry.getUbiRememberDeviceTicket();
    }

    public String getUbiRememberMeTicket() {
        return ubiAccountEntry.getUbiRememberMeTicket();
    }

    public String getUbiProfileId() {
        return ubiAccountEntry.getUbiProfileId();
    }

    public Integer getSoldIn24h() {
        return ubiAccountEntry.getSoldIn24h();
    }

    public Integer getBoughtIn24h() {
        return ubiAccountEntry.getBoughtIn24h();
    }

    public Integer getCreditAmount() {
        return ubiAccountEntry.getCreditAmount();
    }

    public List<String> getOwnedItemsIds() {
        return ubiAccountEntry.getOwnedItemsIds();
    }

    public List<ItemResaleLockWithUbiAccount> getResaleLocks() {
        return ubiAccountEntry.getResaleLocks();
    }

    public List<UbiTrade> getCurrentBuyTrades() {
        return ubiAccountEntry.getCurrentBuyTrades();
    }

    public List<UbiTrade> getCurrentSellTrades() {
        return ubiAccountEntry.getCurrentSellTrades();
    }

    public List<UbiTrade> getFinishedTrades() {
        return ubiAccountEntry.getFinishedTrades();
    }
}
