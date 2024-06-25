package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.user.TelegramUserEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UbiAccountPostgresRepository;
import github.ricemonger.marketplace.services.abstractions.UbiAccountDatabaseService;
import github.ricemonger.utils.dtos.UbiAccount;
import github.ricemonger.utils.dtos.UbiAccountWithTelegram;
import github.ricemonger.utils.exceptions.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.UbiUserDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUbiAccountPostgresService implements UbiAccountDatabaseService {

    private final UbiAccountPostgresRepository ubiAccountRepository;

    private final TelegramUserPostgresRepository userRepository;

    @Override
    public void save(String chatId, UbiAccount account) {
        TelegramUserEntity user = userRepository.findById(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("User with chatId " + chatId + " doesn't exist"));

        ubiAccountRepository.save(new UbiAccountEntity(user.getUser(), account));
    }

    @Override
    public void deleteByChatId(String chatId) {
        TelegramUserEntity user = userRepository.findById(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("User with chatId " + chatId + " doesn't exist"));

        ubiAccountRepository.deleteById(user.getUser());
    }

    @Override
    public UbiAccount findByChatId(String chatId) throws UbiUserDoesntExistException {
        TelegramUserEntity user = userRepository.findById(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("User with chatId " + chatId + " doesn't exist"));

        return ubiAccountRepository.findById(user.getUser()).orElseThrow(() -> new UbiUserDoesntExistException("User with chatId " + chatId +
                                                                                                               " doesn't exist")).toUbiAccount();
    }

    @Override
    public List<UbiAccountWithTelegram> findAll() {
        return ubiAccountRepository.findAll().stream().map(UbiAccountEntity::toUbiAccountWithTelegram).toList();
    }
}