package github.ricemonger.trades_manager.postgres.custom_entities.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomItemIdEntityTest {

    @Test
    public void equals_should_return_false_for_null() {
        assertNotEquals(null, new CustomItemIdEntity());
    }

    @Test
    public void equals_should_return_false_for_different_class() {
        assertNotEquals(new CustomItemIdEntity(), new Object());
    }

    @Test
    public void equals_should_return_true_for_same_object() {
        CustomItemIdEntity item = new CustomItemIdEntity();
        assertEquals(item, item);
    }

    @Test
    public void equals_should_return_true_for_same_id() {
        CustomItemIdEntity item1 = new CustomItemIdEntity("1");
        CustomItemIdEntity item2 = new CustomItemIdEntity("1");
        assertEquals(item1, item2);
    }

    @Test
    public void equals_should_return_false_for_different_id() {
        CustomItemIdEntity item1 = new CustomItemIdEntity("1");
        CustomItemIdEntity item2 = new CustomItemIdEntity("2");
        assertNotEquals(item1, item2);
    }
}