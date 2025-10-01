package github.ricemonger.trades_manager.postgres.custom_entities.manageable_users;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "helper_user")
@Entity(name = "manageable_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomManageableUserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    private Long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private CustomManageableUserUbiAccountEntryEntity ubiAccountEntry;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CustomTradeByFiltersManagerEntity> tradeByFiltersManagers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CustomTradeByItemIdManagerEntity> tradeByItemIdManagers = new ArrayList<>();

    @Column(name = "managing_enabled_flag")
    private Boolean managingEnabledFlag = true;
    @Column(name = "sell_trades_managing_enabled_flag")
    private Boolean sellTradesManagingEnabledFlag = true;
    @Column(name = "buy_trades_managing_enabled_flag")
    private Boolean buyTradesManagingEnabledFlag = true;

    public CustomManageableUserEntity(Long userId) {
        this.id = userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomManageableUserEntity userEntity)) {
            return false;
        }
        return Objects.equals(id, userEntity.id);
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof CustomManageableUserEntity entity) {
            boolean tradeByFiltersManagersAreEqual = tradeByFiltersManagers == null && entity.tradeByFiltersManagers == null || (
                    tradeByFiltersManagers != null && entity.tradeByFiltersManagers != null &&
                    tradeByFiltersManagers.size() == entity.tradeByFiltersManagers.size() &&
                    tradeByFiltersManagers.stream().allMatch(tradeByFiltersManager -> entity.tradeByFiltersManagers.stream().anyMatch(tradeByFiltersManager::equals)));

            boolean tradeByItemIdManagersAreEqual = tradeByItemIdManagers == null && entity.tradeByItemIdManagers == null || (
                    tradeByItemIdManagers != null && entity.tradeByItemIdManagers != null &&
                    tradeByItemIdManagers.size() == entity.tradeByItemIdManagers.size() &&
                    tradeByItemIdManagers.stream().allMatch(tradeByItemIdManager -> entity.tradeByItemIdManagers.stream().anyMatch(tradeByItemIdManager::equals)));
            return equals(entity) &&
                   ubiAccountEntry.equals(entity.ubiAccountEntry) &&
                   tradeByFiltersManagersAreEqual &&
                   tradeByItemIdManagersAreEqual;
        }
        return false;
    }
}
