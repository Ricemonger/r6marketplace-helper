package github.ricemonger.marketplace.graphQl.personal_query_finished_orders;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.personal_query_finished_orders.DTO.Trades;
import github.ricemonger.marketplace.graphQl.personal_query_finished_orders.DTO.trades.Nodes;
import github.ricemonger.marketplace.graphQl.personal_query_finished_orders.DTO.trades.nodes.PaymentOptions;
import github.ricemonger.marketplace.graphQl.personal_query_finished_orders.DTO.trades.nodes.PaymentProposal;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalFinishedOrdersMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PersonalQueryFinishedOrdersMapper {

    private final GraphQlCommonValuesService graphQlCommonValuesService;

    public List<UbiTrade> mapFinishedOrders(Trades trades) throws GraphQlPersonalFinishedOrdersMappingException {
        if (trades == null) {
            throw new GraphQlPersonalFinishedOrdersMappingException("Trades is null");
        }
        List<Nodes> nodes = trades.getNodes();

        return nodes.stream().map(this::mapFinishedOrder).toList();
    }

    public UbiTrade mapFinishedOrder(Nodes node) throws GraphQlPersonalFinishedOrdersMappingException {
        if (node == null) {
            throw new GraphQlPersonalFinishedOrdersMappingException("Node is null");
        }

        UbiTrade result = new UbiTrade();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphQlCommonValuesService.getDateFormat());

        if (node.getTradeId() == null
            || node.getState() == null
            || node.getCategory() == null
            || node.getLastModifiedAt() == null
            || node.getTradeItems() == null || node.getTradeItems().length == 0
            || node.getTradeItems()[0].getItem() == null
            || node.getTradeItems()[0].getItem().getItemId() == null
            || node.getTradeItems()[0].getItem().getName() == null) {
            throw new GraphQlPersonalFinishedOrdersMappingException("One of node fields is null-" + node);
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
                throw new GraphQlPersonalFinishedOrdersMappingException("Payment price or fee is null-" + node);
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
            throw new GraphQlPersonalFinishedOrdersMappingException("Node have both paymentOptions and paymentProposal-" + node);
        } else if (!paymentOptionsIsNull) {
            result.setProposedPaymentPrice(paymentOptions[0].getPrice());
            result.setProposedPaymentFee((int) Math.ceil((double) (paymentOptions[0].getPrice() * graphQlCommonValuesService.getConfigTrades().getFeePercentage()) / 100));
        } else if (!paymentProposalIsNull) {
            result.setProposedPaymentPrice(paymentProposal.getPrice());
            result.setProposedPaymentFee((int) Math.ceil((double) (paymentProposal.getPrice() * graphQlCommonValuesService.getConfigTrades().getFeePercentage()) / 100));
        } else {
            throw new GraphQlPersonalFinishedOrdersMappingException("Node doesnt have neither paymentOptions, neither paymentProposal-" + node);
        }

        return result;
    }
}
