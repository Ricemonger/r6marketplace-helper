package github.ricemonger.telegramBot.executors.tradeManagers.createUpdate.itemFilter;

import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.client.BotInnerService;
import github.ricemonger.telegramBot.executors.MockUpdateInfos;
import github.ricemonger.telegramBot.executors.tradeManagers.edit.itemFilter.TradeByFiltersManagerEditStage3AskFiltersInput;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TradeByFiltersManagerEditStage3AskFiltersInputTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_process_middle_input_with_text() {
        TradeByFiltersManagerEditStage3AskFiltersInput commandExecutor = new TradeByFiltersManagerEditStage3AskFiltersInput();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).saveUserInput(MockUpdateInfos.UPDATE_INFO);
        verify(botInnerService).setUserInputState(MockUpdateInfos.UPDATE_INFO.getChatId(), InputState.TRADE_BY_FILTERS_MANAGER_EDIT_FILTERS_NAMES);

        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), anyString());
    }
}