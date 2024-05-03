package github.ricemonger.marketplace.databases.neo4j.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuyStatsEntityTests {

    @Test
    public void equalsShouldReturnTrueForSame(){
        Assertions.assertEquals(EntitiesStatics.BUY_STATS, EntitiesStatics.BUY_STATS_SAME);
    }

    @Test
    public void equalsShouldReturnFalseForDifferent(){
        Assertions.assertNotEquals(EntitiesStatics.BUY_STATS, EntitiesStatics.BUY_STATS_ALT_ID);
        Assertions.assertNotEquals(EntitiesStatics.BUY_STATS, EntitiesStatics.BUY_STATS_ALT_LOWER_PRICE);
        Assertions.assertNotEquals(EntitiesStatics.BUY_STATS, EntitiesStatics.BUY_STATS_ALT_HIGHEST_PRICE);
        Assertions.assertNotEquals(EntitiesStatics.BUY_STATS, EntitiesStatics.BUY_STATS_ALT_ACTIVE_COUNT);
    }
}