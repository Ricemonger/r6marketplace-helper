package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utilspostgresschema.full_entities.user.TradeByItemIdManagerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeByItemIdManagerEntityMapper {

    public TradeByItemIdManager createDTO(TradeByItemIdManagerEntity entity) {
        return new TradeByItemIdManager(
                entity.getItemId_(),
                entity.getEnabled(),
                entity.getTradeOperationType(),
                entity.getSellBoundaryPrice(),
                entity.getBuyBoundaryPrice(),
                entity.getPriorityMultiplier()
        );
    }
}
