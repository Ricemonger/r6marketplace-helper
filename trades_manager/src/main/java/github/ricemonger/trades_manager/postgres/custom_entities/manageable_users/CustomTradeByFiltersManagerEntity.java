package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.utils.enums.TradeOperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Table(name = "trade_manager_by_item_filters")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CustomTradeByFiltersManagerEntityId.class)
public class CustomTradeByFiltersManagerEntity {
    @MapsId
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomManageableUserEntity user;

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "trade_operation_type")
    private TradeOperationType tradeOperationType;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "trade_manager_by_item_filters_applied_filters",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
                    @JoinColumn(name = "name", referencedColumnName = "name")},
            inverseJoinColumns = @JoinColumn(name = "item_filter_name", referencedColumnName = "name"))
    private List<CustomItemFilterEntity> appliedFilters;

    @Column(name = "min_difference_from_median_price")
    private Integer minDifferenceFromMedianPrice;
    @Column(name = "min_difference_from_median_price_percent")
    private Integer minDifferenceFromMedianPricePercent;

    @Column(name = "priority_multiplier")
    private Integer priorityMultiplier;

    public Long getUserId_() {
        return user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomTradeByFiltersManagerEntity tradeByFiltersManagerEntity)) {
            return false;
        }
        return Objects.equals(user, tradeByFiltersManagerEntity.user) &&
               Objects.equals(name, tradeByFiltersManagerEntity.name);
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof CustomTradeByFiltersManagerEntity entity) {

            boolean appliedFiltersAreEqual = this.appliedFilters == null && entity.appliedFilters == null || (
                    this.appliedFilters != null && entity.appliedFilters != null &&
                    this.appliedFilters.size() == entity.appliedFilters.size() &&
                    this.appliedFilters.stream().allMatch(filterEntity -> entity.appliedFilters.stream().anyMatch(filterEntity::equals)));

            return equals(entity) &&
                   enabled == entity.enabled &&
                   tradeOperationType == entity.tradeOperationType &&
                   appliedFiltersAreEqual &&
                   Objects.equals(minDifferenceFromMedianPrice, entity.minDifferenceFromMedianPrice) &&
                   Objects.equals(minDifferenceFromMedianPricePercent, entity.minDifferenceFromMedianPricePercent) &&
                   Objects.equals(priorityMultiplier, entity.priorityMultiplier);
        }
        return false;
    }

    @Override
    public String toString() {
        return "TradeByFiltersManagerEntity(userId=" + getUserId_() + ", name=" + name + ", enabled=" + enabled + ", tradeOperationType=" + tradeOperationType + ", appliedFilters=" + appliedFilters + ", minDifferenceFromMedianPrice=" + minDifferenceFromMedianPrice + ", minDifferenceFromMedianPricePercent=" + minDifferenceFromMedianPricePercent + ", priorityMultiplier=" + priorityMultiplier + ")";
    }
}
