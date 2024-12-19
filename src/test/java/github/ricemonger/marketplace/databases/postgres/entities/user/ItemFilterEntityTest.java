package github.ricemonger.marketplace.databases.postgres.entities.user;

import github.ricemonger.marketplace.databases.postgres.entities.item.TagEntity;
import github.ricemonger.utils.enums.FilterType;
import github.ricemonger.utils.enums.IsOwnedFilter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ItemFilterEntityTest {

    @Test
    public void getUserId_should_return_user_id() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        ItemFilterEntity filter = new ItemFilterEntity();
        filter.setUser(user);
        assertEquals(1L, filter.getUserId());
    }

    @Test
    public void isFullyEqualExceptUser_should_return_true_if_equal_except_user() {
        ItemFilterEntity filter1 = new ItemFilterEntity();
        filter1.setName("filterName");
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setIsOwned(IsOwnedFilter.OWNED);
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type1");
        filter1.setTags(new HashSet<>(Set.of(new TagEntity())));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(200);

        ItemFilterEntity filter2 = new ItemFilterEntity();
        filter2.setName("filterName");
        filter2.setFilterType(FilterType.ALLOW);
        filter2.setIsOwned(IsOwnedFilter.OWNED);
        filter2.setItemNamePatterns("pattern1");
        filter2.setItemTypes("type1");
        filter2.setTags(new HashSet<>(Set.of(new TagEntity())));
        filter2.setMinSellPrice(100);
        filter2.setMaxBuyPrice(200);

        assertTrue(filter1.isFullyEqualExceptUser(filter2));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_false_if_not_equal_except_user() {
        ItemFilterEntity filter1 = new ItemFilterEntity();
        filter1.setName("filterName1");
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setIsOwned(IsOwnedFilter.OWNED);
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type1");
        filter1.setTags(new HashSet<>(Set.of(new TagEntity())));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(200);

        ItemFilterEntity filter2 = new ItemFilterEntity();
        filter2.setName("filterName1");
        filter2.setFilterType(FilterType.ALLOW);
        filter2.setIsOwned(IsOwnedFilter.OWNED);
        filter2.setItemNamePatterns("pattern1");
        filter2.setItemTypes("type1");
        filter2.setTags(new HashSet<>(Set.of(new TagEntity())));
        filter2.setMinSellPrice(100);
        filter2.setMaxBuyPrice(200);

        filter1.setName("filterName2");
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setName("filterName1");
        filter1.setFilterType(FilterType.DENY);
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setFilterType(FilterType.ALLOW);
        filter1.setIsOwned(IsOwnedFilter.NOT_OWNED);
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setIsOwned(IsOwnedFilter.OWNED);
        filter1.setItemNamePatterns("pattern2");
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setItemNamePatterns("pattern1");
        filter1.setItemTypes("type2");
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setItemTypes("type1");
        filter1.setTags(new HashSet<>());
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setTags(null);
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setTags(Set.of(new TagEntity()));
        filter1.setMinSellPrice(101);
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
        filter1.setMinSellPrice(100);
        filter1.setMaxBuyPrice(201);
        assertFalse(filter1.isFullyEqualExceptUser(filter2));
    }
}