package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.marketplace.databases.postgres.dto_projections.TelegramUserInputProjection;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserInputEntity;
import github.ricemonger.utilspostgresschema.ids.user.TelegramUserInputEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomTelegramUserInputPostgresRepository extends JpaRepository<TelegramUserInputEntity, TelegramUserInputEntityId> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM telegram_user_input t WHERE chat_id = :chatId", nativeQuery = true)
    void deleteAllByChatId(String chatId);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.databases.postgres.dto_projections.TelegramUserInputProjection(" +
           "t.telegramUser.chatId," +
           " t.inputState," +
           " t.value) " +
           "FROM TelegramUserInputEntity t WHERE t.telegramUser.chatId = :chatId AND t.inputState = :inputState")
    Optional<TelegramUserInputProjection> findInputById(String chatId, InputState inputState);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.databases.postgres.dto_projections.TelegramUserInputProjection(" +
           "t.telegramUser.chatId," +
           " t.inputState," +
           " t.value) " +
           "FROM TelegramUserInputEntity t WHERE t.telegramUser.chatId = :chatId")
    List<TelegramUserInputProjection> findAllByChatId(String chatId);
}
