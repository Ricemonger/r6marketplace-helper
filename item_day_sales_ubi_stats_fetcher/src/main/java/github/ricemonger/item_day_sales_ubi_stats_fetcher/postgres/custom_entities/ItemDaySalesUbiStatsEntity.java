package github.ricemonger.item_day_sales_ubi_stats_fetcher.postgres.custom_entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "item_day_sales_ubi_stats")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemDaySalesUbiStatsEntityId.class)
public class ItemDaySalesUbiStatsEntity {
    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemEntity item;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "lowest_price")
    private Integer lowestPrice;
    @Column(name = "average_price")
    private Integer averagePrice;
    @Column(name = "highest_price")
    private Integer highestPrice;
    @Column(name = "items_count")
    private Integer itemsCount;

    @Override
    public int hashCode() {
        return Objects.hash(item, date);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemDaySalesUbiStatsEntity itemDaySalesUbiStatsEntity)) {
            return false;
        }
        return Objects.equals(this.item, itemDaySalesUbiStatsEntity.item) &&
               Objects.equals(this.date, itemDaySalesUbiStatsEntity.date);
    }

    public String getItemId_() {
        return item.getItemId();
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof ItemDaySalesUbiStatsEntity entity) {
            return equals(entity) &&
                   Objects.equals(lowestPrice, entity.lowestPrice) &&
                   Objects.equals(averagePrice, entity.averagePrice) &&
                   Objects.equals(highestPrice, entity.highestPrice) &&
                   Objects.equals(itemsCount, entity.itemsCount);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ItemDaySalesUbiStatsEntity(itemId=" + getItemId_() + ", date=" + date + ", lowestPrice=" + lowestPrice + ", averagePrice=" + averagePrice +
               ", highestPrice=" + highestPrice + ", itemsCount=" + itemsCount + ")";
    }
}
