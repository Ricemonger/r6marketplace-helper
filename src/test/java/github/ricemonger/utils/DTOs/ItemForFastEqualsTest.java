package github.ricemonger.utils.DTOs;

import github.ricemonger.utils.DTOs.items.ItemForFastEquals;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemForFastEqualsTest {
    @Test
    public void equals_should_compare_only_by_itemId() {
        ItemForFastEquals item1 = new ItemForFastEquals();
        item1.setItemId("itemId");
        item1.setName("name1");
        item1.setTags(List.of("tag1"));
        item1.setMaxBuyPrice(120);
        item1.setMinSellPrice(100);
        item1.setRarity(ItemRarity.UNCOMMON);
        item1.setLastSoldAt(LocalDateTime.MIN);
        item1.setLastSoldPrice(110);
        item1.setType(ItemType.WeaponSkin);
        item1.setAssetUrl("url1");

        ItemForFastEquals item2 = new ItemForFastEquals();
        item2.setItemId("itemId");
        item2.setName("name2");
        item2.setTags(List.of("tag2"));
        item2.setMaxBuyPrice(130);
        item2.setMinSellPrice(110);
        item2.setRarity(ItemRarity.RARE);
        item2.setLastSoldAt(LocalDateTime.MIN.plusSeconds(1000));
        item2.setLastSoldPrice(120);
        item2.setType(ItemType.CharacterUniform);
        item2.setAssetUrl("url2");

        assertEquals(item1, item2);

        item2.setItemId("itemId2");

        assertNotEquals(item1, item2);
    }

    @Test
    public void hashCode_should_return_hashCode_of_itemId() {
        ItemForFastEquals item = new ItemForFastEquals();
        item.setItemId("itemId");

        assertEquals("itemId".hashCode(), item.hashCode());
    }
}