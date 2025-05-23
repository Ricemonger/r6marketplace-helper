package github.ricemonger.telegramBot.update_consumer.executors.help;

import github.ricemonger.telegramBot.client.PublicBotCommands;
import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class HelpDirectTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_send_help_text() {
        HelpDirect helpDirect = new HelpDirect();
        helpDirect.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), eq(PublicBotCommands.getHelpText()));
    }
}