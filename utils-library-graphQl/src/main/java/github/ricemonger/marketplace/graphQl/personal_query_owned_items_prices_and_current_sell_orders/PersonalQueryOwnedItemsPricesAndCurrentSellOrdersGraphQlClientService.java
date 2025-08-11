package github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders;

import github.ricemonger.marketplace.graphQl.GraphQlClientFactory;
import github.ricemonger.marketplace.graphQl.GraphQlDocuments;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.Meta;
import github.ricemonger.utils.DTOs.personal.FastUbiUserStats;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalOwnedItemsMappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;

import java.util.ArrayList;

@RequiredArgsConstructor
public class PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService {
    private final GraphQlClientFactory graphQlClientFactory;

    private final GraphQlVariablesService graphQlVariablesService;

    private final PersonalQueryOwnedItemsPricesAndCurrentSellOrdersMapper personalQueryOwnedItemsPricesAndCurrentSellOrdersMapper;

    public FastUbiUserStats fetchOwnedItemsCurrentPricesAndSellOrdersForUser(AuthorizationDTO authorizationDTO, int ownedItemsLimit) throws GraphQlPersonalOwnedItemsMappingException {
        HttpGraphQlClient client = graphQlClientFactory.createAuthorizedUserClient(authorizationDTO);

        if (ownedItemsLimit <= GraphQlVariablesService.MAX_LIMIT) {

            Meta meta = client
                    .document(GraphQlDocuments.QUERY_OWNED_ITEMS_PRICES_AND_CURRENT_SELL_ORDERS_DOCUMENT)
                    .variables(graphQlVariablesService.getFetchOwnedItemsPricesAndCurrentSellOrdersVariables(0, ownedItemsLimit, 0, GraphQlVariablesService.MAX_LIMIT))
                    .retrieve("game.viewer.meta")
                    .toEntity(Meta.class)
                    .block();

            return personalQueryOwnedItemsPricesAndCurrentSellOrdersMapper.mapOwnedItemsPricesAndCurrentSellOrders(meta);

        } else {

            FastUbiUserStats result = new FastUbiUserStats();
            result.setOwnedItemsCurrentPrices(new ArrayList<>());

            int requestsToDo = (int) Math.ceil((float) ownedItemsLimit / GraphQlVariablesService.MAX_LIMIT);

            for (int i = 0; i < requestsToDo - 1; i++) {
                Meta meta = client
                        .document(GraphQlDocuments.QUERY_OWNED_ITEMS_PRICES_AND_CURRENT_SELL_ORDERS_DOCUMENT)
                        .variables(graphQlVariablesService.getFetchOwnedItemsPricesAndCurrentSellOrdersVariables(GraphQlVariablesService.MAX_LIMIT * i, GraphQlVariablesService.MAX_LIMIT, 0, GraphQlVariablesService.MAX_LIMIT))
                        .retrieve("game.viewer.meta")
                        .toEntity(Meta.class)
                        .block();

                FastUbiUserStats tempUserStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersMapper.mapOwnedItemsPricesAndCurrentSellOrders(meta);

                result.getOwnedItemsCurrentPrices().addAll(tempUserStats.getOwnedItemsCurrentPrices());
            }

            int lastRequestLimit = ownedItemsLimit % GraphQlVariablesService.MAX_LIMIT == 0 ? GraphQlVariablesService.MAX_LIMIT : ownedItemsLimit % GraphQlVariablesService.MAX_LIMIT;

            Meta meta = client
                    .document(GraphQlDocuments.QUERY_OWNED_ITEMS_PRICES_AND_CURRENT_SELL_ORDERS_DOCUMENT)
                    .variables(graphQlVariablesService.getFetchOwnedItemsPricesAndCurrentSellOrdersVariables(GraphQlVariablesService.MAX_LIMIT * (requestsToDo - 1), lastRequestLimit, 0, GraphQlVariablesService.MAX_LIMIT))
                    .retrieve("game.viewer.meta")
                    .toEntity(Meta.class)
                    .block();

            FastUbiUserStats tempUserStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersMapper.mapOwnedItemsPricesAndCurrentSellOrders(meta);

            result.getOwnedItemsCurrentPrices().addAll(tempUserStats.getOwnedItemsCurrentPrices());
            result.setCurrentSellOrders(tempUserStats.getCurrentSellOrders());

            return result;

        }
    }
}
