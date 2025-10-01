package github.ricemonger.users_ubi_accs_reauthorizer.scheduled_tasks;


import github.ricemonger.users_ubi_accs_reauthorizer.services.DTOs.UserUnauthorizedUbiAccount;
import github.ricemonger.users_ubi_accs_reauthorizer.services.NotificationService;
import github.ricemonger.users_ubi_accs_reauthorizer.services.UserUbiAccountEntryAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledAllUbiUsersReauthorization {

    private final UserUbiAccountEntryAuthorizationService userUbiAccountEntryAuthorizationService;

    private final NotificationService telegramBotService;

    @Scheduled(fixedRateString = "${app.scheduling.fixedRate}", initialDelayString = "${app.scheduling.initialDelay}")
    public void reauthorizeAllUbiUsersAndNotifyAboutFailures() {
        Collection<UserUnauthorizedUbiAccount> toNotify = userUbiAccountEntryAuthorizationService.reauthorizeAllUbiUsersAndGetUnauthorizedList();
        for (UserUnauthorizedUbiAccount user : toNotify) {
            telegramBotService.sendAuthorizationNotification(user.getId(), String.format("Your Ubisoft account with email :%s could no be authorized. " +
                                                                                         "Please reauthorize it in /credentials tab.", user.getEmail()));
        }
    }
}
