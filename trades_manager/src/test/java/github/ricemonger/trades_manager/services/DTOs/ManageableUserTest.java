package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ManageableUserTest {

    @Test
    public void getUbiProfileId_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getUbiProfileId());
    }

    @Test
    public void getUbiProfileId_should_return_ubiProfileId_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 0, 0, List.of(), List.of(), List.of(), List.of()));

        assertEquals("ubiProfileId", manageableUser.getUbiProfileId());
    }

    @Test
    public void getSoldIn24h_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getSoldIn24h());
    }

    @Test
    public void getSoldIn24h_should_return_soldIn24h_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 1, 0, List.of(), List.of(), List.of(), List.of()));

        assertEquals(1, manageableUser.getSoldIn24h());
    }

    @Test
    public void getBoughtIn24h_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getBoughtIn24h());
    }

    @Test
    public void getBoughtIn24h_should_return_boughtIn24h_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 0, 1, List.of(), List.of(), List.of(), List.of()));

        assertEquals(1, manageableUser.getBoughtIn24h());
    }

    @Test
    public void getCreditAmount_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getCreditAmount());
    }

    @Test
    public void getCreditAmount_should_return_creditAmount_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 1, 0, 0, List.of(), List.of(), List.of(), List.of()));

        assertEquals(1, manageableUser.getCreditAmount());
    }

    @Test
    public void getOwnedItemsIds_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getOwnedItemsIds());
    }

    @Test
    public void getOwnedItemsIds_should_return_ownedItemsIds_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 0, 0, List.of("1", "2"), List.of(), List.of(), List.of()));

        assertEquals(List.of("1", "2"), manageableUser.getOwnedItemsIds());
    }

    @Test
    public void getResaleLocks_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getResaleLocks());
    }

    @Test
    public void getResaleLocks_should_return_resaleLocks_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 0, 0, List.of(), List.of(new ItemResaleLock("1", LocalDateTime.of(2022, 1, 1, 1, 1))),
                List.of(),
                List.of()));

        assertEquals(List.of(new ItemResaleLock("1", LocalDateTime.of(2022, 1, 1, 1, 1))), manageableUser.getResaleLocks());
    }

    @Test
    public void getCurrentSellTrades_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getCurrentSellTrades());
    }

    @Test
    public void getCurrentSellTrades_should_return_currentSellTrades_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 0, 0, List.of(), List.of(), List.of(new Trade()),
                List.of()));

        assertEquals(List.of(new Trade()), manageableUser.getCurrentSellTrades());
    }

    @Test
    public void getCurrentBuyTrades_should_return_null_when_ubiAccountStats_is_null() {
        ManageableUser manageableUser = new ManageableUser();

        assertNull(manageableUser.getCurrentBuyTrades());
    }

    @Test
    public void getCurrentBuyTrades_should_return_currentBuyTrades_when_ubiAccountStats_is_not_null() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats("ubiProfileId", 0, 0, 0, List.of(), List.of(), List.of(),
                List.of(new Trade())));

        assertEquals(List.of(new Trade()), manageableUser.getCurrentBuyTrades());
    }

    @Test
    public void toAuthorizationDTO_should_return_AuthorizationDTO() {
        ManageableUser manageableUser = new ManageableUser();
        manageableUser.setUbiAccountStats(new UbiAccountStats());
        manageableUser.getUbiAccountStats().setUbiProfileId("ubiProfileId");
        manageableUser.setUbiAuthTicket("ubiAuthTicket");
        manageableUser.setUbiSessionId("ubiSessionId");
        manageableUser.setUbiSpaceId("ubiSpaceId");
        manageableUser.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        manageableUser.setUbiRememberMeTicket("ubiRememberMeTicket");

        AuthorizationDTO authorizationDTO = manageableUser.toAuthorizationDTO();

        assertEquals("ubiProfileId", authorizationDTO.getProfileId());
        assertEquals("ubiAuthTicket", authorizationDTO.getTicket());
        assertEquals("ubiSessionId", authorizationDTO.getSessionId());
        assertEquals("ubiSpaceId", authorizationDTO.getSpaceId());
        assertEquals("ubiRememberDeviceTicket", authorizationDTO.getRememberDeviceTicket());
        assertEquals("ubiRememberMeTicket", authorizationDTO.getRememberMeTicket());
    }
}