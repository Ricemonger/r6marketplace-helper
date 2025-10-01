package github.ricemonger.telegramBot.update_consumer;


import github.ricemonger.marketplace.services.*;
import github.ricemonger.marketplace.services.DTOs.*;
import github.ricemonger.telegramBot.CallbackButton;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.client.TelegramBotClientService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.Tag;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.enums.TagGroup;
import github.ricemonger.utils.enums.TradeOperationType;
import github.ricemonger.utils.exceptions.server.InvalidTelegramUserInputException;
import github.ricemonger.utils.services.calculators.TradePriorityExpressionDeserializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BotInnerServiceTest {
    @SpyBean
    private BotInnerService botInnerService;
    @MockBean
    private TelegramUserService telegramUserService;
    @MockBean
    private ItemFilterService itemFilterService;
    @MockBean
    private TradeManagerService tradeManagerService;
    @MockBean
    private ItemFilterFromInputsMapper itemFilterFromInputsMapper;
    @MockBean
    private TradeManagerFromInputsMapper tradeManagerFromInputsMapper;
    @MockBean
    private TelegramBotClientService telegramBotClientService;
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private TagService tagService;
    @MockBean
    private TradePriorityExpressionDeserializer tradePriorityExpressionDeserializer;

    @Test
    public void sendText_should_handle_to_service() {
        UpdateInfo updateInfo = new UpdateInfo();
        String text = "text";
        botInnerService.sendText(updateInfo, text);

        verify(telegramBotClientService).sendText(same(updateInfo), same(text));
    }

    @Test
    public void askFromInlineKeyboard_should_handle_to_service() {
        UpdateInfo updateInfo = new UpdateInfo();
        String text = "text";
        int buttonsInLine = 3;
        CallbackButton[] buttons = new CallbackButton[0];
        botInnerService.askFromInlineKeyboard(updateInfo, text, buttonsInLine, buttons);

        verify(telegramBotClientService).askFromInlineKeyboard(same(updateInfo), same(text), same(buttonsInLine), same(buttons));
    }

    @Test
    public void sendMultipleObjectStringsGroupedInMessages_should_handle_to_service() {
        List objects = Mockito.mock(List.class);

        botInnerService.sendMultipleObjectStringsGroupedInMessages(objects, 1, 2L);

        verify(telegramBotClientService).sendMultipleObjectStringsGroupedInMessages(same(objects), eq(1), eq(2L));
    }

    @Test
    public void sendItemsByUserItemShowSettingsAndUserInputOffset_should_send_proper_amount_of_messages_if_few_in_message() {

    }

    @Test
    public void deleteMessage_should_handle_to_service() {
        UpdateInfo updateInfo = new UpdateInfo();
        botInnerService.deleteMessage(updateInfo);
        verify(telegramBotClientService).deleteMessage(same(updateInfo));
    }

    @Test
    public void registerUser_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.registerUser(chatId);
        verify(telegramUserService).registerTelegramUser(chatId);
    }

    @Test
    public void isUserRegistered_should_return_service_result() {
        long chatId = 1L;
        when(telegramUserService.isTelegramUserRegistered(chatId)).thenReturn(true);
        assertTrue(botInnerService.isUserRegistered(chatId));

        when(telegramUserService.isTelegramUserRegistered(chatId)).thenReturn(false);
        assertFalse(botInnerService.isUserRegistered(chatId));
    }

    @Test
    public void setUserInputState_should_handle_to_service() {
        long chatId = 1L;
        InputState inputState = InputState.ITEMS_SHOW_OFFSET;
        botInnerService.setUserInputState(chatId, inputState);
        verify(telegramUserService).setUserInputState(chatId, inputState);
    }

    @Test
    public void saveUserInput_should_save_InputAndSet_input_State_from_message_text() {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setChatId(1L);
        updateInfo.setInputState(InputState.UBI_ACCOUNT_ENTRY_EMAIL);
        updateInfo.setInputGroup(InputGroup.UBI_ACCOUNT_ENTRY_LINK);
        updateInfo.setMessageText("text");
        updateInfo.setHasMessage(true);

        botInnerService.saveUserInput(updateInfo);

        verify(telegramUserService).saveUserInput(updateInfo.getChatId(), updateInfo.getInputState(), updateInfo.getMessageText());
    }

    @Test
    public void saveUserInput_should_save_InputAndSet_input_State_from_callback_data() {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setChatId(1L);
        updateInfo.setInputState(InputState.UBI_ACCOUNT_ENTRY_EMAIL);
        updateInfo.setInputGroup(InputGroup.UBI_ACCOUNT_ENTRY_LINK);
        updateInfo.setCallbackQueryData("data");
        updateInfo.setHasCallBackQuery(true);

        botInnerService.saveUserInput(updateInfo);

        verify(telegramUserService).saveUserInput(updateInfo.getChatId(), updateInfo.getInputState(), updateInfo.getCallbackQueryData());
    }

    @Test
    public void saveUserInput_State_should_throw_if_no_text_or_data_provided() {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setChatId(1L);
        updateInfo.setInputState(InputState.UBI_ACCOUNT_ENTRY_EMAIL);
        updateInfo.setInputGroup(InputGroup.UBI_ACCOUNT_ENTRY_LINK);

        assertThrows(InvalidTelegramUserInputException.class, () -> botInnerService.saveUserInput(updateInfo));
    }

    @Test
    public void clearUserInputs_AndSetInputStateAndGroup_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.clearUserInputsAndSetInputStateAndGroup(chatId, InputState.ITEM_FILTER_NAME, InputGroup.ITEMS_SHOW);
        verify(telegramUserService).clearUserInputsAndSetInputStateAndGroup(chatId, InputState.ITEM_FILTER_NAME, InputGroup.ITEMS_SHOW);
    }

    @Test
    public void getUserInputByState_should_return_service_result() {
        when(telegramUserService.getUserInputByState(1L, InputState.UBI_ACCOUNT_ENTRY_EMAIL)).thenReturn("input");
        assertEquals("input", botInnerService.getUserInputByState(1L, InputState.UBI_ACCOUNT_ENTRY_EMAIL));
    }

    @Test
    public void getStringOfAllTagsNamesByTagGroup_should_return_service_result() {
        TagGroup tagGroup = TagGroup.Weapon;

        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("value1", "name1", TagGroup.Weapon));
        tags.add(new Tag("value2", "name2", TagGroup.Weapon));
        when(tagService.getAllTagsByTagGroup(tagGroup)).thenReturn(tags);

        assertEquals("name1,name2", botInnerService.getStringOfAllTagsNamesByTagGroup(TagGroup.Weapon));
    }

    @Test
    public void saveUserItemFilterByUserInput_should_generate_item_filter_from_inputs_and_handle_to_service() {
        ItemFilter filter = Mockito.mock(ItemFilter.class);
        when(botInnerService.generateItemFilterByUserInput(1L)).thenReturn(filter);

        botInnerService.saveUserItemFilterByUserInput(1L);

        verify(itemFilterService).save(eq("1"), same(filter));
    }

    @Test
    public void generateItemFilterByUserInput_should_return_filter_with_name_and_rarity() {
        List<TelegramUserInput> inputs = Mockito.mock(List.class);
        ItemFilter filter = Mockito.mock(ItemFilter.class);
        when(telegramUserService.getAllUserInputs(1L)).thenReturn(inputs);
        when(itemFilterFromInputsMapper.generateItemFilterByUserInput(same(inputs))).thenReturn(filter);

        assertSame(filter, botInnerService.generateItemFilterByUserInput(1L));
    }

    @Test
    public void getAllUserItemFilterNames_should_return_service_result() {
        List list = Mockito.mock(List.class);
        when(itemFilterService.getAllNamesByChatId("1")).thenReturn(list);

        assertSame(list, botInnerService.getAllUserItemFiltersNames(1L));
    }

    @Test
    public void getUserItemFilterByUserCurrentInputCallbackFilterName_should_return_service_result() {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setChatId(1L);
        updateInfo.setHasCallBackQuery(true);
        updateInfo.setCallbackQueryData(Callbacks.INPUT_CALLBACK_PREFIX + "filter_name");
        ItemFilter filter = Mockito.mock(ItemFilter.class);
        when(itemFilterService.getById("1", "filter_name")).thenReturn(filter);

        assertSame(filter, botInnerService.getUserItemFilterByUserCurrentInputCallbackFilterName(updateInfo));
    }

    @Test
    public void removeUserItemFilterByUserInputCallbackFilterName_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.ITEM_FILTER_NAME)).thenReturn(Callbacks.INPUT_CALLBACK_PREFIX + "filter_name");

        botInnerService.removeUserItemFilterByUserInputCallbackFilterName(1L);

        verify(itemFilterService).deleteById("1", "filter_name");
    }

    @Test
    public void getItemShowAppliedFiltersNames_should_return_service_result() {
        List list = Mockito.mock(List.class);
        when(telegramUserService.getUserItemShowAppliedFiltersNames(1L)).thenReturn(list);

        assertSame(list, botInnerService.getItemShowAppliedFiltersNames(1L));
    }

    @Test
    public void getUserItemShowSettings_should_return_service_result() {
        ItemShowSettings settings = Mockito.mock(ItemShowSettings.class);
        when(telegramUserService.getUserItemShowSettings(1L)).thenReturn(settings);
        assertSame(settings, botInnerService.getUserItemShowSettings(1L));
    }

    @Test
    public void setUserItemShowSettingsFewItemsInMessageFlag_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.setUserItemShowSettingsFewItemsInMessageFlag(chatId, true);
        verify(telegramUserService).setUserItemShowFewItemsInMessageFlag(chatId, true);

        reset(telegramUserService);

        botInnerService.setUserItemShowSettingsFewItemsInMessageFlag(chatId, false);
        verify(telegramUserService).setUserItemShowFewItemsInMessageFlag(chatId, false);
    }

    @Test
    public void setUserItemShowSettingsMessageLimitOrEdgeValueByUserInput_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.setUserItemShowSettingsMessageLimitOrEdgeValueByUserInput(chatId);
        verify(telegramUserService).setUserItemShowMessagesLimitByUserInput(chatId);
    }

    @Test
    public void setUserItemShownFieldsSettingByUserInput_should_handle_to_service() {
        botInnerService.setUserItemShownFieldsSettingByUserInput(1L);

        verify(telegramUserService).setUserItemShownFieldsSettingsByUserInput(1L, Callbacks.INPUT_CALLBACK_TRUE, Callbacks.INPUT_CALLBACK_FALSE);
    }

    @Test
    public void addItemShowAppliedFilterByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.ITEM_FILTER_NAME)).thenReturn(Callbacks.INPUT_CALLBACK_PREFIX + "filter_name");

        botInnerService.addItemShowAppliedFilterByUserInput(1L);

        verify(telegramUserService).addUserItemShowAppliedFilter(1L, "filter_name");
    }

    @Test
    public void deleteItemShowAppliedFilterByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.ITEM_FILTER_NAME)).thenReturn(Callbacks.INPUT_CALLBACK_PREFIX + "filter_name");

        botInnerService.deleteItemShowAppliedFilterByUserInput(1L);

        verify(telegramUserService).removeUserItemShowAppliedFilter(1L, "filter_name");
    }

    @Test
    public void saveUserTradeByItemIdManagerByUserInput_should_handle_to_service() {
        TradeByItemIdManager manager = Mockito.mock(TradeByItemIdManager.class);
        TradeManagersSettings settings = new TradeManagersSettings(true, true, true, "expression1", true, "expression2");

        when(telegramUserService.getUserTradeManagersSettings(1L)).thenReturn(settings);
        doReturn(manager).when(botInnerService).generateTradeByItemIdManagerByUserInput(1L, TradeOperationType.SELL);

        botInnerService.saveUserTradeByItemIdManagerByUserInput(1L, TradeOperationType.SELL);

        verify(tradeManagerService).saveTradeByItemIdManager(eq("1"), same(manager));
    }

    @Test
    public void saveUserTradeByFiltersManagerByUserInput_should_handle_to_service() {
        TradeByFiltersManager manager = Mockito.mock(TradeByFiltersManager.class);
        TradeManagersSettings settings = new TradeManagersSettings(true, true, true, "expression1", true, "expression2");
        when(telegramUserService.getUserTradeManagersSettings(1L)).thenReturn(settings);
        doReturn(manager).when(botInnerService).generateTradeByFiltersManagerByUserInput(1L);

        botInnerService.saveUserTradeByFiltersManagerByUserInput(1L);

        verify(tradeManagerService).saveTradeByFiltersManager(eq("1"), same(manager));
    }

    @Test
    public void generateTradeByItemIdManagerByUserInput_ItemId_should_return_service_result() {
        List inputs = Mockito.mock(List.class);
        when(telegramUserService.getAllUserInputs(1L)).thenReturn(inputs);

        TradeManagersSettings settings = new TradeManagersSettings(true, true, true, "expression1", false, "expression2");
        doReturn(settings).when(botInnerService).getUserTradeManagersSettings(1L);

        TradeByItemIdManager tradeManager = Mockito.mock(TradeByItemIdManager.class);
        when(tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(same(inputs), eq(TradeOperationType.BUY), eq(true))).thenReturn(tradeManager);

        assertSame(tradeManager, botInnerService.generateTradeByItemIdManagerByUserInput(1L, TradeOperationType.BUY));
    }

    @Test
    public void generateTradeByFiltersManagerByUserInput_ItemId_should_return_service_result() {
        List inputs = Mockito.mock(List.class);
        when(telegramUserService.getAllUserInputs(1L)).thenReturn(inputs);

        TradeManagersSettings settings = new TradeManagersSettings(true, true, true, "expression1", false, "expression2");
        doReturn(settings).when(botInnerService).getUserTradeManagersSettings(1L);

        TradeByFiltersManager tradeManager = Mockito.mock(TradeByFiltersManager.class);
        when(tradeManagerFromInputsMapper.generateTradeByFiltersManagerByUserInput(same(inputs), eq(true))).thenReturn(tradeManager);

        assertSame(tradeManager, botInnerService.generateTradeByFiltersManagerByUserInput(1L));
    }

    @Test
    public void getItemByUserInputItemId_should_return_service_result() {
        Item item = new Item();
        item.setName("item_name");

        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID)).thenReturn("item_id");
        when(itemService.getItemById("item_id")).thenReturn(item);

        assertSame(botInnerService.getItemByUserInputItemId(1L), item);
    }

    @Test
    public void invertUserTradeByFiltersManagerEnabledByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_FILTERS_MANAGER_NAME)).thenReturn("name");

        botInnerService.invertUserTradeByFiltersManagerEnabledByUserInput(1L);

        verify(tradeManagerService).invertTradeByFiltersManagerEnabledFlagById("1", "name");
    }

    @Test
    public void invertUserTradeByItemIdManagerEnabledByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID)).thenReturn("item_id");

        botInnerService.invertUserTradeByItemIdManagerEnabledByUserInput(1L);

        verify(tradeManagerService).invertTradeByItemIdManagerEnabledFlagById("1", "item_id");
    }

    @Test
    public void removeUserTradeByItemIdManagerByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID)).thenReturn("item_id");

        botInnerService.removeUserTradeByItemIdManagerByUserInput(1L);

        verify(tradeManagerService).deleteTradeByItemIdManagerById("1", "item_id");
    }

    @Test
    public void removeUserTradeByFiltersManagerByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_FILTERS_MANAGER_NAME)).thenReturn("name");

        botInnerService.removeUserTradeByFiltersManagerByUserInput(1L);

        verify(tradeManagerService).deleteTradeByFiltersManagerById("1", "name");
    }

    @Test
    public void getUserTradeByItemIdManagerByUserInputItemId_should_return_service_result() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("item_id");
        tradeManager.setPriorityMultiplier(100);
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID)).thenReturn("item_id");
        when(tradeManagerService.getTradeByItemIdManagerById("1", "item_id")).thenReturn(tradeManager);

        assertEquals(tradeManager, botInnerService.getUserTradeByItemIdManagerByUserInputItemId(1L));
    }

    @Test
    public void getUserTradeByFiltersManagerByUserInputItemId_should_return_service_result() {
        TradeByFiltersManager tradeManager = new TradeByFiltersManager();
        tradeManager.setName("name");
        tradeManager.setPriorityMultiplier(100);
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_BY_FILTERS_MANAGER_NAME)).thenReturn("name");
        when(tradeManagerService.getTradeByFiltersManagerById("1", "name")).thenReturn(tradeManager);

        assertEquals(tradeManager, botInnerService.getUserTradeByFiltersManagerByUserInputName(1L));
    }

    @Test
    public void getAllUserTradeByItemIdManagers_should_return_service_result() {
        List<TradeByItemIdManager> tradeManagers = new ArrayList<>();
        tradeManagers.add(new TradeByItemIdManager());

        when(tradeManagerService.getAllTradeByItemIdManagersByChatId("1")).thenReturn(tradeManagers);

        assertEquals(tradeManagers, botInnerService.getAllUserTradeByItemIdManagers(1L));
    }

    @Test
    public void getAllUserTradeByFiltersManagers_should_return_service_result() {
        List<TradeByFiltersManager> tradeManagers = new ArrayList<>();
        tradeManagers.add(new TradeByFiltersManager());

        when(tradeManagerService.getAllTradeByFiltersManagersByChatId("1")).thenReturn(tradeManagers);

        assertEquals(tradeManagers, botInnerService.getAllUserTradeByFiltersManagers(1L));
    }

    @Test
    public void getUserTradeManagersSettings_should_return_service_result() {
        TradeManagersSettings settings = new TradeManagersSettings();
        settings.setNewManagersAreActiveFlag(true);
        settings.setManagingEnabledFlag(false);
        when(telegramUserService.getUserTradeManagersSettings(1L)).thenReturn(settings);
        assertEquals(settings, botInnerService.getUserTradeManagersSettings(1L));
    }

    @Test
    public void setUserTradeManagersSettingsNewManagersAreActiveFlag_should_handle_to_service_true_flag() {
        botInnerService.setUserTradeManagersSettingsNewManagersAreActiveFlag(1L, true);
        verify(telegramUserService).setUserTradeManagersSettingsNewManagersAreActiveFlag(1L, true);
    }

    @Test
    public void setUserTradeManagersSettingsNewManagersAreActiveFlag_should_handle_to_service_false_flag() {
        botInnerService.setUserTradeManagersSettingsNewManagersAreActiveFlag(1L, false);
        verify(telegramUserService).setUserTradeManagersSettingsNewManagersAreActiveFlag(1L, false);
    }

    @Test
    public void setUserTradeManagersSettingsManagingEnabledFlag_should_handle_to_service_true_flag() {
        botInnerService.setUserTradeManagersSettingsManagingEnabledFlag(1L, true);
        verify(telegramUserService).setUserTradeManagersSettingsManagingEnabledFlag(1L, true);
    }

    @Test
    public void setUserTradeManagersSettingsManagingEnabledFlag_should_handle_to_service_false_flag() {
        botInnerService.setUserTradeManagersSettingsManagingEnabledFlag(1L, false);
        verify(telegramUserService).setUserTradeManagersSettingsManagingEnabledFlag(1L, false);
    }

    @Test
    public void setUserTradeManagersSellSettingsManagingEnabledFlag_should_handle_to_service_true_flag() {
        botInnerService.setUserTradeManagersSellSettingsManagingEnabledFlag(1L, true);
        verify(telegramUserService).setUserTradeManagersSellSettingsManagingEnabledFlag(1L, true);
    }

    @Test
    public void setUserTradeManagersSellSettingsManagingEnabledFlag_should_handle_to_service_false_flag() {
        botInnerService.setUserTradeManagersSellSettingsManagingEnabledFlag(1L, false);
        verify(telegramUserService).setUserTradeManagersSellSettingsManagingEnabledFlag(1L, false);
    }

    @Test
    public void setUserTradeManagersBuySettingsManagingEnabledFlag_should_handle_to_service_true_flag() {
        botInnerService.setUserTradeManagersBuySettingsManagingEnabledFlag(1L, true);
        verify(telegramUserService).setUserTradeManagersBuySettingsManagingEnabledFlag(1L, true);
    }

    @Test
    public void setUserTradeManagersBuySettingsManagingEnabledFlag_should_handle_to_service_false_flag() {
        botInnerService.setUserTradeManagersBuySettingsManagingEnabledFlag(1L, false);
        verify(telegramUserService).setUserTradeManagersBuySettingsManagingEnabledFlag(1L, false);
    }

    @Test
    public void setUserTradeManagersSellSettingsTradePriorityExpressionByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_MANAGERS_SETTINGS_TRADE_PRIORITY_EXPRESSION)).thenReturn("expression");

        botInnerService.setUserTradeManagersSellSettingsTradePriorityExpressionByUserInput(1L);

        verify(telegramUserService).setUserTradeManagersSellSettingsTradePriorityExpression(1L, "expression");
    }

    @Test
    public void setUserTradeManagersBuySettingsTradePriorityExpressionByUserInput_should_handle_to_service() {
        when(telegramUserService.getUserInputByState(1L, InputState.TRADE_MANAGERS_SETTINGS_TRADE_PRIORITY_EXPRESSION)).thenReturn("expression");

        botInnerService.setUserTradeManagersBuySettingsTradePriorityExpressionByUserInput(1L);

        verify(telegramUserService).setUserTradeManagersBuySettingsTradePriorityExpression(1L, "expression");
    }

    @Test
    public void invertUserPrivateNotificationsFlag_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.invertUserPrivateNotificationsFlag(chatId);
        verify(telegramUserService).invertUserPrivateNotificationsFlag(chatId);
    }

    @Test
    public void invertUserPublicNotificationsFlag_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.invertUserPublicNotificationsFlag(chatId);
        verify(telegramUserService).invertUserPublicNotificationsFlag(chatId);
    }

    @Test
    public void invertUserUbiStatsUpdatedNotificationsFlag_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.invertUserUbiStatsUpdatedNotificationsFlag(chatId);
        verify(telegramUserService).invertUserUbiStatsUpdatedNotificationsFlag(chatId);
    }

    @Test
    public void invertUserTradeManagerNotificationsFlag_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.invertUserTradeManagerNotificationsFlag(chatId);
        verify(telegramUserService).invertUserTradeManagerNotificationsFlag(chatId);
    }

    @Test
    public void invertUserAuthorizationNotificationsFlag_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.invertUserAuthorizationNotificationsFlag(chatId);
        verify(telegramUserService).invertUserAuthorizationNotificationsFlag(chatId);
    }

    @Test
    public void getUserNotificationsSettings_should_return_service_result() {
        NotificationsSettings settings = mock(NotificationsSettings.class);

        when(telegramUserService.getUserNotificationsSettings(1L)).thenReturn(settings);

        assertSame(settings, botInnerService.getUserNotificationsSettings(1L));
    }

    @Test
    public void addUserUbiAccountEntryByUserInput_should_get_both_email_and_password_from_two_inputs_if_conditions_doesnt_met() {
        botInnerService.addUserUbiAccountEntryByUserInput(1L);
        verify(telegramUserService).authorizeAndSaveUbiUserByUserInput(1L);
    }

    @Test
    public void reauthorizeUbiAccountEntryBy2FACode_should_handle_to_service() {
        botInnerService.reauthorizeUbiAccountEntryBy2FACode(1L);
        verify(telegramUserService).reauthorizeAndSaveExistingUbiUserBy2FACodeByUserInput(1L);
    }

    @Test
    public void removeUserUbiAccountEntry_should_handle_to_service() {
        long chatId = 1L;
        botInnerService.removeUserUbiAccountEntry(chatId);
        verify(telegramUserService).removeUbiUserByChatId(chatId);
    }

    @Test
    public void getUserUbiAccountEntryEmail_should_return_service_result() {
        String email = "Email";
        UbiAccountAuthorizationEntry entry = new UbiAccountAuthorizationEntry();
        entry.setEmail(email);
        when(telegramUserService.getUbiUserByChatId(1L)).thenReturn(entry);

        assertEquals(email, botInnerService.getUserUbiAccountEntryEmail(1L));
    }

    @Test
    public void isTradePriorityExpressionValid_should_return_service_result() {
        when(tradePriorityExpressionDeserializer.isValidExpression("1")).thenReturn(true);

        assertTrue(botInnerService.isTradePriorityExpressionValid("1"));

        when(tradePriorityExpressionDeserializer.isValidExpression("2")).thenReturn(false);

        assertFalse(botInnerService.isTradePriorityExpressionValid("2"));
    }
}
