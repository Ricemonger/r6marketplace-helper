package github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user;

import github.ricemonger.marketplace.databases.postgres.repositories.CustomUserPostgresRepository;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class TelegramUserEntityMapperTest {
    @Autowired
    private TelegramUserEntityMapper telegramUserEntityMapper;
    @MockBean
    private CustomUserPostgresRepository userRepository;

    @Test
    public void createNewEntityForNewUser_should_create_and_return_new_entity_and_create_new_user() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        doReturn(userEntity).when(userRepository).save(any(UserEntity.class));

        TelegramUserEntity entity = telegramUserEntityMapper.createNewEntityForNewUser("chatId");

        assertEquals("chatId", entity.getChatId());
        assertSame(userEntity, entity.getUser());
    }
}