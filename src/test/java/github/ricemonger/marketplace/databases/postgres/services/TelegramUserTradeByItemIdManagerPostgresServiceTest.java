package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TelegramUserEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByItemIdManagerEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UserEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.ItemPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.TelegramUserPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.TradeByItemIdManagerPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UserPostgresRepository;
import github.ricemonger.utils.DTOs.TradeByItemIdManager;
import github.ricemonger.utils.exceptions.client.ItemDoesntExistException;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.client.TradeByItemIdManagerDoesntExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TelegramUserTradeByItemIdManagerPostgresServiceTest {
    private final static String CHAT_ID = "1";
    private final static String ANOTHER_CHAT_ID = "2";

    @Autowired
    private TelegramUserTradeByItemIdManagerPostgresService telegramUserTradeManagerByItemIdService;
    @Autowired
    private TradeByItemIdManagerPostgresRepository tradeManagerByItemIdRepository;
    @Autowired
    private TelegramUserPostgresRepository telegramUserRepository;
    @Autowired
    private UserPostgresRepository userRepository;
    @Autowired
    private ItemPostgresRepository itemRepository;

    @BeforeEach
    public void setUp() {
        tradeManagerByItemIdRepository.deleteAll();
        telegramUserRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
        createTelegramUser(CHAT_ID);
        createItem("1");
    }

    private TelegramUserEntity createTelegramUser(String chatId) {
        UserEntity user = userRepository.save(new UserEntity());
        return telegramUserRepository.save(new TelegramUserEntity(chatId, user));
    }

    private ItemEntity createItem(String itemId) {
        return itemRepository.save(new ItemEntity(itemId));
    }

    @Test
    public void save_should_create_new_trade_manager_if_doesnt_exist() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createItem("2");
        tradeManager.setItemId("2");
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createTelegramUser(ANOTHER_CHAT_ID);
        telegramUserTradeManagerByItemIdService.save(ANOTHER_CHAT_ID, tradeManager);

        assertEquals(2, telegramUserRepository.findById(CHAT_ID).get().getUser().getTradeByItemIdManagers().size());
        assertEquals(1, telegramUserRepository.findById(ANOTHER_CHAT_ID).get().getUser().getTradeByItemIdManagers().size());
        assertEquals(3, tradeManagerByItemIdRepository.findAll().size());
    }

    @Test
    public void save_should_update_existing_trade_manager() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");
        tradeManager.setBuyBoundaryPrice(2);
        tradeManager.setSellBoundaryPrice(4);
        tradeManager.setPriority(5);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        tradeManager.setBuyBoundaryPrice(7);
        tradeManager.setSellBoundaryPrice(9);
        tradeManager.setPriority(10);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        TradeByItemIdManagerEntity tradeManagerEntity = tradeManagerByItemIdRepository.findAll().get(0);
        assertEquals(7, tradeManagerEntity.getBuyBoundaryPrice());
        assertEquals(9, tradeManagerEntity.getSellBoundaryPrice());
        assertEquals(10, tradeManagerEntity.getPriority());
    }

    @Test
    public void save_should_throw_exception_if_telegram_user_doesnt_exist() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.save(ANOTHER_CHAT_ID, tradeManager));
    }

    @Test
    public void save_should_throw_exception_if_item_doesnt_exist() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("2");

        assertThrows(ItemDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager));
    }

    @Test
    public void invertEnabledFlagById_should_invert_enabled_flag() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");
        tradeManager.setEnabled(true);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createItem("2");
        tradeManager.setItemId("2");
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createTelegramUser(ANOTHER_CHAT_ID);
        telegramUserTradeManagerByItemIdService.save(ANOTHER_CHAT_ID, tradeManager);

        telegramUserTradeManagerByItemIdService.invertEnabledFlagById(CHAT_ID, "1");

        assertFalse(telegramUserTradeManagerByItemIdService.findById(CHAT_ID, "1").isEnabled());
        assertTrue(telegramUserTradeManagerByItemIdService.findById(CHAT_ID, "2").isEnabled());
        assertEquals(2,
                telegramUserRepository.findById(CHAT_ID).get().getUser().getTradeByItemIdManagers().size());
        assertEquals(2, telegramUserRepository.findAll().size());
        assertEquals(2, telegramUserTradeManagerByItemIdService.findAllByChatId(CHAT_ID).size());
        assertEquals(3, tradeManagerByItemIdRepository.findAll().size());
    }

    @Test
    public void invertEnabledFlagById_should_throw_exception_if_telegram_user_doesnt_exist() {
        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.invertEnabledFlagById(ANOTHER_CHAT_ID, "1"));
    }

    @Test
    public void invertEnabledFlagById_should_throw_exception_if_trade_manager_doesnt_exist() {
        assertThrows(TradeByItemIdManagerDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.invertEnabledFlagById(CHAT_ID, "1"));
    }

    @Test
    public void deleteById_should_remove_trade_manager() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createItem("2");
        tradeManager.setItemId("2");
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createTelegramUser(ANOTHER_CHAT_ID);
        telegramUserTradeManagerByItemIdService.save(ANOTHER_CHAT_ID, tradeManager);

        telegramUserTradeManagerByItemIdService.deleteById(CHAT_ID, "2");

        assertEquals(1, telegramUserRepository.findById(CHAT_ID).get().getUser().getTradeByItemIdManagers().size());
        assertEquals(1, telegramUserRepository.findById(ANOTHER_CHAT_ID).get().getUser().getTradeByItemIdManagers().size());
        assertEquals(2, tradeManagerByItemIdRepository.findAll().size());
    }

    @Test
    public void deleteById_should_throw_exception_if_telegram_user_doesnt_exist() {
        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.deleteById(ANOTHER_CHAT_ID, "1"));
    }

    @Test
    public void findById_should_return_trade_manager() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");
        tradeManager.setBuyBoundaryPrice(1);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createItem("2");
        tradeManager.setItemId("2");
        tradeManager.setBuyBoundaryPrice(3);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createTelegramUser(ANOTHER_CHAT_ID);
        tradeManager.setItemId("1");
        tradeManager.setBuyBoundaryPrice(2);
        telegramUserTradeManagerByItemIdService.save(ANOTHER_CHAT_ID, tradeManager);

        TradeByItemIdManager foundTradeManager = telegramUserTradeManagerByItemIdService.findById(CHAT_ID, "1");

        assertEquals(1, foundTradeManager.getBuyBoundaryPrice());
    }

    @Test
    public void findById_should_throw_exception_if_telegram_user_doesnt_exist() {
        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.findById(ANOTHER_CHAT_ID, "1"));
    }

    @Test
    public void findById_should_throw_exception_if_trade_manager_doesnt_exist() {
        assertThrows(TradeByItemIdManagerDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.findById(CHAT_ID, "1"));
    }

    @Test
    public void findAllByChatId_should_return_all_trade_managers_for_user() {
        TradeByItemIdManager tradeManager = new TradeByItemIdManager();
        tradeManager.setItemId("1");
        tradeManager.setBuyBoundaryPrice(1);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createItem("2");
        tradeManager.setItemId("2");
        tradeManager.setBuyBoundaryPrice(3);
        telegramUserTradeManagerByItemIdService.save(CHAT_ID, tradeManager);

        createTelegramUser(ANOTHER_CHAT_ID);
        tradeManager.setItemId("1");
        tradeManager.setBuyBoundaryPrice(2);
        telegramUserTradeManagerByItemIdService.save(ANOTHER_CHAT_ID, tradeManager);

        Collection<TradeByItemIdManager> tradeManagers = telegramUserTradeManagerByItemIdService.findAllByChatId(CHAT_ID);

        assertEquals(2, tradeManagers.size());
    }

    @Test
    public void findAllByChatId_should_throw_exception_if_telegram_user_doesnt_exist() {
        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserTradeManagerByItemIdService.findAllByChatId(ANOTHER_CHAT_ID));
    }
}