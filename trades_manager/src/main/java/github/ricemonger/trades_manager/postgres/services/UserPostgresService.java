package github.ricemonger.trades_manager.postgres.services;

import github.ricemonger.trades_manager.postgres.repositories.CustomManageableUserPostgresRepository;
import github.ricemonger.trades_manager.postgres.services.entity_mappers.user.UserEntityMapper;
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

    private final CustomManageableUserPostgresRepository userRepository;

    private final UserEntityMapper userEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ManageableUser> getAllManageableUsers() {
        return userRepository.findAllManageableUsers().stream().map(userEntityMapper::createManageableUser).toList();
    }
}
