package github.ricemonger.trades_manager.postgres.repositories;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CustomManageableUserPostgresRepositoryTest {
    @Autowired
    private CustomManageableUserPostgresRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void findAllUsersWithTradeManagers_should_return_only_manageable_users_has_entry() {

        //Have nothing

        userRepository.save(new CustomManageableUserEntity());
        userRepository.save(new CustomManageableUserEntity());
        userRepository.save(new CustomManageableUserEntity());

        // Have managers, true flag and ubi account entry - must be returned

        CustomManageableUserEntity userWithTradeByItemIdManagerTrue = new CustomManageableUserEntity();
        CustomUbiAccountStatsEntity ubiAccountStatsEntity1 = new CustomUbiAccountStatsEntity();
        ubiAccountStatsEntity1.setUbiProfileId("ubiProfileId1");
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity1 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity1.setUser(userWithTradeByItemIdManagerTrue);
        ubiAccountEntryEntity1.setEmail("email");
        ubiAccountEntryEntity1.setUbiAccountStats(ubiAccountStatsEntity1);
        userWithTradeByItemIdManagerTrue.setUbiAccountEntry(ubiAccountEntryEntity1);
        userWithTradeByItemIdManagerTrue.setManagingEnabledFlag(true);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityTrue = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityTrue.setItem(new CustomItemIdEntity("1"));
        tradeByItemIdManagerEntityTrue.setUser(userWithTradeByItemIdManagerTrue);
        userWithTradeByItemIdManagerTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityTrue);

        CustomManageableUserEntity userWithTradeByFiltersManagerTrue = new CustomManageableUserEntity();
        CustomUbiAccountStatsEntity ubiAccountStatsEntity2 = new CustomUbiAccountStatsEntity();
        ubiAccountStatsEntity2.setUbiProfileId("ubiProfileId2");
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity2 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity2.setUser(userWithTradeByFiltersManagerTrue);
        ubiAccountEntryEntity2.setEmail("email");
        ubiAccountEntryEntity2.setUbiAccountStats(ubiAccountStatsEntity2);
        userWithTradeByFiltersManagerTrue.setUbiAccountEntry(ubiAccountEntryEntity2);
        userWithTradeByFiltersManagerTrue.setManagingEnabledFlag(true);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityTrue = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityTrue.setName("name");
        tradeByFiltersManagerEntityTrue.setUser(userWithTradeByFiltersManagerTrue);
        userWithTradeByFiltersManagerTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityTrue);

        CustomManageableUserEntity userWithBothManagersTrue = new CustomManageableUserEntity();
        CustomUbiAccountStatsEntity ubiAccountStatsEntity3 = new CustomUbiAccountStatsEntity();
        ubiAccountStatsEntity3.setUbiProfileId("ubiProfileId3");
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity3 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity3.setUser(userWithBothManagersTrue);
        ubiAccountEntryEntity3.setEmail("email");
        ubiAccountEntryEntity3.setUbiAccountStats(ubiAccountStatsEntity3);
        userWithBothManagersTrue.setUbiAccountEntry(ubiAccountEntryEntity3);
        userWithBothManagersTrue.setManagingEnabledFlag(true);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothTrue = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothTrue.setItem(new CustomItemIdEntity("2"));
        tradeByItemIdManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothTrue = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothTrue.setName("name");
        tradeByFiltersManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        userWithBothManagersTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothTrue);
        userWithBothManagersTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothTrue);

        userRepository.save(userWithTradeByItemIdManagerTrue);
        userRepository.save(userWithTradeByFiltersManagerTrue);
        userRepository.save(userWithBothManagersTrue);

        // Have managers and ubi account entry, but false flag

        CustomManageableUserEntity userWithTradeByItemIdManagerFalse = new CustomManageableUserEntity();
        CustomUbiAccountStatsEntity ubiAccountStatsEntity4 = new CustomUbiAccountStatsEntity();
        ubiAccountStatsEntity4.setUbiProfileId("ubiProfileId4");
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity4 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity4.setUser(userWithTradeByItemIdManagerFalse);
        ubiAccountEntryEntity4.setEmail("email");
        ubiAccountEntryEntity4.setUbiAccountStats(ubiAccountStatsEntity4);
        userWithTradeByItemIdManagerFalse.setUbiAccountEntry(ubiAccountEntryEntity4);
        userWithTradeByItemIdManagerFalse.setManagingEnabledFlag(false);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityFalse = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityFalse.setItem(new CustomItemIdEntity("3"));
        tradeByItemIdManagerEntityFalse.setUser(userWithTradeByItemIdManagerFalse);
        userWithTradeByItemIdManagerFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityFalse);

        CustomManageableUserEntity userWithTradeByFiltersManagerFalse = new CustomManageableUserEntity();
        CustomUbiAccountStatsEntity ubiAccountStatsEntity5 = new CustomUbiAccountStatsEntity();
        ubiAccountStatsEntity5.setUbiProfileId("ubiProfileId5");
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity5 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity5.setUser(userWithTradeByFiltersManagerFalse);
        ubiAccountEntryEntity5.setEmail("email");
        ubiAccountEntryEntity5.setUbiAccountStats(ubiAccountStatsEntity5);
        userWithTradeByFiltersManagerFalse.setUbiAccountEntry(ubiAccountEntryEntity5);
        userWithTradeByFiltersManagerFalse.setManagingEnabledFlag(false);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityFalse = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityFalse.setName("name");
        tradeByFiltersManagerEntityFalse.setUser(userWithTradeByFiltersManagerFalse);
        userWithTradeByFiltersManagerFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityFalse);

        CustomManageableUserEntity userWithBothManagersFalse = new CustomManageableUserEntity();
        CustomUbiAccountStatsEntity ubiAccountStatsEntity6 = new CustomUbiAccountStatsEntity();
        ubiAccountStatsEntity6.setUbiProfileId("ubiProfileId6");
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity6 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity6.setUser(userWithBothManagersFalse);
        ubiAccountEntryEntity6.setEmail("email");
        ubiAccountEntryEntity6.setUbiAccountStats(ubiAccountStatsEntity6);
        userWithBothManagersFalse.setUbiAccountEntry(ubiAccountEntryEntity6);
        userWithBothManagersFalse.setManagingEnabledFlag(false);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothFalse = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothFalse.setItem(new CustomItemIdEntity("4"));
        tradeByItemIdManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothFalse = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothFalse.setName("name");
        tradeByFiltersManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        userWithBothManagersFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothFalse);
        userWithBothManagersFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothFalse);

        userRepository.save(userWithTradeByItemIdManagerFalse);
        userRepository.save(userWithTradeByFiltersManagerFalse);
        userRepository.save(userWithBothManagersFalse);

        assertEquals(9, userRepository.count());
        assertEquals(3, userRepository.findAllManageableUsers().size());
    }

    @Test
    public void findAllUsersWithTradeManagers_should_return_only_manageable_users_has_no_entry() {

        //Have nothing

        userRepository.save(new CustomManageableUserEntity());
        userRepository.save(new CustomManageableUserEntity());
        userRepository.save(new CustomManageableUserEntity());

        // Have managers and true flag but no ubi account entry

        CustomManageableUserEntity userWithTradeByItemIdManagerTrue = new CustomManageableUserEntity();
        userWithTradeByItemIdManagerTrue.setManagingEnabledFlag(true);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityTrue = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityTrue.setItem(new CustomItemIdEntity("1"));
        tradeByItemIdManagerEntityTrue.setUser(userWithTradeByItemIdManagerTrue);
        userWithTradeByItemIdManagerTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityTrue);

        CustomManageableUserEntity userWithTradeByFiltersManagerTrue = new CustomManageableUserEntity();
        userWithTradeByFiltersManagerTrue.setManagingEnabledFlag(true);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityTrue = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityTrue.setName("name");
        tradeByFiltersManagerEntityTrue.setUser(userWithTradeByFiltersManagerTrue);
        userWithTradeByFiltersManagerTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityTrue);

        CustomManageableUserEntity userWithBothManagersTrue = new CustomManageableUserEntity();
        userWithBothManagersTrue.setManagingEnabledFlag(true);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothTrue = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothTrue.setItem(new CustomItemIdEntity("2"));
        tradeByItemIdManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothTrue = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothTrue.setName("name");
        tradeByFiltersManagerEntityForBothTrue.setUser(userWithBothManagersTrue);
        userWithBothManagersTrue.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothTrue);
        userWithBothManagersTrue.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothTrue);

        userRepository.save(userWithTradeByItemIdManagerTrue);
        userRepository.save(userWithTradeByFiltersManagerTrue);
        userRepository.save(userWithBothManagersTrue);

        // Have managers, but false flag and no entry

        CustomManageableUserEntity userWithTradeByItemIdManagerFalse = new CustomManageableUserEntity();
        userWithTradeByItemIdManagerFalse.setManagingEnabledFlag(false);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityFalse = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityFalse.setItem(new CustomItemIdEntity("3"));
        tradeByItemIdManagerEntityFalse.setUser(userWithTradeByItemIdManagerFalse);
        userWithTradeByItemIdManagerFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityFalse);

        CustomManageableUserEntity userWithTradeByFiltersManagerFalse = new CustomManageableUserEntity();
        userWithTradeByFiltersManagerFalse.setManagingEnabledFlag(false);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityFalse = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityFalse.setName("name");
        tradeByFiltersManagerEntityFalse.setUser(userWithTradeByFiltersManagerFalse);
        userWithTradeByFiltersManagerFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityFalse);

        CustomManageableUserEntity userWithBothManagersFalse = new CustomManageableUserEntity();
        userWithBothManagersFalse.setManagingEnabledFlag(false);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothFalse = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothFalse.setItem(new CustomItemIdEntity("4"));
        tradeByItemIdManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothFalse = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothFalse.setName("name");
        tradeByFiltersManagerEntityForBothFalse.setUser(userWithBothManagersFalse);
        userWithBothManagersFalse.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothFalse);
        userWithBothManagersFalse.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothFalse);

        userRepository.save(userWithTradeByItemIdManagerFalse);
        userRepository.save(userWithTradeByFiltersManagerFalse);
        userRepository.save(userWithBothManagersFalse);

        // Have managers and true flag and ubi account entry but no ubi account stats

        CustomManageableUserEntity userWithTradeByItemIdManagerTrueNoStats = new CustomManageableUserEntity();
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity1 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity1.setUser(userWithTradeByItemIdManagerTrueNoStats);
        ubiAccountEntryEntity1.setEmail("email");
        userWithTradeByItemIdManagerTrueNoStats.setUbiAccountEntry(ubiAccountEntryEntity1);
        userWithTradeByItemIdManagerTrueNoStats.setManagingEnabledFlag(true);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityTrueNoStats = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityTrueNoStats.setItem(new CustomItemIdEntity("5"));
        tradeByItemIdManagerEntityTrueNoStats.setUser(userWithTradeByItemIdManagerTrueNoStats);
        userWithTradeByItemIdManagerTrueNoStats.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityTrueNoStats);

        CustomManageableUserEntity userWithTradeByFiltersManagerTrueNoStats = new CustomManageableUserEntity();
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity2 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity2.setUser(userWithTradeByFiltersManagerTrueNoStats);
        ubiAccountEntryEntity2.setEmail("email");
        userWithTradeByFiltersManagerTrueNoStats.setUbiAccountEntry(ubiAccountEntryEntity2);
        userWithTradeByFiltersManagerTrueNoStats.setManagingEnabledFlag(true);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityTrueNoStats = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityTrueNoStats.setName("name");
        tradeByFiltersManagerEntityTrueNoStats.setUser(userWithTradeByFiltersManagerTrueNoStats);
        userWithTradeByFiltersManagerTrueNoStats.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityTrueNoStats);

        CustomManageableUserEntity userWithBothManagersTrueNoStats = new CustomManageableUserEntity();
        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity3 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity3.setUser(userWithBothManagersTrueNoStats);
        ubiAccountEntryEntity3.setEmail("email");
        userWithBothManagersTrueNoStats.setUbiAccountEntry(ubiAccountEntryEntity3);
        userWithBothManagersTrueNoStats.setManagingEnabledFlag(true);
        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntityForBothTrueNoStats = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntityForBothTrueNoStats.setItem(new CustomItemIdEntity("6"));
        tradeByItemIdManagerEntityForBothTrueNoStats.setUser(userWithBothManagersTrueNoStats);
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntityForBothTrueNoStats = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntityForBothTrueNoStats.setName("name");
        tradeByFiltersManagerEntityForBothTrueNoStats.setUser(userWithBothManagersTrueNoStats);
        userWithBothManagersTrueNoStats.getTradeByItemIdManagers().add(tradeByItemIdManagerEntityForBothTrueNoStats);
        userWithBothManagersTrueNoStats.getTradeByFiltersManagers().add(tradeByFiltersManagerEntityForBothTrueNoStats);

        userRepository.save(userWithTradeByItemIdManagerTrueNoStats);
        userRepository.save(userWithTradeByFiltersManagerTrueNoStats);
        userRepository.save(userWithBothManagersTrueNoStats);

        assertEquals(12, userRepository.count());
        assertEquals(0, userRepository.findAllManageableUsers().size());
    }
}