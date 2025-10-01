package github.ricemonger.trades_manager.postgres.custom_entities.items;

import github.ricemonger.utils.enums.TagGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomTagEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomTagEntity tag = new CustomTagEntity();
        assertEquals(tag, tag);
    }

    @Test
    public void equals_should_return_true_if_equal_id() {
        CustomTagEntity tag1 = new CustomTagEntity();
        tag1.setValue("tagValue");
        tag1.setName("tagName");
        tag1.setTagGroup(TagGroup.Season);

        CustomTagEntity tag2 = new CustomTagEntity();
        tag2.setValue("tagValue");

        assertEquals(tag1, tag2);
    }

    @Test
    public void equals_should_return_false_if_null() {
        CustomTagEntity tag = new CustomTagEntity();
        assertNotEquals(null, tag);
    }

    @Test
    public void equals_should_return_false_if_different_ids() {
        CustomTagEntity tag1 = new CustomTagEntity();
        tag1.setValue("tagValue1");

        CustomTagEntity tag2 = new CustomTagEntity();
        tag2.setValue("tagValue2");

        assertNotEquals(tag1, tag2);
    }

    @Test
    public void isFullyEqual_should_return_true_if_same() {
        CustomTagEntity tag = new CustomTagEntity();
        assertTrue(tag.isFullyEqual(tag));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal() {
        CustomTagEntity tag1 = new CustomTagEntity();
        tag1.setValue("tagValue");
        tag1.setName("tagName");
        tag1.setTagGroup(TagGroup.Season);

        CustomTagEntity tag2 = new CustomTagEntity();
        tag2.setValue("tagValue");
        tag2.setName("tagName");
        tag2.setTagGroup(TagGroup.Season);

        assertTrue(tag1.isFullyEqual(tag2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_equal() {
        CustomTagEntity tag1 = new CustomTagEntity();
        tag1.setValue("tagValue1");
        tag1.setName("tagName1");
        tag1.setTagGroup(TagGroup.Season);

        CustomTagEntity tag2 = new CustomTagEntity();
        tag2.setValue("tagValue1");
        tag2.setName("tagName1");
        tag2.setTagGroup(TagGroup.Season);

        tag1.setValue("tagValue2");
        assertFalse(tag1.isFullyEqual(tag2));
        tag1.setValue("tagValue1");
        tag1.setName("tagName2");
        assertFalse(tag1.isFullyEqual(tag2));
        tag1.setName("tagName1");
        tag1.setTagGroup(TagGroup.Rarity);
        assertFalse(tag1.isFullyEqual(tag2));
    }
}