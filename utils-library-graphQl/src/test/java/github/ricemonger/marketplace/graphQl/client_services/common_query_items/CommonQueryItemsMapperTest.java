package github.ricemonger.marketplace.graphQl.client_services.common_query_items;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.common_query_items.CommonQueryItemsMapper;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.Node;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.MarketData;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.marketData.BuyStats;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.marketData.LastSoldAt;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.marketData.SellStats;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import github.ricemonger.utils.exceptions.server.GraphQlCommonItemMappingException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommonQueryItemsMapperTest {

    private GraphQlCommonValuesService commonValuesService = mock(GraphQlCommonValuesService.class);

    private CommonQueryItemsMapper commonQueryItemsMapper = spy(new CommonQueryItemsMapper(commonValuesService));

    @Test
    public void mapItems_should_map_each_item() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        Collection<Node> nodes = new ArrayList<>();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node1 = createNode(dtf, date);
        node1.getItem().setItemId("1");
        nodes.add(node1);

        Node node2 = createNode(dtf, date);
        node2.getItem().setItemId("2");
        nodes.add(node2);

        Node node3 = createNode(dtf, date);
        node3.getItem().setItemId("3");
        nodes.add(node3);

        List<Item> itemMainFields = commonQueryItemsMapper.mapItems(nodes);

        assertEquals(3, itemMainFields.size());
        verify(commonQueryItemsMapper, times(3)).mapItem(any());
    }

    @Test
    public void mapItem_should_map_item_with_valid_fields() throws ParseException {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);

        Item expectedItem = new Item();
        expectedItem.setItemId("1");
        expectedItem.setAssetUrl("assetUrl");
        expectedItem.setName("name");
        expectedItem.setTags(List.of("Y2S1", "Y2S2"));
        expectedItem.setRarity(ItemRarity.UNKNOWN);
        expectedItem.setType(ItemType.WeaponSkin);
        expectedItem.setMaxBuyPrice(100);
        expectedItem.setBuyOrdersCount(10);
        expectedItem.setMinSellPrice(50);
        expectedItem.setSellOrdersCount(5);
        expectedItem.setLastSoldPrice(75);
        expectedItem.setLastSoldAt(date);

        Item resultItem = commonQueryItemsMapper.mapItem(node);

        assertTrue(expectedItem.isFullyEquals(resultItem));
    }

    @Test
    public void mapItem_should_map_item_with_invalid_item_type() throws ParseException {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getItem().setType("invalidType");

        String uncommonRarityTag = "Y2S1";
        when(commonValuesService.getUncommonTagValue()).thenReturn(uncommonRarityTag);

        Item expectedItem = new Item();
        expectedItem.setItemId("1");
        expectedItem.setAssetUrl("assetUrl");
        expectedItem.setName("name");
        expectedItem.setTags(List.of("Y2S1", "Y2S2"));
        expectedItem.setRarity(ItemRarity.UNCOMMON);
        expectedItem.setType(ItemType.Unknown);
        expectedItem.setMaxBuyPrice(100);
        expectedItem.setBuyOrdersCount(10);
        expectedItem.setMinSellPrice(50);
        expectedItem.setSellOrdersCount(5);
        expectedItem.setLastSoldPrice(75);
        expectedItem.setLastSoldAt(date);

        Item resultItem = commonQueryItemsMapper.mapItem(node);

        assertTrue(expectedItem.isFullyEquals(resultItem));
    }

    @Test
    public void mapItem_should_map_item_with_null_BuyStats() throws ParseException {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().setBuyStats(null);

        when(commonValuesService.getUncommonTagValue()).thenReturn("Y2S1");
        when(commonValuesService.getRareTagValue()).thenReturn("Y2S2");


        Item expectedItem = new Item();
        expectedItem.setItemId("1");
        expectedItem.setAssetUrl("assetUrl");
        expectedItem.setName("name");
        expectedItem.setTags(List.of("Y2S1", "Y2S2"));
        expectedItem.setRarity(ItemRarity.RARE);
        expectedItem.setType(ItemType.WeaponSkin);
        expectedItem.setMaxBuyPrice(0);
        expectedItem.setBuyOrdersCount(0);
        expectedItem.setMinSellPrice(50);
        expectedItem.setSellOrdersCount(5);
        expectedItem.setLastSoldPrice(75);
        expectedItem.setLastSoldAt(date);

        Item resultItem = commonQueryItemsMapper.mapItem(node);

        assertTrue(expectedItem.isFullyEquals(resultItem));
    }

    @Test
    public void mapItem_should_map_item_with_null_SellStats() throws ParseException {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().setSellStats(null);

        when(commonValuesService.getRareTagValue()).thenReturn("Y2S1");
        when(commonValuesService.getEpicTagValue()).thenReturn("Y2S2");

        Item expectedItem = new Item();
        expectedItem.setItemId("1");
        expectedItem.setAssetUrl("assetUrl");
        expectedItem.setName("name");
        expectedItem.setTags(List.of("Y2S1", "Y2S2"));
        expectedItem.setRarity(ItemRarity.EPIC);
        expectedItem.setType(ItemType.WeaponSkin);
        expectedItem.setMaxBuyPrice(100);
        expectedItem.setBuyOrdersCount(10);
        expectedItem.setMinSellPrice(0);
        expectedItem.setSellOrdersCount(0);
        expectedItem.setLastSoldPrice(75);
        expectedItem.setLastSoldAt(date);

        Item resultItem = commonQueryItemsMapper.mapItem(node);

        assertTrue(expectedItem.isFullyEquals(resultItem));
    }

    @Test
    public void mapItem_should_map_item_with_null_LastSoldAt() throws ParseException {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().setLastSoldAt(null);

        when(commonValuesService.getEpicTagValue()).thenReturn("Y2S1");
        when(commonValuesService.getLegendaryTagValue()).thenReturn("Y2S2");

        Item expectedItem = new Item();
        expectedItem.setItemId("1");
        expectedItem.setAssetUrl("assetUrl");
        expectedItem.setName("name");
        expectedItem.setTags(List.of("Y2S1", "Y2S2"));
        expectedItem.setRarity(ItemRarity.LEGENDARY);
        expectedItem.setType(ItemType.WeaponSkin);
        expectedItem.setMaxBuyPrice(100);
        expectedItem.setBuyOrdersCount(10);
        expectedItem.setMinSellPrice(50);
        expectedItem.setSellOrdersCount(5);
        expectedItem.setLastSoldPrice(0);
        expectedItem.setLastSoldAt(LocalDateTime.of(1970, 1, 1, 0, 0, 0));

        Item resultItem = commonQueryItemsMapper.mapItem(node);

        assertTrue(expectedItem.isFullyEquals(resultItem));
    }

    @Test
    public void mapItem_should_throw_exception_when_node_is_null() {
        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(null));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_is_null() {
        Node node = new Node(null, new MarketData());
        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_id_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getItem().setItemId(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_assetUrl_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getItem().setAssetUrl(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_name_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getItem().setName(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_item_tags_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getItem().setTags(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }


    @Test
    public void mapItem_should_throw_exception_when_item_type_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getItem().setType(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_market_data_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.setMarketData(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_buyStats_activeCount_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().getBuyStats()[0].setActiveCount(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_buyStats_highestPrice_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().getBuyStats()[0].setHighestPrice(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_sellStats_activeCount_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().getSellStats()[0].setActiveCount(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_sellStats_lowestPrice_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().getSellStats()[0].setLowestPrice(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_lastSoldAt_performedAt_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().getLastSoldAt()[0].setPerformedAt(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    @Test
    public void mapItem_should_throw_exception_when_lastSoldAt_price_is_null() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        Node node = createNode(dtf, date);
        node.getMarketData().getLastSoldAt()[0].setPrice(null);

        assertThrows(GraphQlCommonItemMappingException.class, () -> commonQueryItemsMapper.mapItem(node));
    }

    private Node createNode(DateTimeFormatter dtf, LocalDateTime date) {
        github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.Item nodeItem =
                new github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.Item();

        BuyStats buyStats = new BuyStats();
        buyStats.setHighestPrice(100);
        buyStats.setActiveCount(10);

        SellStats sellStats = new SellStats();
        sellStats.setLowestPrice(50);
        sellStats.setActiveCount(5);

        LastSoldAt lastSoldAt = new LastSoldAt();
        lastSoldAt.setPrice(75);
        lastSoldAt.setPerformedAt(dtf.format(date));

        MarketData nodeMarketData = new MarketData(new SellStats[]{sellStats}, new BuyStats[]{buyStats}, new LastSoldAt[]{lastSoldAt});

        nodeItem.setItemId("1");
        nodeItem.setAssetUrl("assetUrl");
        nodeItem.setName("name");
        nodeItem.setTags(List.of("Y2S1", "Y2S2"));
        nodeItem.setType(ItemType.WeaponSkin.name());

        return new Node(nodeItem, nodeMarketData);
    }
}