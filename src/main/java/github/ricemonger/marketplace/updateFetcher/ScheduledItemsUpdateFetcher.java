package github.ricemonger.marketplace.updateFetcher;


import github.ricemonger.marketplace.updateFetcher.feign.ItemsUpdateFetcherFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ScheduledItemsUpdateFetcher {

    private final UbiServiceConfiguration ubiServiceConfiguration;

    private final ItemsUpdateFetcherFeignClient itemsUpdateFetcherFeignClient;

    @Scheduled(fixedRate = 1 * 60 * 1000) // 5min
    public void fetchUpdates() {

        System.out.println(ubiServiceConfiguration.getAuthorization());

        int offset = 0;

        FetchUpdateRequestVariables vars = new FetchUpdateRequestVariables();
        vars.setMarketplaceOperationName(MarketplaceOperationName.GetMarketableItems);
        vars.setWithOwnership(true);
        vars.setSpaceId(FetchUpdateRequestVariables.DEFAULT_SPACE_ID);
        vars.setLimit(500);
        vars.setOffset(offset);
        vars.setFilterByTypes(Collections.emptyList());
        vars.setFilterByTags(Collections.emptyList());
        vars.setSortByField(SortingField.ACTIVE_COUNT);
        vars.setSortByOrderType(OrderType.Sell);

        String update = itemsUpdateFetcherFeignClient.fetchUpdate(
                ubiServiceConfiguration.getAuthorization(),
                ubiServiceConfiguration.getRegionId(),
                ubiServiceConfiguration.getLocaleCode(),
                ubiServiceConfiguration.getUbiAppId(),
                ubiServiceConfiguration.getSessionId(),
                ubiServiceConfiguration.getProfileId(),
                buildPayload(vars));

        System.out.println(update);
    }

    private String buildPayload(FetchUpdateRequestVariables payloadVariables) {

        StringBuilder sb = new StringBuilder("{\"variables\":{");

        sb.append("\"withOwnership\":").append(payloadVariables.isWithOwnership()).append(",");

        sb.append("\"spaceId\":\"").append(payloadVariables.getSpaceId()).append("\",");

        sb.append("\"limit\":").append(payloadVariables.getLimit()).append(",");

        sb.append("\"offset\":").append(payloadVariables.getOffset()).append(",");

        sb.append("\"filterBy\":{");
        sb.append("\"types\":[").append("],");
        sb.append("\"tags\":[").append("]},");

        sb.append("\"sortBy\":{");
        sb.append("\"field\":\"").append(payloadVariables.getSortByField()).append("\",");
        sb.append("\"orderType\":\"").append(payloadVariables.getSortByOrderType()).append("\",");
        sb.append("\"direction\":\"").append(FetchUpdateRequestVariables.SORT_BY_DIRECTION).append("\",");
        sb.append("\"paymentItemId\":\"").append(FetchUpdateRequestVariables.PAYMENT_ITEM_ID).append("\"}},");

        sb.append("\"query\":\"query ").append(payloadVariables.getMarketplaceOperationName());
        sb.append("($spaceId: String!, $limit: Int!, $offset: Int, $filterBy: " + "MarketableItemFilter, $withOwnership: Boolean = ").append(payloadVariables.isWithOwnership());
        sb.append(", $sortBy: MarketableItemSort) {\\n  game(spaceId: $spaceId) {\\n    id\\n    marketableItems(\\n      limit: $limit\\n      " +
            "offset: $offset\\n      filterBy: $filterBy\\n      sortBy: $sortBy\\n      withMarketData: true\\n    ) {\\n      nodes {\\n        ...MarketableItemFragment\\n        __typename\\n      }\\n      totalCount\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\\nfragment MarketableItemFragment on MarketableItem {\\n  item {\\n    ...SecondaryStoreItemFragment\\n    ...SecondaryStoreItemOwnershipFragment @include(if: $withOwnership)\\n    __typename\\n  }\\n  marketData {\\n    ...MarketDataFragment\\n    __typename\\n  }\\n  viewer {\\n    meta {\\n      id\\n      activeTrade {\\n        ...TradeFragment\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment SecondaryStoreItemFragment on SecondaryStoreItem {\\n  id\\n  assetUrl\\n  itemId\\n  name\\n  tags\\n  type\\n  viewer {\\n    meta {\\n      id\\n      isReserved\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment SecondaryStoreItemOwnershipFragment on SecondaryStoreItem {\\n  viewer {\\n    meta {\\n      id\\n      isOwned\\n      quantity\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment MarketDataFragment on MarketableItemMarketData {\\n  id\\n  sellStats {\\n    id\\n    paymentItemId\\n    lowestPrice\\n    highestPrice\\n    activeCount\\n    __typename\\n  }\\n  buyStats {\\n    id\\n    paymentItemId\\n    lowestPrice\\n    highestPrice\\n    activeCount\\n    __typename\\n  }\\n  lastSoldAt {\\n    id\\n    paymentItemId\\n    price\\n    performedAt\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment TradeFragment on Trade {\\n  id\\n  tradeId\\n  state\\n  category\\n  createdAt\\n  expiresAt\\n  lastModifiedAt\\n  failures\\n  tradeItems {\\n    id\\n    item {\\n      ...SecondaryStoreItemFragment\\n      ...SecondaryStoreItemOwnershipFragment\\n      __typename\\n    }\\n    __typename\\n  }\\n  payment {\\n    id\\n    item {\\n      ...SecondaryStoreItemQuantityFragment\\n      __typename\\n    }\\n    price\\n    transactionFee\\n    __typename\\n  }\\n  paymentOptions {\\n    id\\n    item {\\n      ...SecondaryStoreItemQuantityFragment\\n      __typename\\n    }\\n    price\\n    transactionFee\\n    __typename\\n  }\\n  paymentProposal {\\n    id\\n    item {\\n      ...SecondaryStoreItemQuantityFragment\\n      __typename\\n    }\\n    price\\n    __typename\\n  }\\n  viewer {\\n    meta {\\n      id\\n      tradesLimitations {\\n        ...TradesLimitationsFragment\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment SecondaryStoreItemQuantityFragment on SecondaryStoreItem {\\n  viewer {\\n    meta {\\n      id\\n      quantity\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment TradesLimitationsFragment on UserGameTradesLimitations {\\n  id\\n  buy {\\n    resolvedTransactionCount\\n    resolvedTransactionPeriodInMinutes\\n    activeTransactionCount\\n    __typename\\n  }\\n  sell {\\n    resolvedTransactionCount\\n    resolvedTransactionPeriodInMinutes\\n    activeTransactionCount\\n    resaleLocks {\\n      itemId\\n      expiresAt\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\"}");

        return sb.toString();
    }
}
