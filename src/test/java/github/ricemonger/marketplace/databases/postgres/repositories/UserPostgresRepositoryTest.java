package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByFiltersManagerEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByItemIdManagerEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountEntryEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UserEntity;
import github.ricemonger.utils.dtos.UbiAccountAuthorizationEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserPostgresRepositoryTest {

    @Autowired
    private UserPostgresRepository userPostgresRepository;
    @Autowired
    private ItemPostgresRepository itemPostgresRepository;
    @Autowired
    private UbiAccountEntryPostgresRepository ubiAccountEntryPostgresRepository;

    @BeforeEach
    void setUp() {
        userPostgresRepository.deleteAll();
        itemPostgresRepository.deleteAll();
        ubiAccountEntryPostgresRepository.deleteAll();
    }

    @Test
    public void findAllUsersWithTradeManagers_should_return_only_manageable_users_has_entry() {

        //Have nothing

        userPostgresRepository.save(new UserEntity());
        userPostgresRepository.save(new UserEntity());
        userPostgresRepository.save(new UserEntity());

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntry = new UbiAccountAuthorizationEntry();
        ubiAccountAuthorizationEntry.setEmail("email");
        ubiAccountAuthorizationEntry.setUbiProfileId("ubiProfileId");

        // Have managers, true flag and ubi account entry - must be returned

        UserEntity userWithTradeByItemIdManagerTrue = new UserEntity();
        userWithTradeByItemIdManagerTrue.setUbiAccountEntry(new UbiAccountEntryEntity(userWithTradeByItemIdManagerTrue, ubiAccountAuthorizationEntry));
        userWithTradeByItemIdManagerTrue.setManagingEnabledFlag(true);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityTrue = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityTrue.setItem(new ItemEntity("1"));
        tradeByItemIdManagerEntityTrue.setUser(userWithTradeByItemIdManagerTrue);
        userWithTradeByItemIdManagerTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityTrue);

        UserEntity userWithTradeByFiltersManagerTrue = new UserEntity();
        userWithTradeByFiltersManagerTrue.setUbiAccountEntry(new UbiAccountEntryEntity(userWithTradeByFiltersManagerTrue, ubiAccountAuthorizationEntry));
        userWithTradeByFiltersManagerTrue.setManagingEnabledFlag(true);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityTrue = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityTrue.setName("name");
        tradeByFiltersManagerEntityTrue.setUser(userWithTradeByFiltersManagerTrue);
        userWithTradeByFiltersManagerTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityTrue);

        UserEntity userWithBothManagersTrue = new UserEntity();
        userWithBothManagersTrue.setUbiAccountEntry(new UbiAccountEntryEntity(userWithBothManagersTrue, ubiAccountAuthorizationEntry));
        userWithBothManagersTrue.setManagingEnabledFlag(true);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothTrue = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothTrue.setItem(new ItemEntity("2"));
        tradeByItemIdManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothTrue = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothTrue.setName("name");
        tradeByFiltersManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        userWithBothManagersTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothTrue);
        userWithBothManagersTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothTrue);

        userPostgresRepository.save(userWithTradeByItemIdManagerTrue);
        userPostgresRepository.save(userWithTradeByFiltersManagerTrue);
        userPostgresRepository.save(userWithBothManagersTrue);

        // Have managers and ubi account entry, but false flag

        UserEntity userWithTradeByItemIdManagerFalse = new UserEntity();
        userWithTradeByItemIdManagerFalse.setUbiAccountEntry(new UbiAccountEntryEntity(userWithTradeByItemIdManagerFalse, ubiAccountAuthorizationEntry));
        userWithTradeByItemIdManagerFalse.setManagingEnabledFlag(false);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityFalse = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityFalse.setItem(new ItemEntity("3"));
        tradeByItemIdManagerEntityFalse.setUser(userWithTradeByItemIdManagerFalse);
        userWithTradeByItemIdManagerFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityFalse);

        UserEntity userWithTradeByFiltersManagerFalse = new UserEntity();
        userWithTradeByFiltersManagerFalse.setUbiAccountEntry(new UbiAccountEntryEntity(userWithTradeByFiltersManagerFalse, ubiAccountAuthorizationEntry));
        userWithTradeByFiltersManagerFalse.setManagingEnabledFlag(false);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityFalse = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityFalse.setName("name");
        tradeByFiltersManagerEntityFalse.setUser(userWithTradeByFiltersManagerFalse);
        userWithTradeByFiltersManagerFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityFalse);

        UserEntity userWithBothManagersFalse = new UserEntity();
        userWithBothManagersFalse.setUbiAccountEntry(new UbiAccountEntryEntity(userWithBothManagersFalse, ubiAccountAuthorizationEntry));
        userWithBothManagersFalse.setManagingEnabledFlag(false);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothFalse = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothFalse.setItem(new ItemEntity("4"));
        tradeByItemIdManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothFalse = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothFalse.setName("name");
        tradeByFiltersManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        userWithBothManagersFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothFalse);
        userWithBothManagersFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothFalse);

        userPostgresRepository.save(userWithTradeByItemIdManagerFalse);
        userPostgresRepository.save(userWithTradeByFiltersManagerFalse);
        userPostgresRepository.save(userWithBothManagersFalse);

        assertEquals(9, userPostgresRepository.count());
        assertEquals(3, userPostgresRepository.findAllManageableUsers().size());
    }

    @Test
    public void findAllUsersWithTradeManagers_should_return_only_manageable_users_has_no_entry() {

        //Have nothing

        userPostgresRepository.save(new UserEntity());
        userPostgresRepository.save(new UserEntity());
        userPostgresRepository.save(new UserEntity());

        // Have managers and true flag but no ubi account entry

        UserEntity userWithTradeByItemIdManagerTrue = new UserEntity();
        userWithTradeByItemIdManagerTrue.setManagingEnabledFlag(true);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityTrue = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityTrue.setItem(new ItemEntity("1"));
        tradeByItemIdManagerEntityTrue.setUser(userWithTradeByItemIdManagerTrue);
        userWithTradeByItemIdManagerTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityTrue);

        UserEntity userWithTradeByFiltersManagerTrue = new UserEntity();
        userWithTradeByFiltersManagerTrue.setManagingEnabledFlag(true);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityTrue = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityTrue.setName("name");
        tradeByFiltersManagerEntityTrue.setUser(userWithTradeByFiltersManagerTrue);
        userWithTradeByFiltersManagerTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityTrue);

        UserEntity userWithBothManagersTrue = new UserEntity();
        userWithBothManagersTrue.setManagingEnabledFlag(true);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothTrue = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothTrue.setItem(new ItemEntity("2"));
        tradeByItemIdManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothTrue = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothTrue.setName("name");
        tradeByFiltersManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        userWithBothManagersTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothTrue);
        userWithBothManagersTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothTrue);

        userPostgresRepository.save(userWithTradeByItemIdManagerTrue);
        userPostgresRepository.save(userWithTradeByFiltersManagerTrue);
        userPostgresRepository.save(userWithBothManagersTrue);

        // Have managers, but false flag and no entry

        UserEntity userWithTradeByItemIdManagerFalse = new UserEntity();
        userWithTradeByItemIdManagerFalse.setManagingEnabledFlag(false);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityFalse = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityFalse.setItem(new ItemEntity("3"));
        tradeByItemIdManagerEntityFalse.setUser(userWithTradeByItemIdManagerFalse);
        userWithTradeByItemIdManagerFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityFalse);

        UserEntity userWithTradeByFiltersManagerFalse = new UserEntity();
        userWithTradeByFiltersManagerFalse.setManagingEnabledFlag(false);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityFalse = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityFalse.setName("name");
        tradeByFiltersManagerEntityFalse.setUser(userWithTradeByFiltersManagerFalse);
        userWithTradeByFiltersManagerFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityFalse);

        UserEntity userWithBothManagersFalse = new UserEntity();
        userWithBothManagersFalse.setManagingEnabledFlag(false);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothFalse = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothFalse.setItem(new ItemEntity("4"));
        tradeByItemIdManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothFalse = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothFalse.setName("name");
        tradeByFiltersManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        userWithBothManagersFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothFalse);
        userWithBothManagersFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothFalse);

        userPostgresRepository.save(userWithTradeByItemIdManagerFalse);
        userPostgresRepository.save(userWithTradeByFiltersManagerFalse);
        userPostgresRepository.save(userWithBothManagersFalse);

        assertEquals(9, userPostgresRepository.count());
        assertEquals(0, userPostgresRepository.findAllManageableUsers().size());
    }
}