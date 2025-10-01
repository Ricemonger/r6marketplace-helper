package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.ids.user.ItemResaleLockEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "item_resale_lock")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemResaleLockEntityId.class)
public class ItemResaleLockEntity {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ubi_profile_id", referencedColumnName = "ubi_profile_id")
    private UbiAccountStatsEntity ubiAccount;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemEntity item;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Override
    public int hashCode() {
        return Objects.hash(ubiAccount, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemResaleLockEntity itemResaleLockEntity)) {
            return false;
        }
        return Objects.equals(ubiAccount, itemResaleLockEntity.ubiAccount) &&
               Objects.equals(item, itemResaleLockEntity.item);
    }

    @Override
    public String toString() {

        String itemId = item == null || item.getItemId() == null ? "null" : item.getItemId();

        String profileId = ubiAccount == null || ubiAccount.getUbiProfileId() == null ? "null" : ubiAccount.getUbiProfileId();

        return "ItemResaleLockEntity{" +
               "profileId=" + profileId +
               ", itemId=" + itemId +
               ", expiresAt=" + expiresAt +
               '}';
    }
}
