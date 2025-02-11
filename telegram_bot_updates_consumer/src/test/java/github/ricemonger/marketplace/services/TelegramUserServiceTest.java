package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.services.DTOs.*;
import github.ricemonger.marketplace.services.abstractions.TelegramUserDatabaseService;
import github.ricemonger.marketplace.services.abstractions.TelegramUserInputDatabaseService;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.server.TelegramUserInputDoesntExistException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TelegramUserServiceTest {
    @SpyBean
    private TelegramUserService telegramUserService;
    @MockBean
    private TelegramUserDatabaseService telegramUserDatabaseService;
    @MockBean
    private TelegramUserInputDatabaseService inputDatabaseService;
    @MockBean
    private UbiAccountEntryService ubiAccountEntryService;
    @MockBean
    private CommonValuesService commonValuesService;

    @Test
    public void registerTelegramUser_should_handle_to_service() {
        telegramUserService.registerTelegramUser(123L);
        verify(telegramUserDatabaseService).register("123");
    }

    @Test
    public void isTelegramUserRegistered_should_return_service_result_true() {
        when(telegramUserDatabaseService.isRegistered("123")).thenReturn(true);
        assertTrue(telegramUserService.isTelegramUserRegistered(123L));
    }

    @Test
    public void isTelegramUserRegistered_should_return_service_result_false() {
        when(telegramUserDatabaseService.isRegistered("123")).thenReturn(false);
        assertFalse(telegramUserService.isTelegramUserRegistered(123L));
    }

    @Test
    public void setUserInputState_should_handle_to_service() {
        telegramUserService.setUserInputState(123L, InputState.ITEM_FILTER_NAME);
        verify(telegramUserDatabaseService).setUserInputState("123", InputState.ITEM_FILTER_NAME);
    }

    @Test
    public void setUserInputStateAndGroup_should_handle_to_service() {
        telegramUserService.setUserInputStateAndGroup(123L, InputState.ITEM_FILTER_NAME, InputGroup.UBI_ACCOUNT_ENTRY_LINK);
        verify(telegramUserDatabaseService).setUserInputStateAndGroup("123", InputState.ITEM_FILTER_NAME, InputGroup.UBI_ACCOUNT_ENTRY_LINK);
    }

    @Test
    public void saveUserInput_should_handle_to_service() {
        telegramUserService.saveUserInput(123L, InputState.ITEM_FILTER_NAME, "test");
        verify(inputDatabaseService).save("123", InputState.ITEM_FILTER_NAME, "test");
    }

    @Test
    public void clearUserInputsAndSetInputStateAndGroup_should_handle_to_service() {
        telegramUserService.clearUserInputsAndSetInputStateAndGroup(123L, InputState.ITEM_FILTER_NAME, InputGroup.UBI_ACCOUNT_ENTRY_LINK);
        verify(inputDatabaseService).deleteAllByChatId("123");
        verify(telegramUserDatabaseService).setUserInputStateAndGroup("123", InputState.ITEM_FILTER_NAME, InputGroup.UBI_ACCOUNT_ENTRY_LINK);
    }

    @Test
    public void getUserInputByState_should_return_service_result() {
        when(inputDatabaseService.findById("123", InputState.ITEM_FILTER_NAME)).thenReturn(new TelegramUserInput("123", InputState.ITEM_FILTER_NAME, "test"));
        assertEquals("test", telegramUserService.getUserInputByState(123L, InputState.ITEM_FILTER_NAME));
    }

    @Test
    public void getAllUserInputs_should_return_service_result() {
        when(inputDatabaseService.findAllByChatId("123")).thenReturn(List.of(new TelegramUserInput("123", InputState.ITEM_FILTER_NAME, "test")));
        assertEquals(1, telegramUserService.getAllUserInputs(123L).size());
    }

    @Test
    public void getTelegramUserInputStateAndGroup_should_return_service_result() {
        when(telegramUserDatabaseService.findUserInputStateAndGroupById("123")).thenReturn(
                new TelegramUserInputStateAndGroup("chatId", InputState.ITEM_FILTER_NAME, InputGroup.UBI_ACCOUNT_ENTRY_LINK));

        TelegramUserInputStateAndGroup result = telegramUserService.getTelegramUserInputStateAndGroup(123L);

        assertEquals("chatId", result.getChatId());
        assertEquals(InputState.ITEM_FILTER_NAME, result.getInputState());
        assertEquals(InputGroup.UBI_ACCOUNT_ENTRY_LINK, result.getInputGroup());
    }

    @Test
    public void getUserItemShowSettings_should_return_service_result() {
        ItemShowSettings itemShowSettings = Mockito.mock(ItemShowSettings.class);
        when(telegramUserDatabaseService.findUserItemShowSettings("123")).thenReturn(itemShowSettings);
        assertSame(itemShowSettings, telegramUserService.getUserItemShowSettings(123L));
    }

    @Test
    public void setUserItemShowFewItemsInMessageFlag_should_handle_to_service() {
        telegramUserService.setUserItemShowFewItemsInMessageFlag(123L, true);
        verify(telegramUserDatabaseService).setUserItemShowFewItemsInMessageFlag("123", true);
    }

    @Test
    public void setUserItemShowMessagesLimitByUserInput_should_handle_to_service_valid_value() {
        when(commonValuesService.getMaximumTelegramMessageLimit()).thenReturn(10);
        doReturn("5").when(telegramUserService).getUserInputByState(123L, InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT);
        telegramUserService.setUserItemShowMessagesLimitByUserInput(123L);
        verify(telegramUserDatabaseService).setUserItemShowMessagesLimit("123", 5);
    }

    @Test
    public void setUserItemShowMessagesLimitByUserInput_should_handle_to_service_big_value() {
        when(commonValuesService.getMaximumTelegramMessageLimit()).thenReturn(10);
        doReturn("15").when(telegramUserService).getUserInputByState(123L, InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT);
        telegramUserService.setUserItemShowMessagesLimitByUserInput(123L);
        verify(telegramUserDatabaseService).setUserItemShowMessagesLimit("123", 10);
    }

    @Test
    public void setUserItemShowMessagesLimitByUserInput_should_handle_to_service_small_value() {
        when(commonValuesService.getMaximumTelegramMessageLimit()).thenReturn(10);
        doReturn("0").when(telegramUserService).getUserInputByState(123L, InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT);
        telegramUserService.setUserItemShowMessagesLimitByUserInput(123L);
        verify(telegramUserDatabaseService).setUserItemShowMessagesLimit("123", 1);
    }

    @Test
    public void setUserItemShowMessagesLimitByUserInput_should_handle_to_service_input_doesnt_exist() {
        when(commonValuesService.getMaximumTelegramMessageLimit()).thenReturn(10);
        doThrow(new TelegramUserInputDoesntExistException("")).when(telegramUserService).getUserInputByState(123L, InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT);
        telegramUserService.setUserItemShowMessagesLimitByUserInput(123L);
        verify(telegramUserDatabaseService).setUserItemShowMessagesLimit("123", 10);
    }

    @Test
    public void setUserItemShowMessagesLimitByUserInput_should_handle_to_service_input_is_not_number() {
        when(commonValuesService.getMaximumTelegramMessageLimit()).thenReturn(10);
        doReturn("test").when(telegramUserService).getUserInputByState(123L, InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT);
        telegramUserService.setUserItemShowMessagesLimitByUserInput(123L);
        verify(telegramUserDatabaseService).setUserItemShowMessagesLimit("123", 10);
    }

    @Test
    public void setUserItemShownFieldsSettingsByUserInput_should_handle_to_service() {
        TelegramUserInput input1 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_ITEM_NAME, "true");
        TelegramUserInput input2 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_ITEM_TYPE, "false");
        TelegramUserInput input3 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_MAX_BUY_PRICE, "true");
        TelegramUserInput input4 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_BUY_ORDERS_COUNT, "false");
        TelegramUserInput input5 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_MIN_SELL_PRICE, "true");
        TelegramUserInput input6 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_SELL_ORDERS_COUNT, "false");
        TelegramUserInput input7 = new TelegramUserInput("123", InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_PICTURE, "true");
        when(inputDatabaseService.findAllByChatId("123")).thenReturn(List.of(input1, input2, input3, input4, input5, input6, input7));

        ItemShownFieldsSettings shownFieldsSettings = new ItemShownFieldsSettings(true, false, true, false, true, false, true);

        telegramUserService.setUserItemShownFieldsSettingsByUserInput(123L, "anyValue", "false");

        verify(telegramUserDatabaseService).setUserItemShowFieldsSettings("123", shownFieldsSettings);
    }

    @Test
    public void addUserItemShowAppliedFilter_should_handle_to_service() {
        telegramUserService.addUserItemShowAppliedFilter(123L, "test");
        verify(telegramUserDatabaseService).addUserItemShowAppliedFilter("123", "test");
    }

    @Test
    public void removeUserItemShowAppliedFilter_should_handle_to_service() {
        telegramUserService.removeUserItemShowAppliedFilter(123L, "test");
        verify(telegramUserDatabaseService).removeUserItemShowAppliedFilter("123", "test");
    }

    @Test
    public void getUserItemShowAppliedFiltersNames_should_return_service_result() {
        when(telegramUserDatabaseService.findAllUserItemShowAppliedFiltersNames("123")).thenReturn(List.of("test", "test1"));

        List<String> result = telegramUserService.getUserItemShowAppliedFiltersNames(123L);

        assertEquals(2, result.size());
        assertTrue(result.contains("test"));
        assertTrue(result.contains("test1"));
    }

    @Test
    public void getUserTradeManagersSettings_should_return_service_result() {
        TradeManagersSettings tradeManagersSettings = Mockito.mock(TradeManagersSettings.class);
        when(telegramUserDatabaseService.findUserTradeManagersSettings("123")).thenReturn(tradeManagersSettings);
        assertSame(tradeManagersSettings, telegramUserService.getUserTradeManagersSettings(123L));
    }

    @Test
    public void setUserTradeManagersSettingsNewManagersAreActiveFlag_should_handle_to_service() {
        telegramUserService.setUserTradeManagersSettingsNewManagersAreActiveFlag(123L, true);
        verify(telegramUserDatabaseService).setUserTradeManagersSettingsNewManagersAreActiveFlag("123", true);
    }

    @Test
    public void setUserTradeManagersSettingsManagingEnabledFlag_should_handle_to_service() {
        telegramUserService.setUserTradeManagersSettingsManagingEnabledFlag(123L, false);
        verify(telegramUserDatabaseService).setUserTradeManagersSettingsManagingEnabledFlag("123", false);
    }

    @Test
    public void setUserTradeManagersSellSettingsManagingEnabledFlag_should_handle_to_service() {
        telegramUserService.setUserTradeManagersSellSettingsManagingEnabledFlag(123L, true);
        verify(telegramUserDatabaseService).setUserTradeManagersSellSettingsManagingEnabledFlag("123", true);
    }

    @Test
    public void setUserTradeManagersBuySettingsManagingEnabledFlag_should_handle_to_service() {
        telegramUserService.setUserTradeManagersBuySettingsManagingEnabledFlag(123L, false);
        verify(telegramUserDatabaseService).setUserTradeManagersBuySettingsManagingEnabledFlag("123", false);
    }

    @Test
    public void setUserTradeManagersSellSettingsTradePriorityExpression_should_handle_to_service() {
        telegramUserService.setUserTradeManagersSellSettingsTradePriorityExpression(123L, "test");
        verify(telegramUserDatabaseService).setUserTradeManagersSellSettingsTradePriorityExpression("123", "test");
    }

    @Test
    public void setUserTradeManagersBuySettingsTradePriorityExpression_should_handle_to_service() {
        telegramUserService.setUserTradeManagersBuySettingsTradePriorityExpression(123L, "test");
        verify(telegramUserDatabaseService).setUserTradeManagersBuySettingsTradePriorityExpression("123", "test");
    }

    @Test
    public void invertUserPrivateNotificationsFlag_should_handle_to_service() {
        telegramUserService.invertUserPrivateNotificationsFlag(123L);
        verify(telegramUserDatabaseService).invertUserPrivateNotificationsFlag("123");
    }

    @Test
    public void invertUserPublicNotificationsFlag_should_handle_to_service() {
        telegramUserService.invertUserPublicNotificationsFlag(123L);
        verify(telegramUserDatabaseService).invertUserPublicNotificationsFlag("123");
    }

    @Test
    public void invertUserUbiStatsUpdatedNotificationsFlag_should_handle_to_service() {
        telegramUserService.invertUserUbiStatsUpdatedNotificationsFlag(123L);
        verify(telegramUserDatabaseService).invertUserUbiStatsUpdatedNotificationsFlag("123");
    }

    @Test
    public void invertUserTradeManagerNotificationsFlag_should_handle_to_service() {
        telegramUserService.invertUserTradeManagerNotificationsFlag(123L);
        verify(telegramUserDatabaseService).invertUserTradeManagerNotificationsFlag("123");
    }

    @Test
    public void invertUserAuthorizationNotificationsFlag_should_handle_to_service() {
        telegramUserService.invertUserAuthorizationNotificationsFlag(123L);
        verify(telegramUserDatabaseService).invertUserAuthorizationNotificationsFlag("123");
    }

    @Test
    public void getUserNotificationsSettings_should_return_service_result() {
        NotificationsSettings notificationsSettings = Mockito.mock(NotificationsSettings.class);
        when(telegramUserDatabaseService.findUserNotificationsSettings("123")).thenReturn(notificationsSettings);
        assertSame(notificationsSettings, telegramUserService.getUserNotificationsSettings(123L));
    }

    @Test
    public void authorizeAndSaveUbiUserByUserInput_should_handle_to_service() {
        TelegramUserInput input1 = new TelegramUserInput("123", InputState.UBI_ACCOUNT_ENTRY_EMAIL, "email");
        TelegramUserInput input2 = new TelegramUserInput("123", InputState.UBI_ACCOUNT_ENTRY_PASSWORD, "password");
        TelegramUserInput input3 = new TelegramUserInput("123", InputState.UBI_ACCOUNT_ENTRY_2FA_CODE, "2faCode");
        when(inputDatabaseService.findAllByChatId("123")).thenReturn(List.of(input1, input2, input3));

        telegramUserService.authorizeAndSaveUbiUserByUserInput(123L);

        verify(inputDatabaseService).deleteAllByChatId("123");

        verify(ubiAccountEntryService).authorizeAndSaveUbiAccountEntry("123", "email", "password", "2faCode");
    }

    @Test
    public void reauthorizeAndSaveExistingUbiUserBy2FACodeByUserInput_should_handle_to_service() {
        doReturn("2faCode").when(telegramUserService).getUserInputByState(123L, InputState.UBI_ACCOUNT_ENTRY_2FA_CODE);

        telegramUserService.reauthorizeAndSaveExistingUbiUserBy2FACodeByUserInput(123L);

        verify(ubiAccountEntryService).reauthorizeAndSaveExistingUbiAccountEntryBy2FACode("123", "2faCode");
    }

    @Test
    public void removeUbiUserByChatId_should_handle_to_service() {
        telegramUserService.removeUbiUserByChatId(123L);
        verify(ubiAccountEntryService).deleteByChatId("123");
    }

    @Test
    public void getUbiUserByChatId_should_return_service_result() {
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntry = Mockito.mock(UbiAccountAuthorizationEntry.class);
        when(ubiAccountEntryService.findByChatId("123")).thenReturn(ubiAccountAuthorizationEntry);
        assertSame(ubiAccountAuthorizationEntry, telegramUserService.getUbiUserByChatId(123L));
    }
}