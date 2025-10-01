package github.ricemonger.item_stats_fetcher.databases.postgres.custom_entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "item_sale")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemSaleEntityId.class)
public class ItemSaleEntity {
    @MapsId
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemMainFieldsEntity item;
    @Id
    @Column(name = "sold_at")
    private LocalDateTime soldAt;
    @Column(name = "price")
    private Integer price;

    public ItemSaleEntity(String itemId) {
        this.item = new ItemMainFieldsEntity(itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, soldAt);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemSaleEntity itemSaleEntity)) {
            return false;
        }
        return Objects.equals(this.item, itemSaleEntity.item) &&
               Objects.equals(this.soldAt, itemSaleEntity.soldAt);
    }

    public String getItemId_() {
        return item.getItemId();
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof ItemSaleEntity entity) {
            return equals(entity) && Objects.equals(price, entity.price);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ItemSaleEntity(itemId=" + getItemId_() + ", soldAt=" + soldAt + ", price=" + price + ")";
    }
}
