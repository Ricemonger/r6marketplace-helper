package github.ricemonger.telegramBot.executors.credentials.link;

import github.ricemonger.telegramBot.InputGroup;
import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.client.BotInnerService;
import github.ricemonger.telegramBot.executors.MockUpdateInfos;
import github.ricemonger.telegramBot.executors.ubi_account_entry.link.UbiAccountEntryLinkStage1AskFullOrEmailInput;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UbiAccountEntryLinkStage1AskFullOrEmailInputTest {

    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_add_credentials_if_full_input() {
        UbiAccountEntryLinkStage1AskFullOrEmailInput ubiAccountEntryLinkStage1AskFullOrEmailInput = new UbiAccountEntryLinkStage1AskFullOrEmailInput();

        ubiAccountEntryLinkStage1AskFullOrEmailInput.initAndExecute(MockUpdateInfos.UPDATE_INFO_FULL_INPUT, botInnerService);

        verify(botInnerService).addUserUbiAccountEntryByUserInput(MockUpdateInfos.UPDATE_INFO_FULL_INPUT.getChatId());

        verify(botInnerService).saveUserInput(MockUpdateInfos.UPDATE_INFO_FULL_INPUT);
        verify(botInnerService).setUserInputGroup(MockUpdateInfos.UPDATE_INFO_FULL_INPUT.getChatId(), InputGroup.BASE);
        verify(botInnerService).setUserInputState(MockUpdateInfos.UPDATE_INFO_FULL_INPUT.getChatId(), InputState.BASE);
    }

    @Test
    public void initAndExecute_should_request_password_if_only_email_input() {
        UbiAccountEntryLinkStage1AskFullOrEmailInput ubiAccountEntryLinkStage1AskFullOrEmailInput = new UbiAccountEntryLinkStage1AskFullOrEmailInput();

        ubiAccountEntryLinkStage1AskFullOrEmailInput.initAndExecute(MockUpdateInfos.UPDATE_INFO_EMAIL_INPUT, botInnerService);

        verify(botInnerService, never()).addUserUbiAccountEntryByUserInput(MockUpdateInfos.UPDATE_INFO_FULL_INPUT.getChatId());

        verify(botInnerService).saveUserInput(MockUpdateInfos.UPDATE_INFO_EMAIL_INPUT);
        verify(botInnerService).setUserInputState(MockUpdateInfos.UPDATE_INFO_EMAIL_INPUT.getChatId(), InputState.UBI_ACCOUNT_ENTRY_PASSWORD);
    }

}