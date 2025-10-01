package github.ricemonger.utilspostgresschema.full_entities.item;

import github.ricemonger.utilspostgresschema.ids.item.ItemSaleEntityId;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemEntity item;
    @Id
    @Column(name = "sold_at")
    private LocalDateTime soldAt;

    @Column(name = "price")
    private Integer price;

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
}
