package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.services.abstractions.TradeManagerByItemFilterDatabaseService;
import github.ricemonger.marketplace.services.abstractions.TradeManagerByItemIdDatabaseService;
import github.ricemonger.utils.dtos.TradeManagerByItemFilters;
import github.ricemonger.utils.dtos.TradeManagerByItemId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TradeManagerService {

    private final TradeManagerByItemIdDatabaseService tradeManagerByItemIdDatabaseService;

    private final TradeManagerByItemFilterDatabaseService tradeManagerByItemFiltersDatabaseService;

    public void saveTradeManagerByItemId(TradeManagerByItemId tradeManager) {
        tradeManagerByItemIdDatabaseService.save(tradeManager);
    }

    public void saveTradeManagerByItemFilter(TradeManagerByItemFilters tradeManager) {
        tradeManagerByItemFiltersDatabaseService.save(tradeManager);
    }

    public void deleteTradeManagerByItemIdById(String chatId, String itemId) {
        tradeManagerByItemIdDatabaseService.deleteById(chatId, itemId);
    }

    public TradeManagerByItemId getTradeManagerByItemIdById(String chatId, String itemId) {
        return tradeManagerByItemIdDatabaseService.findById(chatId, itemId);
    }

    public Collection<TradeManagerByItemId> getTradeManagersByItemId(String chatId) {
        return tradeManagerByItemIdDatabaseService.findAll(chatId);
    }

    public Collection<TradeManagerByItemFilters> getTradeManagersByItemFilters(String chatId) {
        return tradeManagerByItemFiltersDatabaseService.findAll(chatId);
    }
}
