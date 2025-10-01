package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomItemResaleLockEntityTest {

    @Test
    public void getItemId_should_return_item_itemId() {
        CustomItemResaleLockEntity itemResaleLockEntity = new CustomItemResaleLockEntity();
        CustomItemIdEntity itemIdEntity = new CustomItemIdEntity();
        itemIdEntity.setItemId("itemId");
        itemResaleLockEntity.setItem(itemIdEntity);
        assertEquals("itemId", itemResaleLockEntity.getItemId_());
    }

    @Test
    public void equals_should_return_true_for_same() {
        CustomItemResaleLockEntity itemResaleLockEntity = new CustomItemResaleLockEntity();
        assertEquals(itemResaleLockEntity, itemResaleLockEntity);
    }

    @Test
    public void equals_should_return_true_for_same_ids() {
        CustomItemResaleLockEntity itemResaleLockEntity = new CustomItemResaleLockEntity();
        CustomItemIdEntity itemIdEntity = new CustomItemIdEntity();
        itemIdEntity.setItemId("itemId");
        itemResaleLockEntity.setItem(itemIdEntity);
        itemResaleLockEntity.setUbiAccount(new CustomUbiAccountStatsEntity("ubiAccount"));
        CustomItemResaleLockEntity itemResaleLockEntity1 = new CustomItemResaleLockEntity();
        CustomItemIdEntity itemIdEntity1 = new CustomItemIdEntity();
        itemIdEntity1.setItemId("itemId");
        itemResaleLockEntity1.setItem(itemIdEntity1);
        itemResaleLockEntity1.setUbiAccount(new CustomUbiAccountStatsEntity("ubiAccount"));

        assertEquals(itemResaleLockEntity, itemResaleLockEntity1);
    }


}