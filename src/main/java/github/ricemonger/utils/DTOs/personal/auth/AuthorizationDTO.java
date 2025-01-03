package github.ricemonger.utils.DTOs.personal.auth;

import github.ricemonger.utils.DTOs.personal.UbiAccountEntryWithTelegram;
import github.ricemonger.utils.DTOs.personal.UserForCentralTradeManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationDTO {
    private String ticket;
    private String profileId;
    private String spaceId;
    private String sessionId;
    private String rememberDeviceTicket;
    private String rememberMeTicket;

    public AuthorizationDTO(UserForCentralTradeManager userForCentralTradeManager) {
        this.ticket = userForCentralTradeManager.getUbiAuthTicket();
        this.profileId = userForCentralTradeManager.getUbiProfileId();
        this.spaceId = userForCentralTradeManager.getUbiSpaceId();
        this.sessionId = userForCentralTradeManager.getUbiSessionId();
        this.rememberDeviceTicket = userForCentralTradeManager.getUbiRememberDeviceTicket();
        this.rememberMeTicket = userForCentralTradeManager.getUbiRememberMeTicket();
    }

    public AuthorizationDTO(UbiAccountEntryWithTelegram ubiAccountWithTelegram) {
        this.ticket = ubiAccountWithTelegram.getUbiAuthTicket();
        this.profileId = ubiAccountWithTelegram.getAuthorizationEntryProfileId();
        this.spaceId = ubiAccountWithTelegram.getUbiSpaceId();
        this.sessionId = ubiAccountWithTelegram.getUbiSessionId();
        this.rememberDeviceTicket = ubiAccountWithTelegram.getUbiRememberDeviceTicket();
        this.rememberMeTicket = ubiAccountWithTelegram.getUbiRememberMeTicket();
    }
}
