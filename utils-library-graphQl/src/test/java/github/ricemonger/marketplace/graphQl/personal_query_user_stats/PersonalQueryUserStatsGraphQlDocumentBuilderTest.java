package github.ricemonger.marketplace.graphQl.personal_query_user_stats;

import github.ricemonger.marketplace.graphQl.BuiltGraphQlDocument;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PersonalQueryUserStatsGraphQlDocumentBuilderTest {

    private GraphQlVariablesService graphQlVariablesService = mock(GraphQlVariablesService.class);

    private PersonalQueryUserStatsGraphQlDocumentBuilder personalQueryUserStatsGraphQlDocumentBuilder =
            new PersonalQueryUserStatsGraphQlDocumentBuilder(graphQlVariablesService);

    @Test
    public void buildPersonalQueryUserStatsDocument_should_throw_if_0_expected_queries() {
        assertThrows(IllegalArgumentException.class, () -> {
            personalQueryUserStatsGraphQlDocumentBuilder.buildPersonalQueryUserStatsDocument(0);
        });
    }

    @Test
    public void buildPersonalQueryUserStatsDocument_should_return_expected_result_for_1_expected_query() {
        BuiltGraphQlDocument expected = new BuiltGraphQlDocument();

        when(graphQlVariablesService.getDefaultFetchUserStatsVariables()).thenReturn(Map.of(
                "paymentItemId", "paymentItemIdVar1",
                "tradesOffset", "tradesOffsetVar1"
        ));

        expected.setDocument("query GetUserStats(\n" +
                             " $spaceId: String!,\n" +
                             " $tradesLimit: Int!,\n" +
                             " $tradesOffset: Int,\n" +
                             " $paymentItemId: String!,\n" +
                             " $ownedItemsSortBy: MarketableItemSort,\n" +
                             " $ownedItemsLimit: Int!\n" +
                             ", $ownedItemsOffset0: Int) {\n" +
                             "game(spaceId: $spaceId) {\n" +
                             "    viewer {\n" +
                             "        meta {\n" +
                             "marketableItems0: marketableItems(\n" +
                             "                    limit: $ownedItemsLimit\n" +
                             "                    offset: $ownedItemsOffset0\n" +
                             "                    sortBy: $ownedItemsSortBy\n" +
                             "                    withMarketData: false\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "                tradesLimitations {\n" +
                             "                    ...TradesLimitationsFragment\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "\n" +
                             "                secondaryStoreItem(itemId: $paymentItemId) {\n" +
                             "                    meta {\n" +
                             "                        quantity\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "\n" +
                             "                currentTrades: trades(\n" +
                             "                                     limit: $tradesLimit\n" +
                             "                                     offset: $tradesOffset\n" +
                             "                                     filterBy: {states: [Created]}\n" +
                             "                                     sortBy: {field: LAST_MODIFIED_AT}\n" +
                             "                                 ) {\n" +
                             "                                     nodes {\n" +
                             "                                         ...TradeFragment\n" +
                             "                                         __typename\n" +
                             "                                     }\n" +
                             "                                     __typename\n" +
                             "                                 }\n" +
                             "\n" +
                             "                finishedTrades: trades(\n" +
                             "                                     limit: $tradesLimit\n" +
                             "                                     offset: $tradesOffset\n" +
                             "                                     filterBy: {states: [Succeeded, Failed]}\n" +
                             "                                     sortBy: {field: LAST_MODIFIED_AT}\n" +
                             "                                 ) {\n" +
                             "                                     nodes {\n" +
                             "                                         ...TradeFragment\n" +
                             "                                         __typename\n" +
                             "                                     }\n" +
                             "                                     __typename\n" +
                             "                                 }\n" +
                             "\n" +
                             "                __typename\n" +
                             "            }\n" +
                             "            __typename\n" +
                             "        }\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "}\n" +
                             "\n" +
                             "fragment TradesLimitationsFragment on UserGameTradesLimitations {\n" +
                             "    buy {\n" +
                             "        resolvedTransactionCount\n" +
                             "        activeTransactionCount\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    sell {\n" +
                             "        resolvedTransactionCount\n" +
                             "        activeTransactionCount\n" +
                             "        resaleLocks {\n" +
                             "            itemId\n" +
                             "            expiresAt\n" +
                             "            __typename\n" +
                             "        }\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    __typename\n" +
                             "}\n" +
                             "\n" +
                             "fragment MarketableItemFragment on MarketableItem {\n" +
                             "    item {\n" +
                             "        ...SecondaryStoreItemFragment\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    __typename\n" +
                             "}\n" +
                             "\n" +
                             "fragment TradeFragment on Trade {\n" +
                             "    tradeId\n" +
                             "    state\n" +
                             "    category\n" +
                             "    expiresAt\n" +
                             "    lastModifiedAt\n" +
                             "    tradeItems {\n" +
                             "        item {\n" +
                             "            itemId\n" +
                             "            name\n" +
                             "            __typename\n" +
                             "        }\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    payment {\n" +
                             "        price\n" +
                             "        transactionFee\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    paymentOptions {\n" +
                             "        price\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    paymentProposal {\n" +
                             "        price\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    __typename\n" +
                             "}\n" +
                             "\n" +
                             "fragment SecondaryStoreItemFragment on SecondaryStoreItem {\n" +
                             "     itemId\n" +
                             "}\n");

        expected.setVariables(
                Map.of(
                        "ownedItemsOffset0", 0,
                        "paymentItemId", "paymentItemIdVar1",
                        "tradesOffset", "tradesOffsetVar1"
                )
        );

        expected.setAliasesToFields(
                Map.of("marketableItems0", "marketableItems")
        );

        BuiltGraphQlDocument result = personalQueryUserStatsGraphQlDocumentBuilder.buildPersonalQueryUserStatsDocument(1);

        System.out.println(result);

        assertEquals(expected.getDocument(), result.getDocument());
        assertEquals(expected.getVariables().size(), result.getVariables().size());
        assertTrue(expected.getVariables().entrySet().containsAll(result.getVariables().entrySet()));
        assertEquals(expected.getAliasesToFields().size(), result.getAliasesToFields().size());
        assertTrue(expected.getAliasesToFields().entrySet().containsAll(result.getAliasesToFields().entrySet()));
    }

    @Test
    public void buildPersonalQueryUserStatsDocument_should_return_expected_result_for_3_expected_queries() {

        when(graphQlVariablesService.getDefaultFetchUserStatsVariables()).thenReturn(Map.of(
                "spaceId", "spaceIdVar3",
                "tradesLimit", "tradesLimitVar3"
        ));

        BuiltGraphQlDocument expected = new BuiltGraphQlDocument();
        expected.setDocument("query GetUserStats(\n" +
                             " $spaceId: String!,\n" +
                             " $tradesLimit: Int!,\n" +
                             " $tradesOffset: Int,\n" +
                             " $paymentItemId: String!,\n" +
                             " $ownedItemsSortBy: MarketableItemSort,\n" +
                             " $ownedItemsLimit: Int!\n" +
                             ", $ownedItemsOffset0: Int, $ownedItemsOffset1: Int, $ownedItemsOffset2: Int) {\n" +
                             "game(spaceId: $spaceId) {\n" +
                             "    viewer {\n" +
                             "        meta {\n" +
                             "marketableItems0: marketableItems(\n" +
                             "                    limit: $ownedItemsLimit\n" +
                             "                    offset: $ownedItemsOffset0\n" +
                             "                    sortBy: $ownedItemsSortBy\n" +
                             "                    withMarketData: false\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "marketableItems1: marketableItems(\n" +
                             "                    limit: $ownedItemsLimit\n" +
                             "                    offset: $ownedItemsOffset1\n" +
                             "                    sortBy: $ownedItemsSortBy\n" +
                             "                    withMarketData: false\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "marketableItems2: marketableItems(\n" +
                             "                    limit: $ownedItemsLimit\n" +
                             "                    offset: $ownedItemsOffset2\n" +
                             "                    sortBy: $ownedItemsSortBy\n" +
                             "                    withMarketData: false\n" +
                             "                ) {\n" +
                             "                    nodes {\n" +
                             "                        ...MarketableItemFragment\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    totalCount\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "                tradesLimitations {\n" +
                             "                    ...TradesLimitationsFragment\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "\n" +
                             "                secondaryStoreItem(itemId: $paymentItemId) {\n" +
                             "                    meta {\n" +
                             "                        quantity\n" +
                             "                        __typename\n" +
                             "                    }\n" +
                             "                    __typename\n" +
                             "                }\n" +
                             "\n" +
                             "                currentTrades: trades(\n" +
                             "                                     limit: $tradesLimit\n" +
                             "                                     offset: $tradesOffset\n" +
                             "                                     filterBy: {states: [Created]}\n" +
                             "                                     sortBy: {field: LAST_MODIFIED_AT}\n" +
                             "                                 ) {\n" +
                             "                                     nodes {\n" +
                             "                                         ...TradeFragment\n" +
                             "                                         __typename\n" +
                             "                                     }\n" +
                             "                                     __typename\n" +
                             "                                 }\n" +
                             "\n" +
                             "                finishedTrades: trades(\n" +
                             "                                     limit: $tradesLimit\n" +
                             "                                     offset: $tradesOffset\n" +
                             "                                     filterBy: {states: [Succeeded, Failed]}\n" +
                             "                                     sortBy: {field: LAST_MODIFIED_AT}\n" +
                             "                                 ) {\n" +
                             "                                     nodes {\n" +
                             "                                         ...TradeFragment\n" +
                             "                                         __typename\n" +
                             "                                     }\n" +
                             "                                     __typename\n" +
                             "                                 }\n" +
                             "\n" +
                             "                __typename\n" +
                             "            }\n" +
                             "            __typename\n" +
                             "        }\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "}\n" +
                             "\n" +
                             "fragment TradesLimitationsFragment on UserGameTradesLimitations {\n" +
                             "    buy {\n" +
                             "        resolvedTransactionCount\n" +
                             "        activeTransactionCount\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    sell {\n" +
                             "        resolvedTransactionCount\n" +
                             "        activeTransactionCount\n" +
                             "        resaleLocks {\n" +
                             "            itemId\n" +
                             "            expiresAt\n" +
                             "            __typename\n" +
                             "        }\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    __typename\n" +
                             "}\n" +
                             "\n" +
                             "fragment MarketableItemFragment on MarketableItem {\n" +
                             "    item {\n" +
                             "        ...SecondaryStoreItemFragment\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    __typename\n" +
                             "}\n" +
                             "\n" +
                             "fragment TradeFragment on Trade {\n" +
                             "    tradeId\n" +
                             "    state\n" +
                             "    category\n" +
                             "    expiresAt\n" +
                             "    lastModifiedAt\n" +
                             "    tradeItems {\n" +
                             "        item {\n" +
                             "            itemId\n" +
                             "            name\n" +
                             "            __typename\n" +
                             "        }\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    payment {\n" +
                             "        price\n" +
                             "        transactionFee\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    paymentOptions {\n" +
                             "        price\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    paymentProposal {\n" +
                             "        price\n" +
                             "        __typename\n" +
                             "    }\n" +
                             "    __typename\n" +
                             "}\n" +
                             "\n" +
                             "fragment SecondaryStoreItemFragment on SecondaryStoreItem {\n" +
                             "     itemId\n" +
                             "}\n");

        expected.setVariables(Map.of(
                "ownedItemsOffset0", 0,
                "ownedItemsOffset1", 500,
                "ownedItemsOffset2", 1000,
                "tradesLimit", "tradesLimitVar3",
                "spaceId", "spaceIdVar3"
        ));

        expected.setAliasesToFields(Map.of(
                "marketableItems0", "marketableItems",
                "marketableItems1", "marketableItems",
                "marketableItems2", "marketableItems"
        ));

        BuiltGraphQlDocument result = personalQueryUserStatsGraphQlDocumentBuilder.buildPersonalQueryUserStatsDocument(3);

        System.out.println(result);

        assertEquals(expected.getDocument(), result.getDocument());
        assertEquals(expected.getVariables().size(), result.getVariables().size());
        assertTrue(expected.getVariables().entrySet().containsAll(result.getVariables().entrySet()));
        assertEquals(expected.getAliasesToFields().size(), result.getAliasesToFields().size());
        assertTrue(expected.getAliasesToFields().entrySet().containsAll(result.getAliasesToFields().entrySet()));
    }
}