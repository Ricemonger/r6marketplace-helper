package github.ricemonger.ubi_users_stats_fetcher.postgres.services;


import github.ricemonger.ubi_users_stats_fetcher.postgres.repositories.CustomUserUbiAccountEntryPostgresRepository;
import github.ricemonger.ubi_users_stats_fetcher.postgres.services.entity_mappers.user.UbiAccountEntryEntityMapper;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UserAuthorizedUbiAccount;
import github.ricemonger.ubi_users_stats_fetcher.services.abstractions.UserUbiAccountEntryDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserUbiAccountEntryPostgresService implements UserUbiAccountEntryDatabaseService {

    private final CustomUserUbiAccountEntryPostgresRepository customUserUbiAccountEntryPostgresRepository;

    private final UbiAccountEntryEntityMapper ubiAccountEntryEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserAuthorizedUbiAccount> findAllUserAuthorizedUbiAccounts() {
        return customUserUbiAccountEntryPostgresRepository.findAllUserAuthorizedUbiAccounts().stream().map(ubiAccountEntryEntityMapper::createUserUbiAccountEntry).toList();
    }
}
