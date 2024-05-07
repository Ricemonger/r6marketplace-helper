package github.ricemonger.telegramBot.updateReceiver;

import github.ricemonger.marketplace.databases.neo4j.services.TelegramLinkedUserService;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.executors.InputGroup;
import github.ricemonger.telegramBot.executors.InputState;
import github.ricemonger.utils.exceptions.TelegramUserDoesntExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateToUpdateInfoMapper {

    private final TelegramLinkedUserService telegramLinkedUserService;

    public UpdateInfo map(Update update) {
        UpdateInfo updateInfo = new UpdateInfo();

        updateInfo.setUpdateId(update.getUpdateId());
        updateInfo.setHasMessage(update.hasMessage());
        updateInfo.setHasCallBackQuery(update.hasCallbackQuery());

        if (update.hasMessage()) {
            updateInfo.setChatId(update.getMessage().getChatId());
            updateInfo.setMessageText(update.getMessage().getText());
        }
        if (update.hasCallbackQuery()) {
            updateInfo.setChatId(update.getCallbackQuery().getMessage().getChatId());
            updateInfo.setCallbackQueryData(update.getCallbackQuery().getData());
        }

        InputState inputState;
        InputGroup inputGroup;

        try{
            inputState = telegramLinkedUserService.getUserInputState(updateInfo.getChatId());
            inputGroup = telegramLinkedUserService.getUserInputGroup(updateInfo.getChatId());
        }
        catch(TelegramUserDoesntExistException e){
            inputState = InputState.BASE;
            inputGroup = InputGroup.BASE;
        }

        updateInfo.setInputState(inputState);
        updateInfo.setInputGroup(inputGroup);

        if (updateInfo.getInputState() == null) {
            updateInfo.setInputState(InputState.BASE);
        }

        if (updateInfo.getInputGroup() == null) {
            updateInfo.setInputGroup(InputGroup.BASE);
        }

        return updateInfo;
    }
}
