package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utilspostgresschema.ids.user.UbiAccountEntryEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Table(name = "ubi_account_authorization_entry")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UbiAccountEntryEntityId.class)
public class UbiAccountEntryEntity {
    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "encoded_password")
    private String encodedPassword;

    @Column(name = "ubi_session_id")
    private String ubiSessionId;
    @Column(name = "ubi_space_id")
    private String ubiSpaceId;
    @Column(columnDefinition = "TEXT", name = "ubi_auth_ticket")
    private String ubiAuthTicket;
    @Column(columnDefinition = "TEXT", name = "ubi_remember_device_ticket")
    private String ubiRememberDeviceTicket;
    @Column(columnDefinition = "TEXT", name = "ubi_remember_me_ticket")
    private String ubiRememberMeTicket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ubi_profile_id", referencedColumnName = "ubi_profile_id")
    private UbiAccountStatsEntity ubiAccountStats;

    @Override
    public int hashCode() {
        return Objects.hash(user, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UbiAccountEntryEntity ubiAccountEntryEntity)) {
            return false;
        }
        return Objects.equals(user, ubiAccountEntryEntity.user) &&
               Objects.equals(email, ubiAccountEntryEntity.email);
    }
}
