package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.settings.buyTradeManagingFlag;

import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TradeManagersSettingsChangeBuyManagingEnabledFlagNoCallbackTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_setUserTradeManagersSettingsManagingEnabledFlag_false_and_send_text() {
        TradeManagersSettingsChangeBuyManagingEnabledFlagNoCallback command = new TradeManagersSettingsChangeBuyManagingEnabledFlagNoCallback();

        command.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).setUserTradeManagersBuySettingsManagingEnabledFlag(MockUpdateInfos.UPDATE_INFO.getChatId(), false);
        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), anyString());
    }
}