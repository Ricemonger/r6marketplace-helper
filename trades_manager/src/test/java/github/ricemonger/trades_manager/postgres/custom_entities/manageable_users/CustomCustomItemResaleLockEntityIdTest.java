package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomCustomItemResaleLockEntityIdTest {

    @Test
    public void hashCode_should_return_same_for_equal_ids() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        entity1.setBoughtIn24h(1);
        CustomItemIdEntity item1 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id1 = new CustomItemResaleLockEntityId(entity1, item1);

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        entity2.setBoughtIn24h(2);
        CustomItemIdEntity item2 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id2 = new CustomItemResaleLockEntityId(entity2, item2);

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_equal_ids() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        entity1.setBoughtIn24h(1);
        CustomItemIdEntity item1 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id1 = new CustomItemResaleLockEntityId(entity1, item1);

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        entity2.setBoughtIn24h(2);
        CustomItemIdEntity item2 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id2 = new CustomItemResaleLockEntityId(entity2, item2);

        assertEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_ubiProfileId() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        CustomItemIdEntity item1 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id1 = new CustomItemResaleLockEntityId(entity1, item1);

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId2");
        CustomItemIdEntity item2 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id2 = new CustomItemResaleLockEntityId(entity2, item2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void equals_should_return_false_for_different_items() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        CustomItemIdEntity item1 = new CustomItemIdEntity("itemId1");

        CustomItemResaleLockEntityId id1 = new CustomItemResaleLockEntityId(entity1, item1);

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId1");
        CustomItemIdEntity item2 = new CustomItemIdEntity("itemId2");

        CustomItemResaleLockEntityId id2 = new CustomItemResaleLockEntityId(entity2, item2);

        assertNotEquals(id1, id2);
    }
}