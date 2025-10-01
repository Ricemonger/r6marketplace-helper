package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomCustomManageableUserUbiAccountEntryEntityIdTest {

    @Test
    public void hashCode_should_return_same_hash_for_equal_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setUbiAccountEntry(new CustomManageableUserUbiAccountEntryEntity());

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setUbiAccountEntry(null);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user1, "email@example.com");
        CustomManageableUserUbiAccountEntryEntityId id2 = new CustomManageableUserUbiAccountEntryEntityId(user2, "email@example.com");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void hashCode_should_return_different_hash_for_different_users() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(2L);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user1, "email@example.com");
        CustomManageableUserUbiAccountEntryEntityId id2 = new CustomManageableUserUbiAccountEntryEntityId(user2, "email@example.com");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void hashCode_should_return_different_hash_for_different_emails() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user, "email1@example.com");
        CustomManageableUserUbiAccountEntryEntityId id2 = new CustomManageableUserUbiAccountEntryEntityId(user, "email2@example.com");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_equal_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        user1.setTradeByFiltersManagers(null);

        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(1L);
        user2.setTradeByFiltersManagers(List.of());

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user1, "email@example.com");
        CustomManageableUserUbiAccountEntryEntityId id2 = new CustomManageableUserUbiAccountEntryEntityId(user2, "email@example.com");

        assertEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_objects() {
        CustomManageableUserEntity user1 = new CustomManageableUserEntity();
        user1.setId(1L);
        CustomManageableUserEntity user2 = new CustomManageableUserEntity();
        user2.setId(2L);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user1, "email@example.com");
        CustomManageableUserUbiAccountEntryEntityId id2 = new CustomManageableUserUbiAccountEntryEntityId(user2, "email@example.com");

        assertNotEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_emails() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user, "email1@example.com");
        CustomManageableUserUbiAccountEntryEntityId id2 = new CustomManageableUserUbiAccountEntryEntityId(user, "email2@example.com");

        assertNotEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_null() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user, "email@example.com");

        assertNotEquals(null, id1);
    }

    @Test
    public void equals_should_return_false_for_different_class() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);

        CustomManageableUserUbiAccountEntryEntityId id1 = new CustomManageableUserUbiAccountEntryEntityId(user, "email@example.com");
        Object obj = new Object();

        assertNotEquals(id1, obj);
    }
}