package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utils.enums.TradeOperationType;
import github.ricemonger.utilspostgresschema.ids.user.TradeByFiltersManagerEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Table(name = "trade_manager_by_item_filters")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TradeByFiltersManagerEntityId.class)
public class TradeByFiltersManagerEntity {
    @MapsId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "trade_operation_type")
    private TradeOperationType tradeOperationType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "trade_manager_by_item_filters_applied_filters",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id"),
                    @JoinColumn(name = "name", referencedColumnName = "name")},
            inverseJoinColumns = @JoinColumn(name = "item_filter_name", referencedColumnName = "name"))
    private List<ItemFilterEntity> appliedFilters = new ArrayList<>();

    @Column(name = "min_difference_from_median_price")
    private Integer minDifferenceFromMedianPrice;
    @Column(name = "min_difference_from_median_price_percent")
    private Integer minDifferenceFromMedianPricePercent;

    @Column(name = "priority_multiplier")
    private Integer priorityMultiplier;

    @Override
    public int hashCode() {
        return Objects.hash(user, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeByFiltersManagerEntity tradeByFiltersManagerEntity)) {
            return false;
        }
        return Objects.equals(user, tradeByFiltersManagerEntity.user) &&
               Objects.equals(name, tradeByFiltersManagerEntity.name);
    }

    public boolean isFullyEqual(TradeByFiltersManagerEntity manager) {

        boolean appliedFiltersAreEqual = appliedFilters == null && manager.appliedFilters == null || (
                appliedFilters != null && manager.appliedFilters != null &&
                appliedFilters.size() == manager.appliedFilters.size() && new HashSet<>(appliedFilters).containsAll(manager.appliedFilters));

        return equals(manager) &&
               Objects.equals(enabled, manager.enabled) &&
               Objects.equals(tradeOperationType, manager.tradeOperationType) &&
               appliedFiltersAreEqual &&
               Objects.equals(minDifferenceFromMedianPrice, manager.minDifferenceFromMedianPrice) &&
               Objects.equals(minDifferenceFromMedianPricePercent, manager.minDifferenceFromMedianPricePercent) &&
               Objects.equals(priorityMultiplier, manager.priorityMultiplier);
    }
}
