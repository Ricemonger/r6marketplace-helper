package github.ricemonger.utilspostgresschema.full_entities.item;

import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "item")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    @Id
    @Column(name = "item_id")
    private String itemId;

    @Column(name = "asset_url")
    private String assetUrl;
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "item_tags",
            joinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "item_id")},
            inverseJoinColumns = @JoinColumn(name = "tag_value", referencedColumnName = "tag_value"))
    private List<TagEntity> tags;

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

    @Column(name = "month_average_price")
    private Integer monthAveragePrice;
    @Column(name = "month_median_price")
    private Integer monthMedianPrice;
    @Column(name = "month_max_price")
    private Integer monthMaxPrice;
    @Column(name = "month_min_price")
    private Integer monthMinPrice;
    @Column(name = "month_sales_per_day")
    private Integer monthSalesPerDay;
    @Column(name = "month_sales")
    private Integer monthSales;

    @Column(name = "day_average_price")
    private Integer dayAveragePrice;
    @Column(name = "day_median_price")
    private Integer dayMedianPrice;
    @Column(name = "day_max_price")
    private Integer dayMaxPrice;
    @Column(name = "day_min_price")
    private Integer dayMinPrice;
    @Column(name = "day_sales")
    private Integer daySales;

    @Column(name = "priority_to_sell_by_max_buy_price")
    private Long priorityToSellByMaxBuyPrice;
    @Column(name = "priority_to_sell_by_next_fancy_sell_price")
    private Long priorityToSellByNextFancySellPrice;

    @Column(name = "priority_to_buy_by_min_sell_price")
    private Long priorityToBuyByMinSellPrice;

    @Column(name = "priority_to_buy_in_1_hour")
    private Long priorityToBuyIn1Hour;
    @Column(name = "priority_to_buy_in_6_hours")
    private Long priorityToBuyIn6Hours;
    @Column(name = "priority_to_buy_in_24_hours")
    private Long priorityToBuyIn24Hours;
    @Column(name = "priority_to_buy_in_168_hours")
    private Long priorityToBuyIn168Hours;
    @Column(name = "priority_to_buy_in_720_hours")
    private Long priorityToBuyIn720Hours;

    @Column(name = "price_to_buy_in_1_hour")
    private Integer priceToBuyIn1Hour;
    @Column(name = "price_to_buy_in_6_hours")
    private Integer priceToBuyIn6Hours;
    @Column(name = "price_to_buy_in_24_hours")
    private Integer priceToBuyIn24Hours;
    @Column(name = "price_to_buy_in_168_hours")
    private Integer priceToBuyIn168Hours;
    @Column(name = "price_to_buy_in_720_hours")
    private Integer priceToBuyIn720Hours;

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemEntity itemEntity)) {
            return false;
        }
        return Objects.equals(this.itemId, itemEntity.itemId);
    }
}
