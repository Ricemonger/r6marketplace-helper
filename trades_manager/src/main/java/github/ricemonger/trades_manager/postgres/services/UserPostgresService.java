package github.ricemonger.trades_manager.postgres.services;

import github.ricemonger.trades_manager.postgres.repositories.CustomUserPostgresRepository;
import github.ricemonger.trades_manager.postgres.services.entity_mappers.user.ManageableUserFactory;
import github.ricemonger.trades_manager.services.DTOs.ManageableUser;
import github.ricemonger.trades_manager.services.abstractions.UserDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPostgresService implements UserDatabaseService {

    private final CustomUserPostgresRepository userRepository;

    private final ManageableUserFactory manageableUserFactory;

    @Override
    @Transactional(readOnly = true)
    public List<ManageableUser> getAllManageableUsers() {
        return userRepository.findAllManageableUsers().stream().map(manageableUserFactory::createManageableUserFromPostgresEntity).toList();
    }
}
