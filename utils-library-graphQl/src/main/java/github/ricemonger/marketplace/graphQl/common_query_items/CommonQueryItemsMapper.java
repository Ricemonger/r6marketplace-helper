package github.ricemonger.marketplace.graphQl.common_query_items;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.Node;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.MarketData;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.marketData.BuyStats;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.marketData.LastSoldAt;
import github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.marketData.SellStats;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemType;
import github.ricemonger.utils.exceptions.server.GraphQlCommonItemMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CommonQueryItemsMapper {

    private final GraphQlCommonValuesService graphQlCommonValuesService;

    public List<Item> mapItems(Collection<Node> nodes) throws GraphQlCommonItemMappingException {
        return nodes.stream().map(this::mapItem).collect(Collectors.toList());
    }

    public Item mapItem(Node node) throws GraphQlCommonItemMappingException {
        if (node == null) {
            throw new GraphQlCommonItemMappingException("Node is null");
        }
        github.ricemonger.marketplace.graphQl.common_query_items.DTO.marketableItems.node.Item item = node.getItem();

        if (item == null) {
            throw new GraphQlCommonItemMappingException("Item is null, node-" + node);
        }

        Item result = new Item();

        if (item.getItemId() == null
            || item.getAssetUrl() == null
            || item.getName() == null
            || item.getTags() == null
            || item.getType() == null) {
            throw new GraphQlCommonItemMappingException("One of Item fields is null, item-" + item);
        }

        result.setItemId(item.getItemId());
        result.setAssetUrl(item.getAssetUrl());
        result.setName(item.getName());
        result.setTags(item.getTags());

        try {
            result.setType(ItemType.valueOf(item.getType()));
        } catch (IllegalArgumentException e) {
            log.error("Unknown item type: {}", item.getType());
            result.setType(ItemType.Unknown);
        }

        if (node.getMarketData() == null) {
            throw new GraphQlCommonItemMappingException("Market data is null, node-" + node);
        }

        MarketData marketData = node.getMarketData();

        BuyStats buyStats = marketData.getBuyStats() == null || marketData.getBuyStats().length == 0 ? null : marketData.getBuyStats()[0];
        SellStats sellStats = marketData.getSellStats() == null || marketData.getSellStats().length == 0 ? null : marketData.getSellStats()[0];
        LastSoldAt lastSoldAt = marketData.getLastSoldAt() == null || marketData.getLastSoldAt().length == 0 ? null : marketData.getLastSoldAt()[0];

        if (buyStats != null) {
            if (buyStats.getHighestPrice() == null || buyStats.getActiveCount() == null) {
                throw new GraphQlCommonItemMappingException("BuyStats highest price or active count is null, buyStats-" + buyStats);
            }
            result.setMaxBuyPrice(buyStats.getHighestPrice());
            result.setBuyOrdersCount(buyStats.getActiveCount());
        } else {
            result.setMaxBuyPrice(0);
            result.setBuyOrdersCount(0);
        }

        if (sellStats != null) {
            if (sellStats.getLowestPrice() == null || sellStats.getActiveCount() == null) {
                throw new GraphQlCommonItemMappingException("SellStats lowest price or active count is null, sellStats-" + sellStats);
            }
            result.setMinSellPrice(sellStats.getLowestPrice());
            result.setSellOrdersCount(sellStats.getActiveCount());
        } else {
            result.setMinSellPrice(0);
            result.setSellOrdersCount(0);
        }

        if (lastSoldAt != null) {
            if (lastSoldAt.getPerformedAt() == null || lastSoldAt.getPrice() == null) {
                throw new GraphQlCommonItemMappingException("LastSoldAt performed at is null, lastSoldAt-" + lastSoldAt);
            }
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphQlCommonValuesService.getDateFormat());
                result.setLastSoldAt(LocalDateTime.parse(lastSoldAt.getPerformedAt(), dtf));
            } catch (DateTimeParseException e) {
                result.setLastSoldAt(LocalDateTime.of(1970, 1, 1, 0, 0));
                log.error("Error parsing date: {}", lastSoldAt.getPerformedAt());
            }
            result.setLastSoldPrice(lastSoldAt.getPrice());
        } else {
            result.setLastSoldAt(LocalDateTime.of(1970, 1, 1, 0, 0));
            result.setLastSoldPrice(0);
        }

        return result;
    }
}
