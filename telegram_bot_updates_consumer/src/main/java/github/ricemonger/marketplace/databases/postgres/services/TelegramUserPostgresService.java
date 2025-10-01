package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.repositories.CustomTelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.CustomUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.TelegramUserEntityMapper;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.UserEntityMapper;
import github.ricemonger.marketplace.services.DTOs.*;
import github.ricemonger.marketplace.services.abstractions.TelegramUserDatabaseService;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.client.TelegramUserAlreadyExistsException;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramUserPostgresService implements TelegramUserDatabaseService {

    private final CustomTelegramUserPostgresRepository telegramUserRepository;

    private final TelegramUserEntityMapper telegramUserEntityMapper;

    private final UserEntityMapper userEntityMapper;

    private final CustomUserPostgresRepository userRepository;

    @Override
    @Transactional
    public void register(String chatId) throws TelegramUserAlreadyExistsException {
        if (telegramUserRepository.existsById(chatId)) {
            throw new TelegramUserAlreadyExistsException("Telegram user with chatId " + chatId + " already exists");
        } else {
            telegramUserRepository.save(telegramUserEntityMapper.createNewEntityForNewUser(chatId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRegistered(String chatId) {
        return telegramUserRepository.existsById(chatId);
    }

    @Override
    @Transactional
    public void setUserInputState(String chatId, InputState inputState) {
        telegramUserRepository.updateInputState(chatId, inputState);
    }

    @Override
    @Transactional
    public void setUserInputStateAndGroup(String chatId, InputState inputState, InputGroup inputGroup) {
        telegramUserRepository.updateInputStateAndGroup(chatId, inputState, inputGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramUserInputStateAndGroup findUserInputStateAndGroupById(String chatId) throws TelegramUserDoesntExistException {
        return telegramUserRepository.findTelegramUserInputStateAndGroupByChatId(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found"));
    }

    @Override
    @Transactional
    public void setUserItemShowFewItemsInMessageFlag(String chatId, boolean flag) throws TelegramUserDoesntExistException {
        telegramUserRepository.updateItemShowFewItemsInMessageFlag(chatId, flag);
    }

    @Override
    @Transactional
    public void setUserItemShowMessagesLimit(String chatId, Integer limit) throws TelegramUserDoesntExistException {
        telegramUserRepository.updateItemShowMessagesLimit(chatId, limit);
    }

    @Override
    @Transactional
    public void setUserItemShowFieldsSettings(String chatId, ItemShownFieldsSettings settings) throws TelegramUserDoesntExistException {
        userRepository.updateItemShowFieldsSettingsByTelegramUserChatId(chatId, userEntityMapper.createItemShownFieldsSettingsProjection(settings));
    }

    @Override
    @Transactional
    public void addUserItemShowAppliedFilter(String chatId, String filterName) throws TelegramUserDoesntExistException {
        Long userId = userRepository.findUserIdByTelegramUserChatId(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found"));

        userRepository.addItemShowAppliedFilter(userId, filterName);
    }

    @Override
    @Transactional
    public void removeUserItemShowAppliedFilter(String chatId, String filterName) throws TelegramUserDoesntExistException {
        Long userId = userRepository.findUserIdByTelegramUserChatId(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found"));

        userRepository.deleteItemShowAppliedFilter(userId, filterName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllUserItemShowAppliedFiltersNames(String chatId) throws TelegramUserDoesntExistException {
        return userRepository.findAllUserItemShowAppliedFiltersNamesByTelegramUserChatId(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemShowSettings findUserItemShowSettings(String chatId) throws TelegramUserDoesntExistException {
        return userEntityMapper.createItemShowSettings(
                userRepository.findItemShowSettingsByTelegramUserChatId(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found")),
                userRepository.findAllUserItemShowAppliedFiltersByTelegramUserChatId(chatId)
        );
    }

    @Override
    @Transactional
    public void setUserTradeManagersSettingsNewManagersAreActiveFlag(String chatId, boolean flag) throws TelegramUserDoesntExistException {
        userRepository.updateTradeManagersSettingsNewManagersAreActiveFlagByTelegramUserChatId(chatId, flag);
    }

    @Override
    @Transactional
    public void setUserTradeManagersSettingsManagingEnabledFlag(String chatId, boolean flag) throws TelegramUserDoesntExistException {
        userRepository.updateTradeManagersSettingsManagingEnabledFlagByTelegramUserChatId(chatId, flag);
    }

    @Override
    @Transactional
    public void setUserTradeManagersSellSettingsManagingEnabledFlag(String chatId, boolean flag) {
        userRepository.updateTradeManagersSellSettingsManagingEnabledFlagByTelegramUserChatId(chatId, flag);
    }

    @Override
    @Transactional
    public void setUserTradeManagersBuySettingsManagingEnabledFlag(String chatId, boolean flag) {
        userRepository.updateTradeManagersBuySettingsManagingEnabledFlagByTelegramUserChatId(chatId, flag);
    }

    @Override
    @Transactional
    public void setUserTradeManagersSellSettingsTradePriorityExpression(String chatId, String expression) {
        userRepository.updateTradeManagersSellSettingsTradePriorityExpressionByTelegramUserChatId(chatId, expression);
    }

    @Override
    @Transactional
    public void setUserTradeManagersBuySettingsTradePriorityExpression(String chatId, String expression) {
        userRepository.updateTradeManagersBuySettingsTradePriorityExpressionByTelegramUserChatId(chatId, expression);
    }

    @Override
    @Transactional(readOnly = true)
    public TradeManagersSettings findUserTradeManagersSettings(String chatId) throws TelegramUserDoesntExistException {
        return userEntityMapper.createTradeManagersSettings(userRepository.findTradeManagersSettingsByTelegramUserChatId(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found")));
    }

    @Override
    @Transactional
    public void invertUserPrivateNotificationsFlag(String chatId) {
        userRepository.invertPrivateNotificationsFlagByTelegramUserChatId(chatId);
    }

    @Override
    @Transactional
    public void invertUserPublicNotificationsFlag(String chatId) {
        userRepository.invertPublicNotificationsFlagByTelegramUserChatId(chatId);
    }

    @Override
    @Transactional
    public void invertUserUbiStatsUpdatedNotificationsFlag(String chatId) {
        userRepository.invertUbiStatsUpdatedNotificationsFlagByTelegramUserChatId(chatId);
    }

    @Override
    @Transactional
    public void invertUserTradeManagerNotificationsFlag(String chatId) {
        userRepository.invertTradeManagerNotificationsFlagByTelegramUserChatId(chatId);
    }

    @Override
    @Transactional
    public void invertUserAuthorizationNotificationsFlag(String chatId) {
        userRepository.invertAuthorizationNotificationsFlagByTelegramUserChatId(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationsSettings findUserNotificationsSettings(String chatId) throws TelegramUserDoesntExistException {
        return userEntityMapper.createNotificationsSettings(userRepository.findNotificationsSettingsByTelegramUserChatId(chatId).orElseThrow(() -> new TelegramUserDoesntExistException("Telegram user with chatId " + chatId + " not found")));
    }
}
