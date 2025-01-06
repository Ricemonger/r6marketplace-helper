package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.entities.manageable_users.TradeEntity;
import github.ricemonger.trades_manager.postgres.services.entity_mappers.item.ItemEntityMapper;
import github.ricemonger.trades_manager.services.DTOs.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeEntityMapper {

    private final ItemEntityMapper itemEntityMapper;

    public Trade createDTO(TradeEntity entity) {
        return new Trade(
                entity.getTradeId(),
                entity.getState(),
                entity.getCategory(),
                entity.getExpiresAt(),
                entity.getLastModifiedAt(),
                itemEntityMapper.createDTO(entity.getItem()),
                entity.getSuccessPaymentPrice(),
                entity.getSuccessPaymentFee(),
                entity.getProposedPaymentPrice(),
                entity.getProposedPaymentFee(),
                entity.getMinutesToTrade(),
                entity.getTradePriority()
        );
    }
}
