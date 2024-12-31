package github.ricemonger.telegramBot.executors.exceptions.client;

import github.ricemonger.telegramBot.executors.MockUpdateInfos;
import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.exceptions.client.TagDoesntExistExceptionExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TagDoesntExistExceptionExecutorTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_notify_user() {
        TagDoesntExistExceptionExecutor commandExecutor = new TagDoesntExistExceptionExecutor();
        commandExecutor.initAndExecute(MockUpdateInfos.UPDATE_INFO, botInnerService);

        verify(botInnerService).sendText(eq(MockUpdateInfos.UPDATE_INFO), anyString());
    }
}