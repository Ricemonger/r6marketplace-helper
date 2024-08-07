package github.ricemonger.telegramBot.updateReceiver.listeners;

import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.executors.ExecutorsService;
import github.ricemonger.telegramBot.executors.cancel.Cancel;
import github.ricemonger.telegramBot.executors.cancel.SilentCancel;
import github.ricemonger.telegramBot.executors.credentials.add.CredentialsAddCallback;
import github.ricemonger.telegramBot.executors.credentials.remove.CredentialsRemoveAllCallback;
import github.ricemonger.telegramBot.executors.credentials.remove.CredentialsRemoveAllConfirmedCallback;
import github.ricemonger.telegramBot.executors.credentials.remove.CredentialsRemoveCallback;
import github.ricemonger.telegramBot.executors.credentials.remove.CredentialsRemoveOneCallback;
import github.ricemonger.telegramBot.executors.credentials.show.CredentialsShowCallback;
import github.ricemonger.telegramBot.executors.marketplace.filters.FiltersCallback;
import github.ricemonger.telegramBot.executors.marketplace.filters.edit.FilterEditStage18FinishCallback;
import github.ricemonger.telegramBot.executors.marketplace.filters.edit.FilterEditStage1AskNameCallback;
import github.ricemonger.telegramBot.executors.marketplace.filters.showOrRemove.FilterRemoveCallback;
import github.ricemonger.telegramBot.executors.marketplace.filters.showOrRemove.FiltersShowCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.ItemsCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.ItemsShowSettingsCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.appliedFilters.ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.itemsInMessage.ItemsShowSettingsChangeItemsInMessageCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.itemsInMessage.ItemsShowSettingsChangeItemsInMessageNoCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.itemsInMessage.ItemsShowSettingsChangeItemsInMessageYesCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.messageLimit.ItemsShowSettingsChangeMessageLimitCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.settings.shownFields.ItemsShowSettingsChangeShownFieldsStage1AskNameFlagCallback;
import github.ricemonger.telegramBot.executors.marketplace.items.show.ItemsShowStage1AskOffsetCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.TradesCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.TradesEditCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.TradesOneItemEditCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.buy.TradesOneItemBuyEditStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.buy.TradesOneItemBuyEditStage6FinishCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.buyAndSell.TradesOneItemBuyAndSellEditStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.buyAndSell.TradesOneItemBuyAndSellEditStage8FinishCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.sell.TradesOneItemSellEditStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.sell.TradesOneItemSellEditStage6FinishCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.settings.TradesSettingsCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.showRemove.TradeManagersByItemFiltersShowAllCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.showRemove.TradeManagersByItemIdShowAllCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.showRemove.TradeManagersShowCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.showRemove.remove.itemId.TradeManagersByItemIdRemoveStage1AskItemIdCallback;
import github.ricemonger.telegramBot.executors.marketplace.tradeManagers.showRemove.remove.itemId.TradeManagersByItemIdRemoveStage3FinishCallback;
import github.ricemonger.telegramBot.executors.start.startYes.StartYesCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackCommandListener {

    private final ExecutorsService executorsService;

    public void handleUpdate(UpdateInfo updateInfo) {

        String data = updateInfo.getCallbackQueryData();

        switch (data) {
            case Callbacks.START_YES -> executorsService.execute(StartYesCallback.class, updateInfo);

            case Callbacks.CREDENTIALS_ADD -> executorsService.execute(CredentialsAddCallback.class, updateInfo);

            case Callbacks.CREDENTIALS_REMOVE -> executorsService.execute(CredentialsRemoveCallback.class, updateInfo);

            case Callbacks.CREDENTIALS_REMOVE_ALL -> executorsService.execute(CredentialsRemoveAllCallback.class, updateInfo);

            case Callbacks.CREDENTIALS_REMOVE_ALL_CONFIRMED -> executorsService.execute(CredentialsRemoveAllConfirmedCallback.class, updateInfo);

            case Callbacks.CREDENTIALS_REMOVE_ONE -> executorsService.execute(CredentialsRemoveOneCallback.class, updateInfo);

            case Callbacks.CREDENTIALS_SHOW -> executorsService.execute(CredentialsShowCallback.class, updateInfo);

            case Callbacks.FILTERS -> executorsService.execute(FiltersCallback.class, updateInfo);

            case Callbacks.FILTER_EDIT -> executorsService.execute(FilterEditStage1AskNameCallback.class, updateInfo);

            case Callbacks.FILTER_EDIT_FINISH -> executorsService.execute(FilterEditStage18FinishCallback.class, updateInfo);

            case Callbacks.FILTERS_SHOW -> executorsService.execute(FiltersShowCallback.class, updateInfo);

            case Callbacks.FILTER_REMOVE_FINISH -> executorsService.execute(FilterRemoveCallback.class, updateInfo);

            case Callbacks.ITEMS -> executorsService.execute(ItemsCallback.class, updateInfo);

            case Callbacks.ITEMS_SHOW -> executorsService.execute(ItemsShowStage1AskOffsetCallback.class, updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS -> executorsService.execute(ItemsShowSettingsCallback.class, updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE ->
                    executorsService.execute(ItemsShowSettingsChangeItemsInMessageCallback.class, updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_YES ->
                    executorsService.execute(ItemsShowSettingsChangeItemsInMessageYesCallback.class, updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_NO ->
                    executorsService.execute(ItemsShowSettingsChangeItemsInMessageNoCallback.class,
                            updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_MESSAGE_LIMIT ->
                    executorsService.execute(ItemsShowSettingsChangeMessageLimitCallback.class, updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_SHOWN_FIELDS ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage1AskNameFlagCallback.class,
                            updateInfo);

            case Callbacks.ITEMS_SHOW_SETTINGS_CHANGE_APPLIED_FILTERS ->
                    executorsService.execute(ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback.class, updateInfo);

            case Callbacks.TRADES -> executorsService.execute(TradesCallback.class, updateInfo);

            case Callbacks.TRADE_EDIT -> executorsService.execute(TradesEditCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM -> executorsService.execute(TradesOneItemEditCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM_TYPE_BUY -> executorsService.execute(TradesOneItemBuyEditStage1AskItemIdCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM_BUY_FINISH -> executorsService.execute(TradesOneItemBuyEditStage6FinishCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM_TYPE_SELL -> executorsService.execute(TradesOneItemSellEditStage1AskItemIdCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM_SELL_FINISH -> executorsService.execute(TradesOneItemSellEditStage6FinishCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM_TYPE_BUY_AND_SELL ->
                    executorsService.execute(TradesOneItemBuyAndSellEditStage1AskItemIdCallback.class, updateInfo);

            case Callbacks.TRADES_EDIT_ONE_ITEM_BUY_AND_SELL_FINISH -> executorsService.execute(TradesOneItemBuyAndSellEditStage8FinishCallback.class,
                    updateInfo);

            case Callbacks.TRADES_SETTINGS -> executorsService.execute(TradesSettingsCallback.class, updateInfo);

            case Callbacks.TRADES_SHOW_OR_REMOVE -> executorsService.execute(TradeManagersShowCallback.class, updateInfo);

            case Callbacks.TRADES_SHOW_BY_ITEM_FILTERS -> executorsService.execute(TradeManagersByItemFiltersShowAllCallback.class, updateInfo);

            case Callbacks.TRADES_SHOW_BY_ITEM_ID -> executorsService.execute(TradeManagersByItemIdShowAllCallback.class, updateInfo);

            case Callbacks.TRADES_REMOVE_BY_ITEM_ID -> executorsService.execute(TradeManagersByItemIdRemoveStage1AskItemIdCallback.class, updateInfo);

            case Callbacks.TRADES_REMOVE_BY_ITEM_ID_FINISH ->
                    executorsService.execute(TradeManagersByItemIdRemoveStage3FinishCallback.class, updateInfo);

            case Callbacks.CANCEL -> executorsService.execute(Cancel.class, updateInfo);

            case Callbacks.SILENT_CANCEL -> executorsService.execute(SilentCancel.class, updateInfo);

            default -> throw new IllegalStateException("Unexpected callback value: " + data);
        }
    }
}
