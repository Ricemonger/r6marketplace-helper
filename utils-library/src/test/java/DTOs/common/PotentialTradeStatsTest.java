package DTOs.common;

import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PotentialTradeStatsTest {

    @Test
    public void isValid_should_return_false_when_price_is_null() {
        PotentialTradeStats potentialTradeStats = new PotentialTradeStats(null, 1, 1L);
        assertFalse(potentialTradeStats.isValid());
    }

    @Test
    public void isValid_should_return_false_when_prognosedTradeSuccessMinutes_is_null() {
        PotentialTradeStats potentialTradeStats = new PotentialTradeStats(1, null, 1L);
        assertFalse(potentialTradeStats.isValid());
    }

    @Test
    public void isValid_should_return_false_when_tradePriority_is_null() {
        PotentialTradeStats potentialTradeStats = new PotentialTradeStats(1, 1, null);
        assertFalse(potentialTradeStats.isValid());
    }
}