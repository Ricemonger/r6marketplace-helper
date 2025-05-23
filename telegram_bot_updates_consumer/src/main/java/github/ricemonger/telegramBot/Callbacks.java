package github.ricemonger.telegramBot;

public class Callbacks {

    public static final String EMPTY = "SKIP_CALLBACK_DATA";

    public static final String INPUT_CALLBACK_PREFIX = "INPUT_CALLBACK_PREFIX_";

    public static final String INPUT_CALLBACK_TRUE = "INPUT_CALLBACK_TRUE";

    public static final String INPUT_CALLBACK_FALSE = "INPUT_CALLBACK_FALSE";

//--------------------------------------------------------------------------------------------------

    public static final String CANCEL = "CANCEL_CALLBACK_DATA";

    public static final String CANCEL_SILENT = "SILENT_CANCEL_CALLBACK_DATA";

//--------------------------------------------------------------------------------------------------

    public static final String ITEM_FILTER_TYPE_ALLOW = "ITEM_FILTER_TYPE_ALLOW";

    public static final String ITEM_FILTER_TYPE_DENY = "ITEM_FILTER_TYPE_DENY";

    public static final String ITEM_FILTER_EDIT = "ITEM_FILTER_EDIT";

    public static final String ITEM_FILTER_EDIT_FINISH_CONFIRMED = "ITEM_FILTER_EDIT_FINISH_CONFIRMED";

    public static final String ITEM_FILTERS_SHOW_ALL = "ITEM_FILTERS_SHOW_ALL";

    public static final String ITEM_FILTER_REMOVE_FINISH_CONFIRMED = "ITEM_FILTER_REMOVE_FINISH_CONFIRMED";

//--------------------------------------------------------------------------------------------------

    public static final String ITEMS_SHOW_SETTINGS = "ITEMS_SHOW_SETTINGS";

    public static final String ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE = "ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE";

    public static final String ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_NO = "ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_NO";

    public static final String ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_YES = "ITEMS_SHOW_SETTINGS_CHANGE_FEW_ITEMS_IN_MESSAGE_CALLBACK_YES";

    public static final String ITEMS_SHOW_SETTINGS_CHANGE_MESSAGE_LIMIT = "ITEMS_SHOW_SETTINGS_CHANGE_MESSAGE_LIMIT";

    public static final String ITEMS_SHOW_SETTINGS_CHANGE_SHOWN_FIELDS = "ITEMS_SHOW_SETTINGS_CHANGE_SHOWN_FIELDS";

    public static final String ITEMS_SHOW_SETTINGS_CHANGE_APPLIED_FILTERS = "ITEMS_SHOW_SETTINGS_CHANGE_APPLIED_FILTERS";

    public static final String DELETE_ITEM_SHOW_APPLIED_FILTER = "DELETE_ITEM_SHOW_APPLIED_FILTER";

    public static final String ADD_ITEM_SHOW_APPLIED_FILTER = "ADD_ITEM_SHOW_APPLIED_FILTER";


    public static final String ITEMS_SHOW = "ITEMS_SHOW";

//--------------------------------------------------------------------------------------------------

    public static final String NOTIFICATIONS_SETTINGS_INVERT_PRIVATE_FLAG = "NOTIFICATIONS_SETTINGS_INVERT_PRIVATE_FLAG";

    public static final String NOTIFICATIONS_SETTINGS_INVERT_PUBLIC_FLAG = "NOTIFICATIONS_SETTINGS_INVERT_PUBLIC_FLAG";

    public static final String NOTIFICATIONS_SETTINGS_INVERT_UBI_STATS_UPDATED_FLAG = "NOTIFICATIONS_SETTINGS_INVERT_UBI_STATS_UPDATED_FLAG";

    public static final String NOTIFICATIONS_SETTINGS_INVERT_TRADE_MANAGER_FLAG = "NOTIFICATIONS_SETTINGS_INVERT_TRADE_MANAGER_FLAG";

    public static final String NOTIFICATIONS_SETTINGS_INVERT_AUTHORIZATION_FLAG = "NOTIFICATIONS_SETTINGS_INVERT_AUTHORIZATION_FLAG";

//--------------------------------------------------------------------------------------------------

    public static final String START_YES = "START_YES";

//--------------------------------------------------------------------------------------------------

    public static final String TRADE_MANAGERS_EDIT = "TRADE_MANAGERS_EDIT";


    public static final String TRADE_BY_FILTERS_MANAGER_EDIT = "TRADE_BY_FILTERS_MANAGER_EDIT";

    public static final String TRADE_BY_FILTERS_MANAGER_TYPE_BUY_EDIT = "TRADES_EDIT_ITEM_FILTER_TRADE_TYPE_BUY";

    public static final String TRADE_BY_FILTERS_MANAGER_TYPE_SELL_EDIT = "TRADES_EDIT_ITEM_FILTER_TRADE_TYPE_SELL";

    public static final String TRADE_BY_FILTERS_MANAGER_TYPE_BUY_AND_SELL_EDIT = "TRADES_EDIT_ITEM_FILTER_TRADE_TYPE_BUY_AND_SELL";

    public static final String TRADE_BY_FILTERS_MANAGER_EDIT_FINISH_CONFIRMED = "TRADE_BY_FILTERS_MANAGER_EDIT_FINISH_CONFIRMED";


    public static final String TRADE_BY_ITEM_ID_MANAGER_EDIT = "TRADE_BY_ITEM_ID_MANAGER_EDIT";

    public static final String TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT = "TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT";

