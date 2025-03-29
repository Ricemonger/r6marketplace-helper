package github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user;


import github.ricemonger.marketplace.databases.postgres.repositories.CustomUserPostgresRepository;
import github.ricemonger.marketplace.services.configurations.ItemTradePriorityByExpressionCalculatorConfiguration;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramUserEntityMapper {

    private final CustomUserPostgresRepository userRepository;

    public TelegramUserEntity createDefaultEntityForNewUser(String chatId) {
        UserEntity userEntity = userRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity = new TelegramUserEntity();
        telegramUserEntity.setChatId(chatId);
        telegramUserEntity.setUser(userEntity);
        return telegramUserEntity;
    }
}
