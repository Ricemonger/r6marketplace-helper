package github.ricemonger.marketplace.databases.postgres.entities.user;

import github.ricemonger.utils.DTOs.UbiAccountAuthorizationEntryEntityDTO;
import github.ricemonger.utils.DTOs.UbiAccountAuthorizationEntryWithTelegram;
import github.ricemonger.utils.DTOs.UbiAccountEntryEntityDTO;
import github.ricemonger.utils.DTOs.UbiAccountEntryEntityDTOWithTelegram;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity(name = "ubi_account_authorization_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UbiAccountEntryEntityId.class)
public class UbiAccountEntryEntity {

    @Id
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserEntity user;

    @Id
    private String email;

    private String encodedPassword;

    private String ubiSessionId;
    private String ubiSpaceId;
    @Column(columnDefinition = "TEXT")
    private String ubiAuthTicket;
    @Column(columnDefinition = "TEXT")
    private String ubiRememberDeviceTicket;
    @Column(columnDefinition = "TEXT")
    private String ubiRememberMeTicket;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ubiProfileId", referencedColumnName = "ubiProfileId")
    private UbiAccountStatsEntity ubiAccountStats;

    public UbiAccountEntryEntity(UserEntity user, UbiAccountStatsEntity ubiAccountStats, UbiAccountAuthorizationEntryEntityDTO account) {
        this.user = user;
        this.email = account.getEmail();
        this.encodedPassword = account.getEncodedPassword();
        this.ubiAccountStats = ubiAccountStats;
        this.ubiSessionId = account.getUbiSessionId();
        this.ubiSpaceId = account.getUbiSpaceId();
        this.ubiAuthTicket = account.getUbiAuthTicket();
        this.ubiRememberDeviceTicket = account.getUbiRememberDeviceTicket();
        this.ubiRememberMeTicket = account.getUbiRememberMeTicket();
    }

    public UbiAccountAuthorizationEntryWithTelegram toUbiAccountAuthorizationEntryWithTelegram() {
        UbiAccountAuthorizationEntryWithTelegram ubiAccountWithTelegram = new UbiAccountAuthorizationEntryWithTelegram();
        ubiAccountWithTelegram.setUbiAccountAuthorizationEntryEntityDTO(this.toUbiAccountAuthorizationEntry());
        try {
            ubiAccountWithTelegram.setChatId(this.user.getTelegramUser().getChatId());
            ubiAccountWithTelegram.setPrivateNotificationsEnabledFlag(this.user.getPrivateNotificationsEnabledFlag());
        } catch (NullPointerException e) {
            log.error("Telegram user not found for user with id: " + this.user.getId());
            throw new TelegramUserDoesntExistException("Telegram user not found for user with id: " + this.user.getId());
        }
        return ubiAccountWithTelegram;
    }

    public UbiAccountAuthorizationEntryEntityDTO toUbiAccountAuthorizationEntry() {
        UbiAccountAuthorizationEntryEntityDTO ubiAccountEntry = new UbiAccountAuthorizationEntryEntityDTO();
        ubiAccountEntry.setUbiProfileId(this.ubiAccountStats.getUbiProfileId());
        ubiAccountEntry.setEmail(this.email);
        ubiAccountEntry.setEncodedPassword(this.encodedPassword);
        ubiAccountEntry.setUbiSessionId(this.ubiSessionId);
        ubiAccountEntry.setUbiSpaceId(this.ubiSpaceId);
        ubiAccountEntry.setUbiAuthTicket(this.ubiAuthTicket);
        ubiAccountEntry.setUbiRememberDeviceTicket(this.ubiRememberDeviceTicket);
        ubiAccountEntry.setUbiRememberMeTicket(this.ubiRememberMeTicket);
        return ubiAccountEntry;
    }

    public UbiAccountEntryEntityDTOWithTelegram toUbiAccountEntryWithTelegram() {
        UbiAccountEntryEntityDTO ubiAccountEntryEntityDTO = new UbiAccountEntryEntityDTO();
        ubiAccountEntryEntityDTO.setUbiAccountAuthorizationEntryEntityDTO(this.toUbiAccountAuthorizationEntry());
        ubiAccountEntryEntityDTO.setUbiAccountStatsEntityDTO(this.ubiAccountStats.toUbiAccountStatsEntityDTO());
        UbiAccountEntryEntityDTOWithTelegram ubiAccountEntryEntityDTOWithTelegram = new UbiAccountEntryEntityDTOWithTelegram();
        ubiAccountEntryEntityDTOWithTelegram.setUbiAccountEntryEntityDTO(ubiAccountEntryEntityDTO);
        try {
            ubiAccountEntryEntityDTOWithTelegram.setChatId(this.user.getTelegramUser().getChatId());
            ubiAccountEntryEntityDTOWithTelegram.setPrivateNotificationsEnabledFlag(this.user.getPrivateNotificationsEnabledFlag());
        } catch (NullPointerException e) {
            log.error("Telegram user not found for user with id: " + this.user.getId());
            throw new TelegramUserDoesntExistException("Telegram user not found for user with id: " + this.user.getId());
        }
        return ubiAccountEntryEntityDTOWithTelegram;
    }
}