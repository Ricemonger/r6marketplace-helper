package github.ricemonger.telegramBot.updateReceiver.listeners;

import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.executors.ExecutorsService;
import github.ricemonger.telegramBot.executors.cancel.Cancel;
import github.ricemonger.telegramBot.executors.cancel.SilentCancel;
import github.ricemonger.telegramBot.executors.itemFilters.edit.FilterEditStage16FinishConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.itemFilters.edit.FilterEditStage1AskNameCallback;
import github.ricemonger.telegramBot.executors.itemFilters.showOrRemove.FilterRemoveStage3ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.itemFilters.showOrRemove.FiltersShowAllNamesStage1AskNameCallback;
import github.ricemonger.telegramBot.executors.items.settings.ItemsShowSettingsCallback;
import github.ricemonger.telegramBot.executors.items.settings.appliedFilters.ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback;
import github.ricemonger.telegramBot.executors.items.settings.itemsInMessage.ItemsShowSettingsChangeItemsInMessageCallback;
import github.ricemonger.telegramBot.executors.items.settings.itemsInMessage.ItemsShowSettingsChangeItemsInMessageNoCallback;
import github.ricemonger.telegramBot.executors.items.settings.itemsInMessage.ItemsShowSettingsChangeItemsInMessageYesCallback;
import github.ricemonger.telegramBot.executors.items.settings.messageLimit.ItemsShowSettingsChangeMessageLimitCallback;
import github.ricemonger.telegramBot.executors.items.settings.shownFields.ItemsShowSettingsChangeShownFieldsStage1AskNameFlagCallback;
import github.ricemonger.telegramBot.executors.items.show.ItemsShowStage1AskOffsetCallback;
import github.ricemonger.telegramBot.executors.start.startYes.StartYesCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.TradeManagersEditAskManagerTypeCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.itemFilter.TradeByFiltersManagerEditStage1AskNameCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.itemFilter.TradeByFiltersManagerEditStage8ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.TradeByItemIdManagerEditAskTradeTypeCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.buy.TradeByItemIdManagerBuyEditStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.buy.TradeByItemIdManagerBuyEditStage5ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.buyAndSell.TradeByItemIdManagerBuyAndSellEditStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.buyAndSell.TradeByItemIdManagerBuyAndSellEditStage6ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.sell.TradeByItemIdManagerSellEditStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.oneItem.sell.TradeByItemIdManagerSellEditStage5ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.TradeManagersSettingsCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.managingEnabledFlag.TradeManagersSettingsChangeManagingEnabledFlagAskFlagCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.managingEnabledFlag.TradeManagersSettingsChangeManagingEnabledFlagNoCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.managingEnabledFlag.TradeManagersSettingsChangeManagingEnabledFlagYesCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.newManagersAreActiveFlag.TradeManagersSettingsChangeNewManagersAreActiveFlagAskFlagCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.newManagersAreActiveFlag.TradeManagersSettingsChangeNewManagersAreActiveFlagNoCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.settings.newManagersAreActiveFlag.TradeManagersSettingsChangeNewManagersAreActiveFlagYesCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.TradeByFiltersManagersShowAllCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.TradeByItemIdManagersShowAllCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.TradeManagersChooseTypeCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.remove_or_change_enabled.itemFilters.TradeByFiltersManagerChangeEnabledStage3ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.remove_or_change_enabled.itemFilters.TradeByFiltersManagerRemoveOrChangeEnabledStage1AskNameCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.remove_or_change_enabled.itemFilters.TradeByFiltersManagerRemoveStage3ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.remove_or_change_enabled.itemId.TradeByItemIdManagerChangeEnabledStage3ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.remove_or_change_enabled.itemId.TradeByItemIdManagerRemoveOrChangeEnabledStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.tradeManagers.showRemoveChangeEnabled.remove_or_change_enabled.itemId.TradeByItemIdManagerRemoveStage3ConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.ubi_account_entry.link.UbiAccountEntryAuthorizeStage1AskEmailCallback;
import github.ricemonger.telegramBot.executors.ubi_account_entry.reauth_two_fa_code.UbiAccountEntryReauthorizeEnter2FACodeStage1Ask2FACodeCallback;
import github.ricemonger.telegramBot.executors.ubi_account_entry.show.UbiAccountEntryShowCallback;
import github.ricemonger.telegramBot.executors.ubi_account_entry.unlink.UbiAccountEntryUnlinkConfirmedFinishCallback;
import github.ricemonger.telegramBot.executors.ubi_account_entry.unlink.UbiAccountEntryUnlinkRequestCallback;
import github.ricemonger.utils.exceptions.server.UnexpectedCallbackCommandException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CallbackCommandListenerTest {
    @Autowired
    private CallbackCommandListener callbackCommandListener;
    @MockBean
    private ExecutorsService executorsService;

    @Test
    public void handleUpdate_should_cancel() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.CANCEL));

        verify(executorsService).execute(Cancel.class, updateInfo(Callbacks.CANCEL));
    }

    @Test
    public void handleUpdate_should_silent_cancel() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.CANCEL_SILENT));

        verify(executorsService).execute(SilentCancel.class, updateInfo(Callbacks.CANCEL_SILENT));
    }

    @Test
    public void handleUpdate_should_item_filter_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEM_FILTER_EDIT));

        verify(executorsService).execute(FilterEditStage1AskNameCallback.class, updateInfo(Callbacks.ITEM_FILTER_EDIT));
    }

    @Test
    public void handelUpdate_should_item_filter_edit_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEM_FILTER_EDIT_FINISH_CONFIRMED));

        verify(executorsService).execute(FilterEditStage16FinishConfirmedFinishCallback.class, updateInfo(Callbacks.ITEM_FILTER_EDIT_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_item_filters_show_all() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEM_FILTERS_SHOW_ALL));

        verify(executorsService).execute(FiltersShowAllNamesStage1AskNameCallback.class, updateInfo(Callbacks.ITEM_FILTERS_SHOW_ALL));
    }

    @Test
    public void handleUpdate_should_item_filter_remove_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEM_FILTER_REMOVE_FINISH_CONFIRMED));

        verify(executorsService).execute(FilterRemoveStage3ConfirmedFinishCallback.class, updateInfo(Callbacks.ITEM_FILTER_REMOVE_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_items_show_settings() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS));

        verify(executorsService).execute(ItemsShowSettingsCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS));
    }

    @Test
    public void handleUpdate_should_items_show_settings_change_applied_filters() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_APPLIED_FILTERS));

        verify(executorsService).execute(ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_APPLIED_FILTERS));
    }

    @Test
    public void handleUpdate_should_items_show_settings_change_few_items_in_message() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE));

        verify(executorsService).execute(ItemsShowSettingsChangeItemsInMessageCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE));
    }

    @Test
    public void handleUpdate_should_items_show_settings_change_few_items_in_message_callback_no() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_NO));

        verify(executorsService).execute(ItemsShowSettingsChangeItemsInMessageNoCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_NO));
    }

    @Test
    public void handleUpdate_should_items_show_settings_change_few_items_in_message_callback_yes() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_YES));

        verify(executorsService).execute(ItemsShowSettingsChangeItemsInMessageYesCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_YES));
    }

    @Test
    public void handleUpdate_should_items_show_settings_change_message_limit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_MESSAGE_LIMIT));

        verify(executorsService).execute(ItemsShowSettingsChangeMessageLimitCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_MESSAGE_LIMIT));
    }

    @Test
    public void handleUpdate_should_items_show_settings_change_shown_fields() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_SHOWN_FIELDS));

        verify(executorsService).execute(ItemsShowSettingsChangeShownFieldsStage1AskNameFlagCallback.class, updateInfo(Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_SHOWN_FIELDS));
    }

    @Test
    public void handleUpdate_should_items_show() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.ITEMS_SHOW));

        verify(executorsService).execute(ItemsShowStage1AskOffsetCallback.class, updateInfo(Callbacks.ITEMS_SHOW));
    }

    @Test
    public void handleUpdate_should_start_yes() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.START_YES));

        verify(executorsService).execute(StartYesCallback.class, updateInfo(Callbacks.START_YES));
    }

    @Test
    public void handleUpdate_should_trade_managers_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_EDIT));

        verify(executorsService).execute(TradeManagersEditAskManagerTypeCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_EDIT));
    }

    @Test
    public void handleUpdate_should_trade_by_filters_manager_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_EDIT));

        verify(executorsService).execute(TradeByFiltersManagerEditStage1AskNameCallback.class, updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_EDIT));
    }

    @Test
    public void handleUpdate_should_trade_by_filters_manager_edit_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_EDIT_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByFiltersManagerEditStage8ConfirmedFinishCallback.class, updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_EDIT_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_EDIT));

        verify(executorsService).execute(TradeByItemIdManagerEditAskTradeTypeCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_EDIT));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_type_buy_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT));

        verify(executorsService).execute(TradeByItemIdManagerBuyEditStage1AskItemIdCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_type_buy_edit_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByItemIdManagerBuyEditStage5ConfirmedFinishCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_type_sell_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT));

        verify(executorsService).execute(TradeByItemIdManagerSellEditStage1AskItemIdCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_type_sell_edit_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByItemIdManagerSellEditStage5ConfirmedFinishCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_type_buy_and_sell_edit() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT));

        verify(executorsService).execute(TradeByItemIdManagerBuyAndSellEditStage1AskItemIdCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_type_buy_and_sell_edit_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByItemIdManagerBuyAndSellEditStage6ConfirmedFinishCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS));

        verify(executorsService).execute(TradeManagersSettingsCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings_change_new_managers_are_active_flag() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG));

        verify(executorsService).execute(TradeManagersSettingsChangeNewManagersAreActiveFlagAskFlagCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings_change_new_managers_are_active_flag_no() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_NO));

        verify(executorsService).execute(TradeManagersSettingsChangeNewManagersAreActiveFlagNoCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_NO));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings_change_new_managers_are_active_flag_yes() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_YES));

        verify(executorsService).execute(TradeManagersSettingsChangeNewManagersAreActiveFlagYesCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_YES));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings_change_managing_enabled_flag() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG));

        verify(executorsService).execute(TradeManagersSettingsChangeManagingEnabledFlagAskFlagCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings_change_managing_enabled_flag_no() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_NO));

        verify(executorsService).execute(TradeManagersSettingsChangeManagingEnabledFlagNoCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_NO));
    }

    @Test
    public void handleUpdate_should_trade_managers_settings_change_managing_enabled_flag_yes() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_YES));

        verify(executorsService).execute(TradeManagersSettingsChangeManagingEnabledFlagYesCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_YES));
    }

    @Test
    public void handleUpdate_should_trade_managers_show_choose_type() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_MANAGERS_SHOW_CHOOSE_TYPE));

        verify(executorsService).execute(TradeManagersChooseTypeCallback.class, updateInfo(Callbacks.TRADE_MANAGERS_SHOW_CHOOSE_TYPE));
    }

    @Test
    public void handleUpdate_should_trade_by_filters_managers_show_all() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGERS_SHOW_ALL));

        verify(executorsService).execute(TradeByFiltersManagersShowAllCallback.class, updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGERS_SHOW_ALL));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_managers_show_all() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGERS_SHOW_ALL));

        verify(executorsService).execute(TradeByItemIdManagersShowAllCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGERS_SHOW_ALL));
    }

    @Test
    public void handleUpdate_should_trade_by_filters_manager_remove_or_change_enabled() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_REMOVE_OR_ENABLED_CHANGE));

        verify(executorsService).execute(TradeByFiltersManagerRemoveOrChangeEnabledStage1AskNameCallback.class, updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_REMOVE_OR_ENABLED_CHANGE));
    }

    @Test
    public void handleUpdate_should_trade_by_filters_manager_change_enabled_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_CHANGE_ENABLED_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByFiltersManagerChangeEnabledStage3ConfirmedFinishCallback.class,
                updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_CHANGE_ENABLED_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_by_filters_manager_remove_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_REMOVE_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByFiltersManagerRemoveStage3ConfirmedFinishCallback.class, updateInfo(Callbacks.TRADE_BY_FILTERS_MANAGER_REMOVE_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_remove_or_change_enabled() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_REMOVE_OR_ENABLED_CHANGE));

        verify(executorsService).execute(TradeByItemIdManagerRemoveOrChangeEnabledStage1AskItemIdCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_REMOVE_OR_ENABLED_CHANGE));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_change_enabled_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_CHANGE_ENABLED_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByItemIdManagerChangeEnabledStage3ConfirmedFinishCallback.class,
                updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_CHANGE_ENABLED_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_trade_by_item_id_manager_remove_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_REMOVE_FINISH_CONFIRMED));

        verify(executorsService).execute(TradeByItemIdManagerRemoveStage3ConfirmedFinishCallback.class, updateInfo(Callbacks.TRADE_BY_ITEM_ID_MANAGER_REMOVE_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_ubi_account_entry_link() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_LINK));

        verify(executorsService).execute(UbiAccountEntryAuthorizeStage1AskEmailCallback.class, updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_LINK));
    }

    @Test
    public void handleUpdate_should_ubi_account_entry_show() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_SHOW));

        verify(executorsService).execute(UbiAccountEntryShowCallback.class, updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_SHOW));
    }

    @Test
    public void handleUpdate_should_ubi_account_entry_unlink_request() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_UNLINK_REQUEST));

        verify(executorsService).execute(UbiAccountEntryUnlinkRequestCallback.class, updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_UNLINK_REQUEST));
    }

    @Test
    public void handleUpdate_should_ubi_account_entry_unlink_finish_confirmed() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_UNLINK_FINISH_CONFIRMED));

        verify(executorsService).execute(UbiAccountEntryUnlinkConfirmedFinishCallback.class, updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_UNLINK_FINISH_CONFIRMED));
    }

    @Test
    public void handleUpdate_should_ubi_account_entry_reauthorize_enter_2fa_code() {
        callbackCommandListener.handleUpdate(updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_REAUTHORIZE_2FA_CODE));

        verify(executorsService).execute(UbiAccountEntryReauthorizeEnter2FACodeStage1Ask2FACodeCallback.class, updateInfo(Callbacks.UBI_ACCOUNT_ENTRY_REAUTHORIZE_2FA_CODE));
    }

    @Test
    public void handleUpdate_should_throw() {
        assertThrows(UnexpectedCallbackCommandException.class, () -> callbackCommandListener.handleUpdate(updateInfo("unexpected")));
    }

    private UpdateInfo updateInfo(String data) {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setCallbackQueryData(data);
        return updateInfo;
    }
}