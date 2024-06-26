package github.ricemonger.marketplace.services.abstractions;

import github.ricemonger.utils.dtos.TradeManagerByItemFilters;

import java.util.Collection;

public interface TradeManagerByItemFilterDatabaseService {
    void save(TradeManagerByItemFilters tradeManager);

    Collection<TradeManagerByItemFilters> findAll(String chatId);
}
