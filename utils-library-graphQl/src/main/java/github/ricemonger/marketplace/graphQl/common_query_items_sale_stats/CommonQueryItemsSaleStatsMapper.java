package github.ricemonger.marketplace.graphQl.common_query_items_sale_stats;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.common_query_items_sale_stats.DTO.marketableItems.Node;
import github.ricemonger.marketplace.graphQl.common_query_items_sale_stats.DTO.marketableItems.node.Item;
import github.ricemonger.marketplace.graphQl.common_query_items_sale_stats.DTO.marketableItems.node.PriceHistory;
import github.ricemonger.utils.DTOs.common.GroupedItemDaySalesUbiStats;
import github.ricemonger.utils.DTOs.common.ItemDaySalesUbiStats;
import github.ricemonger.utils.exceptions.server.GraphQlCommonItemsSaleStatsMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CommonQueryItemsSaleStatsMapper {

    private final GraphQlCommonValuesService graphQlCommonValuesService;

    public List<GroupedItemDaySalesUbiStats> mapAllItemsSaleStats(Collection<Node> nodes) throws GraphQlCommonItemsSaleStatsMappingException {
        return nodes.stream().map(this::mapItemSaleStats).collect(Collectors.toList());
    }

    public GroupedItemDaySalesUbiStats mapItemSaleStats(Node node) throws GraphQlCommonItemsSaleStatsMappingException {
        if (node == null) {
            throw new GraphQlCommonItemsSaleStatsMappingException("Node is null");
        }
        GroupedItemDaySalesUbiStats result = new GroupedItemDaySalesUbiStats();

        Item item = node.getItem();
        if (item == null || item.getItemId() == null) {
            throw new GraphQlCommonItemsSaleStatsMappingException("Item or it's id is null, node-" + node);
        }
        result.setItemId(item.getItemId());

        if (node.getPriceHistory() == null) {
            throw new GraphQlCommonItemsSaleStatsMappingException("PriceHistory is null, node-" + node);
        }
        List<ItemDaySalesUbiStats> itemSaleUbiStats = mapAllItemDaySales(item.getItemId(), node.getPriceHistory());
        result.setDaySales(itemSaleUbiStats);

        return result;
    }

    public List<ItemDaySalesUbiStats> mapAllItemDaySales(String itemId, List<PriceHistory> priceHistories) throws GraphQlCommonItemsSaleStatsMappingException {
        return priceHistories.stream().map(priceHistory -> mapItemDaySales(itemId, priceHistory)).collect(Collectors.toList());
    }

    public ItemDaySalesUbiStats mapItemDaySales(String itemId, PriceHistory priceHistory) throws GraphQlCommonItemsSaleStatsMappingException {
        if (priceHistory == null) {
            throw new GraphQlCommonItemsSaleStatsMappingException("PriceHistory is null");
        }
        ItemDaySalesUbiStats result = new ItemDaySalesUbiStats();

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphQlCommonValuesService.getItemSaleStatsDateFormat());
            result.setDate(LocalDate.parse(priceHistory.getDate(), dtf));
        } catch (NullPointerException e) {
            throw new GraphQlCommonItemsSaleStatsMappingException("Date is null or invalid, priceHistory-" + priceHistory);
        }

        if (priceHistory.getItemsCount() == null || priceHistory.getLowestPrice() == null || priceHistory.getHighestPrice() == null || priceHistory.getAveragePrice() == null) {
            throw new GraphQlCommonItemsSaleStatsMappingException("One of priceHistory values is null:" + priceHistory);
        }
        result.setItemId(itemId);
        result.setItemsCount(priceHistory.getItemsCount());
        result.setLowestPrice(priceHistory.getLowestPrice());
        result.setHighestPrice(priceHistory.getHighestPrice());
        result.setAveragePrice(priceHistory.getAveragePrice());

        return result;
    }
}
