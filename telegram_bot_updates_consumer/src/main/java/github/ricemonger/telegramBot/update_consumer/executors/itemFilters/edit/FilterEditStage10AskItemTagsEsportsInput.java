package github.ricemonger.telegramBot.update_consumer.executors.itemFilters.edit;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.enums.TagGroup;

public class FilterEditStage10AskItemTagsEsportsInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.ITEM_FILTER_ITEM_TAGS_ESPORTS);
        String tags = botInnerService.getStringOfAllTagsNamesByTagGroup(TagGroup.Esports_Team);
        askFromInlineKeyboardOrSkip("Please enter esports teams tags, dived by \",\" or \"|\". Teams list:\n" + tags, 1);
    }
}
