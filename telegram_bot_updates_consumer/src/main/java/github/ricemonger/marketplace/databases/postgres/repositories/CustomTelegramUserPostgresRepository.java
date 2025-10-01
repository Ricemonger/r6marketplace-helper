package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.marketplace.services.DTOs.TelegramUserInputStateAndGroup;
import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CustomTelegramUserPostgresRepository extends JpaRepository<TelegramUserEntity, String> {
    @Transactional
    @Modifying
    @Query("UPDATE TelegramUserEntity t SET t.inputState = :inputState WHERE t.chatId = :chatId")
    void updateInputState(String chatId, InputState inputState);

    @Transactional
    @Modifying
    @Query("UPDATE TelegramUserEntity t SET t.inputState = :inputState, t.inputGroup = :inputGroup WHERE t.chatId = :chatId")
    void updateInputStateAndGroup(String chatId, InputState inputState, InputGroup inputGroup);

    @Transactional
    @Modifying
    @Query("UPDATE TelegramUserEntity t SET t.itemShowFewInMessageFlag = :flag WHERE t.chatId = :chatId")
    void updateItemShowFewItemsInMessageFlag(String chatId, boolean flag);

    @Transactional
    @Modifying
    @Query("UPDATE TelegramUserEntity t SET t.itemShowMessagesLimit = :limit WHERE t.chatId = :chatId")
    void updateItemShowMessagesLimit(String chatId, Integer limit);

    @Transactional(readOnly = true)
    @Query("SELECT new github.ricemonger.marketplace.services.DTOs.TelegramUserInputStateAndGroup(t.chatId, t.inputState, t.inputGroup) " +
           "FROM TelegramUserEntity t WHERE t.chatId = :chatId")
    Optional<TelegramUserInputStateAndGroup> findTelegramUserInputStateAndGroupByChatId(String chatId);
}
