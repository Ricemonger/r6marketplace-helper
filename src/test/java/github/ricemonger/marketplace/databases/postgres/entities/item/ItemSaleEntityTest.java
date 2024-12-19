package github.ricemonger.marketplace.databases.postgres.entities.item;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemSaleEntityTest {
    @Test
    public void getItemId_should_return_item_itemId() {
        ItemEntity item = new ItemEntity();
        item.setItemId("itemId");
        ItemSaleEntity itemSaleEntity = new ItemSaleEntity();
        itemSaleEntity.setItem(item);
        assertEquals("itemId", itemSaleEntity.getItemId());
    }

    @Test
    public void isFullyEqual_should_return_true_if_same() {
        ItemSaleEntity entity = new ItemSaleEntity();
        assertTrue(entity.isFullyEqual(entity));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal() {
        ItemEntity item = new ItemEntity();
        item.setItemId("itemId");

        ItemSaleEntity entity1 = new ItemSaleEntity();
        entity1.setItem(item);
        entity1.setSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setPrice(100);

        ItemSaleEntity entity2 = new ItemSaleEntity();
        entity2.setItem(item);
        entity2.setSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity2.setPrice(100);

        assertTrue(entity1.isFullyEqual(entity2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_equal() {
        ItemEntity item1 = new ItemEntity();
        item1.setItemId("itemId1");

        ItemEntity item2 = new ItemEntity();
        item2.setItemId("itemId");

        ItemSaleEntity entity1 = new ItemSaleEntity();
        entity1.setItem(item1);
        entity1.setSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setPrice(100);

        ItemSaleEntity entity2 = new ItemSaleEntity();
        entity2.setItem(item2);
        entity2.setSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity2.setPrice(100);

        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setItem(item2);
        entity1.setSoldAt(LocalDateTime.of(2021, 1, 2, 0, 0));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setSoldAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity1.setPrice(101);
        assertFalse(entity1.isFullyEqual(entity2));
    }
}