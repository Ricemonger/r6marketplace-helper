package github.ricemonger.telegramBot.update_consumer.executors.ubi_account_entry.link;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.client.UbiUserAuthorizationClientErrorException;
import github.ricemonger.utils.exceptions.server.UbiUserAuthorizationServerErrorException;

public class UbiAccountEntryAuthorizeStage3Ask2FaCodeInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand()
            throws TelegramUserDoesntExistException,
            UbiUserAuthorizationClientErrorException,
            UbiUserAuthorizationServerErrorException {
        processMiddleInput(InputState.UBI_ACCOUNT_ENTRY_2FA_CODE, "Please provide your current Ubisoft 2FA code:");
        botInnerService.deleteMessage(updateInfo);
    }
}
