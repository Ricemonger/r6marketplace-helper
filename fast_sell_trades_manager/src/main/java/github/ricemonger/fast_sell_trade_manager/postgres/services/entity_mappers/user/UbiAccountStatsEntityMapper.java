package github.ricemonger.fast_sell_trade_manager.postgres.services.entity_mappers.user;


import github.ricemonger.fast_sell_trade_manager.postgres.dto_projections.FastSellManagedUserProjection;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UbiAccountStatsEntityMapper {

    public ManagedUser createFastSellManagedUser(FastSellManagedUserProjection projection, List<String> resaleLocks) {
        return new ManagedUser(
                projection.getUbiAuthTicket(),
                projection.getUbiProfileId(),
                projection.getUbiSpaceId(),
                projection.getUbiSessionId(),
                projection.getUbiRememberDeviceTicket(),
                projection.getUbiRememberMeTicket(),
                projection.getSoldIn24h(),
                resaleLocks
        );
    }
}
