package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageableUserCustomManageableUserUbiAccountEntryEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomManageableUserUbiAccountEntryEntity accountEntry = new CustomManageableUserUbiAccountEntryEntity();
        assertEquals(accountEntry, accountEntry);
    }

    @Test
    public void equals_should_return_true_if_equal_ids() {
        CustomManageableUserUbiAccountEntryEntity accountEntry1 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry1.setUser(new CustomManageableUserEntity(1L));
        accountEntry1.setEmail("email");
        accountEntry1.setUbiAccountStats(new CustomUbiAccountStatsEntity("profileId"));
        accountEntry1.setUbiSessionId("sessionId");
        accountEntry1.setUbiSpaceId("spaceId");
        accountEntry1.setUbiAuthTicket("authTicket");
        accountEntry1.setUbiRememberDeviceTicket("rememberDeviceTicket");
        accountEntry1.setUbiRememberMeTicket("rememberMeTicket");

        CustomManageableUserUbiAccountEntryEntity accountEntry2 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry2.setUser(new CustomManageableUserEntity(1L));
        accountEntry2.setEmail("email");

        assertEquals(accountEntry1, accountEntry2);
    }

    @Test
    public void equals_should_return_false_for_null() {
        CustomManageableUserUbiAccountEntryEntity accountEntry = new CustomManageableUserUbiAccountEntryEntity();
        assertNotEquals(null, accountEntry);
    }

    @Test
    public void equals_should_return_false_if_different_ids() {
        CustomManageableUserUbiAccountEntryEntity accountEntry1 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry1.setUser(new CustomManageableUserEntity(1L));
        accountEntry1.setEmail("email");

        CustomManageableUserUbiAccountEntryEntity accountEntry2 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry2.setUser(new CustomManageableUserEntity(1L));
        accountEntry2.setEmail("email");

        accountEntry1.setUser(new CustomManageableUserEntity(2L));
        assertNotEquals(accountEntry1, accountEntry2);
        accountEntry1.setUser(new CustomManageableUserEntity(1L));
        accountEntry1.setEmail("email1");
        assertNotEquals(accountEntry1, accountEntry2);
    }

    @Test
    public void constructor_should_set_id_fields() {
        CustomManageableUserUbiAccountEntryEntity accountEntry = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry.setUser(new CustomManageableUserEntity(1L));
        accountEntry.setEmail("email");
        accountEntry.setUbiAccountStats(new CustomUbiAccountStatsEntity("profileId"));

        assertEquals(1L, accountEntry.getUserId_());
        assertEquals("email", accountEntry.getEmail());
        assertEquals("profileId", accountEntry.getProfileId_());
    }

    @Test
    public void getUserId_should_return_user_id() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomManageableUserUbiAccountEntryEntity accountEntry = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry.setUser(user);
        assertEquals(1L, accountEntry.getUserId_());
    }

    @Test
    public void getProfileId_should_return_profile_id() {
        CustomUbiAccountStatsEntity stats = new CustomUbiAccountStatsEntity();
        stats.setUbiProfileId("profileId");
        CustomManageableUserUbiAccountEntryEntity accountEntry = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry.setUbiAccountStats(stats);
        assertEquals("profileId", accountEntry.getProfileId_());
    }

    @Test
    public void isFullyEqualExceptUser_should_return_true_if_equal_() {
        CustomUbiAccountStatsEntity stats = new CustomUbiAccountStatsEntity();
        stats.setUbiProfileId("profileId");
        stats.setCreditAmount(100);
        stats.setOwnedItems(List.of());

        CustomManageableUserUbiAccountEntryEntity accountEntry1 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry1.setUser(new CustomManageableUserEntity(1L));
        accountEntry1.setEmail("email@example.com");
        accountEntry1.setUbiSessionId("sessionId");
        accountEntry1.setUbiSpaceId("spaceId");
        accountEntry1.setUbiAuthTicket("authTicket");
        accountEntry1.setUbiRememberDeviceTicket("rememberDeviceTicket");
        accountEntry1.setUbiRememberMeTicket("rememberMeTicket");
        accountEntry1.setUbiAccountStats(stats);

        CustomManageableUserUbiAccountEntryEntity accountEntry2 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry2.setUser(new CustomManageableUserEntity(1L));
        accountEntry2.setEmail("email@example.com");
        accountEntry2.setUbiSessionId("sessionId");
        accountEntry2.setUbiSpaceId("spaceId");
        accountEntry2.setUbiAuthTicket("authTicket");
        accountEntry2.setUbiRememberDeviceTicket("rememberDeviceTicket");
        accountEntry2.setUbiRememberMeTicket("rememberMeTicket");
        accountEntry2.setUbiAccountStats(stats);

        assertTrue(accountEntry1.isFullyEqual(accountEntry2));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_false_if_not_equal_() {
        CustomUbiAccountStatsEntity stats1 = new CustomUbiAccountStatsEntity();
        stats1.setUbiProfileId("profileId");
        stats1.setCreditAmount(100);
        stats1.setOwnedItems(List.of());

        CustomManageableUserUbiAccountEntryEntity accountEntry1 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry1.setUser(new CustomManageableUserEntity(1L));
        accountEntry1.setEmail("email@example.com");
        accountEntry1.setUbiSessionId("sessionId");
        accountEntry1.setUbiSpaceId("spaceId");
        accountEntry1.setUbiAuthTicket("authTicket");
        accountEntry1.setUbiRememberDeviceTicket("rememberDeviceTicket");
        accountEntry1.setUbiRememberMeTicket("rememberMeTicket");
        accountEntry1.setUbiAccountStats(stats1);

        CustomUbiAccountStatsEntity stats2 = new CustomUbiAccountStatsEntity();
        stats2.setUbiProfileId("profileId");
        stats2.setCreditAmount(100);
        stats2.setOwnedItems(List.of());

        CustomManageableUserUbiAccountEntryEntity accountEntry2 = new CustomManageableUserUbiAccountEntryEntity();
        accountEntry2.setUser(new CustomManageableUserEntity(1L));
        accountEntry2.setEmail("email@example.com");
        accountEntry2.setUbiSessionId("sessionId");
        accountEntry2.setUbiSpaceId("spaceId");
        accountEntry2.setUbiAuthTicket("authTicket");
        accountEntry2.setUbiRememberDeviceTicket("rememberDeviceTicket");
        accountEntry2.setUbiRememberMeTicket("rememberMeTicket");
        accountEntry2.setUbiAccountStats(stats2);

        accountEntry1.setUser(new CustomManageableUserEntity(2L));
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setUser(new CustomManageableUserEntity(1L));
        accountEntry1.setEmail("email1@example.com");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setEmail("email@example.com");
        accountEntry1.setUbiSessionId("sessionId1");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setUbiSessionId("sessionId");
        accountEntry1.setUbiSpaceId("spaceId1");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setUbiSpaceId("spaceId");
        accountEntry1.setUbiAuthTicket("authTicket1");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setUbiAuthTicket("authTicket");
        accountEntry1.setUbiRememberDeviceTicket("rememberDeviceTicket1");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setUbiRememberDeviceTicket("rememberDeviceTicket");
        accountEntry1.setUbiRememberMeTicket("rememberMeTicket1");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.setUbiRememberMeTicket("rememberMeTicket");
        accountEntry1.getUbiAccountStats().setUbiProfileId("profileId1");
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.getUbiAccountStats().setUbiProfileId("profileId");
        accountEntry1.getUbiAccountStats().setCreditAmount(101);
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.getUbiAccountStats().setCreditAmount(100);
        accountEntry1.getUbiAccountStats().setOwnedItems(List.of(new CustomItemIdEntity()));
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
        accountEntry1.getUbiAccountStats().setOwnedItems(null);
        assertFalse(accountEntry1.isFullyEqual(accountEntry2));
    }
}