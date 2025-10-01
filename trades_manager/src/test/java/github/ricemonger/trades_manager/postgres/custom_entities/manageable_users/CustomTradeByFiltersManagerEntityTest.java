package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomTradeByFiltersManagerEntityTest {
    @Test
    public void equals_should_return_true_if_same() {
        CustomTradeByFiltersManagerEntity manager1 = new CustomTradeByFiltersManagerEntity();

        assertEquals(manager1, manager1);
    }

    @Test
    public void equals_should_return_true_if_equal_ids() {
        CustomTradeByFiltersManagerEntity manager1 = new CustomTradeByFiltersManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setName("name");
        manager1.setEnabled(true);
        manager1.setAppliedFilters(List.of());
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setMinDifferenceFromMedianPrice(10);
        manager1.setMinDifferenceFromMedianPricePercent(5);
        manager1.setPriorityMultiplier(2);

        CustomTradeByFiltersManagerEntity manager2 = new CustomTradeByFiltersManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setName("name");

        assertEquals(manager1, manager2);
    }

    @Test
    public void equals_should_return_false_if_null() {
        CustomTradeByFiltersManagerEntity manager1 = new CustomTradeByFiltersManagerEntity();

        assertNotEquals(null, manager1);
    }

    @Test
    public void equals_should_return_false_if_different_ids() {
        CustomTradeByFiltersManagerEntity manager1 = new CustomTradeByFiltersManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setName("name");

        CustomTradeByFiltersManagerEntity manager2 = new CustomTradeByFiltersManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setName("name");

        manager1.setUser(new CustomManageableUserEntity(2L));
        assertNotEquals(manager1, manager2);
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setName("name2");
        assertNotEquals(manager1, manager2);
    }

    @Test
    public void getUserId_should_return_user_id() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomTradeByFiltersManagerEntity manager = new CustomTradeByFiltersManagerEntity();
        manager.setUser(user);
        assertEquals(1L, manager.getUserId_());
    }

    @Test
    public void isFullyEqualExceptUser_should_return_true_if_equal_() {
        CustomItemFilterEntity filter = new CustomItemFilterEntity();
        filter.setUser(new CustomManageableUserEntity(1L));

        CustomTradeByFiltersManagerEntity manager1 = new CustomTradeByFiltersManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setName("managerName");
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setAppliedFilters(List.of(filter));
        manager1.setMinDifferenceFromMedianPrice(10);
        manager1.setMinDifferenceFromMedianPricePercent(5);
        manager1.setPriorityMultiplier(2);

        CustomTradeByFiltersManagerEntity manager2 = new CustomTradeByFiltersManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setName("managerName");
        manager2.setEnabled(true);
        manager2.setTradeOperationType(TradeOperationType.BUY);
        manager2.setAppliedFilters(List.of(filter));
        manager2.setMinDifferenceFromMedianPrice(10);
        manager2.setMinDifferenceFromMedianPricePercent(5);
        manager2.setPriorityMultiplier(2);

        assertTrue(manager1.isFullyEqual(manager2));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_false_if_not_equal_() {
        CustomItemFilterEntity filter = new CustomItemFilterEntity();
        filter.setUser(new CustomManageableUserEntity(1L));

        CustomTradeByFiltersManagerEntity manager1 = new CustomTradeByFiltersManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setName("managerName1");
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setAppliedFilters(List.of(filter));
        manager1.setMinDifferenceFromMedianPrice(10);
        manager1.setMinDifferenceFromMedianPricePercent(5);
        manager1.setPriorityMultiplier(2);

        CustomTradeByFiltersManagerEntity manager2 = new CustomTradeByFiltersManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setName("managerName1");
        manager2.setEnabled(true);
        manager2.setTradeOperationType(TradeOperationType.BUY);
        manager2.setAppliedFilters(List.of(filter));
        manager2.setMinDifferenceFromMedianPrice(10);
        manager2.setMinDifferenceFromMedianPricePercent(5);
        manager2.setPriorityMultiplier(2);

        manager1.setUser(new CustomManageableUserEntity(2L));
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setName("managerName2");
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setName("managerName1");
        manager1.setEnabled(false);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.SELL);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setAppliedFilters(List.of());
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setAppliedFilters(null);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setAppliedFilters(List.of(filter));
        manager1.setMinDifferenceFromMedianPrice(20);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setMinDifferenceFromMedianPrice(10);
        manager1.setMinDifferenceFromMedianPricePercent(10);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setMinDifferenceFromMedianPricePercent(5);
        manager1.setPriorityMultiplier(3);
        assertFalse(manager1.isFullyEqual(manager2));
    }
}