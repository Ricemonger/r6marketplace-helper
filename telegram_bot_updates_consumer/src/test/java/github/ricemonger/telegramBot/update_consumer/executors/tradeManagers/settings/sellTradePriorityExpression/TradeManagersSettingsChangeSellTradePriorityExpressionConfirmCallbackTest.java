package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.settings.sellTradePriorityExpression;

import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TradeManagersSettingsChangeSellTradePriorityExpressionConfirmCallbackTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_update_expression() {
        TradeManagersSettingsChangeSellTradePriorityExpressionConfirmCallback executor = new TradeManagersSettingsChangeSellTradePriorityExpressionConfirmCallback();
        executor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).setUserTradeManagersSellSettingsTradePriorityExpressionByUserInput(MockUpdateInfos.UPDATE_INFO.getChatId());

        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), anyString());
    }
}