package github.ricemonger.fast_sell_trade_manager.services.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class FastSellManagementConfiguration {
    @Value("${fast_sell_management.userId}")
    private Long userId;
    @Value("${fast_sell_management.email}")
    private String email;
    @Value("${fast_sell_management.min_median_price_difference}")
    private Integer minMedianPriceDifference;
    @Value("${fast_sell_management.min_median_price_difference_percentage}")
    private Integer minMedianPriceDifferencePercentage;
    @Value("${fast_sell_management.owned_items_limit}")
    private Integer ownedItemsLimit;
    @Value("${fast_sell_management.fetch_users_items_limit}")
    private Integer fetchUsersItemsLimit;
    @Value("${fast_sell_management.fetch_users_items_offset}")
    private Integer fetchUsersItemsOffset;
    @Value("${fast_sell_management.sleep_after_execution_time_ms}")
    private Integer sleepAfterExecutionTime;
}
