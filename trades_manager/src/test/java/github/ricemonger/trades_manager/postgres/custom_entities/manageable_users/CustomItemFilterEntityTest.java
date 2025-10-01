package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomTagEntity;
import github.ricemonger.utils.enums.FilterType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomItemFilterEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomItemFilterEntity filter = new CustomItemFilterEntity();
        assertEquals(filter, filter);
    }

    @Test
    public void equals_should_return_true_if_equal_id_fields() {
        CustomItemFilterEntity filter1 = new CustomItemFilterEntity();
        filter1.setUser(new CustomManageableUserEntity(1L));
        filter1.setName("filterName");
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type1");
        filter1.setTags(Set.of(new CustomTagEntity()));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(200);

        CustomItemFilterEntity filter2 = new CustomItemFilterEntity();
        filter2.setUser(new CustomManageableUserEntity(1L));
        filter2.setName("filterName");
        filter2.setFilterType(FilterType.DENY);
        filter2.setItemNamePatterns("pattern2");
        filter2.setItemTypes("type2");
        filter2.setTags(null);
        filter2.setMinSellPrice(1000);
        filter2.setMaxBuyPrice(2000);

        assertEquals(filter1, filter2);
    }

    @Test
    public void equals_should_return_false_if_null() {
        CustomItemFilterEntity filter1 = new CustomItemFilterEntity();
        assertNotEquals(null, filter1);
    }

    @Test
    public void equals_should_return_false_if_different_id_fields() {
        CustomItemFilterEntity filter1 = new CustomItemFilterEntity();
        filter1.setUser(new CustomManageableUserEntity(1L));
        filter1.setName("filterName");

        CustomItemFilterEntity filter2 = new CustomItemFilterEntity();
        filter2.setUser(new CustomManageableUserEntity(1L));
        filter2.setName("filterName");

        filter1.setUser(new CustomManageableUserEntity(2L));
        assertNotEquals(filter1, filter2);
        filter1.setUser(new CustomManageableUserEntity(1L));
        filter1.setName("filterName2");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void getUserId_should_return_user_id() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomItemFilterEntity filter = new CustomItemFilterEntity();
        filter.setUser(user);
        assertEquals(1L, filter.getUserId_());
    }

    @Test
    public void isFullyEqualExceptUser_should_return_true_if_equal_() {
        CustomItemFilterEntity filter1 = new CustomItemFilterEntity();
        filter1.setUser(new CustomManageableUserEntity(1L));
        filter1.setName("filterName");
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type1");
        filter1.setTags(new HashSet<>(Set.of(new CustomTagEntity())));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(200);

        CustomItemFilterEntity filter2 = new CustomItemFilterEntity();
        filter2.setUser(new CustomManageableUserEntity(1L));
        filter2.setName("filterName");
        filter2.setFilterType(FilterType.ALLOW);
        filter2.setItemNamePatterns("pattern1");
        filter2.setItemTypes("type1");
        filter2.setTags(new HashSet<>(Set.of(new CustomTagEntity())));
        filter2.setMinSellPrice(100);
        filter2.setMaxBuyPrice(200);

        assertTrue(filter1.isFullyEqual(filter2));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_false_if_not_equal_() {
        CustomItemFilterEntity filter1 = new CustomItemFilterEntity();
        filter1.setUser(new CustomManageableUserEntity(1L));
        filter1.setName("filterName1");
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type1");
        filter1.setTags(new HashSet<>(Set.of(new CustomTagEntity())));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(200);

        CustomItemFilterEntity filter2 = new CustomItemFilterEntity();
        filter2.setUser(new CustomManageableUserEntity(1L));
        filter2.setName("filterName1");
        filter2.setFilterType(FilterType.ALLOW);
        filter2.setItemNamePatterns("pattern1");
        filter2.setItemTypes("type1");
        filter2.setTags(new HashSet<>(Set.of(new CustomTagEntity())));
        filter2.setMinSellPrice(100);
        filter2.setMaxBuyPrice(200);

        filter1.setName("filterName2");
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setName("filterName1");
        filter1.setFilterType(FilterType.DENY);
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setItemNamePatterns("pattern2");
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type2");
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setItemTypes("type1");
        filter1.setTags(new HashSet<>());
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setTags(null);
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setTags(Set.of(new CustomTagEntity()));
        filter1.setMinSellPrice(101);
        assertFalse(filter1.isFullyEqual(filter2));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(201);
        assertFalse(filter1.isFullyEqual(filter2));
    }
}