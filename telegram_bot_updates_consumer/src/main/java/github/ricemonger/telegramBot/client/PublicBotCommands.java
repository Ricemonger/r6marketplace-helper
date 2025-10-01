package github.ricemonger.telegramBot.client;


import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public class PublicBotCommands {

    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String ITEMS_COMMAND = "/items";
    public static final String MANAGERS_COMMAND = "/managers";
    public static final String FILTERS_COMMAND = "/filters";
    public static final String CREDENTIALS_COMMAND = "/credentials";
    public static final String NOTIFICATIONS_COMMAND = "/notifications";
    public static final String CANCEL_COMMAND = "/cancel";

    public final static List<BotCommand> botCommands = List.of(
            new BotCommand(START_COMMAND, "Start chatting with bot"),
            new BotCommand(HELP_COMMAND, "Get help with bot interactions"),
            new BotCommand(ITEMS_COMMAND, "Search for items on Marketplace"),
            new BotCommand(MANAGERS_COMMAND, "Operate on your trade managers"),
            new BotCommand(FILTERS_COMMAND, "Manage your item filters"),
            new BotCommand(CREDENTIALS_COMMAND, "Manage your linked Ubisoft account"),
            new BotCommand(NOTIFICATIONS_COMMAND, "Change your notification settings"),
            new BotCommand(CANCEL_COMMAND, "Cancels current operation")
    );

    public static String getHelpText() {
        String sb = START_COMMAND + " - Start chatting with bot\n" +
                    HELP_COMMAND + " - Get help with bot interactions\n" +
                    ITEMS_COMMAND + " - Search for items on Marketplace\n" +
                    """
                            You can look for items on Marketplace by applied to search filters.
                            Sorting options for the items and starting offset can be chosen before search.
                            Item properties to be shown, number of items per one message and
                            maximum amount of messages from one request can be specified too.
                            """ +
                    MANAGERS_COMMAND + " - Operate your trade managers\n" +
                    """
                            Trade Managers are used to automatically operate your trades on Marketplace.
                            They operate on specific items either by item ID or item filters.
                            You can create, update, remove, enable or disable your trade managers.
                            """ +
                    FILTERS_COMMAND + " - Manage your item filters\n" +
                    """
                            Item Filters are used to search for items by specific item properties on Marketplace.
                            You can create, update or remove your item filters.
                            """ +
                    CREDENTIALS_COMMAND + " - Manage your linked Ubisoft account\n" +
                    """
                            You can link or unlink your Ubisoft account to your Telegram account.
                            Due to need to directly log in to Marketplace, your Ubisoft account email and password
                            are required to be stored in the bot 's database. Password is stored in encrypted form.
                            One Ubisoft account can be linked to many Telegram accounts.
                            Each Telegram account can be linked to only one Ubisoft account.
                            Linked Ubisoft account is used to operate on your trades on Marketplace.
                            """ +
                    NOTIFICATIONS_COMMAND + " - Change your notification settings\n" +
                    """
                            You can change your notification settings for the bot.
                            You can enable or disable notifications for different types of events.
                            """ +
                    CANCEL_COMMAND + " - Cancels current operation\n";
        return sb;
    }
}
