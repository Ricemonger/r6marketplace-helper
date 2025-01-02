package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.dto_projections.ItemShowSettingsProjection;
import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.TelegramUserEntityMapper;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.UserEntityMapper;
import github.ricemonger.marketplace.services.DTOs.ItemShowSettings;
import github.ricemonger.marketplace.services.DTOs.ItemShownFieldsSettings;
import github.ricemonger.marketplace.services.DTOs.TelegramUserInputStateAndGroup;
import github.ricemonger.marketplace.services.DTOs.TradeManagersSettings;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.client.TelegramUserAlreadyExistsException;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TelegramUserPostgresServiceTest {
    @Autowired
    private TelegramUserPostgresService telegramUserService;
    @MockBean
    private TelegramUserPostgresRepository telegramUserRepository;
    @MockBean
    private TelegramUserEntityMapper telegramUserEntityMapper;
    @MockBean
    private UserEntityMapper userEntityMapper;
    @MockBean
    private UserPostgresRepository userRepository;

    @Test
    public void register_should_save_new_user_if_doesnt_exist() {
        when(telegramUserRepository.existsById("chatId")).thenReturn(true);

        TelegramUserEntity entity = Mockito.mock(TelegramUserEntity.class);
        when(telegramUserEntityMapper.createNewEntityForNewUser("chatId")).thenReturn(entity);

        telegramUserService.register("chatId");

        Mockito.verify(telegramUserRepository).save(same(entity));
    }

    @Test
    public void register_should_throw_if_user_already_exists() {
        when(telegramUserRepository.existsById("chatId")).thenReturn(false);

        TelegramUserEntity entity = Mockito.mock(TelegramUserEntity.class);
        when(telegramUserEntityMapper.createNewEntityForNewUser("chatId")).thenReturn(entity);

        assertThrows(TelegramUserAlreadyExistsException.class, () -> telegramUserService.register("chatId"));
    }

    @Test
    public void isRegistered_should_return_true_if_user_exists() {
        when(telegramUserRepository.existsById("chatId")).thenReturn(true);

        assertTrue(telegramUserService.isRegistered("chatId"));
    }

    @Test
    public void isRegistered_should_return_false_if_user_doesnt_exist() {
        when(telegramUserRepository.existsById("chatId")).thenReturn(false);

        assertFalse(telegramUserService.isRegistered("chatId"));
    }

    @Test
    public void setUserInputGroup_should_update_input_group() {
        telegramUserService.setUserInputGroup("chatId", InputGroup.ITEMS_SHOW);

        Mockito.verify(telegramUserRepository).updateInputGroup("chatId", InputGroup.ITEMS_SHOW);
    }

    @Test
    public void setUserInputState_should_update_input_state() {
        telegramUserService.setUserInputState("chatId", InputState.ITEM_FILTER_NAME);

        Mockito.verify(telegramUserRepository).updateInputState("chatId", InputState.ITEM_FILTER_NAME);
    }

    @Test
    public void setUserInputStateAndGroup_should_update_input_state_and_group() {
        telegramUserService.setUserInputStateAndGroup("chatId", InputState.ITEM_FILTER_NAME, InputGroup.ITEMS_SHOW);

        Mockito.verify(telegramUserRepository).updateInputStateAndGroup("chatId", InputState.ITEM_FILTER_NAME, InputGroup.ITEMS_SHOW);
    }

    @Test
    public void findUserInputStateAndGroupById_should_return_input_state_and_group() {
        TelegramUserInputStateAndGroup inputStateAndGroup = Mockito.mock(TelegramUserInputStateAndGroup.class);
        when(telegramUserRepository.findTelegramUserInputStateAndGroupByChatId("chatId")).thenReturn(Optional.of(inputStateAndGroup));

        assertSame(inputStateAndGroup, telegramUserService.findUserInputStateAndGroupById("chatId"));
    }

    @Test
    public void findUserInputStateAndGroupById_should_throw_if_user_doesnt_exist() {
        when(telegramUserRepository.findTelegramUserInputStateAndGroupByChatId("chatId")).thenReturn(Optional.empty());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserService.findUserInputStateAndGroupById("chatId"));
    }

    @Test
    public void setUserItemShowFewItemsInMessageFlag_should_update_flag_true() {
        telegramUserService.setUserItemShowFewItemsInMessageFlag("chatId", true);

        Mockito.verify(telegramUserRepository).updateItemShowFewItemsInMessageFlag("chatId", true);
    }

    @Test
    public void setUserItemShowFewItemsInMessageFlag_should_update_flag_false() {
        telegramUserService.setUserItemShowFewItemsInMessageFlag("chatId", false);

        Mockito.verify(telegramUserRepository).updateItemShowFewItemsInMessageFlag("chatId", false);
    }

    @Test
    public void setUserItemShowMessagesLimit_should_update_limit() {
        telegramUserService.setUserItemShowMessagesLimit("chatId", 5);

        Mockito.verify(telegramUserRepository).updateItemShowMessagesLimit("chatId", 5);
    }

    @Test
    public void setUserItemShowFieldsSettings_should_update_settings() {
        telegramUserService.setUserItemShowFieldsSettings("chatId", Mockito.mock(ItemShownFieldsSettings.class));

        Mockito.verify(userRepository).updateItemShowFieldsSettingsByTelegramUserChatId("chatId", Mockito.any(ItemShownFieldsSettings.class));
    }

    @Test
    public void addUserItemShowAppliedFilter_should_add_filter() {
        when(userRepository.findUserIdByTelegramUserChatId("chatId")).thenReturn(Optional.of(1L));

        telegramUserService.addUserItemShowAppliedFilter("chatId", "filterName");

        Mockito.verify(userRepository).addItemShowAppliedFilter(1L, "filterName");
    }

    @Test
    public void addUserItemShowAppliedFilter_should_throw_if_user_doesnt_exist() {
        when(userRepository.findUserIdByTelegramUserChatId("chatId")).thenReturn(Optional.empty());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserService.addUserItemShowAppliedFilter("chatId", "filterName"));
    }

    @Test
    public void removeUserItemShowAppliedFilter_should_remove_filter() {
        when(userRepository.findUserIdByTelegramUserChatId("chatId")).thenReturn(Optional.of(1L));

        telegramUserService.removeUserItemShowAppliedFilter("chatId", "filterName");

        Mockito.verify(userRepository).deleteItemShowAppliedFilter(1L, "filterName");
    }

    @Test
    public void removeUserItemShowAppliedFilter_should_throw_if_user_doesnt_exist() {
        when(userRepository.findUserIdByTelegramUserChatId("chatId")).thenReturn(Optional.empty());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserService.removeUserItemShowAppliedFilter("chatId", "filterName"));
    }

    @Test
    public void findAllUserItemShowAppliedFiltersNames_should_return_repository_result() {
        List names = Mockito.mock(List.class);
        when(userRepository.findAllUserItemShowAppliedFiltersNamesByTelegramUserChatId("chatId")).thenReturn(names);

        assertSame(names, telegramUserService.findAllUserItemShowAppliedFiltersNames("chatId"));
    }

    @Test
    public void findUserItemShowSettings_should_return_mapped_settings() {

        ItemShowSettingsProjection projection = Mockito.mock(ItemShowSettingsProjection.class);
        when(userRepository.findItemShowSettingsByTelegramUserChatId("chatId")).thenReturn(Optional.of(projection));

        List filters = Mockito.mock(List.class);
        when(userRepository.findAllUserItemShowAppliedFiltersByTelegramUserChatId("chatId")).thenReturn(filters);

        ItemShowSettings settings = Mockito.mock(ItemShowSettings.class);

        when(userEntityMapper.mapItemShowSettings(projection, filters)).thenReturn(settings);

        assertSame(settings, telegramUserService.findUserItemShowSettings("chatId"));
    }

    @Test
    public void findUserItemShowSettings_should_throw_if_user_doesnt_exist() {
        when(userRepository.findItemShowSettingsByTelegramUserChatId("chatId")).thenReturn(Optional.empty());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserService.findUserItemShowSettings("chatId"));
    }

    @Test
    public void setUserTradeManagersSettingsNewManagersAreActiveFlag_should_update_flag_true() {
        telegramUserService.setUserTradeManagersSettingsNewManagersAreActiveFlag("chatId", true);

        Mockito.verify(userRepository).updateTradeManagersSettingsNewManagersAreActiveFlagByTelegramUserChatId("chatId", true);
    }

    @Test
    public void setUserTradeManagersSettingsNewManagersAreActiveFlag_should_update_flag_false() {
        telegramUserService.setUserTradeManagersSettingsNewManagersAreActiveFlag("chatId", false);

        Mockito.verify(userRepository).updateTradeManagersSettingsNewManagersAreActiveFlagByTelegramUserChatId("chatId", false);
    }

    @Test
    public void setUserTradeManagersSettingsManagingEnabledFlag_should_update_flag_true() {
        telegramUserService.setUserTradeManagersSettingsManagingEnabledFlag("chatId", true);

        Mockito.verify(userRepository).updateTradeManagersSettingsManagingEnabledFlagByTelegramUserChatId("chatId", true);
    }

    @Test
    public void setUserTradeManagersSettingsManagingEnabledFlag_should_update_flag_false() {
        telegramUserService.setUserTradeManagersSettingsManagingEnabledFlag("chatId", false);

        Mockito.verify(userRepository).updateTradeManagersSettingsManagingEnabledFlagByTelegramUserChatId("chatId", false);
    }

    @Test
    public void findUserTradeManagersSettings_should_return_repository_result_if_user_exists() {
        TradeManagersSettings settings = Mockito.mock(TradeManagersSettings.class);
        when(userRepository.findTradeManagersSettingsByTelegramUserChatId("chatId")).thenReturn(Optional.of(settings));

        assertSame(settings, telegramUserService.findUserTradeManagersSettings("chatId"));
    }

    @Test
    public void findUserTradeManagersSettings_should_throw_if_user_doesnt_exist() {
        when(userRepository.findTradeManagersSettingsByTelegramUserChatId("chatId")).thenReturn(Optional.empty());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserService.findUserTradeManagersSettings("chatId"));
    }
}