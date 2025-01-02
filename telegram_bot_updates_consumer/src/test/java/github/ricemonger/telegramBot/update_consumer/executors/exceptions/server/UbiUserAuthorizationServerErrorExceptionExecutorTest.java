package github.ricemonger.telegramBot.update_consumer.executors.exceptions.server;

import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UbiUserAuthorizationServerErrorExceptionExecutorTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_notify_user() {
        UbiUserAuthorizationServerErrorExceptionExecutor commandExecutor = new UbiUserAuthorizationServerErrorExceptionExecutor();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), anyString());
    }
}