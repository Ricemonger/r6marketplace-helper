package github.ricemonger.marketplace.services.abstractions;

import github.ricemonger.utils.dtos.TradeManagerByItemId;
import github.ricemonger.utils.exceptions.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.TradeManagerByItemIdDoesntExistException;

import java.util.List;

public interface TelegramUserTradeManagerByItemIdDatabaseService {
    void save(String chatId, TradeManagerByItemId tradeManager) throws TelegramUserDoesntExistException;

    void deleteById(String chatId, String itemId) throws TelegramUserDoesntExistException;

    TradeManagerByItemId findById(String chatId, String itemId) throws TelegramUserDoesntExistException, TradeManagerByItemIdDoesntExistException;

    List<TradeManagerByItemId> findAllByChatId(String chatId) throws TelegramUserDoesntExistException;
}
