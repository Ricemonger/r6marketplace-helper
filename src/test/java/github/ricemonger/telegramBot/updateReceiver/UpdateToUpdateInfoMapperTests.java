package github.ricemonger.telegramBot.updateReceiver;

import github.ricemonger.marketplace.databases.postgres.entities.TelegramUserEntity;
import github.ricemonger.marketplace.services.TelegramUserService;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.executors.InputGroup;
import github.ricemonger.telegramBot.executors.InputState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UpdateToUpdateInfoMapperTests {

    public static final Update UPDATE = new Update();
    public static final UpdateInfo UPDATE_INFO_CREDENTIALS_INPUTS;
    public static final UpdateInfo UPDATE_INFO_BASE_INPUTS;

    static {
        Message message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText("text");

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setData("data");
        callbackQuery.setMessage(message);

        UPDATE.setUpdateId(1);
        UPDATE.setMessage(message);
        UPDATE.setCallbackQuery(callbackQuery);

        UpdateInfo.UpdateInfoBuilder builder =
                UpdateInfo.builder().
                        updateId(1).
                        chatId(1L).
                        hasMessage(true).
                        messageText("text").
                        hasCallBackQuery(true).
                        callbackQueryData("data");

        UPDATE_INFO_CREDENTIALS_INPUTS = builder.inputState(InputState.CREDENTIALS_PASSWORD).inputGroup(InputGroup.CREDENTIALS_ADD).build();

        UPDATE_INFO_BASE_INPUTS = builder.inputState(InputState.BASE).inputGroup(InputGroup.BASE).build();
    }

    @MockBean
    private TelegramUserService telegramUserService;
    @Autowired
    private UpdateToUpdateInfoMapper updateToUpdateInfoMapper;

    @Test
    public void updateInfoShouldHaveRightFieldsFromUpdateAndUserService() {
        when(telegramUserService.getTelegramUser(1L)).thenReturn(
                new TelegramUserEntity(
                        "1",
                        InputState.CREDENTIALS_PASSWORD,
                        InputGroup.CREDENTIALS_ADD,
                        true
                ));

        assertEquals(updateToUpdateInfoMapper.map(UPDATE), UPDATE_INFO_CREDENTIALS_INPUTS);

        verify(telegramUserService).getTelegramUser(1L);
    }

    @Test
    public void updateInfoShouldGetBaseInputStateAndInputGroupIfUserServiceReturnsNull() {
        when(telegramUserService.getTelegramUser(1L)).thenReturn(
                new TelegramUserEntity(
                        "1",
                        null,
                        null,
                        true
                ));

        assertEquals(updateToUpdateInfoMapper.map(UPDATE), UPDATE_INFO_BASE_INPUTS);

        verify(telegramUserService).getTelegramUser(1L);
    }
}
