package github.ricemonger.marketplace.services.abstractions;

import github.ricemonger.utils.dtos.TradeManagerByItemId;

import java.util.Collection;

public interface TradeManagerByItemIdDatabaseService {
    void save(TradeManagerByItemId tradeManager);

    void deleteById(String chatId, String itemId);

    TradeManagerByItemId findById(String chatId, String itemId);

    Collection<TradeManagerByItemId> findAll(String chatId);
}
