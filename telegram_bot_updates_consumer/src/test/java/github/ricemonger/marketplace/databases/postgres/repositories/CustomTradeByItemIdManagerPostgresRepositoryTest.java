package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.TelegramUserEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.TradeByItemIdManagerEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Disabled
class CustomTradeByItemIdManagerPostgresRepositoryTest {
    @Autowired
    private CustomTradeByItemIdManagerPostgresRepository customTradeByItemIdManagerPostgresRepository;
    @Autowired
    private CustomTelegramUserPostgresRepository customTelegramUserPostgresRepository;
    @Autowired
    private CustomUserPostgresRepository customUserPostgresRepository;
    @Autowired
    private ItemPostgresRepository itemPostgresRepository;

    @BeforeEach
    void setUp() {
        customTradeByItemIdManagerPostgresRepository.deleteAll();
        customTelegramUserPostgresRepository.deleteAll();
        customUserPostgresRepository.deleteAll();
        itemPostgresRepository.deleteAll();
    }

    @Test
    public void invertEnabledFlagByUserTelegramUserChatIdAndItemItemId_should_invert_enabled_flag_for_expected_filter() {
        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setItemId("itemId1");
        itemEntity1 = itemPostgresRepository.save(itemEntity1);

        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setItemId("itemId2");
        itemEntity2 = itemPostgresRepository.save(itemEntity2);

        UserEntity userEntity1 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity1 = new TelegramUserEntity();
        telegramUserEntity1.setChatId("chatId1");
        telegramUserEntity1.setUser(userEntity1);
        telegramUserEntity1 = customTelegramUserPostgresRepository.save(telegramUserEntity1);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity11 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity11.setItem(itemEntity1);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity11.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity11);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity12 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity12.setItem(itemEntity2);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity12.setEnabled(false);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity12);

        UserEntity userEntity2 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity2 = new TelegramUserEntity();
        telegramUserEntity2.setChatId("chatId2");
        telegramUserEntity2.setUser(userEntity2);
        telegramUserEntity2 = customTelegramUserPostgresRepository.save(telegramUserEntity2);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity21 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity21.setItem(itemEntity1);
        telegramUserEntity2.setUser(userEntity2);
        tradeByItemIdManagerEntity21.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity21);

        customTradeByItemIdManagerPostgresRepository.invertEnabledFlagByUserTelegramUserChatIdAndItemItemId("chatId1", "itemId1");
        customTradeByItemIdManagerPostgresRepository.invertEnabledFlagByUserTelegramUserChatIdAndItemItemId("chatId1", "itemId2");

        List<TradeByItemIdManagerEntity> entities = customTradeByItemIdManagerPostgresRepository.findAll();

        assertEquals(3, entities.size());
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId1") && entity.getUser().equals(userEntity1) && !entity.getEnabled()));
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId2") && entity.getUser().equals(userEntity1) && entity.getEnabled()));
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId1") && entity.getUser().equals(userEntity2) && entity.getEnabled()));
    }

    @Test
    public void deleteByUserTelegramUserChatIdAndItemItemId_should_remove_expected_filter() {
        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setItemId("itemId1");
        itemEntity1 = itemPostgresRepository.save(itemEntity1);

        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setItemId("itemId2");
        itemEntity2 = itemPostgresRepository.save(itemEntity2);

        UserEntity userEntity1 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity1 = new TelegramUserEntity();
        telegramUserEntity1.setChatId("chatId1");
        telegramUserEntity1.setUser(userEntity1);
        telegramUserEntity1 = customTelegramUserPostgresRepository.save(telegramUserEntity1);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity11 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity11.setItem(itemEntity1);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity11.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity11);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity12 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity12.setItem(itemEntity2);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity12.setEnabled(false);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity12);

        UserEntity userEntity2 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity2 = new TelegramUserEntity();
        telegramUserEntity2.setChatId("chatId2");
        telegramUserEntity2.setUser(userEntity2);
        telegramUserEntity2 = customTelegramUserPostgresRepository.save(telegramUserEntity2);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity21 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity21.setItem(itemEntity1);
        telegramUserEntity2.setUser(userEntity2);
        tradeByItemIdManagerEntity21.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity21);

        customTradeByItemIdManagerPostgresRepository.deleteByUserTelegramUserChatIdAndItemItemId("chatId1", "itemId1");

        List<TradeByItemIdManagerEntity> entities = customTradeByItemIdManagerPostgresRepository.findAll();

        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId2") && entity.getUser().equals(userEntity1)));
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId1") && entity.getUser().equals(userEntity2)));
    }

    @Test
    public void findByUserTelegramUserChatIdAndItemItemId_should_return_expected_filter() {
        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setItemId("itemId1");
        itemEntity1 = itemPostgresRepository.save(itemEntity1);

        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setItemId("itemId2");
        itemEntity2 = itemPostgresRepository.save(itemEntity2);

        UserEntity userEntity1 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity1 = new TelegramUserEntity();
        telegramUserEntity1.setChatId("chatId1");
        telegramUserEntity1.setUser(userEntity1);
        telegramUserEntity1 = customTelegramUserPostgresRepository.save(telegramUserEntity1);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity11 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity11.setItem(itemEntity1);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity11.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity11);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity12 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity12.setItem(itemEntity2);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity12.setEnabled(false);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity12);

        UserEntity userEntity2 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity2 = new TelegramUserEntity();
        telegramUserEntity2.setChatId("chatId2");
        telegramUserEntity2.setUser(userEntity2);
        telegramUserEntity2 = customTelegramUserPostgresRepository.save(telegramUserEntity2);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity21 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity21.setItem(itemEntity1);
        telegramUserEntity2.setUser(userEntity2);
        tradeByItemIdManagerEntity21.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity21);

        List<TradeByItemIdManagerEntity> entities = customTradeByItemIdManagerPostgresRepository.findAll();
        assertEquals(3, entities.size());

        TradeByItemIdManagerEntity entity = customTradeByItemIdManagerPostgresRepository.findByUserTelegramUserChatIdAndItemItemId("chatId1", "itemId1").get();

        assertEquals("itemId1", entity.getItemId_());
        assertEquals(userEntity1, entity.getUser());
        assertTrue(entity.getEnabled());
    }

    @Test
    public void findAllByUserTelegramUserChatId_should_return_expected_filters() {
        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setItemId("itemId1");
        itemEntity1 = itemPostgresRepository.save(itemEntity1);

        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setItemId("itemId2");
        itemEntity2 = itemPostgresRepository.save(itemEntity2);

        UserEntity userEntity1 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity1 = new TelegramUserEntity();
        telegramUserEntity1.setChatId("chatId1");
        telegramUserEntity1.setUser(userEntity1);
        telegramUserEntity1 = customTelegramUserPostgresRepository.save(telegramUserEntity1);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity11 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity11.setItem(itemEntity1);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity11.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity11);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity12 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity12.setItem(itemEntity2);
        telegramUserEntity1.setUser(userEntity1);
        tradeByItemIdManagerEntity12.setEnabled(false);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity12);

        UserEntity userEntity2 = customUserPostgresRepository.save(new UserEntity());
        TelegramUserEntity telegramUserEntity2 = new TelegramUserEntity();
        telegramUserEntity2.setChatId("chatId2");
        telegramUserEntity2.setUser(userEntity2);
        telegramUserEntity2 = customTelegramUserPostgresRepository.save(telegramUserEntity2);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity21 = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity21.setItem(itemEntity1);
        telegramUserEntity2.setUser(userEntity2);
        tradeByItemIdManagerEntity21.setEnabled(true);
        customTradeByItemIdManagerPostgresRepository.save(tradeByItemIdManagerEntity21);

        List<TradeByItemIdManagerEntity> allEntities = customTradeByItemIdManagerPostgresRepository.findAll();
        assertEquals(3, allEntities.size());

        List<TradeByItemIdManagerEntity> entities = customTradeByItemIdManagerPostgresRepository.findAllByUserTelegramUserChatId("chatId1");

        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId1") && entity.getUser().equals(userEntity1) && entity.getEnabled()));
        assertTrue(entities.stream().anyMatch(entity -> entity.getItemId_().equals("itemId2") && entity.getUser().equals(userEntity1) && !entity.getEnabled()));
    }
}