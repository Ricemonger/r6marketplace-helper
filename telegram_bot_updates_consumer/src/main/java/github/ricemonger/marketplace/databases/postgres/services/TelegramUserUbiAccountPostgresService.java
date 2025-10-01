package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.repositories.CustomUbiAccountEntryPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.UbiAccountEntryEntityMapper;
import github.ricemonger.marketplace.services.DTOs.UbiAccountAuthorizationEntry;
import github.ricemonger.marketplace.services.abstractions.TelegramUserUbiAccountEntryDatabaseService;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryAlreadyExistsException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryDoesntExistException;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountEntryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TelegramUserUbiAccountPostgresService implements TelegramUserUbiAccountEntryDatabaseService {

    private final CustomUbiAccountEntryPostgresRepository customUbiAccountEntryPostgresRepository;

    private final UbiAccountEntryEntityMapper ubiAccountEntryEntityMapper;

    @Override
    @Transactional
    public void save(String chatId, UbiAccountAuthorizationEntry account) {
        UbiAccountEntryEntity ubiAccountEntryEntity = customUbiAccountEntryPostgresRepository.findByUserTelegramUserChatId(chatId).orElse(null);

        if (ubiAccountEntryEntity != null &&
            ubiAccountEntryEntity.getUbiAccountStats() != null &&
            !Objects.equals(ubiAccountEntryEntity.getUbiAccountStats().getUbiProfileId(), account.getUbiProfileId())) {
            throw new UbiAccountEntryAlreadyExistsException("User with chatId " + chatId + " already has another Ubi account");
        } else {
            customUbiAccountEntryPostgresRepository.save(ubiAccountEntryEntityMapper.createEntity(chatId, account));
        }
    }

    @Override
    @Transactional
    public void deleteByChatId(String chatId) {
        customUbiAccountEntryPostgresRepository.deleteByUserTelegramUserChatId(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public UbiAccountAuthorizationEntry findByChatId(String chatId) {
        return ubiAccountEntryEntityMapper.createUbiAccountAuthorizationEntry(customUbiAccountEntryPostgresRepository.findUbiAccountAuthorizationEntryByUserTelegramUserChatId(chatId).orElseThrow(() -> new UbiAccountEntryDoesntExistException("Ubi account entry for chatId " + chatId + " doesn't exist")));
    }
}
