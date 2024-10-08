package github.ricemonger.telegramBot.executors.credentials.link;

import github.ricemonger.telegramBot.InputGroup;
import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.client.BotInnerService;
import github.ricemonger.telegramBot.executors.MockUpdateInfos;
import github.ricemonger.telegramBot.executors.ubi_account_entry.link.UbiAccountEntryLinkStage2AskPasswordInput;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CredentialsAddPasswordInputTest {
    @MockBean
    private BotInnerService botInnerService;

    @Test
    public void initAndExecute_should_add_credentials_if_authorized_successfully() {
        UbiAccountEntryLinkStage2AskPasswordInput ubiAccountEntryLinkStage2AskPasswordInput = new UbiAccountEntryLinkStage2AskPasswordInput();
        ubiAccountEntryLinkStage2AskPasswordInput.initAndExecute(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT, botInnerService);

        verify(botInnerService).addUserUbiAccountEntryByUserInput(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT.getChatId());

        verify(botInnerService).saveUserInput(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT);
        verify(botInnerService).setUserInputState(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT.getChatId(), InputState.BASE);
        verify(botInnerService).setUserInputGroup(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT.getChatId(), InputGroup.BASE);
    }

    @Test
    public void initAndExecute_should_throw_exception_to_next_method_if_thrown() {
        UbiAccountEntryLinkStage2AskPasswordInput ubiAccountEntryLinkStage2AskPasswordInput = new UbiAccountEntryLinkStage2AskPasswordInput();
        doThrow(new RuntimeException()).when(botInnerService).addUserUbiAccountEntryByUserInput(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT.getChatId());

        assertThrows(RuntimeException.class, () -> ubiAccountEntryLinkStage2AskPasswordInput.initAndExecute(MockUpdateInfos.UPDATE_INFO_PASSWORD_INPUT, botInnerService));
    }
}