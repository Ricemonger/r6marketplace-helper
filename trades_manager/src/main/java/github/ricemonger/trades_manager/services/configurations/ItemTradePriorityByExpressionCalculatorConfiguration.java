package github.ricemonger.trades_manager.services.configurations;

import github.ricemonger.utils.services.calculators.ItemTradePriorityByExpressionCalculator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class ItemTradePriorityByExpressionCalculatorConfiguration {
    @Value("${default_values.sell_trade_priority_expression}")
    private String defaultSellTradePriorityExpression;

    @Value("${default_values.buy_trade_priority_expression}")
    private String defaultBuyTradePriorityExpression;
}
