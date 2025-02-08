package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomCustomPrioritizedUbiTradeByFiltersManagerEntityIdTest {
    @Test
    public void hashCode_should_return_same_hash_for_equal_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setUbiAccountEntry(new CustomManageableUserUbiAccountEntryEntity());
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setUbiAccountEntry(null);


        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user1, "filterName");
        CustomTradeByFiltersManagerEntityId id2 = new CustomTradeByFiltersManagerEntityId(user2, "filterName");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void hashCode_should_return_different_hash_for_different_users() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(2L);

        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user1, "filterName");
        CustomTradeByFiltersManagerEntityId id2 = new CustomTradeByFiltersManagerEntityId(user2, "filterName");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void hashCode_should_return_different_hash_for_different_names() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user, "filterName1");
        CustomTradeByFiltersManagerEntityId id2 = new CustomTradeByFiltersManagerEntityId(user, "filterName2");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_equal_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setTradeByItemIdManagers(null);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setTradeByFiltersManagers(List.of());


        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user1, "filterName");
        CustomTradeByFiltersManagerEntityId id2 = new CustomTradeByFiltersManagerEntityId(user2, "filterName");

        assertEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_users() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(2L);

        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user1, "filterName");
        CustomTradeByFiltersManagerEntityId id2 = new CustomTradeByFiltersManagerEntityId(user2, "filterName");

        assertNotEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_names() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user, "filterName1");
        CustomTradeByFiltersManagerEntityId id2 = new CustomTradeByFiltersManagerEntityId(user, "filterName2");

        assertNotEquals(id1, id2);
    }


    @Test
    public void equals_should_return_false_for_null() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user, "filterName");

        assertNotEquals(null, id1);
    }

    @Test
    public void equals_should_return_false_for_different_class() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomTradeByFiltersManagerEntityId id1 = new CustomTradeByFiltersManagerEntityId(user, "filterName");
        Object obj = new Object();

        assertNotEquals(id1, obj);
    }
}