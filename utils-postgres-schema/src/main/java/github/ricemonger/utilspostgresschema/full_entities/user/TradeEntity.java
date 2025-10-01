package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "trade")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeEntity {
    @Id
    @Column(name = "trade_id")
    private String tradeId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private TradeState state;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category")
    private TradeCategory category;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemEntity item;

    @Column(name = "success_payment_price")
    private Integer successPaymentPrice;
    @Column(name = "success_payment_fee")
    private Integer successPaymentFee;

    @Column(name = "proposed_payment_price")
    private Integer proposedPaymentPrice;
    @Column(name = "proposed_payment_fee")
    private Integer proposedPaymentFee;

    @Column(name = "minutes_to_trade")
    private Integer minutesToTrade;
    @Column(name = "trade_priority")
    private Long tradePriority;

    @Override
    public int hashCode() {
        return Objects.hash(tradeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TradeEntity tradeEntity) {
            return Objects.equals(tradeId, tradeEntity.tradeId);
        }
        return false;
    }

    @Override
    public String toString() {

        String itemId = item == null || item.getItemId() == null ? "null" : item.toString();

        return "TradeEntity{" +
               "tradeId='" + tradeId + '\'' +
               ", state=" + state +
               ", category=" + category +
               ", expiresAt=" + expiresAt +
               ", lastModifiedAt=" + lastModifiedAt +
               ", item=" + itemId +
               ", successPaymentPrice=" + successPaymentPrice +
               ", successPaymentFee=" + successPaymentFee +
               ", proposedPaymentPrice=" + proposedPaymentPrice +
               ", proposedPaymentFee=" + proposedPaymentFee +
               ", minutesToTrade=" + minutesToTrade +
               ", tradePriority=" + tradePriority +
               '}';
    }

    public boolean isFullyEqual(TradeEntity trade) {
        return equals(trade) &&
               state == trade.state &&
               category == trade.category &&
               Objects.equals(expiresAt, trade.expiresAt) &&
               Objects.equals(lastModifiedAt, trade.lastModifiedAt) &&
               Objects.equals(item, trade.item) &&
               Objects.equals(successPaymentPrice, trade.successPaymentPrice) &&
               Objects.equals(successPaymentFee, trade.successPaymentFee) &&
               Objects.equals(proposedPaymentPrice, trade.proposedPaymentPrice) &&
               Objects.equals(proposedPaymentFee, trade.proposedPaymentFee) &&
               Objects.equals(minutesToTrade, trade.minutesToTrade) &&
               Objects.equals(tradePriority, trade.tradePriority);
    }
}
