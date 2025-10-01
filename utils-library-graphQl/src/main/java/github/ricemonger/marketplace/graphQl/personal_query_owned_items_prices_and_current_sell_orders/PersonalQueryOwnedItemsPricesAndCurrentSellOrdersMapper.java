package github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders;

import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.Meta;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.MarketableItems;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.Trades;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.marketableItems.Node;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.marketableItems.node.Item;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.marketableItems.node.MarketData;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.marketableItems.node.marketData.BuyStats;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.marketableItems.node.marketData.SellStats;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.trades.Nodes;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.trades.nodes.PaymentOptions;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.FastUbiUserStats;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PersonalQueryOwnedItemsPricesAndCurrentSellOrdersMapper {

    public FastUbiUserStats mapOwnedItemsPricesAndCurrentSellOrders(Meta meta) throws GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException, GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException {

        if (meta == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Meta is null");
        }

        MarketableItems marketableItems = meta.getMarketableItems();

        Trades trades = meta.getTrades();

        if (marketableItems == null || trades == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("MarketableItems or Trades is null");
        }

        List<ItemCurrentPrices> items = mapItems(marketableItems);
        List<SellTrade> currentSellOrders = mapCurrentOrders(trades);

        return new FastUbiUserStats(items, currentSellOrders);
    }

    public List<SellTrade> mapCurrentOrders(Trades trades) throws GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException {
        if (trades == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Trades is null");
        }
        List<Nodes> nodes = trades.getNodes();

        return nodes.stream().map(this::mapCurrentOrder).toList();
    }

    public SellTrade mapCurrentOrder(Nodes node) throws GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException {
        if (node == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Node is null");
        }

        SellTrade result = new SellTrade();

        if (node.getTradeId() == null
            || node.getTradeItems() == null || node.getTradeItems().length == 0
            || node.getTradeItems()[0].getItem() == null
            || node.getTradeItems()[0].getItem().getItemId() == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("One of node fields is null-" + node);
        }

        result.setTradeId(node.getTradeId());

        result.setItemId(node.getTradeItems()[0].getItem().getItemId());

        PaymentOptions[] paymentOptions = node.getPaymentOptions();

        boolean paymentOptionsIsNull = paymentOptions == null || paymentOptions.length == 0 || paymentOptions[0].getPrice() == null;

        if (!paymentOptionsIsNull) {
            result.setPrice(paymentOptions[0].getPrice());
        } else {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Node doesnt have paymentOptions-" + node);
        }

        return result;
    }

    public List<ItemCurrentPrices> mapItems(MarketableItems marketableItems) throws GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException {

        if (marketableItems == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("MarketableItems is null");
        }

        List<Node> nodes = marketableItems.getNodes();

        if (nodes == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Nodes is null");
        }
        return nodes.stream().map(this::mapItem).collect(Collectors.toList());
    }

    public ItemCurrentPrices mapItem(Node node) throws GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException {
        if (node == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Node is null");
        }
        Item item = node.getItem();

        if (item == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Item is null, node-" + node);
        }

        ItemCurrentPrices result = new ItemCurrentPrices();

        if (item.getItemId() == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("One of Item fields is null, item-" + item);
        }

        result.setItemId(item.getItemId());


        if (node.getMarketData() == null) {
            throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("Market data is null, node-" + node);
        }

        MarketData marketData = node.getMarketData();

        BuyStats buyStats = marketData.getBuyStats() == null || marketData.getBuyStats().length == 0 ? null : marketData.getBuyStats()[0];
        SellStats sellStats = marketData.getSellStats() == null || marketData.getSellStats().length == 0 ? null : marketData.getSellStats()[0];

        if (buyStats != null) {
            if (buyStats.getHighestPrice() == null) {
                throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("BuyStats highest price is null, buyStats-" + buyStats);
            }
            result.setMaxBuyPrice(buyStats.getHighestPrice());
        } else {
            result.setMaxBuyPrice(null);
        }

        if (sellStats != null) {
            if (sellStats.getLowestPrice() == null) {
                throw new GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException("SellStats lowest price is null, sellStats-" + sellStats);
            }
            result.setMinSellPrice(sellStats.getLowestPrice());
        } else {
            result.setMinSellPrice(null);
        }

        return result;
    }
}
