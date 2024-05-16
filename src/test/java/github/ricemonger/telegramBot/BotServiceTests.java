package github.ricemonger.telegramBot;

import github.ricemonger.marketplace.databases.neo4j.services.TelegramLinkedUserService;
import github.ricemonger.telegramBot.client.TelegramBotClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BotServiceTests {

    @MockBean
    private TelegramBotClientService telegramBotClientService;

    @MockBean
    private TelegramLinkedUserService telegramLinkedUserService;

    @Autowired
    private BotService botService;

    @Test
    void notifyAllUsersAboutItemAmountIncreaseShouldCallTelegramClientServiceWithNotifiableUsersList() {
        List<String> chatIds = new ArrayList<>();
        chatIds.add("1");
        chatIds.add("2");
        when(telegramLinkedUserService.getAllChatIdsForNotifiableUsers()).thenReturn(chatIds);

        botService.notifyAllUsersAboutItemAmountIncrease(1, 2);

        verify(telegramLinkedUserService).getAllChatIdsForNotifiableUsers();

        verify(telegramBotClientService).sendText(eq(chatIds.get(0)), anyString());
        verify(telegramBotClientService).sendText(eq(chatIds.get(1)), anyString());
    }
}