package github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades;

import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades.nodes.PaymentOptions;
import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades.nodes.PaymentProposal;
import github.ricemonger.marketplace.graphQl.DTOs.personal_query_current_orders.trades.nodes.TradeItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nodes {
    private String tradeId;
    private String state;
    private String category;
    private String expiresAt;
    private String lastModifiedAt;
    private TradeItems[] tradeItems;
    private PaymentOptions[] paymentOptions;
    private PaymentProposal paymentProposal;
}