package github.ricemonger.marketplace.service;

import github.ricemonger.marketplace.UbiServiceConfiguration;
import github.ricemonger.marketplace.databases.neo4j.entities.ItemNode;
import github.ricemonger.marketplace.databases.neo4j.entities.ItemSaleNode;
import github.ricemonger.marketplace.databases.neo4j.enums.ItemType;
import github.ricemonger.marketplace.databases.postgres.entities.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.entities.ItemSaleEntity;
import github.ricemonger.marketplace.databases.postgres.entities.TagEntity;
import github.ricemonger.marketplace.graphQl.graphsDTOs.marketableItems.Node;
import github.ricemonger.marketplace.graphQl.graphsDTOs.marketableItems.node.Item;
import github.ricemonger.marketplace.graphQl.graphsDTOs.marketableItems.node.MarketData;
import github.ricemonger.marketplace.graphQl.graphsDTOs.marketableItems.node.marketData.BuyStats;
import github.ricemonger.marketplace.graphQl.graphsDTOs.marketableItems.node.marketData.LastSoldAt;
import github.ricemonger.marketplace.graphQl.graphsDTOs.marketableItems.node.marketData.SellStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ItemDtoMapper {

    private final SimpleDateFormat performedAtDateFormat;
    private final float marketplaceProfitPercent;

    public ItemDtoMapper(UbiServiceConfiguration ubisoftServiceConfiguration) {
        performedAtDateFormat = new SimpleDateFormat(ubisoftServiceConfiguration.getPerformedAtDateFormat());
        marketplaceProfitPercent = ubisoftServiceConfiguration.getMarketplaceProfitPercent();
    }

    public Set<ItemEntity> nodesDTOToItemEntities(Collection<Node> nodes) {
        return nodes.stream().map(this::nodeDTOToItemEntity).collect(Collectors.toSet());
    }

    public Set<ItemSaleEntity> nodesDTOToItemSaleEntities(Collection<Node> nodes) {
        return nodes.stream().map(this::nodeDTOToItemSaleEntity).collect(Collectors.toSet());
    }

    public ItemEntity nodeDTOToItemEntity(Node node) {
        ItemEntity itemEntity = new ItemEntity();

        Item itemDTO = node.getItem();

        Set<TagEntity> tags = new HashSet<>();
        for (String tag : itemDTO.getTags()) {
            tags.add(new TagEntity(tag));
        }

        MarketData marketDataDTO = node.getMarketData();

        SellStats sellStatsDTO = getSellStats(marketDataDTO);
        BuyStats buyStatsDTO = getBuyStats(marketDataDTO);
        LastSoldAt lastSoldAtDTO = getLastSoldAt(marketDataDTO);

        int sellPrice = sellStatsDTO.getActiveCount() == 0 ? lastSoldAtDTO.getPrice() : sellStatsDTO.getLowestPrice();
        int buyPrice = getNextFancyBuyPrice(buyStatsDTO.getHighestPrice(), sellPrice);
        int priceDifference = (int) (sellPrice * marketplaceProfitPercent) - buyPrice;
        int expectedProfit = Math.max(priceDifference, 0);
        int expectedProfitPercentage = (int) ((expectedProfit * 100.0) / buyPrice);

        itemEntity.setItemFullId(itemDTO.getId());
        itemEntity.setAssetUrl(itemDTO.getAssetUrl());
        itemEntity.setName(itemDTO.getName());
        itemEntity.setTags(tags);
        itemEntity.setType(ItemType.valueOf(itemDTO.getType()));
        itemEntity.setMinSellPrice(sellStatsDTO.getLowestPrice());
        itemEntity.setSellOrdersCount(sellStatsDTO.getActiveCount());
        itemEntity.setMaxBuyPrice(buyStatsDTO.getHighestPrice());
        itemEntity.setBuyOrdersCount(buyStatsDTO.getActiveCount());
        itemEntity.setLastSoldPrice(lastSoldAtDTO.getPrice());
        itemEntity.setExpectedProfit(expectedProfit);
        itemEntity.setExpectedProfitPercentage(expectedProfitPercentage);
        try {
            itemEntity.setLastSoldAt(performedAtDateFormat.parse(lastSoldAtDTO.getPerformedAt()));
        } catch (ParseException e) {
            log.error("Error parsing date: " + lastSoldAtDTO.getPerformedAt());
            itemEntity.setLastSoldAt(new Date(0));
        }
        return itemEntity;
    }

    public ItemSaleEntity nodeDTOToItemSaleEntity(Node node) {
        Item itemDTO = node.getItem();

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setItemFullId(itemDTO.getId());

        MarketData marketDataDTO = node.getMarketData();

        LastSoldAt lastSoldAtDTO = getLastSoldAt(marketDataDTO);

        try {
            return new ItemSaleEntity(itemEntity, performedAtDateFormat.parse(lastSoldAtDTO.getPerformedAt()), lastSoldAtDTO.getPrice());
        } catch (ParseException e) {
            log.error("Error parsing date: " + lastSoldAtDTO.getPerformedAt());
            return new ItemSaleEntity(itemEntity, new Date(0), 0);
        }
    }

    public Set<ItemNode> nodesDTOToItemNodes(Collection<Node> nodes) {
        return nodes.stream().map(this::nodeDTOToItemNode).collect(Collectors.toSet());
    }

    public Set<ItemSaleNode> nodesDTOToItemSaleNodes(Collection<Node> nodes) {
        return nodes.stream().map(this::nodeDTOToItemSaleNode).collect(Collectors.toSet());
    }

    public ItemNode nodeDTOToItemNode(Node node) {
        Item itemDTO = node.getItem();
        MarketData marketDataDTO = node.getMarketData();

        ItemNode.ItemNodeBuilder builder = ItemNode.builder();

        SellStats sellStatsDTO = getSellStats(marketDataDTO);
        BuyStats buyStatsDTO = getBuyStats(marketDataDTO);
        LastSoldAt lastSoldAtDTO = getLastSoldAt(marketDataDTO);

        int sellPrice = sellStatsDTO.getActiveCount() == 0 ? lastSoldAtDTO.getPrice() : sellStatsDTO.getLowestPrice();
        int buyPrice = getNextFancyBuyPrice(buyStatsDTO.getHighestPrice(), sellPrice);
        int priceDifference = (int) (sellPrice * marketplaceProfitPercent) - buyPrice;
        int expectedProfit = Math.max(priceDifference, 0);
        int expectedProfitPercentage = (int) ((expectedProfit * 100.0) / buyPrice);

        builder
                .itemFullId(itemDTO.getId())
                .assetUrl(itemDTO.getAssetUrl())
                .name(itemDTO.getName())
                .tags(itemDTO.getTags())
                .type(ItemType.valueOf(itemDTO.getType()))
                .minSellPrice(sellStatsDTO.getLowestPrice())
                .sellOrders(sellStatsDTO.getActiveCount())
                .maxBuyPrice(buyStatsDTO.getHighestPrice())
                .buyOrders(buyStatsDTO.getActiveCount())
                .lastSoldPrice(lastSoldAtDTO.getPrice())
                .expectedProfit(expectedProfit)
                .expectedProfitPercentage(expectedProfitPercentage);
        try {
            builder.lastSoldAt(performedAtDateFormat.parse(lastSoldAtDTO.getPerformedAt()));
        } catch (ParseException e) {
            log.error("Error parsing date: " + lastSoldAtDTO.getPerformedAt());
            builder.lastSoldAt(new Date(0));
        }

        return builder.build();
    }

    public ItemSaleNode nodeDTOToItemSaleNode(Node node) {
        Item itemDTO = node.getItem();
        MarketData marketDataDTO = node.getMarketData();

        LastSoldAt lastSoldAtDTO = getLastSoldAt(marketDataDTO);
        try {
            return new ItemSaleNode(itemDTO.getId(), performedAtDateFormat.parse(lastSoldAtDTO.getPerformedAt()), lastSoldAtDTO.getPrice());
        } catch (ParseException e) {
            log.error("Error parsing date: " + lastSoldAtDTO.getPerformedAt());
            return new ItemSaleNode(itemDTO.getId(), new Date(0), 0);
        }
    }

    private BuyStats getBuyStats(MarketData marketDataDTO) {
        BuyStats buyStats = marketDataDTO.getBuyStats() == null ? null : marketDataDTO.getBuyStats()[0];
        return Objects.requireNonNullElseGet(buyStats, () -> new BuyStats(null, 0, 0, 0));
    }

    private SellStats getSellStats(MarketData marketDataDTO) {
        SellStats sellStats = marketDataDTO.getSellStats() == null ? null : marketDataDTO.getSellStats()[0];
        return Objects.requireNonNullElseGet(sellStats, () -> new SellStats(null, 0, 0, 0));
    }

    private LastSoldAt getLastSoldAt(MarketData marketDataDTO) {
        LastSoldAt lastSoldAt = marketDataDTO.getLastSoldAt() == null ? null : marketDataDTO.getLastSoldAt()[0];
        return Objects.requireNonNullElseGet(lastSoldAt, () -> new LastSoldAt(null, 0, "1970-01-01T00:00:00.000Z"));
    }

    private int getNextFancyBuyPrice(int buyPrice, int sellPrice) {
        if (buyPrice == 0) {
            return 120;
        } else if (sellPrice < 200) {
            return ((buyPrice + 10) / 10) * 10;
        } else if (sellPrice < 1000) {
            return ((buyPrice + 50) / 50) * 50;
        } else if (sellPrice < 3000) {
            return ((buyPrice + 100) / 100) * 100;
        } else if (sellPrice < 10000) {
            return ((buyPrice + 500) / 500) * 500;
        } else if (sellPrice < 50000) {
            return ((buyPrice + 1000) / 1000) * 1000;
        } else {
            return ((buyPrice + 5000) / 5000) * 5000;
        }
    }
}
