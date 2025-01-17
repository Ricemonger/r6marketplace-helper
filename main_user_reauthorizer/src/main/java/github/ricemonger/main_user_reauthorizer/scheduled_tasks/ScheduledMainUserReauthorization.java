package github.ricemonger.main_user_reauthorizer.scheduled_tasks;


import github.ricemonger.main_user_reauthorizer.authorization.AuthorizationService;
import github.ricemonger.main_user_reauthorizer.services.CommonValuesService;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledMainUserReauthorization {

    private final CommonValuesService commonValuesService;

    private final AuthorizationService authorizationService;

    @Scheduled(fixedRateString = "${app.scheduling.fixedRate}", initialDelayString = "${app.scheduling.initialDelay}")
    public void reauthorizeMainUserAndSave() {
        AuthorizationDTO authorizationDTO = authorizationService.authorizeAndGetBaseAuthorizedDTO(commonValuesService.getMainUserEmail(), commonValuesService.getMainUserPassword());

        commonValuesService.setMainUserAuthorization(authorizationDTO);
    }
}
