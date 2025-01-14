package github.ricemonger.marketplace.graphQl;


public class GraphQlDocuments {
    public final static String QUERY_ITEMS_STATS_DOCUMENT_NAME = "common_query_items";

    public final static String QUERY_ITEMS_MIN_SELL_PRICES_DOCUMENT_NAME = "common_query_items_min_sell_prices";

    public final static String QUERY_ITEMS_SALE_STATS_DOCUMENT_NAME = "common_query_items_sale_stats";

    public final static String QUERY_MARKETPLACE_CONFIG_DOCUMENT_NAME = "config_query_marketplace";

    public final static String QUERY_RESOLVED_TRANSACTION_PERIOD_CONFIG_DOCUMENT_NAME = "config_query_resolved_transaction_period";

    public final static String QUERY_TRADE_CONFIG_DOCUMENT_NAME = "config_query_trade";

    public final static String MUTATION_ORDER_BUY_CREATE_DOCUMENT_NAME = "personal_mutation_order_buy_create";

    public final static String MUTATION_ORDER_BUY_UPDATE_DOCUMENT_NAME = "personal_mutation_order_buy_update";

    public final static String MUTATION_ORDER_CANCEL_DOCUMENT_NAME = "personal_mutation_order_cancel";

    public static final String MUTATION_ORDER_CANCEL_DOCUMENT = """
            mutation CancelOrder($spaceId: String!, $tradeId: String!) {
                cancelOrder(spaceId: $spaceId, tradeId: $tradeId) {
                    trade {
                        ...TradeFragment
                        __typename
                    }
                    __typename
                }
            }
            
            fragment TradeFragment on Trade {
                id
                tradeId
                state
                category
                createdAt
                expiresAt
                lastModifiedAt
                failures
                tradeItems {
                    id
                    item {
                        ...SecondaryStoreItemFragment
                        ...SecondaryStoreItemOwnershipFragment
                        __typename
                    }
                    __typename
                }
                payment {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    transactionFee
                    __typename
                }
                paymentOptions {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    transactionFee
                    __typename
                }
                paymentProposal {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    __typename
                }
                viewer {
                    meta {
                        id
                        tradesLimitations {
                            ...TradesLimitationsFragment
                            __typename
                        }
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemFragment on SecondaryStoreItem {
                id
                assetUrl
                itemId
                name
                tags
                type
                viewer {
                    meta {
                        id
                        isReserved
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemOwnershipFragment on SecondaryStoreItem {
                viewer {
                    meta {
                        id
                        isOwned
                        quantity
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemQuantityFragment on SecondaryStoreItem {
                viewer {
                    meta {
                        id
                        quantity
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment TradesLimitationsFragment on UserGameTradesLimitations {
                id
                buy {
                    resolvedTransactionCount
                    resolvedTransactionPeriodInMinutes
                    activeTransactionCount
                    __typename
                }
                sell {
                    resolvedTransactionCount
                    resolvedTransactionPeriodInMinutes
                    activeTransactionCount
                    resaleLocks {
                        itemId
                        expiresAt
                        __typename
                    }
                    __typename
                }
                __typename
            }
            """;

    public final static String MUTATION_ORDER_SELL_CREATE_DOCUMENT_NAME = "personal_mutation_order_sell_create";

    public static final String MUTATION_ORDER_SELL_CREATE_DOCUMENT = """
            mutation CreateSellOrder($spaceId: String!, $tradeItems: [TradeOrderItem!]!, $paymentOptions: [PaymentItem!]!) {
                createSellOrder(
                    spaceId: $spaceId
                    tradeItems: $tradeItems
                    paymentOptions: $paymentOptions
                ) {
                    trade {
                        ...TradeFragment
                        __typename
                    }
                    __typename
                }
            }
            
            fragment TradeFragment on Trade {
                id
                tradeId
                state
                category
                createdAt
                expiresAt
                lastModifiedAt
                failures
                tradeItems {
                    id
                    item {
                        ...SecondaryStoreItemFragment
                        ...SecondaryStoreItemOwnershipFragment
                        __typename
                    }
                    __typename
                }
                payment {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    transactionFee
                    __typename
                }
                paymentOptions {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    transactionFee
                    __typename
                }
                paymentProposal {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    __typename
                }
                viewer {
                    meta {
                        id
                        tradesLimitations {
                            ...TradesLimitationsFragment
                            __typename
                        }
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemFragment on SecondaryStoreItem {
                id
                assetUrl
                itemId
                name
                tags
                type
                viewer {
                    meta {
                        id
                        isReserved
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemOwnershipFragment on SecondaryStoreItem {
                viewer {
                    meta {
                        id
                        isOwned
                        quantity
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemQuantityFragment on SecondaryStoreItem {
                viewer {
                    meta {
                        id
                        quantity
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment TradesLimitationsFragment on UserGameTradesLimitations {
                id
                buy {
                    resolvedTransactionCount
                    resolvedTransactionPeriodInMinutes
                    activeTransactionCount
                    __typename
                }
                sell {
                    resolvedTransactionCount
                    resolvedTransactionPeriodInMinutes
                    activeTransactionCount
                    resaleLocks {
                        itemId
                        expiresAt
                        __typename
                    }
                    __typename
                }
                __typename
            }
            """;

