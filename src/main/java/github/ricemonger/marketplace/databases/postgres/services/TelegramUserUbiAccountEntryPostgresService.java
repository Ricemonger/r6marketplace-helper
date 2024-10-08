package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.user.TelegramUserEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountEntryEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.ItemPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UbiAccountEntryPostgresRepository;
import github.ricemonger.marketplace.services.abstractions.TelegramUserUbiAccountEntryDatabaseService;
import github.ricemonger.utils.dtos.UbiAccountAuthorizationEntry;
import github.ricemonger.utils.dtos.UbiAccountAuthorizationEntryWithTelegram;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryDoesntExistException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserUbiAccountEntryPostgresService implements TelegramUserUbiAccountEntryDatabaseService {

    private final ItemPostgresRepository itemRepository;

    private final UbiAccountEntryPostgresRepository ubiAccountEntryRepository;

    private final TelegramUserPostgresRepository telegramUserRepository;

    @Override
    @Transactional
    public void saveAuthorizationInfo(String chatId, UbiAccountAuthorizationEntry account) throws TelegramUserDoesntExistException, UbiAccountEntryAlreadyExistsException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        UbiAccountEntryEntity ubiAccountEntry = ubiAccountEntryRepository.findByUserTelegramUserChatId(telegramUser.getChatId()).orElse(null);

        if (ubiAccountEntry != null && !ubiAccountEntry.getUbiProfileId().equals(account.getUbiProfileId())) {
            throw new UbiAccountEntryAlreadyExistsException("User with chatId " + chatId + " already has another Ubi account");
        } else {
            ubiAccountEntryRepository.save(new UbiAccountEntryEntity(telegramUser.getUser(), account));
        }
    }

    @Override
    @Transactional
    public void deleteByChatId(String chatId) throws TelegramUserDoesntExistException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        telegramUser.getUser().setUbiAccountEntry(null);
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public UbiAccountAuthorizationEntry findByChatId(String chatId) throws TelegramUserDoesntExistException, UbiAccountEntryDoesntExistException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        return ubiAccountEntryRepository.findByUserTelegramUserChatId(telegramUser.getChatId())
                .orElseThrow(() -> new UbiAccountEntryDoesntExistException("User with chatId " + chatId + " doesn't have ubi account entry")).toUbiAccountEntryForAuthorization();
    }

    @Override
    public List<UbiAccountAuthorizationEntryWithTelegram> findAll() {
        return ubiAccountEntryRepository.findAll().stream().map(UbiAccountEntryEntity::toUbiAccountWithTelegram).toList();
    }

    private TelegramUserEntity getTelegramUserEntityByIdOrThrow(String chatId) {
        return telegramUserRepository.findById(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found"));
    }
}
