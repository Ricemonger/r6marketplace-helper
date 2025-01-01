package github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user;

import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.services.DTOs.TelegramUserInput;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserInputEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TelegramUserInputEntityMapperTest {
    @Autowired
    private TelegramUserInputEntityMapper telegramUserInputEntityMapper;
    @MockBean
    private TelegramUserPostgresRepository telegramUserPostgresRepository;

    @Test
    public void createEntity_should_properly_map_entity() {
        TelegramUserInput input = new TelegramUserInput("chatId", InputState.ITEM_FILTER_NAME, "value");

        TelegramUserEntity telegramUserEntity = new TelegramUserEntity();
        telegramUserEntity.setChatId("chatId");
        telegramUserEntity.setUser(new UserEntity());
        telegramUserEntity.getUser().setId(1L);
        when(telegramUserPostgresRepository.existsById("chatId")).thenReturn(true);
        when(telegramUserPostgresRepository.getReferenceById("chatId")).thenReturn(telegramUserEntity);

        assertTrue(new TelegramUserInputEntity(telegramUserEntity, InputState.ITEM_FILTER_NAME, "value").isFullyEqual(telegramUserInputEntityMapper.createEntity(input)));
    }

    @Test
    public void createEntity_should_throw_if_user_doesnt_exist() {
        TelegramUserInput input = new TelegramUserInput("chatId", InputState.ITEM_FILTER_NAME, "value");

        when(telegramUserPostgresRepository.existsById("chatId")).thenReturn(false);

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserInputEntityMapper.createEntity(input));
    }

    @Test
    public void createDTO_should_properly_map_dto() {
        TelegramUserEntity telegramUserEntity = new TelegramUserEntity();
        telegramUserEntity.setChatId("chatId");
        telegramUserEntity.setUser(new UserEntity());
        telegramUserEntity.getUser().setId(1L);
        TelegramUserInputEntity entity = new TelegramUserInputEntity(telegramUserEntity, InputState.ITEM_FILTER_NAME, "value");

        assertEquals(new TelegramUserInput("chatId", InputState.ITEM_FILTER_NAME, "value"), telegramUserInputEntityMapper.createDTO(entity));
    }
}