package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomUbiAccountStatsEntityTest {

    @Test
    public void equals_should_return_false_if_null() {
        CustomUbiAccountStatsEntity entity = new CustomUbiAccountStatsEntity();
        assertNotEquals(null, entity);
    }

    @Test
    public void equals_should_return_true_if_same_object() {
        CustomUbiAccountStatsEntity entity = new CustomUbiAccountStatsEntity();
        assertEquals(entity, entity);
    }

    @Test
    public void equals_should_return_true_if_equal_ubiProfileId() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId");
        entity1.setCreditAmount(1);
        entity1.setSoldIn24h(2);
        entity1.setBoughtIn24h(3);
        entity1.setOwnedItems(List.of(new CustomItemIdEntity()));
        entity1.setResaleLocks(List.of(new CustomItemResaleLockEntity()));
        entity1.setCurrentSellTrades(List.of(new CustomTradeEntity()));
        entity1.setCurrentBuyTrades(List.of(new CustomTradeEntity()));

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId");

        assertEquals(entity1, entity2);
    }

    @Test
    public void equals_should_return_false_if_not_equal_ubiProfileId() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId");
        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId2");

        assertNotEquals(entity1, entity2);
    }

    @Test
    public void isFullyEqual_should_return_false_if_null() {
        CustomUbiAccountStatsEntity entity = new CustomUbiAccountStatsEntity();
        assertFalse(entity.isFullyEqual(null));
    }

    @Test
    public void isFullyEqual_should_return_true_if_same_object() {
        CustomUbiAccountStatsEntity entity = new CustomUbiAccountStatsEntity();
        assertTrue(entity.isFullyEqual(entity));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal_ubiProfileId_and_creditAmount() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId");
        entity1.setCreditAmount(1);
        entity1.setSoldIn24h(2);
        entity1.setBoughtIn24h(3);
        entity1.setOwnedItems(List.of(new CustomItemIdEntity()));
        entity1.setResaleLocks(List.of(new CustomItemResaleLockEntity()));
        entity1.setCurrentSellTrades(List.of(new CustomTradeEntity()));
        entity1.setCurrentBuyTrades(List.of(new CustomTradeEntity()));

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId");
        entity2.setCreditAmount(1);
        entity2.setSoldIn24h(2);
        entity2.setBoughtIn24h(3);
        entity2.setOwnedItems(List.of(new CustomItemIdEntity()));
        entity2.setResaleLocks(List.of(new CustomItemResaleLockEntity()));
        entity2.setCurrentSellTrades(List.of(new CustomTradeEntity()));
        entity2.setCurrentBuyTrades(List.of(new CustomTradeEntity()));

        assertTrue(entity1.isFullyEqual(entity2));
    }

    @Test
    public void isFullyEqual_should_return_true_if_different_field() {
        CustomUbiAccountStatsEntity entity1 = new CustomUbiAccountStatsEntity("ubiProfileId");
        entity1.setCreditAmount(1);
        entity1.setSoldIn24h(2);
        entity1.setBoughtIn24h(3);
        entity1.setOwnedItems(List.of(new CustomItemIdEntity()));
        entity1.setResaleLocks(List.of(new CustomItemResaleLockEntity()));
        entity1.setCurrentSellTrades(List.of(new CustomTradeEntity()));
        entity1.setCurrentBuyTrades(List.of(new CustomTradeEntity()));

        CustomUbiAccountStatsEntity entity2 = new CustomUbiAccountStatsEntity("ubiProfileId");
        entity2.setCreditAmount(1);
        entity2.setSoldIn24h(2);
        entity2.setBoughtIn24h(3);
        entity2.setOwnedItems(List.of(new CustomItemIdEntity()));
        entity2.setResaleLocks(List.of(new CustomItemResaleLockEntity()));
        entity2.setCurrentSellTrades(List.of(new CustomTradeEntity()));
        entity2.setCurrentBuyTrades(List.of(new CustomTradeEntity()));

        entity1.setUbiProfileId("ubiProfileId2");
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setUbiProfileId("ubiProfileId");
        entity1.setCreditAmount(2);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setCreditAmount(1);
        entity1.setSoldIn24h(3);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setSoldIn24h(2);
        entity1.setBoughtIn24h(4);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setBoughtIn24h(3);
        entity1.setOwnedItems(List.of(new CustomItemIdEntity(), new CustomItemIdEntity()));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setOwnedItems(null);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setOwnedItems(List.of());
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setOwnedItems(List.of(new CustomItemIdEntity("itemId")));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setOwnedItems(List.of(new CustomItemIdEntity()));
        entity1.setResaleLocks(List.of(new CustomItemResaleLockEntity(), new CustomItemResaleLockEntity()));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setResaleLocks(null);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setResaleLocks(List.of());
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setResaleLocks(List.of(new CustomItemResaleLockEntity()));
        entity1.setCurrentSellTrades(List.of(new CustomTradeEntity(), new CustomTradeEntity()));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setCurrentSellTrades(null);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setCurrentSellTrades(List.of());
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setCurrentSellTrades(List.of(new CustomTradeEntity()));
        entity1.setCurrentBuyTrades(List.of(new CustomTradeEntity(), new CustomTradeEntity()));
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setCurrentBuyTrades(null);
        assertFalse(entity1.isFullyEqual(entity2));
        entity1.setCurrentBuyTrades(List.of());
        assertFalse(entity1.isFullyEqual(entity2));
    }
}