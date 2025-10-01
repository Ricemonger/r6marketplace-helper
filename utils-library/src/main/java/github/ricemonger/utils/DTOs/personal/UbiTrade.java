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
public class UbiTrade implements UbiTradeI {
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

    public String getItemId() {
        return item == null ? null : item.getItemId();
    }

    public String getItemName() {
        return item == null ? null : item.getName();
    }

    public boolean isFullyEqual(UbiTrade other) {
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
