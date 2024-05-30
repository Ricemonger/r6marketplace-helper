package github.ricemonger.telegramBot.client;

import github.ricemonger.marketplace.services.TelegramUserService;
import github.ricemonger.marketplace.services.ItemService;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.executors.InputGroup;
import github.ricemonger.telegramBot.executors.InputState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BotInnerServiceTest {

    @MockBean
    private TelegramBotClientService telegramBotClientService;

    @MockBean
    private TelegramUserService telegramUserService;

    @MockBean
    private ItemService itemService;

    @Autowired
    private BotInnerService botInnerService;

    @Test
    public void askFromInlineKeyboardShouldHandleToService() {
        UpdateInfo updateInfo = new UpdateInfo();
        String text = "text";
        int buttonsInLine = 1;
        CallbackButton[] buttons = new CallbackButton[1];

        botInnerService.askFromInlineKeyboard(updateInfo, text, buttonsInLine, buttons);

        verify(telegramBotClientService).askFromInlineKeyboard(updateInfo, text, buttonsInLine, buttons);
    }

    @Test
    public void sendTextShouldHandleToService() {
        UpdateInfo updateInfo = new UpdateInfo();
        String answer = "answer";

        botInnerService.sendText(updateInfo, answer);

        verify(telegramBotClientService).sendText(updateInfo, answer);
    }

    @Test
    public void isRegisteredShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.isRegistered(chatId);

        verify(telegramUserService).isTelegramUserRegistered(chatId);
    }

    @Test
    public void isRegisteredShouldReturnServiceAnswerIfTrue() {
        Long chatId = 1L;

        when(telegramUserService.isTelegramUserRegistered(chatId)).thenReturn(true);

        assertTrue(botInnerService.isRegistered(chatId));

        when(telegramUserService.isTelegramUserRegistered(chatId)).thenReturn(true);

        assertTrue(botInnerService.isRegistered(chatId));
    }

    @Test
    public void registerUserShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.registerUser(chatId);

        verify(telegramUserService).registerTelegramUser(chatId);
    }

    @Test
    public void addCredentialsFromUserInputsShouldHandleToServiceAndAddIfFull() {
        Long chatId = 1L;
        when(telegramUserService.getUserInputByStateOrNull(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL)).thenReturn("email:password");

        botInnerService.addCredentialsFromUserInputs(chatId);

        verify(telegramUserService).addCredentialsIfValidOrThrowException(chatId, "email", "password");

        verify(telegramUserService).clearUserInputs(chatId);
    }

    @Test
    public void addCredentialsFromUserInputsShouldHandleToServiceAndAddIfSeparated() {
        Long chatId = 1L;
        when(telegramUserService.getUserInputByStateOrNull(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL)).thenReturn("email");
        when(telegramUserService.getUserInputByStateOrNull(chatId, InputState.CREDENTIALS_PASSWORD)).thenReturn("password");

        botInnerService.addCredentialsFromUserInputs(chatId);

        verify(telegramUserService).addCredentialsIfValidOrThrowException(chatId, "email", "password");

        verify(telegramUserService).clearUserInputs(chatId);
    }

    @Test
    public void saveUserInputOrThrowShouldHandleToService() {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setHasMessage(true);
        updateInfo.setMessageText("userInput");

        botInnerService.saveUserInputOrThrow(updateInfo);

        verify(telegramUserService).saveUserInput(updateInfo.getChatId(), updateInfo.getInputState(), "userInput");
    }

    @Test
    public void clearUserInputsShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.clearUserInputs(chatId);

        verify(telegramUserService).clearUserInputs(chatId);
    }

    @Test
    public void setUserNextInputStateShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.setUserNextInputState(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL);

        verify(telegramUserService).setUserNextInputState(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL);
    }

    @Test
    public void setUserNextInputGroupShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.setUserNextInputGroup(chatId, InputGroup.CREDENTIALS_ADD);

        verify(telegramUserService).setUserNextInputGroup(chatId, InputGroup.CREDENTIALS_ADD);
    }

    @Test
    public void getUserInputByStateShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.getUserInputByState(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL);

        verify(telegramUserService).getUserInputByStateOrNull(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL);
    }

    @Test
    public void getUserInputByStateShouldReturnServiceAnswer() {
        Long chatId = 1L;

        when(telegramUserService.getUserInputByStateOrNull(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL)).thenReturn("userInput");

        assertEquals("userInput", botInnerService.getUserInputByState(chatId, InputState.CREDENTIALS_FULL_OR_EMAIL));
    }

    @Test
    public void removeCredentialsByUserInputsShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.removeCredentialsByUserInputs(chatId);

        verify(telegramUserService).removeCredentialsByUserInputs(chatId);
    }

    @Test
    public void removeUserAllCredentialsShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.removeUserAllCredentials(chatId);

        verify(telegramUserService).removeAllCredentials(chatId);
    }

    @Test
    public void getCredentialsEmailsListShouldHandleToService() {
        Long chatId = 1L;

        botInnerService.getCredentialsEmailsList(chatId);

        verify(telegramUserService).getCredentialsEmailsList(chatId);
    }

    @Test
    public void getCredentialsEmailsListShouldReturnServiceAnswer() {
        Long chatId = 1L;

        when(telegramUserService.getCredentialsEmailsList(chatId)).thenReturn(List.of("email1", "email2"));

        assertEquals(List.of("email1", "email2"), botInnerService.getCredentialsEmailsList(chatId));
    }

    @Test
    public void sendDefaultSpeculativeItemsAsMessagesShouldGetFromServiceWithDefaultValuesAndSendByItemsAmount() {
        Long chatId = 1L;

        botInnerService.sendDefaultSpeculativeItemsAsMessages(chatId);

        verify(itemService).getAllSpeculativeItemsByExpectedProfit(50, 40, 0, 15000);

        verify(telegramBotClientService,times(2)).sendText(eq(String.valueOf(chatId)), anyString());
    }
}