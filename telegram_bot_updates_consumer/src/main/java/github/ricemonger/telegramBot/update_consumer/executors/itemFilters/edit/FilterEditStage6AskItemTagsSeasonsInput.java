package github.ricemonger.telegramBot.update_consumer.executors.itemFilters.edit;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.enums.TagGroup;

public class FilterEditStage6AskItemTagsSeasonsInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.ITEM_FILTER_ITEM_TAGS_SEASONS);
        String tags = botInnerService.getStringOfAllTagsNamesByTagGroup(TagGroup.Season);
        askFromInlineKeyboardOrSkip("Please enter seasons, dived by \",\" or \"|\". Seasons list:\n" + tags, 1);
    }
}
