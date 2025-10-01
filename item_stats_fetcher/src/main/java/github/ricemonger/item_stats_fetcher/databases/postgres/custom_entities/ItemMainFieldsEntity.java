package github.ricemonger.item_stats_fetcher.databases.postgres.custom_entities;

import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "item")
@Entity(name = "item_main_fields")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemMainFieldsEntity {
    @Id
    @Column(name = "item_id")
    private String itemId;
    @Column(name = "asset_url")
    private String assetUrl;
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "item_tags",
            joinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "item_id")},
            inverseJoinColumns = @JoinColumn(name = "tag_value", referencedColumnName = "tag_value"))
    private List<TagEntity> tags = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "rarity")
    private ItemRarity rarity;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private ItemType type;

    @Column(name = "max_buy_price")
    private Integer maxBuyPrice;
    @Column(name = "buy_orders_count")
    private Integer buyOrdersCount;

    @Column(name = "min_sell_price")
    private Integer minSellPrice;
    @Column(name = "sell_orders_count")
    private Integer sellOrdersCount;

    @Column(name = "last_sold_at")
    private LocalDateTime lastSoldAt;
    @Column(name = "last_sold_price")
    private Integer lastSoldPrice;

    public ItemMainFieldsEntity(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemMainFieldsEntity itemMainFieldsEntity)) {
            return false;
        }
        return Objects.equals(this.itemId, itemMainFieldsEntity.itemId);
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof ItemMainFieldsEntity itemMainFieldsEntity) {

            boolean tagsAreEqual = tags == null && itemMainFieldsEntity.tags == null || (
                    tags != null && itemMainFieldsEntity.tags != null &&
                    this.tags.size() == itemMainFieldsEntity.tags.size() &&
                    this.tags.stream().allMatch(tst -> itemMainFieldsEntity.tags.stream().anyMatch(tst::equals)));

            return equals(itemMainFieldsEntity) &&
                   Objects.equals(assetUrl, itemMainFieldsEntity.assetUrl) &&
                   Objects.equals(name, itemMainFieldsEntity.name) &&
                   tagsAreEqual &&
                   rarity == itemMainFieldsEntity.rarity &&
                   type == itemMainFieldsEntity.type &&
                   Objects.equals(maxBuyPrice, itemMainFieldsEntity.maxBuyPrice) &&
                   Objects.equals(buyOrdersCount, itemMainFieldsEntity.buyOrdersCount) &&
                   Objects.equals(minSellPrice, itemMainFieldsEntity.minSellPrice) &&
                   Objects.equals(sellOrdersCount, itemMainFieldsEntity.sellOrdersCount) &&
                   Objects.equals(lastSoldAt, itemMainFieldsEntity.lastSoldAt) &&
                   Objects.equals(lastSoldPrice, itemMainFieldsEntity.lastSoldPrice);
        }
        return false;
    }
}
