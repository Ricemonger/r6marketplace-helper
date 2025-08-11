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
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.trades.nodes.TradeItems;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.FastUbiUserStats;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class PersonalQueryOwnedItemsPricesAndCurrentSellOrdersMapperTest {

    private final PersonalQueryOwnedItemsPricesAndCurrentSellOrdersMapper mapper = spy(new PersonalQueryOwnedItemsPricesAndCurrentSellOrdersMapper());

    @Test
    public void mapOwnedItemsPricesAndCurrentSellOrders_should_throw_if_meta_null() {
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapOwnedItemsPricesAndCurrentSellOrders(null);
        });
    }

    @Test
    public void mapOwnedItemsPricesAndCurrentSellOrders_should_throw_if_marketableItems_null() {
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapOwnedItemsPricesAndCurrentSellOrders(new Meta(null, new Trades()));
        });
    }

    @Test
    public void mapOwnedItemsPricesAndCurrentSellOrders_should_throw_if_trades_null() {
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapOwnedItemsPricesAndCurrentSellOrders(new Meta(new MarketableItems(), null));
        });
    }

    @Test
    public void mapOwnedItemsPricesAndCurrentSellOrders_should_map_trades_and_items_and_return_result() {
        MarketableItems marketableItems = mock(MarketableItems.class);
        Trades trades = mock(Trades.class);

        Meta meta = new Meta(marketableItems, trades);

        List<ItemCurrentPrices> items = mock(List.class);
        List<SellTrade> currentSellOrders = mock(List.class);

        doReturn(items).when(mapper).mapItems(marketableItems);
        doReturn(currentSellOrders).when(mapper).mapCurrentOrders(trades);

        FastUbiUserStats result = mapper.mapOwnedItemsPricesAndCurrentSellOrders(meta);

        assertSame(items, result.getOwnedItemsCurrentPrices());
        assertSame(currentSellOrders, result.getCurrentSellOrders());
    }

    @Test
    public void mapCurrentOrders_should_map_each_order() {
        Nodes node1 = createNodeTrades();
        node1.setTradeId("tradeId1");

        Nodes node2 = createNodeTrades();
        node2.setTradeId("tradeId2");

        Nodes node3 = createNodeTrades();
        node3.setTradeId("tradeId3");

        Trades trades = new Trades(List.of(node1, node2, node3));

        assertEquals(3, mapper.mapCurrentOrders(trades).size());

        verify(mapper, times(3)).mapCurrentOrder(any());
    }

    @Test
    public void mapCurrentOrders_should_throw_if_null_trades() {
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrders(null);
        });
    }

    @Test
    public void mapCurrentOrder_should_map_order_if_valid_fields_and_null_paymentProposal() {
        Nodes node = createNodeTrades();

        SellTrade expected = new SellTrade();
        expected.setTradeId("tradeId");
        expected.setItemId("1");
        expected.setPrice(100);

        assertEquals(expected, mapper.mapCurrentOrder(node));
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_node() {
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(null);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_tradeId() {
        Nodes node = createNodeTrades();
        node.setTradeId(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_tradeItems() {
        Nodes node = createNodeTrades();
        node.setTradeItems(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_empty_tradeItems() {
        Nodes node = createNodeTrades();
        node.setTradeItems(new TradeItems[0]);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_item() {
        Nodes node = createNodeTrades();
        node.getTradeItems()[0].setItem(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_itemId() {
        Nodes node = createNodeTrades();
        node.getTradeItems()[0].getItem().setItemId(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_paymentOptions() {
        Nodes node = createNodeTrades();
        node.setPaymentOptions(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_empty_paymentOptions() {
        Nodes node = createNodeTrades();
        node.setPaymentOptions(new PaymentOptions[0]);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    @Test
    public void mapCurrentOrder_should_throw_if_null_paymentOptions_price() {
        Nodes node = createNodeTrades();
        node.getPaymentOptions()[0].setPrice(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> {
            mapper.mapCurrentOrder(node);
        });
    }

    private Nodes createNodeTrades() {
        github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.trades.nodes.tradeItems.Item item = new github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.DTO.meta.trades.nodes.tradeItems.Item();
        item.setItemId("1");
        TradeItems tradeItems = new TradeItems();
        tradeItems.setItem(item);

        PaymentOptions paymentOption = new PaymentOptions(100);

        return new Nodes("tradeId",
                new TradeItems[]{tradeItems},
                new PaymentOptions[]{paymentOption});
    }

    @Test
    public void mapOwnedItems_should_map_each_item() {
        Collection<Node> nodes = new ArrayList<>();

        Node node1 = createNodeItems();
        node1.getItem().setItemId("1");
        nodes.add(node1);

        Node node2 = createNodeItems();
        node2.getItem().setItemId("2");
        nodes.add(node2);

        Node node3 = createNodeItems();
        node3.getItem().setItemId("3");
        nodes.add(node3);

        List<ItemCurrentPrices> itemMainFields = mapper.mapItems(new MarketableItems(new ArrayList<>(nodes), 10));

        assertEquals(3, itemMainFields.size());
        verify(mapper).mapItem(node1);
        verify(mapper).mapItem(node2);
        verify(mapper).mapItem(node3);
    }

    @Test
    public void mapItem_should_map_item_with_valid_fields() throws ParseException {
        Node node = createNodeItems();

        ItemCurrentPrices expectedItem = new ItemCurrentPrices();
        expectedItem.setItemId("1");
        expectedItem.setMaxBuyPrice(100);
        expectedItem.setMinSellPrice(50);

        ItemCurrentPrices resultItem = mapper.mapItem(node);

        assertEquals(resultItem.getItemId(), expectedItem.getItemId());
        assertEquals(resultItem.getMaxBuyPrice(), expectedItem.getMaxBuyPrice());
        assertEquals(resultItem.getMinSellPrice(), expectedItem.getMinSellPrice());
    }

    @Test
    public void mapItem_should_map_item_with_null_BuyStats() throws ParseException {
        Node node = createNodeItems();
        node.getMarketData().setBuyStats(null);

        ItemCurrentPrices expectedItem = new ItemCurrentPrices();
        expectedItem.setItemId("1");
        expectedItem.setMaxBuyPrice(null);
        expectedItem.setMinSellPrice(50);

        ItemCurrentPrices resultItem = mapper.mapItem(node);

        assertEquals(resultItem.getItemId(), expectedItem.getItemId());
        assertEquals(resultItem.getMaxBuyPrice(), expectedItem.getMaxBuyPrice());
        assertEquals(resultItem.getMinSellPrice(), expectedItem.getMinSellPrice());
    }

    @Test
    public void mapItem_should_map_item_with_null_SellStats() throws ParseException {
        Node node = createNodeItems();
        node.getMarketData().setSellStats(null);

        ItemCurrentPrices expectedItem = new ItemCurrentPrices();
        expectedItem.setItemId("1");
        expectedItem.setMaxBuyPrice(100);
        expectedItem.setMinSellPrice(null);

        ItemCurrentPrices resultItem = mapper.mapItem(node);

        assertEquals(resultItem.getItemId(), expectedItem.getItemId());
        assertEquals(resultItem.getMaxBuyPrice(), expectedItem.getMaxBuyPrice());
        assertEquals(resultItem.getMinSellPrice(), expectedItem.getMinSellPrice());
    }

    @Test
    public void mapItem_should_throw_exception_when_node_is_null() {
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> mapper.mapItem(null));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_is_null() {
        Node node = new Node(null, new MarketData());
        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> mapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_id_is_null() {
        Node node = createNodeItems();
        node.getItem().setItemId(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> mapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_market_data_is_null() {
        Node node = createNodeItems();
        node.setMarketData(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> mapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_buyStats_highestPrice_is_null() {
        Node node = createNodeItems();
        node.getMarketData().getBuyStats()[0].setHighestPrice(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> mapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_sellStats_lowestPrice_is_null() {
        Node node = createNodeItems();
        node.getMarketData().getSellStats()[0].setLowestPrice(null);

        assertThrows(GraphQlPersonalOwnedItemPricesAndCurrentSellOrdersMappingException.class, () -> mapper.mapItem(node));
    }

    private Node createNodeItems() {
        Item nodeItem = new Item();

        BuyStats buyStats = new BuyStats();
        buyStats.setHighestPrice(100);

        SellStats sellStats = new SellStats();
        sellStats.setLowestPrice(50);

        MarketData nodeMarketData = new MarketData(new SellStats[]{sellStats}, new BuyStats[]{buyStats});

        nodeItem.setItemId("1");

        return new Node(nodeItem, nodeMarketData);
    }

}