package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.entities.manageable_users.TradeByFiltersManagerEntity;
import github.ricemonger.utils.DTOs.personal.TradeByFiltersManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeByFiltersManagerEntityMapper {

    private final ItemFilterEntityMapper itemFilterEntityMapper;

    public TradeByFiltersManager createDTO(TradeByFiltersManagerEntity entity) {
        return new TradeByFiltersManager(entity.getName(),
                entity.getEnabled(),
                entity.getTradeOperationType(),
                entity.getAppliedFilters().stream().map(itemFilterEntityMapper::createDTO).toList(),
                entity.getMinDifferenceFromMedianPrice(),
                entity.getMinDifferenceFromMedianPricePercent(),
                entity.getPriorityMultiplier());
    }
}
