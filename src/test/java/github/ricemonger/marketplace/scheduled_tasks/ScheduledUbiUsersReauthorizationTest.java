package github.ricemonger.marketplace.scheduled_tasks;

import github.ricemonger.marketplace.services.TelegramLinkedUbiUserService;
import github.ricemonger.telegramBot.client.TelegramBotClientService;
import github.ricemonger.utils.dtos.UbiUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ScheduledUbiUsersReauthorizationTest {

    @MockBean
    private TelegramLinkedUbiUserService telegramLinkedUbiUserService;

    @MockBean
    private TelegramBotClientService telegramBotClientService;

    @Autowired
    private ScheduledUbiUsersReauthorization scheduledUbiUsersReauthorization;

    @Test
    public void reauthorizeUbiUsersAndNotifyAboutFailures_should_reauthorize_and_notify_via_services() {
        List<UbiUser> unauthorizedUsers = new ArrayList<>();
        UbiUser unauthorizedUser = new UbiUser();
        unauthorizedUser.setChatId("1");
        unauthorizedUser.setEmail("email");
        unauthorizedUsers.add(unauthorizedUser);

        when(telegramLinkedUbiUserService.reauthorizeAllUbiUsersAndGetUnauthorizedList()).thenReturn(unauthorizedUsers);

        scheduledUbiUsersReauthorization.reauthorizeUbiUsersAndNotifyAboutFailures();

        verify(telegramLinkedUbiUserService).reauthorizeAllUbiUsersAndGetUnauthorizedList();

        verify(telegramBotClientService).notifyUserAboutUbiAuthorizationFailure("1", "email");
    }
}