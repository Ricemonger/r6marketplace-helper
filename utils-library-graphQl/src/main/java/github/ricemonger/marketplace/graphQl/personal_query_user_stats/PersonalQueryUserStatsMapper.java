package github.ricemonger.marketplace.graphQl.personal_query_user_stats;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.Meta;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.Trades;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.TradesLimitations;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.marketableItems.Node;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.tradeLimitations.sell.ResaleLocks;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.trades.Nodes;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.trades.nodes.PaymentOptions;
import github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.trades.nodes.PaymentProposal;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.*;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalUserStatsMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.ClientGraphQlResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PersonalQueryUserStatsMapper {

    private final GraphQlCommonValuesService graphQlCommonValuesService;

    public UbiUserStats mapUserStats(ClientGraphQlResponse response, Map<String, String> aliasesToFields, String profileId) {
        String metaField = "game.viewer.meta";

        Meta meta = response.field(metaField).toEntity(Meta.class);
        if (meta == null) {
            throw new GraphQlPersonalUserStatsMappingException("Meta is null");
        }
        UbiUserStats userStats = new UbiUserStats();


        int creditAmount;
        if (meta.getSecondaryStoreItem() == null || meta.getSecondaryStoreItem().getMeta() == null) {
            throw new GraphQlPersonalUserStatsMappingException("SecondaryStoreItem or it's Meta is null, cant fetch credits amount for user " + profileId);
        } else {
            creditAmount = mapCreditAmount(meta.getSecondaryStoreItem().getMeta());
        }


        List<UbiTrade> currentOrders;
        if (meta.getCurrentTrades() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Current trades is null, cant fetch orders for user " + profileId);
        } else {
            currentOrders = mapOrders(meta.getCurrentTrades());
        }


        List<UbiTrade> finishedOrders;
        if (meta.getFinishedTrades() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Finished trades is null, cant fetch orders for user " + profileId);
        } else {
            finishedOrders = mapOrders(meta.getFinishedTrades());
        }


        List<String> ownedItemsIds = new ArrayList<>();
        if (meta.getMarketableItems() == null || meta.getMarketableItems().getNodes() == null) {

            if (aliasesToFields == null || aliasesToFields.isEmpty()) {
                throw new GraphQlPersonalUserStatsMappingException("MarketableItems and Aliases are null or empty, cant fetch owned items for user " + profileId);
            }

            for (Map.Entry<String, String> entry : aliasesToFields.entrySet()) {
                if (entry.getValue().equals("marketableItems")) {
                    List<Node> nodes = response.field(metaField + "." + entry.getKey() + ".nodes").toEntityList(Node.class);

                    if (nodes.isEmpty()) {
                        log.info("No owned items found for user by aliases for user: " + profileId);
                    } else {
                        ownedItemsIds.addAll(mapOwnedItems(nodes));
                    }
                }
            }
        } else {
            log.info("Extracting owned items from meta for user: " + profileId);
            ownedItemsIds = mapOwnedItems(meta.getMarketableItems().getNodes());
        }


        UserTradesLimitations tradesLimitations;
        if (meta.getTradesLimitations() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Trades limitations is null, cant fetch trades limitations for user " + profileId);
        } else {
            tradesLimitations = mapTradesLimitationsForUser(meta.getTradesLimitations(), profileId);
        }


        userStats.setCreditAmount(creditAmount);
        userStats.setCurrentOrders(currentOrders);
        userStats.setFinishedOrders(finishedOrders);
        userStats.setOwnedItemsIds(ownedItemsIds);
        userStats.setUserTradesLimitations(tradesLimitations);
        return userStats;
    }

    public int mapCreditAmount(github.ricemonger.marketplace.graphQl.personal_query_user_stats.DTO.meta.secondaryStoreItem.Meta meta) throws GraphQlPersonalUserStatsMappingException {
        if (meta == null || meta.getQuantity() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Meta is null");
        }
        return meta.getQuantity();
    }

    public List<UbiTrade> mapOrders(Trades trades) throws GraphQlPersonalUserStatsMappingException {
        if (trades == null) {
            throw new GraphQlPersonalUserStatsMappingException("Trades is null");
        }
        List<Nodes> nodes = trades.getNodes();

        return nodes.stream().map(this::mapOrder).toList();
    }

    public UbiTrade mapOrder(Nodes node) throws GraphQlPersonalUserStatsMappingException {
        if (node == null) {
            throw new GraphQlPersonalUserStatsMappingException("Node is null");
        }

        UbiTrade result = new UbiTrade();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphQlCommonValuesService.getDateFormat());

        if (node.getTradeId() == null
            || node.getState() == null
            || node.getCategory() == null
            || node.getLastModifiedAt() == null
            || node.getTradeItems() == null || node.getTradeItems().length == 0
            || node.getTradeItems()[0].getItem() == null
            || node.getTradeItems()[0].getItem().getItemId() == null) {
            throw new GraphQlPersonalUserStatsMappingException("One of node fields is null-" + node);
        }

        result.setTradeId(node.getTradeId());

        try {
            result.setState(TradeState.valueOf(node.getState()));
        } catch (IllegalArgumentException e) {
            result.setState(TradeState.Unknown);
            log.error("Invalid tradeState for Node: {}", node);
        }

        try {
            result.setCategory(TradeCategory.valueOf(node.getCategory()));
        } catch (IllegalArgumentException e) {
            result.setCategory(TradeCategory.Unknown);
            log.error("Invalid tradeCategory for Node: {}", node);
        }

        try {
            if (node.getExpiresAt() == null) {
                result.setExpiresAt(LocalDateTime.of(1970, 1, 1, 0, 0));
            }
            result.setExpiresAt(LocalDateTime.parse(node.getExpiresAt(), dtf));
        } catch (DateTimeParseException | NullPointerException e) {
            result.setExpiresAt(LocalDateTime.of(1970, 1, 1, 0, 0));
            log.trace("Invalid expiresAt for order for Node: {}", node);
        }

        try {
            result.setLastModifiedAt(LocalDateTime.parse(node.getLastModifiedAt(), dtf));
        } catch (DateTimeParseException | NullPointerException e) {
            result.setLastModifiedAt(LocalDateTime.of(1970, 1, 1, 0, 0));
            log.error("Invalid lastModifiedAt for Node: {}", node);
        }

        Item item = new Item();
        item.setItemId(node.getTradeItems()[0].getItem().getItemId());
        item.setName(node.getTradeItems()[0].getItem().getName());

        result.setItem(item);

        if (node.getPayment() != null) {
            if (node.getPayment().getPrice() == null || node.getPayment().getTransactionFee() == null) {
                throw new GraphQlPersonalUserStatsMappingException("Payment price or fee is null-" + node);
            }
            result.setSuccessPaymentPrice(node.getPayment().getPrice());
            result.setSuccessPaymentFee(node.getPayment().getTransactionFee());
        } else {
            result.setSuccessPaymentPrice(0);
            result.setSuccessPaymentFee(0);
        }

        PaymentOptions[] paymentOptions = node.getPaymentOptions();
        PaymentProposal paymentProposal = node.getPaymentProposal();

        boolean paymentOptionsIsNull = paymentOptions == null || paymentOptions.length == 0 || paymentOptions[0].getPrice() == null;
        boolean paymentProposalIsNull = paymentProposal == null || paymentProposal.getPrice() == null;

        if (!paymentOptionsIsNull && !paymentProposalIsNull) {
            throw new GraphQlPersonalUserStatsMappingException("Node have both paymentOptions and paymentProposal-" + node);
        } else if (!paymentOptionsIsNull) {
            result.setProposedPaymentPrice(paymentOptions[0].getPrice());
            result.setProposedPaymentFee((int) Math.ceil((double) (paymentOptions[0].getPrice() * graphQlCommonValuesService.getConfigTrades().getFeePercentage()) / 100));
        } else if (!paymentProposalIsNull) {
            result.setProposedPaymentPrice(paymentProposal.getPrice());
            result.setProposedPaymentFee((int) Math.ceil((double) (paymentProposal.getPrice() * graphQlCommonValuesService.getConfigTrades().getFeePercentage()) / 100));
        } else {
            throw new GraphQlPersonalUserStatsMappingException("Node doesnt have neither paymentOptions, neither paymentProposal-" + node);
        }

        return result;
    }

    public List<String> mapOwnedItems(Collection<Node> nodes) throws GraphQlPersonalUserStatsMappingException {
        if (nodes == null) {
            throw new GraphQlPersonalUserStatsMappingException("Nodes is null");
        }

        List<String> result = new ArrayList<>();

        for (Node node : nodes) {
            if (node == null || node.getItem() == null || node.getItem().getItemId() == null) {
                throw new GraphQlPersonalUserStatsMappingException("Node or item or itemId is null in nodes: " + nodes);
            }
            result.add(node.getItem().getItemId());
        }

        return result;
    }

    public UserTradesLimitations mapTradesLimitationsForUser(TradesLimitations tradesLimitations, String ubiProfileId) throws GraphQlPersonalUserStatsMappingException {
        if (tradesLimitations == null) {
            throw new GraphQlPersonalUserStatsMappingException("Trade limitations is null");
        }

        UserTradesLimitations userTradesLimitations = new UserTradesLimitations();
        userTradesLimitations.setUbiProfileId(ubiProfileId);

        if (tradesLimitations.getSell() == null || tradesLimitations.getSell().getResaleLocks() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Sell or resale locks is null in trade limitations-" + tradesLimitations);
        } else {
            userTradesLimitations.setResaleLocks(tradesLimitations.getSell().getResaleLocks().stream()
                    .map(this::mapLockedItem)
                    .toList());
        }

        UserTransactionsCount userTransactionsCount = mapUserTransactionsCount(tradesLimitations);
        userTradesLimitations.setUserTransactionsCount(userTransactionsCount);

        return userTradesLimitations;
    }

    public ItemResaleLock mapLockedItem(ResaleLocks resaleLocks) throws GraphQlPersonalUserStatsMappingException {
        if (resaleLocks == null || resaleLocks.getItemId() == null || resaleLocks.getExpiresAt() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Resale locks or one of it's fields is null:" + resaleLocks);
        }
        ItemResaleLock result = new ItemResaleLock();
        result.setItemId(resaleLocks.getItemId());
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphQlCommonValuesService.getDateFormat());
            result.setExpiresAt(LocalDateTime.parse(resaleLocks.getExpiresAt(), dtf));

        } catch (DateTimeParseException e) {
            result.setExpiresAt(LocalDateTime.MIN);
            log.error("Error parsing date: {}", e.getMessage());
        }

        return result;
    }

    public UserTransactionsCount mapUserTransactionsCount(TradesLimitations tradesLimitations) throws GraphQlPersonalUserStatsMappingException {
        if (tradesLimitations == null
            || tradesLimitations.getBuy() == null
            || tradesLimitations.getSell() == null
            || tradesLimitations.getBuy().getActiveTransactionCount() == null || tradesLimitations.getBuy().getResolvedTransactionCount() == null
            || tradesLimitations.getSell().getActiveTransactionCount() == null || tradesLimitations.getSell().getResolvedTransactionCount() == null) {
            throw new GraphQlPersonalUserStatsMappingException("Trade limitation or one of its fields is null:" + tradesLimitations);
        }

        UserTransactionsCount result = new UserTransactionsCount();
        result.setBuyResolvedTransactionCount(tradesLimitations.getBuy().getResolvedTransactionCount());
        result.setBuyActiveTransactionCount(tradesLimitations.getBuy().getActiveTransactionCount());
        result.setSellResolvedTransactionCount(tradesLimitations.getSell().getResolvedTransactionCount());
        result.setSellActiveTransactionCount(tradesLimitations.getSell().getActiveTransactionCount());

        return result;
    }
}
