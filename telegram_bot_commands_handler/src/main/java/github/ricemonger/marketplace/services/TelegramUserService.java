package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.services.DTOs.*;
import github.ricemonger.marketplace.services.abstractions.TelegramUserDatabaseService;
import github.ricemonger.marketplace.services.abstractions.TelegramUserInputDatabaseService;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.server.MissingCallbackPrefixInUserInputException;
import github.ricemonger.utils.exceptions.server.TelegramUserInputDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserDatabaseService telegramUserDatabaseService;

    private final TelegramUserInputDatabaseService inputDatabaseService;

    private final UbiAccountEntryService ubiAccountEntryService;

    private final CommonValuesService commonValuesService;

    public void registerTelegramUser(Long chatId) {
        telegramUserDatabaseService.register(String.valueOf(chatId));
    }

    public boolean isTelegramUserRegistered(Long chatId) {
        return telegramUserDatabaseService.isRegistered(String.valueOf(chatId));
    }

    public void setUserInputState(Long chatId, InputState inputState) {
        telegramUserDatabaseService.setUserInputState(String.valueOf(chatId), inputState);
    }

    public void setUserInputGroup(Long chatId, InputGroup inputGroup) {
        telegramUserDatabaseService.setUserInputGroup(String.valueOf(chatId), inputGroup);
    }

    public void setUserInputStateAndGroup(Long chatId, InputState inputState, InputGroup inputGroup) {
        telegramUserDatabaseService.setUserInputStateAndGroup(String.valueOf(chatId), inputState, inputGroup);
    }

    public void saveUserInput(Long chatId, InputState inputState, String userInput) {
        inputDatabaseService.save(String.valueOf(chatId), inputState, userInput);
    }

    public void clearUserInputsAndSetInputStateAndGroup(Long chatId, InputState inputState, InputGroup inputGroup) {
        inputDatabaseService.deleteAllByChatId(String.valueOf(chatId));
        telegramUserDatabaseService.setUserInputStateAndGroup(String.valueOf(chatId), inputState, inputGroup);
    }

    public String getUserInputByState(Long chatId, InputState inputState) {
        return getInputValueByState(chatId, inputState);
    }

    public List<TelegramUserInput> getAllUserInputs(Long chatId) {
        return inputDatabaseService.findAllByChatId(String.valueOf(chatId));
    }

    public TelegramUserInputStateAndGroup getTelegramUserInputStateAndGroup(Long chatId) {
        return telegramUserDatabaseService.findUserInputStateAndGroupById(String.valueOf(chatId));
    }

    public ItemShowSettings getItemShowSettings(Long chatId) {
        return telegramUserDatabaseService.findUserItemShowSettings(String.valueOf(chatId));
    }

    public void setItemShowFewItemsInMessageFlag(Long chatId, boolean flag) {
        telegramUserDatabaseService.setUserItemShowFewItemsInMessageFlag(String.valueOf(chatId), flag);
    }

    public Integer setItemShowMessagesLimitByUserInput(Long chatId) {
        Integer limit;

        Integer maximumTelegramMessageLimit = commonValuesService.getMaximumTelegramMessageLimit();

        try {
            limit = Integer.parseInt(getUserInputByState(chatId, InputState.ITEMS_SHOW_SETTING_MESSAGE_LIMIT));
            if (limit < 1) {
                limit = 1;
            } else if (limit > maximumTelegramMessageLimit) {
                limit = maximumTelegramMessageLimit;
            }
        } catch (TelegramUserInputDoesntExistException | NumberFormatException e) {
            limit = maximumTelegramMessageLimit;
        }

        telegramUserDatabaseService.setUserItemShowMessagesLimit(String.valueOf(chatId), limit);

        return limit;
    }

    public void setItemShownFieldsSettingsByUserInput(Long chatId, String trueValue, String falseValue) {
        List<TelegramUserInput> inputs = inputDatabaseService.findAllByChatId(String.valueOf(chatId));

        boolean nameFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_ITEM_NAME, inputs), falseValue);
        boolean itemTypeFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_ITEM_TYPE, inputs), falseValue);
        boolean maxBuyPriceFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_MAX_BUY_PRICE, inputs), falseValue);
        boolean buyOrdersCountFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_BUY_ORDERS_COUNT, inputs), falseValue);
        boolean minSellPriceFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_MIN_SELL_PRICE, inputs), falseValue);
        boolean sellOrdersCountFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_SELL_ORDERS_COUNT, inputs), falseValue);
        boolean pictureFlag = parseBooleanOrTrue(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_PICTURE, inputs), falseValue);

        ItemShownFieldsSettings settings = new ItemShownFieldsSettings(
                nameFlag,
                itemTypeFlag,
                maxBuyPriceFlag,
                buyOrdersCountFlag,
                minSellPriceFlag,
                sellOrdersCountFlag,
                pictureFlag);

        telegramUserDatabaseService.setUserItemShowFieldsSettings(String.valueOf(chatId), settings);
    }

    public void updateItemShowAppliedFiltersSettingsByUserInput(Long chatId, String trueValue, String falseValue) {
        List<TelegramUserInput> inputs = inputDatabaseService.findAllByChatId(String.valueOf(chatId));

        boolean addOrRemove = parseBooleanOrFalse(getInputValueByStateFromCollection(InputState.ITEMS_SHOW_SETTINGS_APPLIED_FILTER_ADD_OR_REMOVE, inputs), trueValue);
        String filterName = removeCallbackPrefixFromInput(getInputValueByStateFromCollection(InputState.ITEM_FILTER_NAME, inputs));

        if (addOrRemove) {
            telegramUserDatabaseService.addUserItemShowAppliedFilter(String.valueOf(chatId), filterName);
        } else {
            telegramUserDatabaseService.removeUserItemShowAppliedFilter(String.valueOf(chatId), filterName);
        }
    }

    public List<String> getUserItemShowAppliedFiltersNames(Long chatId) {
        return telegramUserDatabaseService.findAllUserItemShowAppliedFiltersNames(String.valueOf(chatId));
    }

    public TradeManagersSettings getTradeManagersSettings(Long chatId) {
        return telegramUserDatabaseService.findUserTradeManagersSettings(String.valueOf(chatId));
    }

    public void setTradeManagersSettingsNewManagersAreActiveFlag(Long chatId, boolean flag) {
        telegramUserDatabaseService.setUserTradeManagersSettingsNewManagersAreActiveFlag(String.valueOf(chatId), flag);
    }

    public void setTradeManagersSettingsManagingEnabledFlag(Long chatId, boolean flag) {
        telegramUserDatabaseService.setUserTradeManagersSettingsManagingEnabledFlag(String.valueOf(chatId), flag);
    }

    public void authorizeAndSaveUbiUserByUserInput(Long chatId) {
        List<TelegramUserInput> inputs = inputDatabaseService.findAllByChatId(String.valueOf(chatId));

        String email = getInputValueByStateFromCollection(InputState.UBI_ACCOUNT_ENTRY_EMAIL, inputs);
        String password = getInputValueByStateFromCollection(InputState.UBI_ACCOUNT_ENTRY_PASSWORD, inputs);
        String twoFaCode = getInputValueByStateFromCollection(InputState.UBI_ACCOUNT_ENTRY_2FA_CODE, inputs);

        inputDatabaseService.deleteAllByChatId(String.valueOf(chatId));

        ubiAccountEntryService.authorizeAndSaveUbiAccountEntry(String.valueOf(chatId), email, password, twoFaCode);
    }

    public void reauthorizeAndSaveExistingUbiUserBy2FACodeByUserInput(Long chatId) {
        String twoFaCode = getUserInputByState(chatId, InputState.UBI_ACCOUNT_ENTRY_2FA_CODE);

        ubiAccountEntryService.reauthorizeAndSaveExistingUbiAccountEntryBy2FACode(String.valueOf(chatId), twoFaCode);
    }

    public void removeUbiUserByChatId(Long chatId) {
        ubiAccountEntryService.deleteByChatId(String.valueOf(chatId));
    }

    public UbiAccountAuthorizationEntry getUbiUserByChatId(Long chatId) {
        return ubiAccountEntryService.findByChatId(String.valueOf(chatId));
    }

    private boolean parseBooleanOrFalse(String inputValueByStateFromCollection, String trueValue) {
        return trueValue.equalsIgnoreCase(inputValueByStateFromCollection);
    }

    private boolean parseBooleanOrTrue(String value, String falseValue) {
        return !falseValue.equalsIgnoreCase(value);
    }

    private String getInputValueByState(Long chatId, InputState inputState) {
        return inputDatabaseService.findById(String.valueOf(chatId), inputState).getValue();
    }

    private String getInputValueByStateFromCollection(InputState inputState, Collection<TelegramUserInput> inputs) {
        return inputs
                .stream()
                .filter(input -> input.getInputState().equals(inputState))
                .findFirst()
                .orElseThrow(() -> new TelegramUserInputDoesntExistException("Input with state " + inputState + " could no be found in collection: " + inputs))
                .getValue();
    }

    private String removeCallbackPrefixFromInput(String inputValue) {
        try {
            return inputValue.substring(Callbacks.INPUT_CALLBACK_PREFIX.length());
        } catch (StringIndexOutOfBoundsException e) {
            throw new MissingCallbackPrefixInUserInputException("Callback prefix is missing in user input: " + inputValue);
        }
    }
}