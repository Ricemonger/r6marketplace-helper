package github.ricemonger.marketplace.graphQl.mappers;

import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.Trades;
import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades.Nodes;
import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades.nodes.PaymentOptions;
import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades.nodes.PaymentProposal;
import github.ricemonger.marketplace.services.CommonValuesService;
import github.ricemonger.utils.DTOs.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalCurrentOrderMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonalQueryCurrentOrdersMapper {

    private final CommonValuesService commonValuesService;

    public List<UbiTrade> mapCurrentOrders(Trades trades) throws GraphQlPersonalCurrentOrderMappingException {
        if (trades == null) {
            throw new GraphQlPersonalCurrentOrderMappingException("Trades is null");
        }
        List<Nodes> nodes = trades.getNodes();

        return nodes.stream().map(this::mapCurrentOrder).toList();
    }

    public UbiTrade mapCurrentOrder(Nodes node) throws GraphQlPersonalCurrentOrderMappingException {
        if (node == null) {
            throw new GraphQlPersonalCurrentOrderMappingException("Node is null");
        }

        UbiTrade result = new UbiTrade();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());

        if (node.getTradeId() == null
            || node.getState() == null
            || node.getCategory() == null
            || node.getExpiresAt() == null
            || node.getLastModifiedAt() == null
            || node.getTradeItems() == null || node.getTradeItems().length == 0
            || node.getTradeItems()[0].getItem() == null
            || node.getTradeItems()[0].getItem().getItemId() == null) {
            throw new GraphQlPersonalCurrentOrderMappingException("One of node fields is null-" + node);
        }

        result.setTradeId(node.getTradeId());

        try {
            result.setState(TradeState.valueOf(node.getState()));
        } catch (IllegalArgumentException e) {
            result.setState(TradeState.Unknown);
            log.error("Invalid tradeState: {}", node.getState());
        }

        try {
            result.setCategory(TradeCategory.valueOf(node.getCategory()));
        } catch (IllegalArgumentException e) {
            result.setCategory(TradeCategory.Unknown);
            log.error("Invalid tradeCategory: {}", node.getCategory());
        }

        try {
            result.setExpiresAt(LocalDateTime.parse(node.getExpiresAt(), dtf));
        } catch (DateTimeParseException e) {
            result.setExpiresAt(LocalDateTime.MIN);
            log.error("Invalid expiresAt: {}", node.getExpiresAt());
        }

        try {
            result.setLastModifiedAt(LocalDateTime.parse(node.getLastModifiedAt(), dtf));
        } catch (DateTimeParseException e) {
            result.setLastModifiedAt(LocalDateTime.MIN);
            log.error("Invalid lastModifiedAt: {}", node.getLastModifiedAt());
        }

        result.setItemId(node.getTradeItems()[0].getItem().getItemId());

        result.setSuccessPaymentPrice(0);
        result.setSuccessPaymentFee(0);

        PaymentOptions[] paymentOptions = node.getPaymentOptions();
        PaymentProposal paymentProposal = node.getPaymentProposal();

        if (paymentOptions != null && paymentProposal != null) {
            throw new GraphQlPersonalCurrentOrderMappingException("Node have both paymentOptions and paymentProposal-" + node);
        } else if (paymentOptions != null) {
            if (paymentOptions.length == 0 || paymentOptions[0].getPrice() == null) {
                throw new GraphQlPersonalCurrentOrderMappingException("One of paymentOptions fields is null-" + node);
            }
            result.setProposedPaymentPrice(paymentOptions[0].getPrice());
            result.setProposedPaymentFee((int) Math.ceil(paymentOptions[0].getPrice() / 10.));
        } else if (paymentProposal != null) {
            if (paymentProposal.getPrice() == null || paymentProposal.getTransactionFee() == null) {
                throw new GraphQlPersonalCurrentOrderMappingException("One of paymentProposal fields is null-" + node);
            }
            result.setProposedPaymentPrice(paymentProposal.getPrice());
            result.setProposedPaymentFee(paymentProposal.getTransactionFee());
        } else {
            throw new GraphQlPersonalCurrentOrderMappingException("Node doesnt have neither paymentOptions, neither paymentProposal-" + node);
        }

        return result;
    }
}