    public static final String TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT_FINISH_CONFIRMED = "TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_EDIT_FINISH_CONFIRMED";

    public static final String TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT = "TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT";

    public static final String TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT_FINISH_CONFIRMED = "TRADE_BY_ITEM_ID_MANAGER_TYPE_SELL_EDIT_FINISH_CONFIRMED";

    public static final String TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT = "TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT";

    public static final String TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT_FINISH_CONFIRMED = "TRADE_BY_ITEM_ID_MANAGER_TYPE_BUY_AND_SELL_EDIT_FINISH_CONFIRMED";


    public static final String TRADE_MANAGERS_SETTINGS = "TRADE_MANAGERS_SETTINGS";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG = "TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_YES = "RADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_YES";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_NO = "TRADE_MANAGERS_SETTINGS_CHANGE_MANAGING_ENABLED_FLAG_NO";


    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG = "TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_YES = "TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_YES";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_NO = "TRADE_MANAGERS_SETTINGS_CHANGE_NEW_MANAGERS_ARE_ACTIVE_FLAG_NO";


    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG = "TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGER_ENABLED_FLAG";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG_YES = "TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG_YES";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG_NO = "TRADE_MANAGERS_SETTINGS_CHANGE_SELL_MANAGING_ENABLED_FLAG_NO";


    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_SELL_TRADE_PRIORITY_EXPRESSION = "TRADE_MANAGERS_SETTINGS_CHANGE_SELL_TRADE_PRIORITY_EXPRESSION";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_SELL_TRADE_PRIORITY_EXPRESSION_CONFIRMED = "TRADE_MANAGERS_SETTINGS_CHANGE_SELL_TRADE_PRIORITY_EXPRESSION_CONFIRMED";


    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_BUY_MANAGING_ENABLED_FLAG = "TRADE_MANAGERS_SETTINGS_CHANGE_BUY_MANAGER_ENABLED_FLAG";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_BUY_MANAGING_ENABLED_FLAG_YES = "TRADE_MANAGER_SETTINGS_CHANGE_BUY_MANAGING_ENABLED_FLAG_YES";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_BUY_MANAGING_ENABLED_FLAG_NO = "TRADE_MANAGER_SETTINGS_CHANGE_BUY_MANAGING_ENABLED_FLAG_NO";


    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_BUY_TRADE_PRIORITY_EXPRESSION = "TRADE_MANAGERS_SETTINGS_CHANGE_BUY_TRADE_PRIORITY_EXPRESSION";

    public static final String TRADE_MANAGERS_SETTINGS_CHANGE_BUY_TRADE_PRIORITY_EXPRESSION_CONFIRMED = "TRADE_MANAGERS_SETTINGS_CHANGE_BUY_TRADE_PRIORITY_EXPRESSION_CONFIRMED";


    public static final String TRADE_MANAGERS_SHOW_CHOOSE_TYPE = "TRADE_MANAGERS_SHOW_CHOOSE_TYPE";

    public static final String TRADE_BY_FILTERS_MANAGERS_SHOW_ALL = "TRADE_BY_FILTERS_MANAGERS_SHOW_ALL";

    public static final String TRADE_BY_ITEM_ID_MANAGERS_SHOW_ALL = "TRADE_BY_ITEM_ID_MANAGERS_SHOW_ALL";


    public static final String TRADE_BY_FILTERS_MANAGER_REMOVE_OR_ENABLED_CHANGE = "TRADE_BY_FILTERS_MANAGER_REMOVE";

    public static final String TRADE_BY_FILTERS_MANAGER_CHANGE_ENABLED_FINISH_CONFIRMED = "TRADE_BY_FILTERS_MANAGER_ENABLED_CHANGE_FINISH_CONFIRMED";

    public static final String TRADE_BY_FILTERS_MANAGER_REMOVE_FINISH_CONFIRMED = "TRADE_BY_FILTERS_MANAGER_REMOVE_FINISH_CONFIRMED";


    public static final String TRADE_BY_ITEM_ID_MANAGER_REMOVE_OR_ENABLED_CHANGE = "TRADE_BY_ITEM_ID_MANAGER_REMOVE";

    public static final String TRADE_BY_ITEM_ID_MANAGER_CHANGE_ENABLED_FINISH_CONFIRMED = "TRADE_BY_ITEM_ID_MANAGER_ENABLED_CHANGE_FINISH_CONFIRMED";

    public static final String TRADE_BY_ITEM_ID_MANAGER_REMOVE_FINISH_CONFIRMED = "TRADE_BY_ITEM_ID_MANAGER_REMOVE_FINISH_CONFIRMED";

//--------------------------------------------------------------------------------------------------

    public static final String UBI_ACCOUNT_ENTRY_LINK = "UBI_ACCOUNT_ENTRY_LINK";

    public static final String UBI_ACCOUNT_ENTRY_SHOW = "UBI_ACCOUNT_ENTRY_SHOW";

    public static final String UBI_ACCOUNT_ENTRY_UNLINK_REQUEST = "UBI_ACCOUNT_ENTRY_UNLINK_REQUEST";

    public static final String UBI_ACCOUNT_ENTRY_UNLINK_FINISH_CONFIRMED = "UBI_ACCOUNT_ENTRY_UNLINK_FINISH_CONFIRMED";

    public static final String UBI_ACCOUNT_ENTRY_REAUTHORIZE_2FA_CODE = "UBI_ACCOUNT_ENTRY_REAUTHORIZE_2FA_CODE";
}
