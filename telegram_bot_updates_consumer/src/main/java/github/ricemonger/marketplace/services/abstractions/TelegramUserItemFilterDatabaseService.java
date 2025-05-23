package github.ricemonger.marketplace.services.abstractions;

import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.exceptions.client.ItemFilterDoesntExistException;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;

import java.util.List;

public interface TelegramUserItemFilterDatabaseService {
    void save(String chatId, ItemFilter filter) throws TelegramUserDoesntExistException;

    void deleteById(String chatId, String name) throws TelegramUserDoesntExistException;

    ItemFilter findById(String chatId, String name) throws TelegramUserDoesntExistException, ItemFilterDoesntExistException;

    List<String> findAllNamesByChatId(String chatId) throws TelegramUserDoesntExistException;

    List<ItemFilter> findAllByChatId(String chatId) throws TelegramUserDoesntExistException;
}
