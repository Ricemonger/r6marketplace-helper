package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomTagEntity;
import github.ricemonger.utils.enums.FilterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "item_filter")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CustomItemFilterEntityId.class)
public class CustomItemFilterEntity {
    @MapsId
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomManageableUserEntity user;

    @Id
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "filter_type")
    private FilterType filterType;

    @Column(columnDefinition = "TEXT", name = "item_name_patterns")
    private String itemNamePatterns;

    @Column(columnDefinition = "TEXT", name = "item_types")
    private String itemTypes;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "item_filter_tags",
            joinColumns = {@JoinColumn(name = "item_filter_user_id", referencedColumnName = "user_id"),
                    @JoinColumn(name = "item_filter_name", referencedColumnName = "name")},
            inverseJoinColumns = @JoinColumn(name = "tag_name", referencedColumnName = "name"))
    private Set<CustomTagEntity> tags = new HashSet<>();

    @Column(name = "min_sell_price")
    private Integer minSellPrice;
    @Column(name = "max_buy_price")
    private Integer maxBuyPrice;


    public Long getUserId_() {
        return user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CustomItemFilterEntity itemFilterEntity)) {
            return false;
        }
        return Objects.equals(this.user, itemFilterEntity.user) &&
               Objects.equals(this.name, itemFilterEntity.name);
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof CustomItemFilterEntity entity) {

            boolean tagsAreEqual = this.tags == null && entity.tags == null || (
                    this.tags != null && entity.tags != null &&
                    this.tags.size() == entity.tags.size() &&
                    this.tags.stream().allMatch(tag -> entity.tags.stream().anyMatch(tag::equals)));

            return equals(entity) &&
                   filterType == entity.filterType &&
                   Objects.equals(itemNamePatterns, entity.itemNamePatterns) &&
                   Objects.equals(itemTypes, entity.itemTypes) &&
                   tagsAreEqual &&
                   Objects.equals(minSellPrice, entity.minSellPrice) &&
                   Objects.equals(maxBuyPrice, entity.maxBuyPrice);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ItemFilterEntity(userId=" + getUserId_() + ", name=" + name + ", filterType=" + filterType + ", itemNamePatterns=" + itemNamePatterns + ", itemTypes=" + itemTypes + ", tags=" + tags + ", minSellPrice=" + minSellPrice + ", maxBuyPrice=" + maxBuyPrice + ")";
    }
}
