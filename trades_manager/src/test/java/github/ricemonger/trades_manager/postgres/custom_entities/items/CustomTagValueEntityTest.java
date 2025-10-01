package github.ricemonger.trades_manager.postgres.custom_entities.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomTagValueEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomTagValueEntity tag = new CustomTagValueEntity();
        assertEquals(tag, tag);
    }

    @Test
    public void equals_should_return_true_if_equal_id() {
        CustomTagValueEntity tag1 = new CustomTagValueEntity();
        tag1.setValue("tagValue");

        CustomTagValueEntity tag2 = new CustomTagValueEntity();
        tag2.setValue("tagValue");

        assertEquals(tag1, tag2);
    }

    @Test
    public void equals_should_return_false_if_null() {
        CustomTagValueEntity tag = new CustomTagValueEntity();
        assertNotEquals(null, tag);
    }

    @Test
    public void equals_should_return_false_if_different_ids() {
        CustomTagValueEntity tag1 = new CustomTagValueEntity();
        tag1.setValue("tagValue1");

        CustomTagValueEntity tag2 = new CustomTagValueEntity();
        tag2.setValue("tagValue2");

        assertNotEquals(tag1, tag2);
    }

    @Test
    public void isFullyEqual_should_return_true_if_same() {
        CustomTagValueEntity tag = new CustomTagValueEntity();
        assertTrue(tag.isFullyEqual(tag));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal() {
        CustomTagValueEntity tag1 = new CustomTagValueEntity();
        tag1.setValue("tagValue");

        CustomTagValueEntity tag2 = new CustomTagValueEntity();
        tag2.setValue("tagValue");

        assertTrue(tag1.isFullyEqual(tag2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_equal() {
        CustomTagValueEntity tag1 = new CustomTagValueEntity();
        tag1.setValue("tagValue1");

        CustomTagValueEntity tag2 = new CustomTagValueEntity();
        tag2.setValue("tagValue1");

        tag1.setValue("tagValue2");
        assertFalse(tag1.isFullyEqual(tag2));
    }
}