package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageableManageableUserEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        assertEquals(user, user);
    }

    @Test
    public void equals_should_return_true_if_equal_ids() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setUbiAccountEntry(new CustomManageableUserUbiAccountEntryEntity());
        user1.getUbiAccountEntry().setUser(new CustomManageableUserEntity(1L));
        user1.getUbiAccountEntry().setEmail("email");
        user1.getUbiAccountEntry().setUbiAccountStats(new CustomUbiAccountStatsEntity("ubiProfileId"));
        user1.setTradeByFiltersManagers(List.of(new CustomTradeByFiltersManagerEntity()));
        user1.setTradeByItemIdManagers(List.of(new CustomTradeByItemIdManagerEntity()));

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);

        assertEquals(user1, user2);
    }

    @Test
    public void equals_should_return_false_if_null() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        assertNotEquals(null, user);
    }

    @Test
    public void equals_should_return_false_if_different_ids() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity(1L);

        CustomManageableUserEntity user2 = new CustomManageableUserEntity(2L);

        assertNotEquals(user1, user2);
    }


    @Test
    public void isFullyEqual_should_return_true_if_same() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        assertTrue(user.isFullyEqual(user));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal() {

        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntity1 = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntity1.setUser(new CustomManageableUserEntity(1L));
        tradeByFiltersManagerEntity1.setName("name");

        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntity1 = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity1.setUser(new CustomManageableUserEntity(1L));
        tradeByItemIdManagerEntity1.setItem(new CustomItemIdEntity("itemId1"));

        CustomItemFilterEntity itemFilterEntity1 = new CustomItemFilterEntity();
        itemFilterEntity1.setUser(new CustomManageableUserEntity(1L));
        itemFilterEntity1.setName("name");

        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setUbiAccountEntry(new CustomManageableUserUbiAccountEntryEntity());
        user1.getUbiAccountEntry().setUser(new CustomManageableUserEntity(1L));
        user1.getUbiAccountEntry().setEmail("email");
        user1.getUbiAccountEntry().setUbiAccountStats(new CustomUbiAccountStatsEntity("ubiProfileId"));
        user1.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity1));
        user1.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity1));

        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntity2 = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntity2.setUser(new CustomManageableUserEntity(1L));
        tradeByFiltersManagerEntity2.setName("name");

        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntity2 = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity2.setUser(new CustomManageableUserEntity(1L));
        tradeByItemIdManagerEntity2.setItem(new CustomItemIdEntity("itemId1"));

        CustomItemFilterEntity itemFilterEntity2 = new CustomItemFilterEntity();
        itemFilterEntity2.setUser(new CustomManageableUserEntity(1L));
        itemFilterEntity2.setName("name");

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setUbiAccountEntry(new CustomManageableUserUbiAccountEntryEntity());
        user2.getUbiAccountEntry().setUser(new CustomManageableUserEntity(1L));
        user2.getUbiAccountEntry().setEmail("email");
        user2.getUbiAccountEntry().setUbiAccountStats(new CustomUbiAccountStatsEntity("ubiProfileId"));
        user2.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity2));
        user2.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity2));

        assertTrue(user1.isFullyEqual(user2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_equal() {
        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntity1 = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntity1.setUser(new CustomManageableUserEntity(1L));
        tradeByFiltersManagerEntity1.setName("name");

        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntity1 = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity1.setUser(new CustomManageableUserEntity(1L));
        tradeByItemIdManagerEntity1.setItem(new CustomItemIdEntity("itemId1"));

        CustomItemFilterEntity itemFilterEntity1 = new CustomItemFilterEntity();
        itemFilterEntity1.setUser(new CustomManageableUserEntity(1L));
        itemFilterEntity1.setName("name");

        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity1 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity1.setUser(new CustomManageableUserEntity(1L));
        ubiAccountEntryEntity1.setEmail("email");
        ubiAccountEntryEntity1.setUbiAccountStats(new CustomUbiAccountStatsEntity("ubiProfileId"));

        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntity2 = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntity2.setUser(new CustomManageableUserEntity(2L));
        tradeByFiltersManagerEntity2.setName("name");

        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntity2 = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity2.setUser(new CustomManageableUserEntity(2L));
        tradeByItemIdManagerEntity2.setItem(new CustomItemIdEntity("itemId1"));

        CustomItemFilterEntity itemFilterEntity2 = new CustomItemFilterEntity();
        itemFilterEntity2.setUser(new CustomManageableUserEntity(2L));
        itemFilterEntity2.setName("name");


        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity2 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity2.setUser(new CustomManageableUserEntity(2L));
        ubiAccountEntryEntity2.setEmail("email");
        ubiAccountEntryEntity2.setUbiAccountStats(new CustomUbiAccountStatsEntity("ubiProfileId"));

        CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntity3 = new CustomTradeByFiltersManagerEntity();
        tradeByFiltersManagerEntity3.setUser(new CustomManageableUserEntity(1L));
        tradeByFiltersManagerEntity3.setName("name1");

        CustomTradeByItemIdManagerEntity tradeByItemIdManagerEntity3 = new CustomTradeByItemIdManagerEntity();
        tradeByItemIdManagerEntity3.setUser(new CustomManageableUserEntity(1L));
        tradeByItemIdManagerEntity3.setItem(new CustomItemIdEntity("itemId2"));

        CustomItemFilterEntity itemFilterEntity3 = new CustomItemFilterEntity();
        itemFilterEntity3.setUser(new CustomManageableUserEntity(1L));
        itemFilterEntity3.setName("name2");

        CustomManageableUserUbiAccountEntryEntity ubiAccountEntryEntity3 = new CustomManageableUserUbiAccountEntryEntity();
        ubiAccountEntryEntity3.setUser(new CustomManageableUserEntity(1L));
        ubiAccountEntryEntity3.setEmail("email1");
        ubiAccountEntryEntity3.setUbiAccountStats(new CustomUbiAccountStatsEntity("ubiProfileId1"));

        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setUbiAccountEntry(ubiAccountEntryEntity1);
        user1.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity1));
        user1.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity1));

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setUbiAccountEntry(ubiAccountEntryEntity1);
        user2.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity1));
        user2.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity1));

        user1.setId(2L);
        assertFalse(user1.isFullyEqual(user2));
        user1.setId(1L);
        user1.setUbiAccountEntry(ubiAccountEntryEntity2);
        assertFalse(user1.isFullyEqual(user2));
        user1.setUbiAccountEntry(ubiAccountEntryEntity3);
        assertFalse(user1.isFullyEqual(user2));
        user1.setUbiAccountEntry(ubiAccountEntryEntity1);
        user1.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity2));
        assertFalse(user1.isFullyEqual(user2));
        user1.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity3));
        assertFalse(user1.isFullyEqual(user2));
        user1.setTradeByFiltersManagers(List.of());
        assertFalse(user1.isFullyEqual(user2));
        user1.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity1));
        user1.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity2));
        assertFalse(user1.isFullyEqual(user2));
        user1.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity3));
        assertFalse(user1.isFullyEqual(user2));
        user1.setTradeByItemIdManagers(List.of());
        assertFalse(user1.isFullyEqual(user2));
        user1.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity1));
    }
}