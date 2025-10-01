package github.ricemonger.marketplace.graphQl.common_query_items_prices;

import github.ricemonger.marketplace.graphQl.BuiltGraphQlDocument;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommonQueryItemsPricesGraphQlDocumentBuilderTest {
    private GraphQlVariablesService graphQlVariablesService = mock(GraphQlVariablesService.class);

    private CommonQueryItemsPricesGraphQlDocumentBuilder builder = new CommonQueryItemsPricesGraphQlDocumentBuilder(graphQlVariablesService);

    @Test
    public void buildPersonalQueryUserStatsDocument_should_throw_if_0_limit() {
        assertThrows(IllegalArgumentException.class, () -> {
            builder.buildCommonQueryItemsPricesDocument(0, 0);
        });
    }

    @Test
    public void buildPersonalQueryUserStatsDocument_should_return_expected_result_for_1_expected_query() {
        BuiltGraphQlDocument expected = new BuiltGraphQlDocument();

        when(graphQlVariablesService.getDefaultFetchLimitedItemsPricesVariables()).thenReturn(Map.of(
                "paymentItemId", "paymentItemIdVar1",
                "tradesOffset", "tradesOffsetVar1"
        ));

        expected.setDocument("query GetSellableItems(\n" +
                             "    $spaceId: String!,\n" +
                             "    $sortBy: MarketableItemSort\n" +
                             ", $offset: Int, $limit: Int) {\n" +
                             "    game(spaceId: $spaceId) {\n" +
                             "marketableItems(\n" +
                             "                    limit: $limit\n" +
                             "                    offset: $offset\n" +
                             "                    sortBy: $sortBy\n" +
                             "                    withMarketData: true\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "               __typename\n" +
                             "      }\n" +
                             "      __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment MarketableItemFragment on MarketableItem {\n" +
                             "  item {\n" +
                             "  ...SecondaryStoreItemFragment\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  marketData {\n" +
                             "  ...MarketDataFragment\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment SecondaryStoreItemFragment on SecondaryStoreItem {\n" +
                             "  itemId\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment MarketDataFragment on MarketableItemMarketData {\n" +
                             "  sellStats {\n" +
                             "  lowestPrice\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  buyStats {\n" +
                             "  highestPrice\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  __typename\n" +
                             "  }\n");

        expected.setVariables(
                Map.of(
                        "offset", 25,
                        "limit", 250,
                        "paymentItemId", "paymentItemIdVar1",
                        "tradesOffset", "tradesOffsetVar1"
                )
        );

        expected.setAliasesToFields(
                Map.of()
        );

        BuiltGraphQlDocument result = builder.buildCommonQueryItemsPricesDocument(250, 25);

        System.out.println(result);

        assertEquals(expected.getDocument(), result.getDocument());
        assertEquals(expected.getVariables().size(), result.getVariables().size());
        assertTrue(expected.getVariables().entrySet().containsAll(result.getVariables().entrySet()));
        assertEquals(expected.getAliasesToFields().size(), result.getAliasesToFields().size());
        assertTrue(expected.getAliasesToFields().entrySet().containsAll(result.getAliasesToFields().entrySet()));
    }

    @Test
    public void buildPersonalQueryUserStatsDocument_should_return_expected_result_for_divisible_without_remainder_limit() {
        BuiltGraphQlDocument expected = new BuiltGraphQlDocument();

        when(graphQlVariablesService.getDefaultFetchLimitedItemsPricesVariables()).thenReturn(Map.of(
                "paymentItemId", "paymentItemIdVar1",
                "tradesOffset", "tradesOffsetVar1"
        ));

        expected.setDocument("query GetSellableItems(\n" +
                             "    $spaceId: String!,\n" +
                             "    $sortBy: MarketableItemSort\n" +
                             ", $limit: Int, $offset0: Int, $offset1: Int, $lastQueryLimit: Int) {\n" +
                             "    game(spaceId: $spaceId) {\n" +
                             "marketableItems0: marketableItems(\n" +
                             "                    limit: $limit\n" +
                             "                    offset: $offset0\n" +
                             "                    sortBy: $sortBy\n" +
                             "                    withMarketData: true\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "marketableItems1: marketableItems(\n" +
                             "                    limit: $lastQueryLimit\n" +
                             "                    offset: $offset1\n" +
                             "                    sortBy: $sortBy\n" +
                             "                    withMarketData: true\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "               __typename\n" +
                             "      }\n" +
                             "      __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment MarketableItemFragment on MarketableItem {\n" +
                             "  item {\n" +
                             "  ...SecondaryStoreItemFragment\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  marketData {\n" +
                             "  ...MarketDataFragment\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment SecondaryStoreItemFragment on SecondaryStoreItem {\n" +
                             "  itemId\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment MarketDataFragment on MarketableItemMarketData {\n" +
                             "  sellStats {\n" +
                             "  lowestPrice\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  buyStats {\n" +
                             "  highestPrice\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  __typename\n" +
                             "  }\n");

        expected.setVariables(
                Map.of(
                        "offset0", 50,
                        "offset1", 550,
                        "limit", 500,
                        "paymentItemId", "paymentItemIdVar1",
                        "tradesOffset", "tradesOffsetVar1",
                        "lastQueryLimit", 500
                )
        );

        expected.setAliasesToFields(
                Map.of(
                        "marketableItems0", "marketableItems",
                        "marketableItems1", "marketableItems"
                )
        );

        BuiltGraphQlDocument result = builder.buildCommonQueryItemsPricesDocument(1000, 50);

        System.out.println(result);

        assertEquals(expected.getDocument(), result.getDocument());
        assertEquals(expected.getVariables().size(), result.getVariables().size());
        assertTrue(expected.getVariables().entrySet().containsAll(result.getVariables().entrySet()));
        assertEquals(expected.getAliasesToFields().size(), result.getAliasesToFields().size());
        assertTrue(expected.getAliasesToFields().entrySet().containsAll(result.getAliasesToFields().entrySet()));
    }

    @Test
    public void buildPersonalQueryUserStatsDocument_should_return_expected_result_for_3_expected_queries() {

        when(graphQlVariablesService.getDefaultFetchLimitedItemsPricesVariables()).thenReturn(Map.of(
                "spaceId", "spaceIdVar3",
                "tradesLimit", "tradesLimitVar3"
        ));

        BuiltGraphQlDocument expected = new BuiltGraphQlDocument();
        expected.setDocument("query GetSellableItems(\n" +
                             "    $spaceId: String!,\n" +
                             "    $sortBy: MarketableItemSort\n" +
                             ", $limit: Int, $offset0: Int, $offset1: Int, $offset2: Int, $lastQueryLimit: Int) {\n" +
                             "    game(spaceId: $spaceId) {\n" +
                             "marketableItems0: marketableItems(\n" +
                             "                    limit: $limit\n" +
                             "                    offset: $offset0\n" +
                             "                    sortBy: $sortBy\n" +
                             "                    withMarketData: true\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "marketableItems1: marketableItems(\n" +
                             "                    limit: $limit\n" +
                             "                    offset: $offset1\n" +
                             "                    sortBy: $sortBy\n" +
                             "                    withMarketData: true\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "marketableItems2: marketableItems(\n" +
                             "                    limit: $lastQueryLimit\n" +
                             "                    offset: $offset2\n" +
                             "                    sortBy: $sortBy\n" +
                             "                    withMarketData: true\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "               __typename\n" +
                             "      }\n" +
                             "      __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment MarketableItemFragment on MarketableItem {\n" +
                             "  item {\n" +
                             "  ...SecondaryStoreItemFragment\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  marketData {\n" +
                             "  ...MarketDataFragment\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment SecondaryStoreItemFragment on SecondaryStoreItem {\n" +
                             "  itemId\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "\n" +
                             "  fragment MarketDataFragment on MarketableItemMarketData {\n" +
                             "  sellStats {\n" +
                             "  lowestPrice\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  buyStats {\n" +
                             "  highestPrice\n" +
                             "  __typename\n" +
                             "  }\n" +
                             "  __typename\n" +
                             "  }\n");

        expected.setVariables(Map.of(
                "limit", 500,
                "offset0", 0,
                "offset1", 500,
                "offset2", 1000,
                "lastQueryLimit", 350,
                "tradesLimit", "tradesLimitVar3",
                "spaceId", "spaceIdVar3"
        ));

        expected.setAliasesToFields(Map.of(
                "marketableItems0", "marketableItems",
                "marketableItems1", "marketableItems",
                "marketableItems2", "marketableItems"
        ));

        BuiltGraphQlDocument result = builder.buildCommonQueryItemsPricesDocument(1350, 0);

        System.out.println(result);

        assertEquals(expected.getDocument(), result.getDocument());
        assertEquals(expected.getVariables().size(), result.getVariables().size());
        assertTrue(expected.getVariables().entrySet().containsAll(result.getVariables().entrySet()));
        assertEquals(expected.getAliasesToFields().size(), result.getAliasesToFields().size());
        assertTrue(expected.getAliasesToFields().entrySet().containsAll(result.getAliasesToFields().entrySet()));
    }
}