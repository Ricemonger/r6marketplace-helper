package github.ricemonger.trades_manager;

import github.ricemonger.marketplace.databases.redis.services.RedisUtilsConfiguration;
import github.ricemonger.marketplace.graphQl.personal_mutation_buy_create.PersonalMutationBuyCreateGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_mutation_buy_update.PersonalMutationBuyUpdateGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_mutation_cancel.PersonalMutationCancelGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_create.PersonalMutationSellCreateGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_update.PersonalMutationSellUpdateGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_query_credits_amount.PersonalQueryCreditAmountGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_query_current_orders.PersonalQueryCurrentOrdersGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_query_finished_orders.PersonalQueryFinishedOrdersGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items.PersonalQueryOwnedItemsGraphQlConfiguration;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.PersonalQueryTradesLimitationsGraphQlConfiguration;
import github.ricemonger.utils.SchedulingUtilsConfiguration;
import github.ricemonger.utils.services.calculators.TradeStatsCalculatorUtilsConfiguration;
import github.ricemonger.utilslibrarykafka.KafkaUtilsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"github.ricemonger.trades_manager", "github.ricemonger.utilspostgresschema"},
        basePackageClasses = github.ricemonger.utils.PublicMethodLogger.class)
@Import({
        KafkaUtilsConfiguration.class,
        RedisUtilsConfiguration.class,
        SchedulingUtilsConfiguration.class,
        PersonalMutationBuyCreateGraphQlConfiguration.class,
        PersonalMutationBuyUpdateGraphQlConfiguration.class,
        PersonalMutationSellCreateGraphQlConfiguration.class,
        PersonalMutationSellUpdateGraphQlConfiguration.class,
        PersonalMutationCancelGraphQlConfiguration.class,
        PersonalQueryTradesLimitationsGraphQlConfiguration.class,
        PersonalQueryOwnedItemsGraphQlConfiguration.class,
        PersonalQueryCurrentOrdersGraphQlConfiguration.class,
        PersonalQueryFinishedOrdersGraphQlConfiguration.class,
        PersonalQueryCreditAmountGraphQlConfiguration.class,
        TradeStatsCalculatorUtilsConfiguration.class
})
public class TradesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradesManagerApplication.class, args);
    }

}
