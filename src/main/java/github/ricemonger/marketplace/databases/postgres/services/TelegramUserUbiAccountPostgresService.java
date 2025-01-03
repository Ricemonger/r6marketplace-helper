package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.user.TelegramUserEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountEntryEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UbiAccountAuthorizationEntryPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UbiAccountStatsEntityPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.UbiAccountEntryEntityMapper;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.UbiAccountStatsEntityMapper;
import github.ricemonger.marketplace.services.abstractions.TelegramUserUbiAccountEntryDatabaseService;
import github.ricemonger.utils.DTOs.personal.UbiAccountAuthorizationEntry;
import github.ricemonger.utils.DTOs.personal.UbiAccountAuthorizationEntryWithTelegram;
import github.ricemonger.utils.DTOs.personal.UbiAccountEntryWithTelegram;
import github.ricemonger.utils.DTOs.personal.UbiAccountStatsEntityDTO;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryAlreadyExistsException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserUbiAccountPostgresService implements TelegramUserUbiAccountEntryDatabaseService {

    private final UbiAccountAuthorizationEntryPostgresRepository ubiAccountAuthorizationEntryRepository;

    private final UbiAccountStatsEntityPostgresRepository ubiAccountStatsRepository;

    private final TelegramUserPostgresRepository telegramUserRepository;

    private final UbiAccountEntryEntityMapper ubiAccountEntryEntityMapper;

    private final UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;

    @Override
    @Transactional
    public void saveAuthorizationInfo(String chatId, UbiAccountAuthorizationEntry account) throws TelegramUserDoesntExistException, UbiAccountEntryAlreadyExistsException {
        UbiAccountEntryEntity ubiAccountEntryEntity = ubiAccountAuthorizationEntryRepository.findByUserTelegramUserChatId(chatId).orElse(null);

        if (ubiAccountEntryEntity != null && !ubiAccountEntryEntity.getUbiAccountStats().getUbiProfileId().equals(account.getUbiProfileId())) {
            throw new UbiAccountEntryAlreadyExistsException("User with chatId " + chatId + " already has another Ubi account");
        } else {
            ubiAccountAuthorizationEntryRepository.save(ubiAccountEntryEntityMapper.createEntityForTelegramUser(chatId, account));
        }
    }

    @Override
    @Transactional
    public void saveAllUbiAccountStats(List<UbiAccountStatsEntityDTO> ubiAccounts) {
        ubiAccountStatsRepository.saveAll(ubiAccountStatsEntityMapper.createEntities(ubiAccounts));
    }

    @Override
    @Transactional
    public void deleteAuthorizationInfoByChatId(String chatId) throws TelegramUserDoesntExistException {
        TelegramUserEntity telegramUser = getTelegramUserEntityByIdOrThrow(chatId);

        telegramUser.getUser().setUbiAccountEntry(null);
        telegramUserRepository.save(telegramUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UbiAccountAuthorizationEntry findAuthorizationInfoByChatId(String chatId) throws TelegramUserDoesntExistException, UbiAccountEntryDoesntExistException {
        return ubiAccountEntryEntityMapper.createUbiAccountAuthorizationEntry(ubiAccountAuthorizationEntryRepository.findByUserTelegramUserChatId(chatId)
                .orElseThrow(() -> new UbiAccountEntryDoesntExistException("User with chatId " + chatId + " doesn't have ubi account entry")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbiAccountAuthorizationEntryWithTelegram> findAllAuthorizationInfoForTelegram() {
        return ubiAccountAuthorizationEntryRepository.findAll().stream().map(ubiAccountEntryEntityMapper::createUbiAccountAuthorizationEntryWithTelegram).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbiAccountEntryWithTelegram> findAllForTelegram() {
        return ubiAccountAuthorizationEntryRepository.findAll().stream().map(ubiAccountEntryEntityMapper::createUbiAccountEntryWithTelegram).toList();
    }

    private TelegramUserEntity getTelegramUserEntityByIdOrThrow(String chatId) {
        return telegramUserRepository.findById(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found"));
    }
}
