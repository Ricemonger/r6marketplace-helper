package github.ricemonger.ubi_users_stats_fetcher.services.DTOs;

import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UbiAccountStatsTest {

    @Test
    public void hashCode_should_return_same_hash_for_equal_ubiProfileId() {
        UbiAccountStats stats1 = new UbiAccountStats();
        stats1.setUbiProfileId("ubiProfileId");
        stats1.setSoldIn24h(1);
        stats1.setBoughtIn24h(2);
        stats1.setCreditAmount(3);
        stats1.setOwnedItemsIds(List.of("1"));
        stats1.setResaleLocks(List.of(new ItemResaleLock()));
        stats1.setCurrentSellTrades(List.of(new Trade()));
        stats1.setCurrentBuyTrades(List.of());

        UbiAccountStats stats2 = new UbiAccountStats();
        stats2.setUbiProfileId("ubiProfileId");
        stats2.setSoldIn24h(10);
        stats2.setBoughtIn24h(20);
        stats2.setCreditAmount(30);
        stats2.setOwnedItemsIds(null);
        stats2.setResaleLocks(null);
        stats2.setCurrentSellTrades(null);
        stats2.setCurrentBuyTrades(null);

        assertEquals(stats1.hashCode(), stats2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_equal_ubiProfileId() {
        UbiAccountStats stats1 = new UbiAccountStats();
        stats1.setUbiProfileId("ubiProfileId");
        stats1.setSoldIn24h(1);
        stats1.setBoughtIn24h(2);
        stats1.setCreditAmount(3);
        stats1.setOwnedItemsIds(List.of("1"));
        stats1.setResaleLocks(List.of(new ItemResaleLock()));
        stats1.setCurrentSellTrades(List.of(new Trade()));
        stats1.setCurrentBuyTrades(List.of());

        UbiAccountStats stats2 = new UbiAccountStats();
        stats2.setUbiProfileId("ubiProfileId");
        stats2.setSoldIn24h(10);
        stats2.setBoughtIn24h(20);
        stats2.setCreditAmount(30);
        stats2.setOwnedItemsIds(null);
        stats2.setResaleLocks(null);
        stats2.setCurrentSellTrades(null);
        stats2.setCurrentBuyTrades(null);

        assertEquals(stats1, stats2);
    }

    @Test
    public void equals_should_return_false_for_non_equal_ubiProfileId() {
        UbiAccountStats stats1 = new UbiAccountStats();
        stats1.setUbiProfileId("ubiProfileId1");

        UbiAccountStats stats2 = new UbiAccountStats();
        stats2.setUbiProfileId("ubiProfileId2");

        assertNotEquals(stats1, stats2);
    }

    @Test
    public void isFullyEqual_should_return_true_if_fully_equal() {
        UbiAccountStats stats1 = new UbiAccountStats();
        stats1.setUbiProfileId("ubiProfileId");
        stats1.setSoldIn24h(1);
        stats1.setBoughtIn24h(2);
        stats1.setCreditAmount(3);
        stats1.setOwnedItemsIds(List.of("1"));
        stats1.setResaleLocks(List.of(new ItemResaleLock()));
        stats1.setCurrentSellTrades(List.of(new Trade()));
        stats1.setCurrentBuyTrades(List.of());

        UbiAccountStats stats2 = new UbiAccountStats();
        stats2.setUbiProfileId("ubiProfileId");
        stats2.setSoldIn24h(1);
        stats2.setBoughtIn24h(2);
        stats2.setCreditAmount(3);
        stats2.setOwnedItemsIds(List.of("1"));
        stats2.setResaleLocks(List.of(new ItemResaleLock()));
        stats2.setCurrentSellTrades(List.of(new Trade()));
        stats2.setCurrentBuyTrades(List.of());

        assertTrue(stats2.isFullyEqual(stats2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_fully_equal() {
        UbiAccountStats stats1 = new UbiAccountStats();
        stats1.setUbiProfileId("ubiProfileId");
        stats1.setSoldIn24h(1);
        stats1.setBoughtIn24h(2);
        stats1.setCreditAmount(3);
        stats1.setOwnedItemsIds(List.of("1"));
        stats1.setResaleLocks(List.of(new ItemResaleLock()));
        stats1.setCurrentSellTrades(List.of(new Trade()));
        stats1.setCurrentBuyTrades(List.of());

        UbiAccountStats stats2 = new UbiAccountStats();
        stats2.setUbiProfileId("ubiProfileId");
        stats2.setSoldIn24h(1);
        stats2.setBoughtIn24h(2);
        stats2.setCreditAmount(3);
        stats2.setOwnedItemsIds(List.of("1"));
        stats2.setResaleLocks(List.of(new ItemResaleLock()));
        stats2.setCurrentSellTrades(List.of(new Trade()));
        stats2.setCurrentBuyTrades(List.of());

        stats1.setUbiProfileId("ubiProfileId1");
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setUbiProfileId("ubiProfileId");
        stats1.setSoldIn24h(10);
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setSoldIn24h(1);
        stats1.setBoughtIn24h(20);
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setBoughtIn24h(2);
        stats1.setCreditAmount(30);
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setCreditAmount(3);
        stats1.setOwnedItemsIds(null);
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setOwnedItemsIds(List.of("1"));
        stats1.setResaleLocks(null);
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setResaleLocks(List.of(new ItemResaleLock()));
        stats1.setCurrentSellTrades(null);
        assertFalse(stats2.isFullyEqual(stats1));
        stats1.setCurrentSellTrades(List.of(new Trade()));
        stats1.setCurrentBuyTrades(null);
        assertFalse(stats2.isFullyEqual(stats1));
    }

}