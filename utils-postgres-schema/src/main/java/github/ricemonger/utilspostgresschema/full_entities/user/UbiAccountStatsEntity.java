package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Table(name = "ubi_account_stats")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UbiAccountStatsEntity {
    @Id
    @Column(name = "ubi_profile_id")
    private String ubiProfileId;

    @Column(name = "credit_amount")
    private Integer creditAmount;

    @Column(name = "sold_in_24h")
    private Integer soldIn24h;

    @Column(name = "bought_in_24h")
    private Integer boughtIn24h;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ubi_account_owned_items",
            joinColumns = {@JoinColumn(name = "ubi_profile_id", referencedColumnName = "ubi_profile_id")},
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "item_id"))
    private List<ItemEntity> ownedItems = new ArrayList<>();

    @OneToMany(mappedBy = "ubiAccount", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ItemResaleLockEntity> resaleLocks = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "ubi_account_current_sell_trades",
            joinColumns = {@JoinColumn(name = "ubi_profile_id", referencedColumnName = "ubi_profile_id")},
            inverseJoinColumns = @JoinColumn(name = "trade_id", referencedColumnName = "trade_id"))
    private List<TradeEntity> currentSellTrades = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "ubi_account_current_buy_trades",
            joinColumns = {@JoinColumn(name = "ubi_profile_id", referencedColumnName = "ubi_profile_id")},
            inverseJoinColumns = @JoinColumn(name = "trade_id", referencedColumnName = "trade_id"))
    private List<TradeEntity> currentBuyTrades = new ArrayList<>();

    public UbiAccountStatsEntity(String ubiProfileId) {
        this.ubiProfileId = ubiProfileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubiProfileId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UbiAccountStatsEntity ubiAccountStatsEntity)) {
            return false;
        }
        return Objects.equals(this.ubiProfileId, ubiAccountStatsEntity.ubiProfileId);
    }

    public boolean isFullyEqual(UbiAccountStatsEntity entity1) {
        boolean ownedItemsEqual = this.ownedItems == entity1.ownedItems || (
                this.ownedItems != null && entity1.ownedItems != null &&
                this.ownedItems.size() == entity1.ownedItems.size() &&
                new HashSet<>(this.ownedItems).containsAll(entity1.ownedItems));

        boolean resaleLocksEqual = this.resaleLocks == entity1.resaleLocks || (
                this.resaleLocks != null && entity1.resaleLocks != null &&
                this.resaleLocks.size() == entity1.resaleLocks.size() &&
                new HashSet<>(this.resaleLocks).containsAll(entity1.resaleLocks));

        boolean currentSellTradesEqual = this.currentSellTrades == entity1.currentSellTrades || (
                this.currentSellTrades != null && entity1.currentSellTrades != null &&
                this.currentSellTrades.size() == entity1.currentSellTrades.size() &&
                new HashSet<>(this.currentSellTrades).containsAll(entity1.currentSellTrades));

        boolean currentBuyTradesEqual = this.currentBuyTrades == entity1.currentBuyTrades || (
                this.currentBuyTrades != null && entity1.currentBuyTrades != null &&
                this.currentBuyTrades.size() == entity1.currentBuyTrades.size() &&
                new HashSet<>(this.currentBuyTrades).containsAll(entity1.currentBuyTrades));

        return Objects.equals(this.ubiProfileId, entity1.ubiProfileId) &&
               Objects.equals(this.creditAmount, entity1.creditAmount) &&
               Objects.equals(this.soldIn24h, entity1.soldIn24h) &&
               Objects.equals(this.boughtIn24h, entity1.boughtIn24h) &&
               ownedItemsEqual &&
               resaleLocksEqual &&
               currentSellTradesEqual &&
               currentBuyTradesEqual;
    }
}
