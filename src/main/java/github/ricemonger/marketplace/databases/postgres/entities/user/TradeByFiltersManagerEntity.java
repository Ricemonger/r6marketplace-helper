package github.ricemonger.marketplace.databases.postgres.entities.user;

import github.ricemonger.utils.enums.TradeOperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity(name = "trade_manager_by_item_filters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TradeByFiltersManagerEntityId.class)
public class TradeByFiltersManagerEntity {
    @MapsId
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserEntity user;

    @Id
    private String name;

    private Boolean enabled;

    @Enumerated(EnumType.ORDINAL)
    private TradeOperationType tradeOperationType;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "trade_manager_by_item_filters_applied_filters",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId"),
                    @JoinColumn(name = "name", referencedColumnName = "name")},
            inverseJoinColumns = @JoinColumn(name = "itemFilterName", referencedColumnName = "name"))
    private List<ItemFilterEntity> appliedFilters;

    private Integer minDifferenceFromMedianPrice;
    private Integer minDifferenceFromMedianPricePercent;

    private Integer priorityMultiplier;

    public Long getUserId_() {
        return user.getId();
    }

    public boolean isEqual(Object o) {
        if (this == o) return true;
        if (o instanceof TradeByFiltersManagerEntity entity) {
            return user.isEqual(entity.user) &&
                   Objects.equals(name, entity.name);
        }
        return false;
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof TradeByFiltersManagerEntity entity) {

            boolean appliedFiltersAreEqual = this.appliedFilters == null && entity.appliedFilters == null || (
                    this.appliedFilters != null && entity.appliedFilters != null &&
                    this.appliedFilters.size() == entity.appliedFilters.size() &&
                    this.appliedFilters.stream().allMatch(filterEntity -> entity.appliedFilters.stream().anyMatch(filterEntity::isEqual)));

            return isEqual(entity) &&
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
