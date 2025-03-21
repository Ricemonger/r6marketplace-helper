package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.dto_projections.TelegramUserInputProjection;
import github.ricemonger.marketplace.databases.postgres.repositories.CustomTelegramUserInputPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.TelegramUserInputEntityMapper;
import github.ricemonger.marketplace.services.DTOs.TelegramUserInput;
import github.ricemonger.marketplace.services.abstractions.TelegramUserInputDatabaseService;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.server.TelegramUserInputDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramUserInputPostgresService implements TelegramUserInputDatabaseService {

    private final CustomTelegramUserInputPostgresRepository telegramUserInputRepository;

    private final TelegramUserInputEntityMapper telegramUserInputEntityMapper;

    @Override
    @Transactional
    public void save(String chatId, InputState inputState, String value) throws TelegramUserDoesntExistException {
        telegramUserInputRepository.save(telegramUserInputEntityMapper.createEntity(new TelegramUserInput(chatId, inputState, value)));
    }

    @Override
    @Transactional
    public void deleteAllByChatId(String chatId) throws TelegramUserDoesntExistException {
        telegramUserInputRepository.deleteAllByChatId(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramUserInput findById(String chatId, InputState inputState) throws TelegramUserDoesntExistException, TelegramUserInputDoesntExistException {
        return telegramUserInputRepository.findInputById(chatId, inputState)
                .map(telegramUserInputEntityMapper::createDTO)
                .orElseThrow(() -> new TelegramUserInputDoesntExistException("Telegram user input with chatId " + chatId + " and inputState " + inputState + " doesn't exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelegramUserInput> findAllByChatId(String chatId) throws TelegramUserDoesntExistException {
        Collection<TelegramUserInputProjection> entities = telegramUserInputRepository.findAllByChatId(chatId);

        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        } else {
            return entities.stream()
                    .map(telegramUserInputEntityMapper::createDTO)
                    .toList();
        }
    }
}
