package github.ricemonger.marketplace.databases.postgres.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

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

    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ubiProfileId", referencedColumnName = "ubiProfileId")
    private UbiAccountStatsEntity ubiAccountStats;

    public UbiAccountEntryEntity(Long userId, String email, String ubiProfileId) {
        this(new UserEntity(userId), email, new UbiAccountStatsEntity(ubiProfileId));
    }

    public UbiAccountEntryEntity(UserEntity user, String email, UbiAccountStatsEntity ubiAccountStats) {
        this.user = user;
        this.email = email;
        this.ubiAccountStats = ubiAccountStats;
    }

    public Long getUserId_() {
        return user.getId();
    }

    public String getProfileId_() {
        return this.ubiAccountStats.getUbiProfileId();
    }

    public boolean isEqual(Object o) {
        if (this == o) return true;
        if (o instanceof UbiAccountEntryEntity entity) {
            return user.isEqual(entity.user) &&
                   Objects.equals(email, entity.getEmail());
        }
        return false;
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof UbiAccountEntryEntity entity) {
            return isEqual(entity) &&
                   Objects.equals(encodedPassword, entity.getEncodedPassword()) &&
                   Objects.equals(ubiSessionId, entity.getUbiSessionId()) &&
                   Objects.equals(ubiSpaceId, entity.getUbiSpaceId()) &&
                   Objects.equals(ubiAuthTicket, entity.getUbiAuthTicket()) &&
                   Objects.equals(ubiRememberDeviceTicket, entity.getUbiRememberDeviceTicket()) &&
                   Objects.equals(ubiRememberMeTicket, entity.getUbiRememberMeTicket()) &&
                   ubiAccountStats.isFullyEqual(entity.getUbiAccountStats());
        }
        return false;
    }

    @Override
    public String toString() {
        return "UbiAccountEntryEntity(userId=" + getUserId_() + ", email=" + email + ", encodedPassword=" + encodedPassword + ", ubiSessionId=" + ubiSessionId + ", ubiSpaceId=" + ubiSpaceId + ", ubiAuthTicket=" + ubiAuthTicket + ", ubiRememberDeviceTicket=" + ubiRememberDeviceTicket + ", ubiRememberMeTicket=" + ubiRememberMeTicket + ", ubiAccountStats=" + ubiAccountStats + ")";
    }
}
