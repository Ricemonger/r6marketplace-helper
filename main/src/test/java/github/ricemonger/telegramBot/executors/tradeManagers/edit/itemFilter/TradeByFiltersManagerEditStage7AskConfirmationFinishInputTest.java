package github.ricemonger.telegramBot.executors.tradeManagers.edit.itemFilter;

import github.ricemonger.telegramBot.executors.MockUpdateInfos;
import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.itemFilter.TradeByFiltersManagerEditStage7AskConfirmationFinishInput;
import github.ricemonger.utils.DTOs.personal.TradeByFiltersManager;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TradeByFiltersManagerEditStage7AskConfirmationFinishInputTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_process_last_input_and_ask_confirmation_with_manager_in_text() {
        when(botInnerService.generateTradeByFiltersManagerByUserInput(MockUpdateInfos.UPDATE_INFO.getChatId())).thenReturn(new TradeByFiltersManager());

        TradeByFiltersManagerEditStage7AskConfirmationFinishInput commandExecutor = new TradeByFiltersManagerEditStage7AskConfirmationFinishInput();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).saveUserInputAndSetInputState(MockUpdateInfos.UPDATE_INFO);
        verify(botInnerService).setUserInputState(MockUpdateInfos.UPDATE_INFO.getChatId(), InputState.BASE);
        verify(botInnerService).setUserInputGroup(MockUpdateInfos.UPDATE_INFO.getChatId(), InputGroup.BASE);

        verify(botInnerService).generateTradeByFiltersManagerByUserInput(MockUpdateInfos.UPDATE_INFO.getChatId());

        verify(botInnerService).askFromInlineKeyboard(eq(MockUpdateInfos.UPDATE_INFO), anyString(), anyInt(), any());
    }
}