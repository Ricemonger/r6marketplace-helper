package github.ricemonger.fast_sell_trade_manager.postgres.services.entity_mappers.user;

import github.ricemonger.fast_sell_trade_manager.postgres.dto_projections.FastSellManagedUserProjection;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UbiAccountStatsEntityMapperTest {
    @Autowired
    UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;

    public void createFastSellManagedUser_should_return_expected_result() {
        FastSellManagedUserProjection projection = new FastSellManagedUserProjection();
        projection.setUbiAuthTicket("ubiAuthTicket");
        projection.setUbiProfileId("ubiProfileId");
        projection.setUbiSpaceId("ubiSpaceId");
        projection.setUbiSessionId("ubiSessionId");
        projection.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        projection.setUbiRememberMeTicket("ubiRememberMeTicket");
        projection.setSoldIn24h(1);
        List<String> resaleLocks = List.of("resaleLock1", "resaleLock2");

        ManagedUser result = ubiAccountStatsEntityMapper.createFastSellManagedUser(projection, resaleLocks);

        assertEquals("ubiAuthTicket", result.getUbiAuthTicket());
        assertEquals("ubiProfileId", result.getUbiProfileId());
        assertEquals("ubiSpaceId", result.getUbiSpaceId());
        assertEquals("ubiSessionId", result.getUbiSessionId());
        assertEquals("ubiRememberDeviceTicket", result.getUbiRememberDeviceTicket());
        assertEquals("ubiRememberMeTicket", result.getUbiRememberMeTicket());
        assertEquals(1, result.getSoldIn24h());
        assertTrue(result.getResaleLocks().containsAll(resaleLocks) && result.getResaleLocks().size() == resaleLocks.size());
    }
}