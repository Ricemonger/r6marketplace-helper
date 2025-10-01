package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.marketplace.databases.postgres.dto_projections.ItemShowSettingsProjection;
import github.ricemonger.marketplace.databases.postgres.dto_projections.ItemShownFieldsSettingsProjection;
import github.ricemonger.marketplace.databases.postgres.dto_projections.NotificationsSettingsProjection;
import github.ricemonger.marketplace.databases.postgres.dto_projections.TradeManagersSettingsProjection;
import github.ricemonger.utilspostgresschema.full_entities.user.ItemFilterEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomUserPostgresRepository extends JpaRepository<UserEntity, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.itemShowNameFlag = :#{#settings.itemShowNameFlag}, " +
           "u.itemShowItemTypeFlag = :#{#settings.itemShowItemTypeFlag}, " +
           "u.itemShowMaxBuyPrice = :#{#settings.itemShowMaxBuyPrice}, " +
           "u.itemShowBuyOrdersCountFlag = :#{#settings.itemShowBuyOrdersCountFlag}, " +
           "u.itemShowMinSellPriceFlag = :#{#settings.itemShowMinSellPriceFlag}, " +
           "u.itemsShowSellOrdersCountFlag = :#{#settings.itemsShowSellOrdersCountFlag}, " +
           "u.itemShowPictureFlag = :#{#settings.itemShowPictureFlag} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateItemShowFieldsSettingsByTelegramUserChatId(String chatId, ItemShownFieldsSettingsProjection settings);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_item_show_applied_item_filter (user_id, item_filter_name) " +
                   "VALUES (:userId, :name)", nativeQuery = true)
    void addItemShowAppliedFilter(Long userId, String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_item_show_applied_item_filter " +
                   "WHERE user_id = :userId AND item_filter_name = :name", nativeQuery = true)
    void deleteItemShowAppliedFilter(Long userId, String name);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.newManagersAreActiveFlag = :#{#flag} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateTradeManagersSettingsNewManagersAreActiveFlagByTelegramUserChatId(String chatId, boolean flag);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.managingEnabledFlag = :#{#flag} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateTradeManagersSettingsManagingEnabledFlagByTelegramUserChatId(String chatId, boolean flag);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.sellTradesManagingEnabledFlag = :#{#flag} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateTradeManagersSellSettingsManagingEnabledFlagByTelegramUserChatId(String chatId, boolean flag);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.buyTradesManagingEnabledFlag = :#{#flag} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateTradeManagersBuySettingsManagingEnabledFlagByTelegramUserChatId(String chatId, boolean flag);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.sellTradePriorityExpression = :#{#expression} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateTradeManagersSellSettingsTradePriorityExpressionByTelegramUserChatId(String chatId, String expression);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET " +
           "u.buyTradePriorityExpression = :#{#expression} " +
           "WHERE u.telegramUser.chatId = :chatId")
    void updateTradeManagersBuySettingsTradePriorityExpressionByTelegramUserChatId(String chatId, String expression);

    @Transactional(readOnly = true)
    boolean existsByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    UserEntity getReferenceByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT u.id FROM UserEntity u WHERE u.telegramUser.chatId = :chatId")
    Optional<Long> findUserIdByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT f.name FROM UserEntity u JOIN u.itemShowAppliedFilters f WHERE u.telegramUser.chatId = :chatId")
    List<String> findAllUserItemShowAppliedFiltersNamesByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT f FROM UserEntity u JOIN u.itemShowAppliedFilters f WHERE u.telegramUser.chatId = :chatId")
    List<ItemFilterEntity> findAllUserItemShowAppliedFiltersByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.databases.postgres.dto_projections.ItemShowSettingsProjection(" +
           "u.telegramUser.itemShowMessagesLimit, " +
           "u.telegramUser.itemShowFewInMessageFlag, " +
           "u.itemShowNameFlag, " +
           "u.itemShowItemTypeFlag, " +
           "u.itemShowMaxBuyPrice, " +
           "u.itemShowBuyOrdersCountFlag, " +
           "u.itemShowMinSellPriceFlag, " +
           "u.itemsShowSellOrdersCountFlag, " +
           "u.itemShowPictureFlag) " +
           "FROM UserEntity u WHERE u.telegramUser.chatId = :chatId")
    Optional<ItemShowSettingsProjection> findItemShowSettingsByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.databases.postgres.dto_projections.TradeManagersSettingsProjection(" +
           "u.newManagersAreActiveFlag, " +
           "u.managingEnabledFlag, " +
           "u.sellTradesManagingEnabledFlag, " +
           "u.sellTradePriorityExpression, " +
           "u.buyTradesManagingEnabledFlag," +
           "u.buyTradePriorityExpression) " +
           "FROM UserEntity u WHERE u.telegramUser.chatId = :chatId")
    Optional<TradeManagersSettingsProjection> findTradeManagersSettingsByTelegramUserChatId(String chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE helper_user t " +
                   "SET private_notifications_enabled_flag = NOT (private_notifications_enabled_flag) " +
                   "FROM telegram_user tu " +
                   "WHERE t.id = tu.user_id AND tu.chat_id = :chatId", nativeQuery = true)
    void invertPrivateNotificationsFlagByTelegramUserChatId(String chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE helper_user t " +
                   "SET public_notifications_enabled_flag = NOT (public_notifications_enabled_flag) " +
                   "FROM telegram_user tu " +
                   "WHERE t.id = tu.user_id AND tu.chat_id = :chatId", nativeQuery = true)
    void invertPublicNotificationsFlagByTelegramUserChatId(String chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE helper_user t " +
                   "SET ubi_stats_updated_notifications_enabled_flag = NOT (ubi_stats_updated_notifications_enabled_flag) " +
                   "FROM telegram_user tu " +
                   "WHERE t.id = tu.user_id AND tu.chat_id = :chatId", nativeQuery = true)
    void invertUbiStatsUpdatedNotificationsFlagByTelegramUserChatId(String chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE helper_user t " +
                   "SET trade_manager_notifications_enabled_flag = NOT (trade_manager_notifications_enabled_flag) " +
                   "FROM telegram_user tu " +
                   "WHERE t.id = tu.user_id AND tu.chat_id = :chatId", nativeQuery = true)
    void invertTradeManagerNotificationsFlagByTelegramUserChatId(String chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE helper_user t " +
                   "SET authorization_notifications_enabled_flag = NOT (authorization_notifications_enabled_flag) " +
                   "FROM telegram_user tu " +
                   "WHERE t.id = tu.user_id AND tu.chat_id = :chatId", nativeQuery = true)
    void invertAuthorizationNotificationsFlagByTelegramUserChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.databases.postgres.dto_projections.NotificationsSettingsProjection(" +
           "u.publicNotificationsEnabledFlag," +
           "u.privateNotificationsEnabledFlag," +
           "u.ubiStatsUpdatedNotificationsEnabledFlag," +
           "u.tradeManagerNotificationsEnabledFlag," +
           "u.authorizationNotificationsEnabledFlag) " +
           "FROM UserEntity u WHERE u.telegramUser.chatId = :chatId")
    Optional<NotificationsSettingsProjection> findNotificationsSettingsByTelegramUserChatId(String chatId);
}
