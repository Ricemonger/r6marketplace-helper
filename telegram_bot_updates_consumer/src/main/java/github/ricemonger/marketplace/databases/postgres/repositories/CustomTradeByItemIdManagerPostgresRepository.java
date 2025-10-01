package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.utilspostgresschema.full_entities.user.TradeByItemIdManagerEntity;
import github.ricemonger.utilspostgresschema.ids.user.TradeByItemIdManagerEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomTradeByItemIdManagerPostgresRepository extends JpaRepository<TradeByItemIdManagerEntity, TradeByItemIdManagerEntityId> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE trade_manager_by_item_id t " +
                   "SET enabled = NOT (enabled)" +
                   "FROM telegram_user tu " +
                   "WHERE t.user_id = tu.user_id AND tu.chat_id = :chatId AND t.item_id = :itemId", nativeQuery = true)
    void invertEnabledFlagByUserTelegramUserChatIdAndItemItemId(String chatId, String itemId);

    @Transactional
    @Modifying
    void deleteByUserTelegramUserChatIdAndItemItemId(String chatId, String itemId);

    @Transactional(readOnly = true)
    Optional<TradeByItemIdManagerEntity> findByUserTelegramUserChatIdAndItemItemId(String chatId, String itemId);

    @Transactional(readOnly = true)
    List<TradeByItemIdManagerEntity> findAllByUserTelegramUserChatId(String chatId);
}