    public final static String MUTATION_ORDER_SELL_UPDATE_DOCUMENT_NAME = "personal_mutation_order_sell_update";

    public static final String MUTATION_ORDER_SELL_UPDATE_DOCUMENT = """
            mutation UpdateSellOrder($spaceId: String!, $tradeId: String!, $paymentOptions: [PaymentItem!]!) {
                updateSellOrder(
                    spaceId: $spaceId
                    tradeId: $tradeId
                    paymentOptions: $paymentOptions
                ) {
                    trade {
                        ...TradeFragment
                        __typename
                    }
                    __typename
                }
            }
            
            fragment TradeFragment on Trade {
                id
                tradeId
                state
                category
                createdAt
                expiresAt
                lastModifiedAt
                failures
                tradeItems {
                    id
                    item {
                        ...SecondaryStoreItemFragment
                        ...SecondaryStoreItemOwnershipFragment
                        __typename
                    }
                    __typename
                }
                payment {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    transactionFee
                    __typename
                }
                paymentOptions {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    transactionFee
                    __typename
                }
                paymentProposal {
                    id
                    item {
                        ...SecondaryStoreItemQuantityFragment
                        __typename
                    }
                    price
                    __typename
                }
                viewer {
                    meta {
                        id
                        tradesLimitations {
                            ...TradesLimitationsFragment
                            __typename
                        }
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemFragment on SecondaryStoreItem {
                id
                assetUrl
                itemId
                name
                tags
                type
                viewer {
                    meta {
                        id
                        isReserved
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemOwnershipFragment on SecondaryStoreItem {
                viewer {
                    meta {
                        id
                        isOwned
                        quantity
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemQuantityFragment on SecondaryStoreItem {
                viewer {
                    meta {
                        id
                        quantity
                        __typename
                    }
                    __typename
                }
                __typename
            }
            
            fragment TradesLimitationsFragment on UserGameTradesLimitations {
                id
                buy {
                    resolvedTransactionCount
                    resolvedTransactionPeriodInMinutes
                    activeTransactionCount
                    __typename
                }
                sell {
                    resolvedTransactionCount
                    resolvedTransactionPeriodInMinutes
                    activeTransactionCount
                    resaleLocks {
                        itemId
                        expiresAt
                        __typename
                    }
                    __typename
                }
                __typename
            }""";

    public final static String QUERY_CREDITS_AMOUNT_DOCUMENT_NAME = "personal_query_credits_amount";

    public final static String QUERY_CURRENT_ORDERS_DOCUMENT_NAME = "personal_query_current_orders";

    public final static String QUERY_CURRENT_SELL_ORDERS_DOCUMENT_NAME = "personal_query_current_sell_orders";

    public static final String QUERY_CURRENT_SELL_ORDERS_DOCUMENT = """
            query GetTransactionsPending($spaceId: String!, $limit: Int!, $offset: Int) {
                game(spaceId: $spaceId) {
                    viewer {
                        meta {
                            trades(
                                limit: $limit
                                offset: $offset
                                filterBy: {states: [Created], category: Sell}
                                sortBy: {field: LAST_MODIFIED_AT}
                            ) {
                                nodes {
                                    ...TradeFragment
                                    __typename
                                }
                                __typename
                            }
                            __typename
                        }
                        __typename
                    }
                    __typename
                }
            }
            
            fragment TradeFragment on Trade {
                tradeId
                tradeItems {
                    item {
                        ...SecondaryStoreItemFragment
                        __typename
                    }
                    __typename
                }
                paymentOptions {
                    price
                    __typename
                }
                __typename
            }
            
            fragment SecondaryStoreItemFragment on SecondaryStoreItem {
                itemId
                __typename
            }
            
            """;

    public final static String QUERY_FINISHED_ORDERS_DOCUMENT_NAME = "personal_query_finished_orders";

    public final static String QUERY_LOCKED_ITEMS_DOCUMENT_NAME = "personal_query_locked_items";

    public final static String QUERY_ONE_ITEM_STATS_DOCUMENT_NAME = "personal_query_one_item";

    public final static String QUERY_OWNED_ITEMS_DOCUMENT_NAME = "personal_query_owned_items";

    public final static String QUERY_OWNED_ITEMS_PRICES_DOCUMENT_NAME = "personal_query_owned_items_prices";

    public final static String QUERY_OWNED_ITEMS_PRICES_DOCUMENT = """
            query GetSellableItems($spaceId: String!, $limit: Int!, $offset: Int, $sortBy: MarketableItemSort) {
                game(spaceId: $spaceId) {
                    viewer {
                        meta {
                            marketableItems(
                                limit: $limit
                                offset: $offset
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
                            __typename
                        }
                        __typename
                    }
                    __typename
                }
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
}
