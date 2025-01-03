package github.ricemonger.marketplace.databases.postgres.entities.user;

import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.ItemFilterEntityMapper;
import github.ricemonger.telegramBot.InputGroup;
import github.ricemonger.telegramBot.InputState;
import github.ricemonger.utils.DTOs.personal.ItemShownFieldsSettings;
import github.ricemonger.utils.DTOs.personal.TelegramUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TelegramUserEntityTest {

    @MockBean
    private ItemFilterEntityMapper itemFilterEntityMapper;

    @Test
    public void isEqual_should_return_true_if_same() {
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        assertTrue(telegramUser.isEqual(telegramUser));
    }

    @Test
    public void isEqual_should_return_true_if_equal_ids() {
        TelegramUserEntity telegramUser1 = new TelegramUserEntity();
        telegramUser1.setUser(new UserEntity(1L));
        telegramUser1.setChatId("chatId");
        telegramUser1.setInputState(InputState.BASE);
        telegramUser1.setInputGroup(InputGroup.BASE);
        telegramUser1.setItemShowMessagesLimit(50);
        telegramUser1.setItemShowFewInMessageFlag(false);
        telegramUser1.setTelegramUserInputs(List.of(new TelegramUserInputEntity(telegramUser1, InputState.BASE, "value")));

        TelegramUserEntity telegramUser2 = new TelegramUserEntity();
        telegramUser2.setUser(new UserEntity(1L));
        telegramUser2.setChatId("chatId");
        telegramUser2.setInputState(InputState.UBI_ACCOUNT_ENTRY_2FA_CODE);
        telegramUser2.setInputGroup(InputGroup.ITEM_FILTER_EDIT);
        telegramUser2.setItemShowMessagesLimit(51);
        telegramUser2.setItemShowFewInMessageFlag(true);
        telegramUser2.setTelegramUserInputs(List.of());

        assertTrue(telegramUser1.isEqual(telegramUser2));
    }

    @Test
    public void isEqual_should_return_false_for_null() {
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        assertFalse(telegramUser.isEqual(null));
    }

    @Test
    public void isEqual_should_return_false_if_different_ids() {
        TelegramUserEntity telegramUser1 = new TelegramUserEntity();
        telegramUser1.setUser(new UserEntity(1L));
        telegramUser1.setChatId("chatId");

        TelegramUserEntity telegramUser2 = new TelegramUserEntity();
        telegramUser2.setUser(new UserEntity(1L));
        telegramUser2.setChatId("chatId");


        telegramUser1.setUser(new UserEntity(2L));
        assertFalse(telegramUser1.isEqual(telegramUser2));
        telegramUser1.setUser(new UserEntity(1L));
        telegramUser1.setChatId("chatId2");
        assertFalse(telegramUser1.isEqual(telegramUser2));
    }

    @Test
    public void constructor_should_set_id_fields() {
        TelegramUserEntity telegramUser = new TelegramUserEntity("chatId", 2L);
        assertEquals("chatId", telegramUser.getChatId());
        assertEquals(2L, telegramUser.getUserId_());
    }

    @Test
    public void getUserId_should_return_user_idField() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        telegramUser.setUser(user);
        assertEquals(1L, telegramUser.getUserId_());
    }

    @Test
    public void setShowItemFieldsSettings_should_set_fields_from_dto() {
        ItemShownFieldsSettings settings = new ItemShownFieldsSettings();
        settings.setItemShowNameFlag(true);
        settings.setItemShowItemTypeFlag(true);
        settings.setItemShowMaxBuyPrice(true);
        settings.setItemShowBuyOrdersCountFlag(true);
        settings.setItemShowMinSellPriceFlag(true);
        settings.setItemsShowSellOrdersCountFlag(true);
        settings.setItemShowPictureFlag(true);

        UserEntity user = new UserEntity();
        user.setItemShowNameFlag(false);
        user.setItemShowItemTypeFlag(false);
        user.setItemShowMaxBuyPrice(false);
        user.setItemShowBuyOrdersCountFlag(false);
        user.setItemShowMinSellPriceFlag(false);
        user.setItemsShowSellOrdersCountFlag(false);
        user.setItemShowPictureFlag(false);
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        telegramUser.setUser(user);
        telegramUser.setShowItemFieldsSettings(settings);

        assertTrue(user.getItemShowNameFlag());
        assertTrue(user.getItemShowItemTypeFlag());
        assertTrue(user.getItemShowMaxBuyPrice());
        assertTrue(user.getItemShowBuyOrdersCountFlag());
        assertTrue(user.getItemShowMinSellPriceFlag());
        assertTrue(user.getItemsShowSellOrdersCountFlag());
        assertTrue(user.getItemShowPictureFlag());
    }

    @Test
    public void getItemShowAppliedFilters_should_return_item_show_applied_filters() {
        UserEntity user = new UserEntity();
        ItemFilterEntity itemFilterEntity = new ItemFilterEntity();
        user.setItemShowAppliedFilters(List.of(itemFilterEntity));
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        telegramUser.setUser(user);
        assertEquals(List.of(itemFilterEntity), telegramUser.getItemShowAppliedFilters());
    }

    @Test
    public void setItemShowAppliedFilters_should_set_item_show_applied_filters() {
        UserEntity user = new UserEntity();
        ItemFilterEntity itemFilterEntity = new ItemFilterEntity();
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        telegramUser.setUser(user);
        telegramUser.setItemShowAppliedFilters(List.of(itemFilterEntity));
        assertEquals(List.of(itemFilterEntity), user.getItemShowAppliedFilters());
    }

    @Test
    public void setNewManagersAreActiveFlag_should_set_new_managers_are_active_flag() {
        UserEntity user = new UserEntity();
        user.setNewManagersAreActiveFlag(false);
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        telegramUser.setUser(user);
        telegramUser.setNewManagersAreActiveFlag_(true);
        assertTrue(telegramUser.getUser().getNewManagersAreActiveFlag());
    }

    @Test
    public void setManagingEnabledFlag_should_set_managing_enabled_flag() {
        UserEntity user = new UserEntity();
        user.setManagingEnabledFlag(false);
        TelegramUserEntity telegramUser = new TelegramUserEntity();
        telegramUser.setUser(user);
        telegramUser.setManagingEnabledFlag_(true);
        assertTrue(telegramUser.getUser().getManagingEnabledFlag());
    }

    @Test
    public void setFields_should_set_fields() {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("chatId");
        telegramUser.setInputState(InputState.UBI_ACCOUNT_ENTRY_2FA_CODE);
        telegramUser.setInputGroup(InputGroup.ITEMS_SHOW);
        telegramUser.setPublicNotificationsEnabledFlag(false);
        telegramUser.setItemShowMessagesLimit(51);
        telegramUser.setItemShowFewInMessageFlag(true);
        telegramUser.setItemShowNameFlag(false);
        telegramUser.setItemShowItemTypeFlag(false);
        telegramUser.setItemShowMaxBuyPrice(false);
        telegramUser.setItemShowBuyOrdersCountFlag(false);
        telegramUser.setItemShowMinSellPriceFlag(false);
        telegramUser.setItemsShowSellOrdersCountFlag(false);
        telegramUser.setItemShowPictureFlag(false);
        telegramUser.setItemShowAppliedFilters(List.of());
        telegramUser.setNewManagersAreActiveFlag(false);
        telegramUser.setManagingEnabledFlag(false);

        TelegramUserEntity telegramUserEntity = new TelegramUserEntity();
        telegramUserEntity.setUser(new UserEntity());
        telegramUserEntity.setFields(telegramUser, itemFilterEntityMapper);

        assertEquals("chatId", telegramUserEntity.getChatId());
        assertEquals(InputState.UBI_ACCOUNT_ENTRY_2FA_CODE, telegramUserEntity.getInputState());
        assertEquals(InputGroup.ITEMS_SHOW, telegramUserEntity.getInputGroup());
        assertFalse(telegramUserEntity.getUser().getPublicNotificationsEnabledFlag());
        assertEquals(51, telegramUserEntity.getItemShowMessagesLimit());
        assertTrue(telegramUserEntity.getItemShowFewInMessageFlag());
        assertFalse(telegramUserEntity.getUser().getItemShowNameFlag());
        assertFalse(telegramUserEntity.getUser().getItemShowItemTypeFlag());
        assertFalse(telegramUserEntity.getUser().getItemShowMaxBuyPrice());
        assertFalse(telegramUserEntity.getUser().getItemShowBuyOrdersCountFlag());
        assertFalse(telegramUserEntity.getUser().getItemShowMinSellPriceFlag());
        assertFalse(telegramUserEntity.getUser().getItemsShowSellOrdersCountFlag());
        assertFalse(telegramUserEntity.getUser().getItemShowPictureFlag());
        assertTrue(telegramUserEntity.getUser().getItemShowAppliedFilters().isEmpty());
        assertFalse(telegramUserEntity.getUser().getNewManagersAreActiveFlag());
        assertFalse(telegramUserEntity.getUser().getManagingEnabledFlag());
    }

    @Test
    public void isFullyEqualExceptUser_should_return_true_if_equal_() {
        TelegramUserEntity telegramUser1 = new TelegramUserEntity();
        telegramUser1.setUser(new UserEntity(1L));
        telegramUser1.setChatId("chatId");
        telegramUser1.setInputState(InputState.BASE);
        telegramUser1.setInputGroup(InputGroup.BASE);
        telegramUser1.setItemShowMessagesLimit(50);
        telegramUser1.setItemShowFewInMessageFlag(false);
        telegramUser1.setTelegramUserInputs(List.of(new TelegramUserInputEntity(telegramUser1, InputState.BASE, "value")));

        TelegramUserEntity telegramUser2 = new TelegramUserEntity();
        telegramUser2.setUser(new UserEntity(1L));
        telegramUser2.setChatId("chatId");
        telegramUser2.setInputState(InputState.BASE);
        telegramUser2.setInputGroup(InputGroup.BASE);
        telegramUser2.setItemShowMessagesLimit(50);
        telegramUser2.setItemShowFewInMessageFlag(false);
        telegramUser2.setTelegramUserInputs(List.of(new TelegramUserInputEntity(telegramUser2, InputState.BASE, "value")));

        assertTrue(telegramUser1.isFullyEqual(telegramUser2));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_false_if_not_equal_() {
        TelegramUserEntity telegramUser1 = new TelegramUserEntity();
        telegramUser1.setUser(new UserEntity(1L));
        telegramUser1.setChatId("chatId");
        telegramUser1.setInputState(InputState.BASE);
        telegramUser1.setInputGroup(InputGroup.BASE);
        telegramUser1.setItemShowMessagesLimit(50);
        telegramUser1.setItemShowFewInMessageFlag(false);
        telegramUser1.setTelegramUserInputs(List.of(new TelegramUserInputEntity(telegramUser1, InputState.BASE, "value")));

        TelegramUserEntity telegramUser2 = new TelegramUserEntity();
        telegramUser2.setUser(new UserEntity(1L));
        telegramUser2.setChatId("chatId");
        telegramUser2.setInputState(InputState.BASE);
        telegramUser2.setInputGroup(InputGroup.BASE);
        telegramUser2.setItemShowMessagesLimit(50);
        telegramUser2.setItemShowFewInMessageFlag(false);
        telegramUser2.setTelegramUserInputs(List.of(new TelegramUserInputEntity(telegramUser2, InputState.BASE, "value")));

        telegramUser1.setUser(new UserEntity(2L));
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setUser(new UserEntity(1L));
        telegramUser1.setChatId("chatId2");
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setChatId("chatId");
        telegramUser1.setInputState(InputState.UBI_ACCOUNT_ENTRY_2FA_CODE);
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setInputState(InputState.BASE);
        telegramUser1.setInputGroup(InputGroup.ITEM_FILTER_EDIT);
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setInputGroup(InputGroup.BASE);
        telegramUser1.setItemShowMessagesLimit(51);
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setItemShowMessagesLimit(50);
        telegramUser1.setItemShowFewInMessageFlag(true);
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setItemShowFewInMessageFlag(false);
        telegramUser1.setTelegramUserInputs(List.of());
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
        telegramUser1.setTelegramUserInputs(null);
        assertFalse(telegramUser1.isFullyEqual(telegramUser2));
    }
}