package github.ricemonger.telegramBot.update_consumer.executors.ubi_account_entry.link;

import github.ricemonger.telegramBot.update_consumer.BotInnerService;
import github.ricemonger.telegramBot.update_consumer.executors.MockUpdateInfos;
import github.ricemonger.utils.enums.InputState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UbiAccountEntryAuthorizeStage2AskPasswordInputTest {

    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_request_password_if_only_email_input() {
        UbiAccountEntryAuthorizeStage2AskPasswordInput ubiAccountEntryAuthorizeStage2AskPasswordInput = new UbiAccountEntryAuthorizeStage2AskPasswordInput();

        ubiAccountEntryAuthorizeStage2AskPasswordInput.initAndExecute(MockUpdateInfos.UPDATE_INFO_EMAIL_INPUT, botInnerService);

        verify(botInnerService, never()).addUserUbiAccountEntryByUserInput(MockUpdateInfos.UPDATE_INFO_FULL_INPUT.getChatId());

        verify(botInnerService).saveUserInput(MockUpdateInfos.UPDATE_INFO_EMAIL_INPUT);
        verify(botInnerService).setUserInputState(MockUpdateInfos.UPDATE_INFO_EMAIL_INPUT.getChatId(), InputState.UBI_ACCOUNT_ENTRY_PASSWORD);
    }
}