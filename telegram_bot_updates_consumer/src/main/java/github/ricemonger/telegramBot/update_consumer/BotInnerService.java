package github.ricemonger.telegramBot.update_consumer;

import github.ricemonger.marketplace.services.*;
import github.ricemonger.marketplace.services.DTOs.*;
import github.ricemonger.telegramBot.CallbackButton;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.UpdateInfo;
import github.ricemonger.telegramBot.client.TelegramBotClientService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.Tag;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.enums.TagGroup;
import github.ricemonger.utils.enums.TradeOperationType;
import github.ricemonger.utils.exceptions.server.InvalidTelegramUserInputException;
import github.ricemonger.utils.exceptions.server.MissingCallbackPrefixInUserInputException;
import github.ricemonger.utils.services.calculators.TradePriorityExpressionDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotInnerService {

    private final TelegramUserService telegramUserService;

    private final ItemFilterService itemFilterService;

    private final TradeManagerService tradeManagerService;

    private final ItemFilterFromInputsMapper itemFilterFromInputsMapper;

    private final TradeManagerFromInputsMapper tradeManagerFromInputsMapper;

    private final TelegramBotClientService telegramBotClientService;

    private final CommonValuesService commonValuesService;

    private final ItemService itemService;

    private final TagService tagService;

    private final TradePriorityExpressionDeserializer tradePriorityExpressionDeserializer;

    public void sendText(UpdateInfo updateInfo, String text) {
        telegramBotClientService.sendText(updateInfo, text);
    }

    public void askFromInlineKeyboard(UpdateInfo updateInfo, String text, int buttonsInLine, CallbackButton[] buttons) {
        telegramBotClientService.askFromInlineKeyboard(updateInfo, text, buttonsInLine, buttons);
    }

    public void sendMultipleObjectStringsGroupedInMessages(Collection<?> objects, int objectStringHeight, Long chatId) {
        telegramBotClientService.sendMultipleObjectStringsGroupedInMessages(objects, objectStringHeight, chatId);
    }

    public void sendItemsByUserItemShowSettingsAndUserInputOffset(UpdateInfo updateInfo) {
        ItemShowSettings settings = telegramUserService.getUserItemShowSettings(updateInfo.getChatId());

        int offset = getItemOffsetOrZeroByUserCurrentInput(updateInfo);

        List<Item> items = itemService.getAllItemsByFilters(settings.getItemShowAppliedFilters());

        if (offset >= items.size()) {
            telegramBotClientService.sendText(String.valueOf(updateInfo.getChatId()), "Too big offset or strict filters, no items to show");
            return;
        } else {
            items = items.subList(offset, items.size());
        }

        int maxItemsInMessage = settings.getItemShowFewInMessageFlag() ? commonValuesService.getMaximumTelegramMessageHeight() / settings.getActiveFieldsCount() : 1;
        int messageLimit = settings.getItemShowMessagesLimit();
        int messageCount = 0;

        int itemsInCurrentMessageCount = 0;
        StringBuilder currentMessage = new StringBuilder();

        for (Item item : items) {
            if (messageCount >= messageLimit) {
                break;
            }
            currentMessage.append(settings.showItem(item)).append("\n");
            itemsInCurrentMessageCount++;
            if (itemsInCurrentMessageCount >= maxItemsInMessage) {
                telegramBotClientService.sendText(String.valueOf(updateInfo.getChatId()), currentMessage.toString());
                itemsInCurrentMessageCount = 0;
                currentMessage = new StringBuilder();
                messageCount++;
            }
        }
        if (itemsInCurrentMessageCount > 0) {
            telegramBotClientService.sendText(String.valueOf(updateInfo.getChatId()), currentMessage.toString());
        }
    }

    private int getItemOffsetOrZeroByUserCurrentInput(UpdateInfo updateInfo) {
        String offsetInput;
        int offset;

        offsetInput = updateInfo.getMessageText();

        try {
            offset = offsetInput == null ? 0 : Integer.parseInt(offsetInput);
        } catch (NumberFormatException e) {
            offset = 0;
            log.debug("Offset input is invalid for updateInfo-{}", updateInfo);
        }

        return offset;
    }

    public void deleteMessage(UpdateInfo updateInfo) {
        telegramBotClientService.deleteMessage(updateInfo);
    }

    public void registerUser(Long chatId) {
        telegramUserService.registerTelegramUser(chatId);
    }

    public boolean isUserRegistered(Long chatId) {
        return telegramUserService.isTelegramUserRegistered(chatId);
    }

    public void setUserInputState(Long chatId, InputState inputState) {
        telegramUserService.setUserInputState(chatId, inputState);
    }

    public void saveUserInput(UpdateInfo updateInfo) {
        String userInput;

        if (updateInfo.isHasMessage()) {
            userInput = updateInfo.getMessageText();
        } else if (updateInfo.isHasCallBackQuery()) {
            userInput = updateInfo.getCallbackQueryData();
        } else {
            throw new InvalidTelegramUserInputException("UpdateInfo has no message or callback query");
        }
        telegramUserService.saveUserInput(updateInfo.getChatId(), updateInfo.getInputState(), userInput);
    }

    public void setUserInputStateAndGroup(Long chatId, InputState nextState, InputGroup nextGroup) {
        telegramUserService.setUserInputStateAndGroup(chatId, nextState, nextGroup);
    }

    public void clearUserInputsAndSetInputStateAndGroup(Long chatId, InputState inputState, InputGroup inputGroup) {
        telegramUserService.clearUserInputsAndSetInputStateAndGroup(chatId, inputState, inputGroup);
    }

    public String getUserInputByState(Long chatId, InputState inputState) {
        return telegramUserService.getUserInputByState(chatId, inputState);
    }

    public String getStringOfAllTagsNamesByTagGroup(TagGroup tagGroup) {
        return tagService.getAllTagsByTagGroup(tagGroup).stream().map(Tag::getName).reduce((s, s2) -> s + "," + s2).orElse("");
    }

    public void saveUserItemFilterByUserInput(Long chatId) {
        itemFilterService.save(String.valueOf(chatId), generateItemFilterByUserInput(chatId));
    }

    public ItemFilter generateItemFilterByUserInput(Long chatId) {
        Collection<TelegramUserInput> inputs = telegramUserService.getAllUserInputs(chatId);

        return itemFilterFromInputsMapper.generateItemFilterByUserInput(inputs);
    }

    public List<String> getAllUserItemFiltersNames(Long chatId) {
        return itemFilterService.getAllNamesByChatId(String.valueOf(chatId));
    }

    public ItemFilter getUserItemFilterByUserCurrentInputCallbackFilterName(UpdateInfo updateInfo) {
        return itemFilterService.getById(String.valueOf(updateInfo.getChatId()), removeCallbackPrefixFromInput(updateInfo.getCallbackQueryData()));
    }

    public void removeUserItemFilterByUserInputCallbackFilterName(Long chatId) {
        itemFilterService.deleteById(String.valueOf(chatId), getUserInputValueWithoutCallbackPrefix(chatId, InputState.ITEM_FILTER_NAME));
    }

    public List<String> getItemShowAppliedFiltersNames(Long chatId) {
        return telegramUserService.getUserItemShowAppliedFiltersNames(chatId);
    }

    public ItemShowSettings getUserItemShowSettings(Long chatId) {
        return telegramUserService.getUserItemShowSettings(chatId);
    }

    public void setUserItemShowSettingsFewItemsInMessageFlag(Long chatId, boolean flag) {
        telegramUserService.setUserItemShowFewItemsInMessageFlag(chatId, flag);
    }

    public Integer setUserItemShowSettingsMessageLimitOrEdgeValueByUserInput(Long chatId) {
        return telegramUserService.setUserItemShowMessagesLimitByUserInput(chatId);
    }

    public void setUserItemShownFieldsSettingByUserInput(Long chatId) {
        telegramUserService.setUserItemShownFieldsSettingsByUserInput(chatId, Callbacks.INPUT_CALLBACK_TRUE, Callbacks.INPUT_CALLBACK_FALSE);
    }

    public void addItemShowAppliedFilterByUserInput(Long chatId) {
        telegramUserService.addUserItemShowAppliedFilter(chatId, getUserInputValueWithoutCallbackPrefix(chatId, InputState.ITEM_FILTER_NAME));
    }

    public void deleteItemShowAppliedFilterByUserInput(Long chatId) {
        telegramUserService.removeUserItemShowAppliedFilter(chatId, getUserInputValueWithoutCallbackPrefix(chatId, InputState.ITEM_FILTER_NAME));
    }

    public void saveUserTradeByItemIdManagerByUserInput(Long chatId, TradeOperationType tradeOperationType) {
        tradeManagerService.saveTradeByItemIdManager(String.valueOf(chatId), generateTradeByItemIdManagerByUserInput(chatId, tradeOperationType));
    }

    public void saveUserTradeByFiltersManagerByUserInput(Long chatId) {
        tradeManagerService.saveTradeByFiltersManager(String.valueOf(chatId), generateTradeByFiltersManagerByUserInput(chatId));
    }

    public TradeByItemIdManager generateTradeByItemIdManagerByUserInput(Long chatId, TradeOperationType tradeOperationType) {
        Collection<TelegramUserInput> inputs = telegramUserService.getAllUserInputs(chatId);

        boolean enabledFlag = getUserTradeManagersSettings(chatId).getNewManagersAreActiveFlag();

        return tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, tradeOperationType, enabledFlag);
    }

    public TradeByFiltersManager generateTradeByFiltersManagerByUserInput(Long chatId) {
        Collection<TelegramUserInput> inputs = telegramUserService.getAllUserInputs(chatId);

        boolean enabledFlag = getUserTradeManagersSettings(chatId).getNewManagersAreActiveFlag();

        return tradeManagerFromInputsMapper.generateTradeByFiltersManagerByUserInput(inputs, enabledFlag);
    }

    public Item getItemByUserInputItemId(Long chatId) {
        return itemService.getItemById(getUserInputByState(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID));
    }

    public void invertUserTradeByFiltersManagerEnabledByUserInput(Long chatId) {
        tradeManagerService.invertTradeByFiltersManagerEnabledFlagById(String.valueOf(chatId), getUserInputByState(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME));
    }

    public void invertUserTradeByItemIdManagerEnabledByUserInput(Long chatId) {
        tradeManagerService.invertTradeByItemIdManagerEnabledFlagById(String.valueOf(chatId), getUserInputByState(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID));
    }

    public void removeUserTradeByItemIdManagerByUserInput(Long chatId) {
        tradeManagerService.deleteTradeByItemIdManagerById(String.valueOf(chatId), getUserInputByState(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID));
    }

    public void removeUserTradeByFiltersManagerByUserInput(Long chatId) {
        tradeManagerService.deleteTradeByFiltersManagerById(String.valueOf(chatId), getUserInputByState(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME));
    }

    public TradeByItemIdManager getUserTradeByItemIdManagerByUserInputItemId(Long chatId) {
        return tradeManagerService.getTradeByItemIdManagerById(String.valueOf(chatId), getUserInputByState(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID));
    }

    public TradeByFiltersManager getUserTradeByFiltersManagerByUserInputName(Long chatId) {
        return tradeManagerService.getTradeByFiltersManagerById(String.valueOf(chatId), getUserInputByState(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME));
    }

    public List<TradeByItemIdManager> getAllUserTradeByItemIdManagers(Long chatId) {
        return tradeManagerService.getAllTradeByItemIdManagersByChatId(String.valueOf(chatId));
    }

    public List<TradeByFiltersManager> getAllUserTradeByFiltersManagers(Long chatId) {
        return tradeManagerService.getAllTradeByFiltersManagersByChatId(String.valueOf(chatId));
    }

    public TradeManagersSettings getUserTradeManagersSettings(Long chatId) {
        return telegramUserService.getUserTradeManagersSettings(chatId);
    }

    public void setUserTradeManagersSettingsNewManagersAreActiveFlag(Long chatId, boolean flag) {
        telegramUserService.setUserTradeManagersSettingsNewManagersAreActiveFlag(chatId, flag);
    }

    public void setUserTradeManagersSettingsManagingEnabledFlag(Long chatId, boolean flag) {
        telegramUserService.setUserTradeManagersSettingsManagingEnabledFlag(chatId, flag);
    }

    public void setUserTradeManagersSellSettingsManagingEnabledFlag(Long chatId, boolean flag) {
        telegramUserService.setUserTradeManagersSellSettingsManagingEnabledFlag(chatId, flag);
    }

    public void setUserTradeManagersBuySettingsManagingEnabledFlag(Long chatId, boolean flag) {
        telegramUserService.setUserTradeManagersBuySettingsManagingEnabledFlag(chatId, flag);
    }

    public void setUserTradeManagersSellSettingsTradePriorityExpressionByUserInput(Long chatId) {
        String expression = getUserInputByState(chatId, InputState.TRADE_MANAGERS_SETTINGS_TRADE_PRIORITY_EXPRESSION);

        telegramUserService.setUserTradeManagersSellSettingsTradePriorityExpression(chatId, expression);
    }

    public void setUserTradeManagersBuySettingsTradePriorityExpressionByUserInput(Long chatId) {
        String expression = getUserInputByState(chatId, InputState.TRADE_MANAGERS_SETTINGS_TRADE_PRIORITY_EXPRESSION);

        telegramUserService.setUserTradeManagersBuySettingsTradePriorityExpression(chatId, expression);
    }

    public void invertUserPrivateNotificationsFlag(Long chatId) {
        telegramUserService.invertUserPrivateNotificationsFlag(chatId);
    }

    public void invertUserPublicNotificationsFlag(Long chatId) {
        telegramUserService.invertUserPublicNotificationsFlag(chatId);
    }

    public void invertUserAuthorizationNotificationsFlag(Long chatId) {
        telegramUserService.invertUserAuthorizationNotificationsFlag(chatId);
    }

    public void invertUserTradeManagerNotificationsFlag(Long chatId) {
        telegramUserService.invertUserTradeManagerNotificationsFlag(chatId);
    }

    public void invertUserUbiStatsUpdatedNotificationsFlag(Long chatId) {
        telegramUserService.invertUserUbiStatsUpdatedNotificationsFlag(chatId);
    }

    public NotificationsSettings getUserNotificationsSettings(Long chatId) {
        return telegramUserService.getUserNotificationsSettings(chatId);
    }

    public void addUserUbiAccountEntryByUserInput(Long chatId) {
        telegramUserService.authorizeAndSaveUbiUserByUserInput(chatId);
    }

    public void reauthorizeUbiAccountEntryBy2FACode(Long chatId) {
        telegramUserService.reauthorizeAndSaveExistingUbiUserBy2FACodeByUserInput(chatId);
    }

    public void removeUserUbiAccountEntry(Long chatId) {
        telegramUserService.removeUbiUserByChatId(chatId);
    }

    public String getUserUbiAccountEntryEmail(Long chatId) {
        return telegramUserService.getUbiUserByChatId(chatId).getEmail();
    }

    public boolean isTradePriorityExpressionValid(String expression) {
        return tradePriorityExpressionDeserializer.isValidExpression(expression);
    }

    private String getUserInputValueWithoutCallbackPrefix(Long chatId, InputState inputState) {
        return removeCallbackPrefixFromInput(telegramUserService.getUserInputByState(chatId, inputState));
    }

    private String removeCallbackPrefixFromInput(String inputValue) {
        try {
            return inputValue.substring(Callbacks.INPUT_CALLBACK_PREFIX.length());
        } catch (StringIndexOutOfBoundsException e) {
            throw new MissingCallbackPrefixInUserInputException("Callback prefix is missing in user input: " + inputValue);
        }
    }
}
