package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.settings;

import github.ricemonger.marketplace.services.DTOs.TradeManagersSettings;
import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TradeManagersSettingsCallbackTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_show_current_settings_and_ask_if_change() {
        when(botInnerService.getUserTradeManagersSettings(MockUpdateInfos.UPDATE_INFO.getChatId())).thenReturn(new TradeManagersSettings());

        TradeManagersSettingsCallback commandExecutor = new TradeManagersSettingsCallback();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).getUserTradeManagersSettings(MockUpdateInfos.UPDATE_INFO.getChatId());

        verify(botInnerService).askFromInlineKeyboard(eq(MockUpdateInfos.UPDATE_INFO), anyString(), anyInt(), any());
    }
}