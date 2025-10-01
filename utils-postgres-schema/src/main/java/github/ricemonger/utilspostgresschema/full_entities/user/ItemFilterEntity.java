package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utils.enums.FilterType;
import github.ricemonger.utilspostgresschema.full_entities.item.TagEntity;
import github.ricemonger.utilspostgresschema.ids.user.ItemFilterEntityId;
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
@IdClass(ItemFilterEntityId.class)
public class ItemFilterEntity {
    @MapsId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

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
    private Set<TagEntity> tags = new HashSet<>();

    @Column(name = "min_sell_price")
    private Integer minSellPrice;
    @Column(name = "max_buy_price")
    private Integer maxBuyPrice;

    @Override
    public int hashCode() {
        return Objects.hash(user, name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemFilterEntity itemFilterEntity)) {
            return false;
        }
        return Objects.equals(this.user, itemFilterEntity.user) &&
               Objects.equals(this.name, itemFilterEntity.name);
    }

    public boolean isFullyEqual(ItemFilterEntity filter) {
        if (filter == this) {
            return true;
        } else if (filter == null) {
            return false;
        }

        boolean tagsAreEqual = tags == null && filter.tags == null || (
                tags != null && filter.tags != null &&
                tags.size() == filter.tags.size() && tags.containsAll(filter.tags));

        return equals(filter) &&
               Objects.equals(this.filterType, filter.filterType) &&
               Objects.equals(this.itemNamePatterns, filter.itemNamePatterns) &&
               Objects.equals(this.itemTypes, filter.itemTypes) &&
               tagsAreEqual &&
               Objects.equals(this.minSellPrice, filter.minSellPrice) &&
               Objects.equals(this.maxBuyPrice, filter.maxBuyPrice);
    }
}
