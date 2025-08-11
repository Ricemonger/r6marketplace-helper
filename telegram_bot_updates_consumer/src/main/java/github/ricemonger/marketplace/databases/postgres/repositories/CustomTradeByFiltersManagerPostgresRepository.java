package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.utilspostgresschema.full_entities.user.TradeByFiltersManagerEntity;
import github.ricemonger.utilspostgresschema.ids.user.TradeByFiltersManagerEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomTradeByFiltersManagerPostgresRepository extends JpaRepository<TradeByFiltersManagerEntity, TradeByFiltersManagerEntityId> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE trade_manager_by_item_filters t " +
            "SET enabled = NOT (enabled)" +
            "FROM telegram_user tu " +
            "WHERE t.user_id = tu.user_id AND tu.chat_id = :chatId AND t.name = :name", nativeQuery = true)
    void invertEnabledFlagByUserTelegramUserChatIdAndName(String chatId, String name);

    @Transactional
    @Modifying
    void deleteByUserTelegramUserChatIdAndName(String chatId, String name);

    @Transactional(readOnly = true)
    Optional<TradeByFiltersManagerEntity> findByUserTelegramUserChatIdAndName(String chatId, String name);

    @Transactional(readOnly = true)
    List<TradeByFiltersManagerEntity> findAllByUserTelegramUserChatId(String chatId);
}
