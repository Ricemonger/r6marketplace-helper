package github.ricemonger.telegramBot.update_consumer.listeners;

import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.update_consumer.executors.ExecutorsService;
import github.ricemonger.telegramBot.update_consumer.executors.cancel.Cancel;
import github.ricemonger.telegramBot.update_consumer.executors.cancel.SilentCancel;
import github.ricemonger.telegramBot.update_consumer.executors.itemFilters.edit.*;
import github.ricemonger.telegramBot.update_consumer.executors.itemFilters.showOrRemove.FilterShowChosenStage2RemoveRequestInput;
import github.ricemonger.telegramBot.update_consumer.executors.items.settings.appliedFilters.ItemsShowSettingsChangeAppliedFiltersStage2AskActionInput;
import github.ricemonger.telegramBot.update_consumer.executors.items.settings.messageLimit.ItemsShowSettingsChangeMessageLimitFinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.items.settings.shownFields.*;
import github.ricemonger.telegramBot.update_consumer.executors.items.show.ItemsShowStage2FinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.itemFilter.*;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buy.TradeByItemIdManagerBuyEditStage2AskBoundaryPriceInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buy.TradeByItemIdManagerBuyEditStage3AskPriorityInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buy.TradeByItemIdManagerBuyEditStage4AskConfirmationFinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buyAndSell.TradeByItemIdManagerBuyAndSellEditStage2AskBoundarySellPriceInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buyAndSell.TradeByItemIdManagerBuyAndSellEditStage3AskBoundaryBuyPriceInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buyAndSell.TradeByItemIdManagerBuyAndSellEditStage4AskPriorityInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buyAndSell.TradeByItemIdManagerBuyAndSellEditStage5AskConfirmationFinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.sell.TradeByItemIdManagerSellEditStage2AskBoundaryPriceInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.sell.TradeByItemIdManagerSellEditStage3AskPriorityInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.sell.TradeByItemIdManagerSellEditStage4AskConfirmationFinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.settings.buyTradePriorityExpression.TradeManagersSettingsChangeBuyTradePriorityExpressionAskConfirmInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.settings.sellTradePriorityExpression.TradeManagersSettingsChangeSellTradePriorityExpressionAskConfirmInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.show.remove_or_change_enabled.itemFilters.TradeByFiltersManagerRemoveStage2AskConfirmationFinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.show.remove_or_change_enabled.itemId.TradeByItemIdManagerRemoveStage2AskConfirmationFinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.ubi_account_entry.link.UbiAccountEntryAuthorizeStage2AskPasswordInput;
import github.ricemonger.telegramBot.update_consumer.executors.ubi_account_entry.link.UbiAccountEntryAuthorizeStage3Ask2FaCodeInput;
import github.ricemonger.telegramBot.update_consumer.executors.ubi_account_entry.link.UbiAccountEntryAuthorizeStage4FinishInput;
import github.ricemonger.telegramBot.update_consumer.executors.ubi_account_entry.reauth.UbiAccountEntryReauthorizeEnter2FACodeStage2ExceptionOrSuccessFinishInput;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.server.InputGroupNotSupportedException;
import github.ricemonger.utils.exceptions.server.UnexpectedUserInputStateAndGroupConjunctionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InputCommandListener {

    private final ExecutorsService executorsService;

    public void handleUpdate(UpdateInfo updateInfo) {
        if (cancelMessageTextOrCallbackQueryText(updateInfo)) {
            executorsService.execute(Cancel.class, updateInfo);
        } else if (silentCancelMessageTextOrCallbackQueryText(updateInfo)) {
            executorsService.execute(SilentCancel.class, updateInfo);
        } else {
            switch (updateInfo.getInputGroup()) {

                case ITEM_FILTER_EDIT -> itemFilterEditInputGroup(updateInfo);

                case ITEM_FILTER_SHOW_OR_REMOVE -> itemFilterShowOrRemoveInputGroup(updateInfo);

                case ITEMS_SHOW_SETTING_CHANGE_APPLIED_FILTERS -> itemShowSettingsChangeAppliedFiltersInputGroup(updateInfo);

                case ITEMS_SHOW_SETTINGS_CHANGE_MESSAGE_LIMIT -> itemShowSettingsChangeMessageLimitInputGroup(updateInfo);

                case ITEMS_SHOW_SETTING_CHANGE_SHOWN_FIELDS -> itemShowSettingsChangeShownFieldsInputGroup(updateInfo);

                case ITEMS_SHOW -> itemShowInputGroup(updateInfo);

                case TRADE_BY_FILTERS_MANAGER_EDIT -> tradeByFiltersManagerEditInputGroup(updateInfo);

                case TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT -> tradeByItemIdManagerTypeBuyEditInputGroup(updateInfo);

                case TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT -> tradeByItemIdManagerTypeSellInputGroup(updateInfo);

                case TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT -> tradeByItemIdManagerTypeBuyAndSellInputGroup(updateInfo);

                case TRADE_BY_FILTERS_MANAGER_SHOW_OR_REMOVE -> tradeByFiltersManagerRemoveInputGroup(updateInfo);

                case TRADE_BY_ITEM_ID_MANAGER_SHOW_OR_REMOVE -> tradeByItemIdManagerRemoveInputGroup(updateInfo);

                case TRADE_MANAGERS_SETTINGS_CHANGE_SELL_TRADE_PRIORITY_EXPRESSION ->
                        tradeManagersSettingsChangeSellTradePriorityExpressionInputGroup(updateInfo);

                case TRADE_MANAGERS_SETTINGS_CHANGE_BUY_TRADE_PRIORITY_EXPRESSION ->
                        tradeManagersSettingsChangeBuyTradePriorityExpressionInputGroup(updateInfo);

                case UBI_ACCOUNT_ENTRY_LINK -> ubiAccountEntryLinkInputGroup(updateInfo);

                case UBI_ACCOUNT_ENTRY_REAUTHORIZE_2FA_CODE -> ubiAccountEntryReauthorize2FACodeGroup(updateInfo);

                default -> throw new InputGroupNotSupportedException(updateInfo.getInputGroup().name());
            }
        }
    }

    private boolean cancelMessageTextOrCallbackQueryText(UpdateInfo updateInfo) {
        return (updateInfo.isHasMessage() && updateInfo.getMessageText().equals("/cancel"))
               ||
               updateInfo.isHasCallBackQuery() && updateInfo.getCallbackQueryData().equals(Callbacks.CANCEL);
    }

    private boolean silentCancelMessageTextOrCallbackQueryText(UpdateInfo updateInfo) {
        return (updateInfo.isHasMessage() && updateInfo.getMessageText().equals("/silentCancel"))
               ||
               updateInfo.isHasCallBackQuery() && updateInfo.getCallbackQueryData().equals(Callbacks.CANCEL_SILENT);
    }

    private void itemFilterEditInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case ITEM_FILTER_NAME -> executorsService.execute(FilterEditStage2AskFilterTypeInput.class, updateInfo);

            case ITEM_FILTER_TYPE -> executorsService.execute(FilterEditStage3AskItemNamePatternsInput.class, updateInfo);

            case ITEM_FILTER_ITEM_NAME_PATTERNS -> executorsService.execute(FilterEditStage4AskItemTypesInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TYPES -> executorsService.execute(FilterEditStage5AskItemTagsRarityInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_RARITY -> executorsService.execute(FilterEditStage6AskItemTagsSeasonsInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_SEASONS -> executorsService.execute(FilterEditStage7AskItemTagsOperatorsInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_OPERATORS -> executorsService.execute(FilterEditStage8AskItemTagsWeaponsInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_WEAPONS -> executorsService.execute(FilterEditStage9AskItemTagsEventsInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_EVENTS -> executorsService.execute(FilterEditStage10AskItemTagsEsportsInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_ESPORTS -> executorsService.execute(FilterEditStage11AskItemTagsOtherInput.class, updateInfo);

            case ITEM_FILTER_ITEM_TAGS_OTHER -> executorsService.execute(FilterEditStage12AskMinPriceInput.class, updateInfo);

            case ITEM_FILTER_MIN_PRICE -> executorsService.execute(FilterEditStage13AskMaxPriceInput.class, updateInfo);

            case ITEM_FILTER_MAX_PRICE -> executorsService.execute(FilterEditStage14FinishRequestInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void itemFilterShowOrRemoveInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        if (Objects.requireNonNull(inputState) == InputState.ITEM_FILTER_NAME) {
            executorsService.execute(FilterShowChosenStage2RemoveRequestInput.class, updateInfo);
        } else {
            throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void itemShowSettingsChangeAppliedFiltersInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case ITEM_FILTER_NAME -> executorsService.execute(ItemsShowSettingsChangeAppliedFiltersStage2AskActionInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void itemShowSettingsChangeMessageLimitInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        if (Objects.requireNonNull(inputState) == InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT) {
            executorsService.execute(ItemsShowSettingsChangeMessageLimitFinishInput.class, updateInfo);
        } else {
            throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void itemShowSettingsChangeShownFieldsInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_ITEM_NAME ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage2AskItemTypeFlagInput.class, updateInfo);

            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_ITEM_TYPE ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage3AskMaxBuyPriceFlagInput.class,
                            updateInfo);

            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_MAX_BUY_PRICE ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage4AskBuyOrdersCountFlagInput.class,
                            updateInfo);

            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_BUY_ORDERS_COUNT ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage5AskMinSellPriceFlagInput.class, updateInfo);

            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_MIN_SELL_PRICE ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage6AskSellOrdersCountFlagInput.class,
                            updateInfo);

            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_SELL_ORDERS_COUNT ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage7AskPictureFlagInput.class,
                            updateInfo);

            case ITEMS_SHOW_SETTING_SHOWN_FIELDS_PICTURE ->
                    executorsService.execute(ItemsShowSettingsChangeShownFieldsStage8FinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void itemShowInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        if (Objects.requireNonNull(inputState) == InputState.ITEMS_SHOW_OFFSET) {
            executorsService.execute(ItemsShowStage2FinishInput.class, updateInfo);
        } else {
            throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeByFiltersManagerEditInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_BY_FILTERS_MANAGER_NAME -> executorsService.execute(TradeByFiltersManagerEditStage2AskTypeInput.class, updateInfo);

            case TRADE_BY_FILTERS_MANAGER_TRADE_TYPE -> executorsService.execute(TradeByFiltersManagerEditStage3AskFiltersInput.class, updateInfo);

            case TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES ->
                    executorsService.execute(TradeByFiltersManagerEditStage4AskMinBuySellProfitInput.class, updateInfo);

            case TRADE_BY_FILTERS_MANAGER_MIN_BUY_SELL_PROFIT ->
                    executorsService.execute(TradeByFiltersManagerEditStage5AskMinProfitPercentInput.class, updateInfo);

            case TRADE_BY_FILTERS_MANAGER_MIN_MEDIAN_PRICE_DIFFERENCE_PERCENT ->
                    executorsService.execute(TradeByFiltersManagerEditStage6AskPriorityInput.class, updateInfo);

            case TRADE_BY_FILTERS_MANAGER_PRIORITY ->
                    executorsService.execute(TradeByFiltersManagerEditStage7AskConfirmationFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }


    private void tradeByItemIdManagerTypeBuyEditInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_BY_ITEM_ID_MANAGER_ITEM_ID ->
                    executorsService.execute(TradeByItemIdManagerBuyEditStage2AskBoundaryPriceInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE ->
                    executorsService.execute(TradeByItemIdManagerBuyEditStage3AskPriorityInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_PRIORITY ->
                    executorsService.execute(TradeByItemIdManagerBuyEditStage4AskConfirmationFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeByItemIdManagerTypeSellInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_BY_ITEM_ID_MANAGER_ITEM_ID ->
                    executorsService.execute(TradeByItemIdManagerSellEditStage2AskBoundaryPriceInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE ->
                    executorsService.execute(TradeByItemIdManagerSellEditStage3AskPriorityInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_PRIORITY ->
                    executorsService.execute(TradeByItemIdManagerSellEditStage4AskConfirmationFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeByItemIdManagerTypeBuyAndSellInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_BY_ITEM_ID_MANAGER_ITEM_ID ->
                    executorsService.execute(TradeByItemIdManagerBuyAndSellEditStage2AskBoundarySellPriceInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE ->
                    executorsService.execute(TradeByItemIdManagerBuyAndSellEditStage3AskBoundaryBuyPriceInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE ->
                    executorsService.execute(TradeByItemIdManagerBuyAndSellEditStage4AskPriorityInput.class, updateInfo);

            case TRADE_BY_ITEM_ID_MANAGER_PRIORITY ->
                    executorsService.execute(TradeByItemIdManagerBuyAndSellEditStage5AskConfirmationFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeByFiltersManagerRemoveInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_BY_FILTERS_MANAGER_NAME ->
                    executorsService.execute(TradeByFiltersManagerRemoveStage2AskConfirmationFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeByItemIdManagerRemoveInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_BY_ITEM_ID_MANAGER_ITEM_ID ->
                    executorsService.execute(TradeByItemIdManagerRemoveStage2AskConfirmationFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeManagersSettingsChangeSellTradePriorityExpressionInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_MANAGERS_SETTINGS_TRADE_PRIORITY_EXPRESSION ->
                    executorsService.execute(TradeManagersSettingsChangeSellTradePriorityExpressionAskConfirmInput.class, updateInfo);
            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void tradeManagersSettingsChangeBuyTradePriorityExpressionInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {
            case TRADE_MANAGERS_SETTINGS_TRADE_PRIORITY_EXPRESSION ->
                    executorsService.execute(TradeManagersSettingsChangeBuyTradePriorityExpressionAskConfirmInput.class, updateInfo);
            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void ubiAccountEntryLinkInputGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {

            case UBI_ACCOUNT_ENTRY_EMAIL -> executorsService.execute(UbiAccountEntryAuthorizeStage2AskPasswordInput.class, updateInfo);

            case UBI_ACCOUNT_ENTRY_PASSWORD -> executorsService.execute(UbiAccountEntryAuthorizeStage3Ask2FaCodeInput.class, updateInfo);

            case UBI_ACCOUNT_ENTRY_2FA_CODE -> executorsService.execute(UbiAccountEntryAuthorizeStage4FinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }

    private void ubiAccountEntryReauthorize2FACodeGroup(UpdateInfo updateInfo) {
        InputState inputState = updateInfo.getInputState();

        switch (inputState) {

            case UBI_ACCOUNT_ENTRY_2FA_CODE ->
                    executorsService.execute(UbiAccountEntryReauthorizeEnter2FACodeStage2ExceptionOrSuccessFinishInput.class, updateInfo);

            default ->
                    throw new UnexpectedUserInputStateAndGroupConjunctionException(updateInfo.getInputState().name() + " - state:group - " + updateInfo.getInputGroup().name());
        }
    }
}
