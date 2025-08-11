package github.ricemonger.fast_sell_trade_manager.services.DTOs;

import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagedUser {
    private String ubiAuthTicket;
    private String ubiProfileId;
    private String ubiSpaceId;
    private String ubiSessionId;
    private String ubiRememberDeviceTicket;
    private String ubiRememberMeTicket;
    private Integer soldIn24h;
    private List<String> resaleLocks;

    public AuthorizationDTO toAuthorizationDTO() {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setTicket(ubiAuthTicket);
        authorizationDTO.setProfileId(ubiProfileId);
        authorizationDTO.setSpaceId(ubiSpaceId);
        authorizationDTO.setSessionId(ubiSessionId);
        authorizationDTO.setRememberDeviceTicket(ubiRememberDeviceTicket);
        authorizationDTO.setRememberMeTicket(ubiRememberMeTicket);
        return authorizationDTO;
    }
}
