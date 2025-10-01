package github.ricemonger.ubi_users_stats_fetcher.postgres.services.entity_mappers.user;

import github.ricemonger.ubi_users_stats_fetcher.postgres.repositories.CustomItemPostgresRepository;
import github.ricemonger.utils.DTOs.personal.Trade;
import github.ricemonger.utils.exceptions.client.ItemDoesntExistException;
import github.ricemonger.utilspostgresschema.full_entities.user.TradeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeEntityMapper {

    private final CustomItemPostgresRepository customItemPostgresRepository;

    public TradeEntity createEntity(Trade trade, List<String> existingItemsIds) {
        return new TradeEntity(
                trade.getTradeId(),
                trade.getState(),
                trade.getCategory(),
                trade.getExpiresAt(),
                trade.getLastModifiedAt(),
                existingItemsIds.stream().filter(ex -> ex.equals(trade.getItemId())).map(customItemPostgresRepository::getReferenceById).findFirst().orElseThrow(() -> new ItemDoesntExistException("Item with id " + trade.getItemId() + " not found")),
                trade.getSuccessPaymentPrice(),
                trade.getSuccessPaymentFee(),
                trade.getProposedPaymentPrice(),
                trade.getProposedPaymentFee(),
                trade.getMinutesToTrade(),
                trade.getTradePriority()
        );
    }
}
