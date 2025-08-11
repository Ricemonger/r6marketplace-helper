package github.ricemonger.fast_sell_trade_manager.services.DTOs;

import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagedUserTest {

    @Test
    public void toAuthorizationDTO_should_return_expected_authDTO() {
        ManagedUser managedUser = new ManagedUser("ticket", "profileId", "spaceId", "sessionId", "rememberDeviceTicket",
                "rememberMeTicket", null, null);

        AuthorizationDTO authorizationDTO = managedUser.toAuthorizationDTO();

        assertEquals("ticket", authorizationDTO.getTicket());
        assertEquals("profileId", authorizationDTO.getProfileId());
        assertEquals("spaceId", authorizationDTO.getSpaceId());
        assertEquals("sessionId", authorizationDTO.getSessionId());
        assertEquals("rememberDeviceTicket", authorizationDTO.getRememberDeviceTicket());
        assertEquals("rememberMeTicket", authorizationDTO.getRememberMeTicket());
    }
}