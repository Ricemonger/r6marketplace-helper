package github.ricemonger.telegramBot.executors.itemFilters.edit;

import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.TagGroup;

public class FilterEditStage7AskItemTagsSeasonsInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.ITEM_FILTER_ITEM_TAGS_SEASONS);
        String tags = botInnerService.getStringOfAllTagsNamesByTagGroup(TagGroup.Season);
        askFromInlineKeyboardOrSkip("Please enter seasons, dived by \",\" or \"|\". Seasons list:\n" + tags, 1);
    }
}
