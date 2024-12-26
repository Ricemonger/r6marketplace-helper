package github.ricemonger.marketplace.graphQl.client_services.fetchAllItemsStats;

import github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems;
import github.ricemonger.marketplace.graphQl.GraphQlClientFactory;
import github.ricemonger.marketplace.graphQl.GraphQlDocuments;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import github.ricemonger.utils.exceptions.server.GraphQlCommonItemMappingException;
import org.junit.jupiter.api.Test;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static github.ricemonger.marketplace.graphQl.GraphQlVariablesService.MAX_LIMIT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class FetchAllItemsStatsGraphQlConfigurationTest {

    private final GraphQlVariablesService graphQlVariablesService = mock(GraphQlVariablesService.class);

    private final GraphQlClientFactory graphQlClientFactory = mock(GraphQlClientFactory.class);

    private final CommonQueryItemsMapper commonQueryItemsMapper = mock(CommonQueryItemsMapper.class);

    private final FetchAllItemsStatsGraphQlClientService fetchAllItemsStatsGraphQlClientService = new FetchAllItemsStatsGraphQlClientService(
            graphQlClientFactory,
            graphQlVariablesService,
            commonQueryItemsMapper
    );

    @Test
    public void fetchAllItemStats_should_throw_if_totalCount_or_nodes_is_null() {
        github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems marketableItems = new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems(List.of(), null);

        Map<String, Object> variables = Map.of(
                "withOwnership", false,
                "spaceId", "ubiSpaceId",
                "limit", MAX_LIMIT,
                "offset", 0,
                "sortBy", Map.of(
                        "field", "ACTIVE_COUNT",
                        "orderType", "Sell",
                        "direction", "DESC",
                        "paymentItemId", "paymentItemId"));

        HttpGraphQlClient mockClient = mock(HttpGraphQlClient.class);
        GraphQlClient.RequestSpec mockRequestSpec = mock(GraphQlClient.RequestSpec.class);
        GraphQlClient.RetrieveSpec mockRetrieveSpec = mock(GraphQlClient.RetrieveSpec.class);
        Mono<MarketableItems> mono = Mono.just(marketableItems);

        when(graphQlVariablesService.getFetchItemsVariables(anyInt())).thenReturn(variables);

        when(graphQlClientFactory.createMainUserClient()).thenReturn(mockClient);
        when(mockClient.documentName(GraphQlDocuments.QUERY_ITEMS_STATS_DOCUMENT_NAME)).thenReturn(mockRequestSpec);
        when(mockRequestSpec.variables(variables)).thenReturn(mockRequestSpec);
        when(mockRequestSpec.retrieve("game.marketableItems")).thenReturn(mockRetrieveSpec);
        when(mockRetrieveSpec.toEntity(github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems.class)).thenReturn(mono);

        assertThrows(GraphQlCommonItemMappingException.class, fetchAllItemsStatsGraphQlClientService::fetchAllItemStats);

        marketableItems = new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems(null, 15);
        mono = Mono.just(marketableItems);
        when(mockRetrieveSpec.toEntity(github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems.class)).thenReturn(mono);

        assertThrows(GraphQlCommonItemMappingException.class, fetchAllItemsStatsGraphQlClientService::fetchAllItemStats);
    }

    @Test
    public void fetchAllItemStats_should_be_executed_once_if_totalCount_smaller_then_limit() {
        List<github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node> resultNodes = new ArrayList<>();
        resultNodes.add(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node(null, null));
        resultNodes.add(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node(null, null));

        HttpGraphQlClient mockClient = mock(HttpGraphQlClient.class);
        GraphQlClient.RequestSpec mockRequestSpec = mock(GraphQlClient.RequestSpec.class);

        github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems marketableItems1 =
                new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems(List.of(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node()), MAX_LIMIT - 1);
        Map<String, Object> variables1 = Map.of(
                "withOwnership", false,
                "spaceId", "ubiSpaceId",
                "limit", MAX_LIMIT,
                "offset", 0,
                "sortBy", Map.of(
                        "field", "ACTIVE_COUNT",
                        "orderType", "Sell",
                        "direction", "DESC",
                        "paymentItemId", "paymentItemId"));

        GraphQlClient.RequestSpec mockRequestSpec1 = mock(GraphQlClient.RequestSpec.class);
        GraphQlClient.RetrieveSpec mockRetrieveSpec1 = mock(GraphQlClient.RetrieveSpec.class);
        Mono<github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems> mono1 = Mono.just(marketableItems1);

        github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems marketableItems2 = new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems(List.of(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node()), MAX_LIMIT);

        Map<String, Object> variables2 = Map.of(
                "withOwnership", false,
                "spaceId", "ubiSpaceId",
                "limit", MAX_LIMIT,
                "offset", MAX_LIMIT,
                "sortBy", Map.of(
                        "field", "ACTIVE_COUNT",
                        "orderType", "Sell",
                        "direction", "DESC",
                        "paymentItemId", "paymentItemId"));

        GraphQlClient.RequestSpec mockRequestSpec2 = mock(GraphQlClient.RequestSpec.class);
        GraphQlClient.RetrieveSpec mockRetrieveSpec2 = mock(GraphQlClient.RetrieveSpec.class);
        Mono<github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems> mono2 = Mono.just(marketableItems2);

        when(graphQlVariablesService.getFetchItemsVariables(0)).thenReturn(variables1);
        when(graphQlVariablesService.getFetchItemsVariables(MAX_LIMIT)).thenReturn(variables2);

        when(graphQlClientFactory.createMainUserClient()).thenReturn(mockClient);
        when(mockClient.documentName(GraphQlDocuments.QUERY_ITEMS_STATS_DOCUMENT_NAME)).thenReturn(mockRequestSpec);

        when(mockRequestSpec.variables(variables1)).thenReturn(mockRequestSpec1);
        when(mockRequestSpec1.retrieve("game.marketableItems")).thenReturn(mockRetrieveSpec1);
        when(mockRetrieveSpec1.toEntity(github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems.class)).thenReturn(mono1);

        when(mockRequestSpec.variables(variables2)).thenReturn(mockRequestSpec2);
        when(mockRequestSpec2.retrieve("game.marketableItems")).thenReturn(mockRetrieveSpec2);
        when(mockRetrieveSpec2.toEntity(github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems.class)).thenReturn(mono2);

        fetchAllItemsStatsGraphQlClientService.fetchAllItemStats();

        verify(commonQueryItemsMapper).mapItems(argThat(arg -> arg.contains(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node()) && arg.size() == 1));
    }

    @Test
    public void fetchAllItemStats_should_be_executed_while_limit_bigger_than_totalCount_and_handle_to_mapper() {
        List<github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node> resultNodes = new ArrayList<>();
        resultNodes.add(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node(null, null));
        resultNodes.add(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node(null, null));

        HttpGraphQlClient mockClient = mock(HttpGraphQlClient.class);
        GraphQlClient.RequestSpec mockRequestSpec = mock(GraphQlClient.RequestSpec.class);

        github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems marketableItems1 = new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems(List.of(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node()), MAX_LIMIT);
        Map<String, Object> variables1 = Map.of(
                "withOwnership", false,
                "spaceId", "ubiSpaceId",
                "limit", MAX_LIMIT,
                "offset", 0,
                "sortBy", Map.of(
                        "field", "ACTIVE_COUNT",
                        "orderType", "Sell",
                        "direction", "DESC",
                        "paymentItemId", "paymentItemId"));

        GraphQlClient.RequestSpec mockRequestSpec1 = mock(GraphQlClient.RequestSpec.class);
        GraphQlClient.RetrieveSpec mockRetrieveSpec1 = mock(GraphQlClient.RetrieveSpec.class);
        Mono<github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems> mono1 = Mono.just(marketableItems1);

        github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems marketableItems2 = new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems(List.of(new github.ricemonger.marketplace.graphQl.DTOs.common_query_items.marketableItems.Node()), MAX_LIMIT - 1);

        Map<String, Object> variables2 = Map.of(
                "withOwnership", false,
                "spaceId", "ubiSpaceId",
                "limit", MAX_LIMIT,
                "offset", MAX_LIMIT,
                "sortBy", Map.of(
                        "field", "ACTIVE_COUNT",
                        "orderType", "Sell",
                        "direction", "DESC",
                        "paymentItemId", "paymentItemId"));

        GraphQlClient.RequestSpec mockRequestSpec2 = mock(GraphQlClient.RequestSpec.class);
        GraphQlClient.RetrieveSpec mockRetrieveSpec2 = mock(GraphQlClient.RetrieveSpec.class);
        Mono<github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems> mono2 = Mono.just(marketableItems2);

        when(graphQlVariablesService.getFetchItemsVariables(0)).thenReturn(variables1);
        when(graphQlVariablesService.getFetchItemsVariables(MAX_LIMIT)).thenReturn(variables2);

        when(graphQlClientFactory.createMainUserClient()).thenReturn(mockClient);
        when(mockClient.documentName(GraphQlDocuments.QUERY_ITEMS_STATS_DOCUMENT_NAME)).thenReturn(mockRequestSpec);

        when(mockRequestSpec.variables(variables1)).thenReturn(mockRequestSpec1);
        when(mockRequestSpec1.retrieve("game.marketableItems")).thenReturn(mockRetrieveSpec1);
        when(mockRetrieveSpec1.toEntity(github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems.class)).thenReturn(mono1);

        when(mockRequestSpec.variables(variables2)).thenReturn(mockRequestSpec2);
        when(mockRequestSpec2.retrieve("game.marketableItems")).thenReturn(mockRetrieveSpec2);
        when(mockRetrieveSpec2.toEntity(github.ricemonger.marketplace.graphQl.DTOs.common_query_items.MarketableItems.class)).thenReturn(mono2);

        fetchAllItemsStatsGraphQlClientService.fetchAllItemStats();

        verify(commonQueryItemsMapper).mapItems(argThat(arg -> arg.containsAll(resultNodes) && arg.size() == resultNodes.size()));
    }

}