package github.ricemonger.telegramBot.update_consumer.executors.items.settings.appliedFilters;

import github.ricemonger.telegramBot.CallbackButton;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;

import java.util.ArrayList;
import java.util.List;

public class ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processFirstInput(InputState.ITEM_FILTER_NAME, InputGroup.ITEMS_SHOW_SETTING_CHANGE_APPLIED_FILTERS);

        List<String> allFilters = new ArrayList<>(botInnerService.getAllUserItemFiltersNames(updateInfo.getChatId()));

        List<String> appliedFilters = botInnerService.getItemShowAppliedFiltersNames(updateInfo.getChatId());

        List<String> notAppliedFilters = new ArrayList<>(allFilters);
        notAppliedFilters.removeAll(appliedFilters);

        CallbackButton[] buttons = new CallbackButton[allFilters.size()];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new CallbackButton(allFilters.get(i), Callbacks.INPUT_CALLBACK_PREFIX + allFilters.get(i));
        }

        String appliedFiltersString = String.join(", ", appliedFilters);
        String notAppliedFiltersString = String.join(", ", notAppliedFilters);

        askFromInlineKeyboard(
                "Current filters:\n"
                + appliedFiltersString
                + "\nAvailable filters:\n"
                + notAppliedFiltersString
                + "\nChoose filter to add or remove from current filters.",
                1,
                buttons);
    }
}
