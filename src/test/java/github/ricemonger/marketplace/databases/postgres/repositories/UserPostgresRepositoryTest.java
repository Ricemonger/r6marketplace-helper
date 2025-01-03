package github.ricemonger.marketplace.databases.postgres.repositories;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByFiltersManagerEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TradeByItemIdManagerEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountStatsEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UserEntity;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user.UbiAccountEntryEntityMapper;
import github.ricemonger.utils.DTOs.personal.UbiAccountAuthorizationEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserPostgresRepositoryTest {

    @Autowired
    private UserPostgresRepository userPostgresRepository;
    @Autowired
    private ItemPostgresRepository itemPostgresRepository;
    @Autowired
    private UbiAccountAuthorizationEntryPostgresRepository ubiAccountAuthorizationEntryPostgresRepository;
    @Autowired
    private UbiAccountEntryEntityMapper ubiAccountEntryEntityMapper;

    @BeforeEach
    void setUp() {
        userPostgresRepository.deleteAll();
        itemPostgresRepository.deleteAll();
        ubiAccountAuthorizationEntryPostgresRepository.deleteAll();
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
        UbiAccountStatsEntity ubiAccountStatsEntity1 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity1.setUbiProfileId("ubiProfileId1");
        userWithTradeByItemIdManagerTrue.setUbiAccountEntry(ubiAccountEntryEntityMapper.createEntity(userWithTradeByItemIdManagerTrue,
                ubiAccountStatsEntity1, ubiAccountAuthorizationEntry));
        userWithTradeByItemIdManagerTrue.setManagingEnabledFlag(true);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityTrue = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityTrue.setItem(new ItemEntity("1"));
        tradeByItemIdManagerEntityTrue.setUser(userWithTradeByItemIdManagerTrue);
        userWithTradeByItemIdManagerTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityTrue);

        UserEntity userWithTradeByFiltersManagerTrue = new UserEntity();
        UbiAccountStatsEntity ubiAccountStatsEntity2 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity2.setUbiProfileId("ubiProfileId2");
        userWithTradeByFiltersManagerTrue.setUbiAccountEntry((ubiAccountEntryEntityMapper.createEntity(userWithTradeByFiltersManagerTrue,
                ubiAccountStatsEntity2, ubiAccountAuthorizationEntry)));
        userWithTradeByFiltersManagerTrue.setManagingEnabledFlag(true);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityTrue = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityTrue.setName("name");
        tradeByFiltersManagerEntityTrue.setUser(userWithTradeByFiltersManagerTrue);
        userWithTradeByFiltersManagerTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityTrue);

        UserEntity userWithBothManagersTrue = new UserEntity();
        UbiAccountStatsEntity ubiAccountStatsEntity3 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity3.setUbiProfileId("ubiProfileId3");
        userWithBothManagersTrue.setUbiAccountEntry(ubiAccountEntryEntityMapper.createEntity(userWithBothManagersTrue,
                ubiAccountStatsEntity3, ubiAccountAuthorizationEntry));
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
        UbiAccountStatsEntity ubiAccountStatsEntity4 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity4.setUbiProfileId("ubiProfileId4");
        userWithTradeByItemIdManagerFalse.setUbiAccountEntry(ubiAccountEntryEntityMapper.createEntity(userWithTradeByItemIdManagerFalse,
                ubiAccountStatsEntity4, ubiAccountAuthorizationEntry));
        userWithTradeByItemIdManagerFalse.setManagingEnabledFlag(false);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntityFalse = new TradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityFalse.setItem(new ItemEntity("3"));
        tradeByItemIdManagerEntityFalse.setUser(userWithTradeByItemIdManagerFalse);
        userWithTradeByItemIdManagerFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityFalse);

        UserEntity userWithTradeByFiltersManagerFalse = new UserEntity();
        UbiAccountStatsEntity ubiAccountStatsEntity5 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity5.setUbiProfileId("ubiProfileId5");
        userWithTradeByFiltersManagerFalse.setUbiAccountEntry(ubiAccountEntryEntityMapper.createEntity(userWithTradeByFiltersManagerFalse, ubiAccountStatsEntity5, ubiAccountAuthorizationEntry));
        userWithTradeByFiltersManagerFalse.setManagingEnabledFlag(false);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntityFalse = new TradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityFalse.setName("name");
        tradeByFiltersManagerEntityFalse.setUser(userWithTradeByFiltersManagerFalse);
        userWithTradeByFiltersManagerFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityFalse);

        UserEntity userWithBothManagersFalse = new UserEntity();
        UbiAccountStatsEntity ubiAccountStatsEntity6 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity6.setUbiProfileId("ubiProfileId6");
        userWithBothManagersFalse.setUbiAccountEntry(ubiAccountEntryEntityMapper.createEntity(userWithBothManagersFalse,
                ubiAccountStatsEntity6, ubiAccountAuthorizationEntry));
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