package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomTradeByItemIdManagerEntityTest {
    @Test
    public void equals_should_return_true_if_same_object() {
        CustomTradeByItemIdManagerEntity manager = new CustomTradeByItemIdManagerEntity();
        assertEquals(manager, manager);
    }

    @Test
    public void equals_should_return_true_if_equal_ids() {
        CustomTradeByItemIdManagerEntity manager1 = new CustomTradeByItemIdManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setItem(new CustomItemIdEntity("itemId"));
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setSellBoundaryPrice(100);
        manager1.setBuyBoundaryPrice(200);
        manager1.setPriorityMultiplier(2);

        CustomTradeByItemIdManagerEntity manager2 = new CustomTradeByItemIdManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setItem(new CustomItemIdEntity("itemId"));

        assertEquals(manager1, manager2);
    }

    @Test
    public void equals_should_return_false_if_null() {
        CustomTradeByItemIdManagerEntity manager = new CustomTradeByItemIdManagerEntity();
        assertNotEquals(null, manager);
    }

    @Test
    public void equals_should_return_false_if_different_ids() {
        CustomTradeByItemIdManagerEntity manager1 = new CustomTradeByItemIdManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setItem(new CustomItemIdEntity("itemId"));

        CustomTradeByItemIdManagerEntity manager2 = new CustomTradeByItemIdManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setItem(new CustomItemIdEntity("itemId"));

        manager1.setUser(new CustomManageableUserEntity(2L));
        assertNotEquals(manager1, manager2);
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setItem(new CustomItemIdEntity("itemId2"));
        assertNotEquals(manager1, manager2);
    }

    @Test
    public void getUserId_should_return_user_idField() {
        CustomManageableUserEntity user = new CustomManageableUserEntity();
        user.setId(1L);
        CustomTradeByItemIdManagerEntity manager = new CustomTradeByItemIdManagerEntity();
        manager.setUser(user);
        assertEquals(1L, manager.getUserId_());
    }

    @Test
    public void getItemId_should_return_item_id() {
        CustomItemIdEntity item = new CustomItemIdEntity();
        item.setItemId("itemId");
        CustomTradeByItemIdManagerEntity manager = new CustomTradeByItemIdManagerEntity();
        manager.setItem(item);
        assertEquals("itemId", manager.getItemId_());
    }

    @Test
    public void isFullyEqual_should_return_true_if_same_object() {
        CustomTradeByItemIdManagerEntity manager = new CustomTradeByItemIdManagerEntity();
        assertTrue(manager.isFullyEqual(manager));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_true_if_equal_() {
        CustomTradeByItemIdManagerEntity manager1 = new CustomTradeByItemIdManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setItem(new CustomItemIdEntity("itemId"));
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setSellBoundaryPrice(100);
        manager1.setBuyBoundaryPrice(200);
        manager1.setPriorityMultiplier(2);

        CustomTradeByItemIdManagerEntity manager2 = new CustomTradeByItemIdManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setItem(new CustomItemIdEntity("itemId"));
        manager2.setEnabled(true);
        manager2.setTradeOperationType(TradeOperationType.BUY);
        manager2.setSellBoundaryPrice(100);
        manager2.setBuyBoundaryPrice(200);
        manager2.setPriorityMultiplier(2);

        assertTrue(manager1.isFullyEqual(manager2));
    }

    @Test
    public void isFullyEqualExceptUser_should_return_false_if_not_equal_() {
        CustomTradeByItemIdManagerEntity manager1 = new CustomTradeByItemIdManagerEntity();
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.setItem(new CustomItemIdEntity("itemId"));
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setSellBoundaryPrice(100);
        manager1.setBuyBoundaryPrice(200);
        manager1.setPriorityMultiplier(2);

        CustomTradeByItemIdManagerEntity manager2 = new CustomTradeByItemIdManagerEntity();
        manager2.setUser(new CustomManageableUserEntity(1L));
        manager2.setItem(new CustomItemIdEntity("itemId"));
        manager2.setEnabled(true);
        manager2.setTradeOperationType(TradeOperationType.BUY);
        manager2.setSellBoundaryPrice(100);
        manager2.setBuyBoundaryPrice(200);
        manager2.setPriorityMultiplier(2);

        manager1.setUser(new CustomManageableUserEntity(2L));
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setUser(new CustomManageableUserEntity(1L));
        manager1.getItem().setItemId("itemId2");
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.getItem().setItemId("itemId");
        manager1.setEnabled(false);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setEnabled(true);
        manager1.setTradeOperationType(TradeOperationType.SELL);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setTradeOperationType(TradeOperationType.BUY);
        manager1.setSellBoundaryPrice(101);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setSellBoundaryPrice(100);
        manager1.setBuyBoundaryPrice(201);
        assertFalse(manager1.isFullyEqual(manager2));
        manager1.setBuyBoundaryPrice(200);
        manager1.setPriorityMultiplier(3);
        assertFalse(manager1.isFullyEqual(manager2));
    }
}