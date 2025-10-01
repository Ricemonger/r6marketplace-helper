package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemEntity;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
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
public class CustomTradeEntity {
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
    private CustomItemEntity item;

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
        if (obj instanceof CustomTradeEntity tradeEntity) {
            return Objects.equals(tradeId, tradeEntity.tradeId);
        }
        return false;
    }
}
