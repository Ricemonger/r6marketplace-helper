package github.ricemonger.telegramBot.executors.tradeManagers.showRemove.remove_or_change_enabled.itemFilters;

import github.ricemonger.telegramBot.client.BotInnerService;
import github.ricemonger.telegramBot.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TradeByFiltersManagerRemoveStage3ConfirmedFinishCallbackTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void intAndExecute_should_removeTradeByFiltersManager() {
        TradeByFiltersManagerRemoveStage3ConfirmedFinishCallback commandExecutor = new TradeByFiltersManagerRemoveStage3ConfirmedFinishCallback();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).removeUserTradeByFiltersManagerByUserInput(MockUpdateInfos.UPDATE_INFO.getChatId());

        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), anyString());
    }
}