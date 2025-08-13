package github.ricemonger.fast_sell_trade_manager.services.configurations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FastSellManagementConfigurationTest {
    @Autowired
    private FastSellManagementConfiguration fastSellManagementConfiguration;

    @Test
    public void values_should_not_be_null() {
        assertNotNull(fastSellManagementConfiguration.getUserId());
        assertNotNull(fastSellManagementConfiguration.getEmail());
        assertNotNull(fastSellManagementConfiguration.getMinMedianPriceDifference());
        assertNotNull(fastSellManagementConfiguration.getMinMedianPriceDifferencePercentage());
        assertNotNull(fastSellManagementConfiguration.getOwnedItemsLimit());
        assertNotNull(fastSellManagementConfiguration.getFetchUsersItemsLimit());
        assertNotNull(fastSellManagementConfiguration.getFetchUsersItemsOffset());
        assertNotNull(fastSellManagementConfiguration.getSleepAfterExecutionTime());
    }
}