package github.ricemonger.marketplace.databases.postgres.entities.user;

import github.ricemonger.utils.dtos.UbiAccountEntry;
import github.ricemonger.utils.dtos.UbiAccountWithTelegram;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Entity(name = "ubi_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UbiAccountEntryEntityId.class)
public class UbiAccountEntryEntity {

    @Id
    private String ubiProfileId;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserEntity user;

    private String email;
    private String encodedPassword;

    private String ubiSessionId;
    private String ubiSpaceId;
    @Column(columnDefinition = "TEXT")
    private String ubiAuthTicket;
    @Column(columnDefinition = "TEXT")
    private String ubiTwoFactorAuthTicket;
    @Column(columnDefinition = "TEXT")
    private String ubiRememberDeviceTicket;
    @Column(columnDefinition = "TEXT")
    private String ubiRememberMeTicket;

    @Column(columnDefinition = "TEXT")
    private String ownedItemsIds;

    private Integer soldIn24h;

    private Integer boughtIn24h;

    private Integer availableBuySlots;

    private Integer availableSellSlots;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "ubi_account_current_resale_locks",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = @JoinColumn(name = "resaleLockId", referencedColumnName = "id"))
    private List<ItemResaleLockEntity> resaleLocks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "ubi_account_current_buy_trades",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = @JoinColumn(name = "tradeId", referencedColumnName = "tradeId"))
    private List<UbiTradeEntity> currentBuyTrades = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "ubi_account_current_sell_trades",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = @JoinColumn(name = "tradeId", referencedColumnName = "tradeId"))
    private List<UbiTradeEntity> currentSellTrades = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "ubi_account_finished_trades",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = @JoinColumn(name = "tradeId", referencedColumnName = "tradeId"))
    private List<UbiTradeEntity> finishedTrades = new ArrayList<>();

    public UbiAccountEntryEntity(UserEntity user, UbiAccountEntry account) {
        this.user = user;
        this.email = account.getEmail();
        this.encodedPassword = account.getEncodedPassword();
        this.ubiProfileId = account.getUbiProfileId();
        this.ubiSessionId = account.getUbiSessionId();
        this.ubiSpaceId = account.getUbiSpaceId();
        this.ubiAuthTicket = account.getUbiAuthTicket();
        this.ubiTwoFactorAuthTicket = account.getUbiTwoFactorAuthTicket();
        this.ubiRememberDeviceTicket = account.getUbiRememberDeviceTicket();
        this.ubiRememberMeTicket = account.getUbiRememberMeTicket();
        StringBuilder itemsIds = new StringBuilder();
        if (account.getOwnedItemsIds() != null) {
            for (String id : account.getOwnedItemsIds()) {
                itemsIds.append(id).append(",");
            }
            try {
                itemsIds.deleteCharAt(itemsIds.length() - 1);
            } catch (StringIndexOutOfBoundsException e) {
                log.error("Owned items ids list is empty");
            }
        }
        this.ownedItemsIds = itemsIds.toString();
    }

    public UbiAccountWithTelegram toUbiAccountWithTelegram() {
        UbiAccountWithTelegram ubiAccountWithTelegram = new UbiAccountWithTelegram();
        ubiAccountWithTelegram.setUbiAccountEntry(this.toUbiAccount());
        ubiAccountWithTelegram.setChatId(this.user.getTelegramUser().getChatId());
        return ubiAccountWithTelegram;
    }

    public UbiAccountEntry toUbiAccount() {
        UbiAccountEntry ubiAccountEntry = new UbiAccountEntry();
        ubiAccountEntry.setEmail(this.email);
        ubiAccountEntry.setEncodedPassword(this.encodedPassword);
        ubiAccountEntry.setUbiProfileId(this.ubiProfileId);
        ubiAccountEntry.setUbiSessionId(this.ubiSessionId);
        ubiAccountEntry.setUbiSpaceId(this.ubiSpaceId);
        ubiAccountEntry.setUbiAuthTicket(this.ubiAuthTicket);
        ubiAccountEntry.setUbiTwoFactorAuthTicket(this.ubiTwoFactorAuthTicket);
        ubiAccountEntry.setUbiRememberDeviceTicket(this.ubiRememberDeviceTicket);
        ubiAccountEntry.setUbiRememberMeTicket(this.ubiRememberMeTicket);
        List<String> itemIds = new ArrayList<>();
        if (this.getOwnedItemsIds() != null) {
            String[] thisOwnedItemsIds = this.getOwnedItemsIds().split(",");
            itemIds = new ArrayList<>(Arrays.asList(thisOwnedItemsIds));
        }
        ubiAccountEntry.setOwnedItemsIds(itemIds);
        return ubiAccountEntry;
    }
}
