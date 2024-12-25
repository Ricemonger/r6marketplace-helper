package github.ricemonger.item_stats_fetcher.services;

import github.ricemonger.marketplace.graphQl.GraphQlClientFactory;
import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.GraphQlUbiServiceConfiguration;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import github.ricemonger.marketplace.graphQl.client_services.fetchAllItemsStats.CommonQueryItemsMapper;
import github.ricemonger.marketplace.graphQl.client_services.fetchAllItemsStats.FetchAllItemsStatsGraphQlClientService;
import github.ricemonger.utils.abstractions.CommonValuesDatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQlConfig {
    @Bean
    public GraphQlUbiServiceConfiguration graphQlUbiServiceConfiguration() {
        return new GraphQlUbiServiceConfiguration();
    }

    @Bean
    public GraphQlCommonValuesService graphQlCommonValuesService(CommonValuesDatabaseService commonValuesDatabaseService, GraphQlUbiServiceConfiguration graphQlUbiServiceConfiguration) {
        return new GraphQlCommonValuesService(commonValuesDatabaseService, graphQlUbiServiceConfiguration);
    }

    @Bean
    public GraphQlClientFactory graphQlClientFactory(GraphQlCommonValuesService graphQlCommonValuesService) {
        return new GraphQlClientFactory(graphQlCommonValuesService);
    }

    @Bean
    public GraphQlVariablesService graphQlVariablesService(GraphQlCommonValuesService graphQlCommonValuesService) {
        return new GraphQlVariablesService(graphQlCommonValuesService);
    }

    @Bean
    public CommonQueryItemsMapper commonQueryItemsMapper(GraphQlCommonValuesService graphQlCommonValuesService) {
        return new CommonQueryItemsMapper(graphQlCommonValuesService);
    }

    @Bean
    public FetchAllItemsStatsGraphQlClientService fetchAllItemsStatsGraphQlClientService(GraphQlClientFactory graphQlClientFactory, GraphQlVariablesService graphQlVariablesService, CommonQueryItemsMapper commonQueryItemsMapper) {
        return new FetchAllItemsStatsGraphQlClientService(graphQlClientFactory, graphQlVariablesService, commonQueryItemsMapper);
    }
}
