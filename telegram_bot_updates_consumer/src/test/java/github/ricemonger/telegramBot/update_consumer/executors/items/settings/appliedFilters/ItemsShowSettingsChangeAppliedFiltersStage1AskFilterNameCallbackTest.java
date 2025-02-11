package github.ricemonger.telegramBot.update_consumer.executors.items.settings.appliedFilters;

import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallbackTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_process_first_input_and_show_filters_lists_and_ask_from_keyboard() {
        when(botInnerService.getItemShowAppliedFiltersNames(MockUpdateInfos.UPDATE_INFO.getChatId())).thenReturn(List.of());
        when(botInnerService.getAllUserItemFiltersNames(MockUpdateInfos.UPDATE_INFO.getChatId())).thenReturn(new ArrayList<>());

        ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback commandExecutor = new ItemsShowSettingsChangeAppliedFiltersStage1AskFilterNameCallback();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).clearUserInputsAndSetInputStateAndGroup(eq(MockUpdateInfos.UPDATE_INFO.getChatId()), eq(InputState.ITEM_FILTER_NAME), eq(InputGroup.ITEMS_SHOW_SETTING_CHANGE_APPLIED_FILTERS));

        verify(botInnerService).getItemShowAppliedFiltersNames(eq(MockUpdateInfos.UPDATE_INFO.getChatId()));
        verify(botInnerService).getAllUserItemFiltersNames(eq(MockUpdateInfos.UPDATE_INFO.getChatId()));

        verify(botInnerService).askFromInlineKeyboard(eq(MockUpdateInfos.UPDATE_INFO), anyString(), anyInt(), any());
    }
}