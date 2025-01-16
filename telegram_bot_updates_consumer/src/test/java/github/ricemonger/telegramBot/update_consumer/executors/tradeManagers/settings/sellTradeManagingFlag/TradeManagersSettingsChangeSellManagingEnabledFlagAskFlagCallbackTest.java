package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.settings.sellTradeManagingFlag;

import github.ricemonger.telegramBot.CallbackButton;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TradeManagersSettingsChangeSellManagingEnabledFlagAskFlagCallbackTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_Should_askYesORNoFromInlineKeyboard() {
        TradeManagersSettingsChangeSellManagingEnabledFlagAskFlagCallback executor = new TradeManagersSettingsChangeSellManagingEnabledFlagAskFlagCallback();
        executor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        CallbackButton yesButton = new CallbackButton("Yes", Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG_YES);
        CallbackButton noButton = new CallbackButton("No", Callbacks.TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG_NO);

        verify(botInnerService).askFromInlineKeyboard(eq(MockUpdateInfos.UPDATE_INFO), anyString(), eq(2),
                argThat(argument -> Arrays.stream(argument).toList().containsAll(Arrays.asList(yesButton, noButton)) && argument.length == 2));

    }
}