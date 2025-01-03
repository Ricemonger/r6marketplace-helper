package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.user.TelegramUserEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByFiltersManagerEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByFiltersManagerEntityId;
import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.TradeByFiltersManagerPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.TradeByFiltersManagerEntityMapper;
import github.ricemonger.marketplace.services.abstractions.TelegramUserTradeByFiltersManagerDatabaseService;
import github.ricemonger.utils.DTOs.personal.TradeByFiltersManager;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.client.TradeByFiltersManagerDoesntExistException;
import github.ricemonger.utils.exceptions.client.TradeByItemIdManagerDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserTradeByFiltersManagerPostgresService implements TelegramUserTradeByFiltersManagerDatabaseService {

    private final TradeByFiltersManagerPostgresRepository tradeByFiltersManagerRepository;

    private final TelegramUserPostgresRepository telegramUserRepository;

    private final TradeByFiltersManagerEntityMapper tradeByFiltersManagerEntityMapper;

    @Override
    @Transactional
    public void save(String chatId, TradeByFiltersManager tradeManager) throws TelegramUserDoesntExistException {
        tradeByFiltersManagerRepository.save(tradeByFiltersManagerEntityMapper.createEntityForTelegramUser(chatId, tradeManager));
    }

    @Override
    @Transactional
    public void invertEnabledFlagById(String chatId, String name) throws TelegramUserDoesntExistException, TradeByFiltersManagerDoesntExistException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        TradeByFiltersManagerEntity manager = tradeByFiltersManagerRepository.findById
                        (new TradeByFiltersManagerEntityId(telegramUser.getUser(), name))
                .orElseThrow(() -> new TradeByFiltersManagerDoesntExistException(String.format("Trade manager by chatId %s and itemId %s not found",
                        chatId, name)));

        manager.setEnabled(!manager.getEnabled());

        tradeByFiltersManagerRepository.save(manager);
    }

    @Override
    @Transactional
    public void deleteById(String chatId, String name) throws TelegramUserDoesntExistException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        List<TradeByFiltersManagerEntity> managers = telegramUser.getUser().getTradeByFiltersManagers();

        Iterator<TradeByFiltersManagerEntity> iterator = managers.iterator();

        while (iterator.hasNext()) {
            TradeByFiltersManagerEntity manager = iterator.next();
            if (manager.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }

        telegramUserRepository.save(telegramUser);
    }

    @Override
    @Transactional(readOnly = true)
    public TradeByFiltersManager findById(String chatId, String name) throws TelegramUserDoesntExistException, TradeByFiltersManagerDoesntExistException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        return tradeByFiltersManagerRepository.findById(new TradeByFiltersManagerEntityId(telegramUser.getUser(), name)).map(tradeByFiltersManagerEntityMapper::createDTO).orElseThrow(() -> new TradeByItemIdManagerDoesntExistException(String.format("Trade manager by chatId %s and name %s not found", chatId, name)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradeByFiltersManager> findAllByChatId(String chatId) throws TelegramUserDoesntExistException {
        return getTelegramUserEntityByIdOrThrow(chatId).getUser().getTradeByFiltersManagers().stream().map(tradeByFiltersManagerEntityMapper::createDTO).toList();
    }

    private TelegramUserEntity getTelegramUserEntityByIdOrThrow(String chatId) throws TelegramUserDoesntExistException {
        return telegramUserRepository.findById(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found"));
    }
}
