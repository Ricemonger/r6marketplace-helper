package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomCustomTradeByItemIdManagerEntityIdTest {

    @Test
    public void hashCode_should_return_same_hash_for_equal_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setUbiAccountEntry(null);
        CustomItemIdEntity item1 = new CustomItemIdEntity();
        item1.setItemId("item1");

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setUbiAccountEntry(new CustomManageableUserUbiAccountEntryEntity());
        CustomItemIdEntity item2 = new CustomItemIdEntity();
        item2.setItemId("item1");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user1, item1);
        CustomTradeByItemIdManagerEntityId id2 = new CustomTradeByItemIdManagerEntityId(user2, item2);

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void hashCode_should_return_different_hash_for_different_users() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(2L);
        CustomItemIdEntity item = new CustomItemIdEntity();
        item.setItemId("item1");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user1, item);
        CustomTradeByItemIdManagerEntityId id2 = new CustomTradeByItemIdManagerEntityId(user2, item);

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void hashCode_should_return_different_hash_for_different_items() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomItemIdEntity item1 = new CustomItemIdEntity();
        item1.setItemId("item1");
        CustomItemIdEntity item2 = new CustomItemIdEntity();
        item2.setItemId("item2");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user, item1);
        CustomTradeByItemIdManagerEntityId id2 = new CustomTradeByItemIdManagerEntityId(user, item2);

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_equal_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setTradeByFiltersManagers(null);
        CustomItemIdEntity item1 = new CustomItemIdEntity();
        item1.setItemId("item1");

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setTradeByFiltersManagers(List.of());
        CustomItemIdEntity item2 = new CustomItemIdEntity();
        item2.setItemId("item1");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user1, item1);
        CustomTradeByItemIdManagerEntityId id2 = new CustomTradeByItemIdManagerEntityId(user2, item2);

        assertEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_users() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(2L);
        CustomItemIdEntity item = new CustomItemIdEntity();
        item.setItemId("item1");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user1, item);
        CustomTradeByItemIdManagerEntityId id2 = new CustomTradeByItemIdManagerEntityId(user2, item);

        assertNotEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_items() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomItemIdEntity item1 = new CustomItemIdEntity();
        item1.setItemId("item1");
        CustomItemIdEntity item2 = new CustomItemIdEntity();
        item2.setItemId("item2");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user, item1);
        CustomTradeByItemIdManagerEntityId id2 = new CustomTradeByItemIdManagerEntityId(user, item2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_null() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomItemIdEntity item = new CustomItemIdEntity();
        item.setItemId("item1");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user, item);

        assertNotEquals(null, id1);
    }

    @Test
    public void equals_should_return_false_for_different_class() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomItemIdEntity item = new CustomItemIdEntity();
        item.setItemId("item1");

        CustomTradeByItemIdManagerEntityId id1 = new CustomTradeByItemIdManagerEntityId(user, item);
        Object obj = new Object();

        assertNotEquals(id1, obj);
    }
}