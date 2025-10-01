package github.ricemonger.utils.DTOs.personal;


import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade implements UbiTradeI {
    private String tradeId;
    private TradeState state;
    private TradeCategory category;
    private LocalDateTime expiresAt;
    private LocalDateTime lastModifiedAt;

    private Item item;

    private Integer successPaymentPrice;
    private Integer successPaymentFee;

    private Integer proposedPaymentPrice;
    private Integer proposedPaymentFee;

    private Integer minutesToTrade;
    private Long tradePriority;

    public Trade(UbiTradeI ubiTrade) {
        this.tradeId = ubiTrade.getTradeId();
        this.state = ubiTrade.getState();
        this.category = ubiTrade.getCategory();
        this.expiresAt = ubiTrade.getExpiresAt();
        this.lastModifiedAt = ubiTrade.getLastModifiedAt();
        this.item = ubiTrade.getItem();
        this.successPaymentPrice = ubiTrade.getSuccessPaymentPrice();
        this.successPaymentFee = ubiTrade.getSuccessPaymentFee();
        this.proposedPaymentPrice = ubiTrade.getProposedPaymentPrice();
        this.proposedPaymentFee = ubiTrade.getProposedPaymentFee();
    }

    public String getItemId() {
        return item == null ? null : item.getItemId();
    }

    public String getItemName() {
        return item == null ? null : item.getName();
    }

    public boolean isFullyEqual(Trade other) {
        if (other == null) {
            return false;
        }
        boolean itemsAreEqual = item == null ? other.item == null : Objects.equals(item.getItemId(), other.item.getItemId());

        return Objects.equals(tradeId, other.tradeId) &&
               state == other.state &&
               category == other.category &&
               Objects.equals(expiresAt, other.expiresAt) &&
               Objects.equals(lastModifiedAt, other.lastModifiedAt) &&
               itemsAreEqual &&
               Objects.equals(successPaymentPrice, other.successPaymentPrice) &&
               Objects.equals(successPaymentFee, other.successPaymentFee) &&
               Objects.equals(proposedPaymentPrice, other.proposedPaymentPrice) &&
               Objects.equals(proposedPaymentFee, other.proposedPaymentFee);
    }
}
