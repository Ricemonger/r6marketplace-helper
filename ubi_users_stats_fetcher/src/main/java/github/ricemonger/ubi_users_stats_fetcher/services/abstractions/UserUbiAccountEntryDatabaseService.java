package github.ricemonger.ubi_users_stats_fetcher.services.abstractions;


import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UserAuthorizedUbiAccount;

import java.util.List;

public interface UserUbiAccountEntryDatabaseService {
    List<UserAuthorizedUbiAccount> findAllUserAuthorizedUbiAccounts();
}
