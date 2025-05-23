package github.ricemonger.marketplace.graphQl.common_query_items_prices;

import github.ricemonger.marketplace.graphQl.BuiltGraphQlDocument;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommonQueryItemsPricesGraphQlDocumentBuilder {

    private final static String QUERY_PART_BEFORE_VARS =
            """
                    query GetSellableItems(
                        $spaceId: String!,
                        $sortBy: MarketableItemSort
                    """;

    private final static String QUERY_PART_AFTER_VARS_BEFORE_ADDITIONAL_QUERIES = """ 
            ) {
                game(spaceId: $spaceId) {
            """;

    private final static String QUERY_PART_AFTER_ADDITIONAL_QUERIES = """ 
                           __typename
                  }
                  __typename
              }
            
              fragment MarketableItemFragment on MarketableItem {
              item {
              ...SecondaryStoreItemFragment
              __typename
              }
              marketData {
              ...MarketDataFragment
              __typename
              }
              __typename
              }
            
              fragment SecondaryStoreItemFragment on SecondaryStoreItem {
              itemId
              __typename
              }
            
              fragment MarketDataFragment on MarketableItemMarketData {
              sellStats {
              lowestPrice
              __typename
              }
              buyStats {
              highestPrice
              __typename
              }
              __typename
              }
            """;

    private final static String ADDITIONAL_QUERY_NAME = "marketableItems";

    private final static String ADDITIONAL_QUERY_TEMPLATE = """
            marketableItems(
                                limit: $limit
                                offset: $defaultVarKey
                                sortBy: $sortBy
                                withMarketData: true
                            ) {
                                nodes {
                                    ...MarketableItemFragment
                                    __typename
                                }
                                totalCount
                                __typename
                            }
            """;

    private final GraphQlVariablesService graphQlVariablesService;

    public BuiltGraphQlDocument buildCommonQueryItemsPricesDocument(Integer limit, Integer offset) {
        StringBuilder document = new StringBuilder();
        Map<String, Object> resultingVariables = new LinkedHashMap<>();
        Map<String, String> aliasesToFields = new LinkedHashMap<>();

        Map<String, Object> defaultVariables = graphQlVariablesService.getDefaultFetchLimitedItemsPricesVariables();

        int expectedOwnedItemsQueries = (int) Math.ceil((double) limit / GraphQlVariablesService.MAX_LIMIT);

        if (expectedOwnedItemsQueries <= 0) {
            throw new IllegalArgumentException("Expected owned items queries should be greater than 0");
        }

        StringBuilder variablesSection = new StringBuilder();
        StringBuilder queriesSection = new StringBuilder();

        if (expectedOwnedItemsQueries == 1) {
            createVariable("offset", offset, resultingVariables, variablesSection);
            createVariable("limit", limit, resultingVariables, variablesSection);

            queriesSection.append(createItemQuerySection("offset"));
        } else {
            createVariable("limit", GraphQlVariablesService.MAX_LIMIT, resultingVariables, variablesSection);
            for (int i = 0; i < expectedOwnedItemsQueries; i++) {
                String varKey = "offset" + i;
                createVariable(varKey, offset + i * GraphQlVariablesService.MAX_LIMIT, resultingVariables, variablesSection);

                if (i == expectedOwnedItemsQueries - 1) {
                    int limitRemainder;
                    if (limit < GraphQlVariablesService.MAX_LIMIT) {
                        limitRemainder = limit;
                    } else {
                        limitRemainder = limit % ((expectedOwnedItemsQueries - 1) * GraphQlVariablesService.MAX_LIMIT);
                        if (limitRemainder == 0) {
                            limitRemainder = GraphQlVariablesService.MAX_LIMIT;
                        }
                    }
                    createVariable("lastQueryLimit", limitRemainder, resultingVariables, variablesSection);
                }

                String alias = ADDITIONAL_QUERY_NAME + i;

                aliasesToFields.put(alias, ADDITIONAL_QUERY_NAME);

                if (i == expectedOwnedItemsQueries - 1) {
                    queriesSection.append(createAliasedItemsQuerySection(alias, varKey, "lastQueryLimit"));
                } else {
                    queriesSection.append(createAliasedItemsQuerySection(alias, varKey));
                }

                aliasesToFields.put(ADDITIONAL_QUERY_NAME + i, ADDITIONAL_QUERY_NAME);
            }
        }

        document.append(QUERY_PART_BEFORE_VARS);

        document.append(variablesSection);

        document.append(QUERY_PART_AFTER_VARS_BEFORE_ADDITIONAL_QUERIES);

        document.append(queriesSection);

        document.append(QUERY_PART_AFTER_ADDITIONAL_QUERIES);

        resultingVariables.putAll(defaultVariables);

        return new BuiltGraphQlDocument(
                document.toString(),
                resultingVariables,
                aliasesToFields
        );
    }

    private void createVariable(String varKey, Integer varValue, Map<String, Object> resultingVariables, StringBuilder variablesSection) {
        resultingVariables.put(varKey, varValue);

        variablesSection.append(", ");
        variablesSection.append(createIntVariableSection(varKey));
    }

    private String createIntVariableSection(String varKey) {
        return "$" + varKey + ": Int";
    }

    private String createItemQuerySection(String offsetVarKey) {
        return ADDITIONAL_QUERY_TEMPLATE.replace("$defaultVarKey", "$" + offsetVarKey);
    }

    private String createAliasedItemsQuerySection(String alias, String offsetVarKey) {
        return alias + ": " + ADDITIONAL_QUERY_TEMPLATE.replace("$defaultVarKey", "$" + offsetVarKey);
    }

    private String createAliasedItemsQuerySection(String alias, String offsetVarKey, String limitVarKey) {
        return alias + ": " + ADDITIONAL_QUERY_TEMPLATE.replace("$defaultVarKey", "$" + offsetVarKey).replace("$limit", "$" + limitVarKey);
    }
}
