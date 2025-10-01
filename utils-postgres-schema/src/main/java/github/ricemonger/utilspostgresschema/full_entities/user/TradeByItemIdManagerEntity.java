package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utils.enums.TradeOperationType;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.ids.user.TradeByItemIdManagerEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Table(name = "trade_manager_by_item_id")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TradeByItemIdManagerEntityId.class)
public class TradeByItemIdManagerEntity {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemEntity item;

    @Column(name = "enabled")
    private Boolean enabled;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "trade_operation_type")
    private TradeOperationType tradeOperationType;

    @Column(name = "sell_boundary_price")
    private Integer sellBoundaryPrice;
    @Column(name = "buy_boundary_price")
    private Integer buyBoundaryPrice;

    @Column(name = "priority_multiplier")
    private Integer priorityMultiplier;

    public String getItemId_() {
        return item.getItemId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeByItemIdManagerEntity tradeByItemIdManagerEntity)) {
            return false;
        }
        return Objects.equals(user, tradeByItemIdManagerEntity.user) &&
               Objects.equals(item, tradeByItemIdManagerEntity.item);
    }

    public boolean isFullyEqual(TradeByItemIdManagerEntity manager) {
        return equals(manager) &&
               Objects.equals(enabled, manager.enabled) &&
               Objects.equals(tradeOperationType, manager.tradeOperationType) &&
               Objects.equals(sellBoundaryPrice, manager.sellBoundaryPrice) &&
               Objects.equals(buyBoundaryPrice, manager.buyBoundaryPrice) &&
               Objects.equals(priorityMultiplier, manager.priorityMultiplier);
    }
}
