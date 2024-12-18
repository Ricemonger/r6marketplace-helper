package github.ricemonger.utils.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbiAccountAuthorizationEntryWithTelegram {
    private String chatId;

    private Boolean privateNotificationsEnabledFlag;

    private UbiAccountAuthorizationEntryEntityDTO ubiAccountAuthorizationEntryEntityDTO;

    public UbiAccountAuthorizationEntryWithTelegram(UbiAccountAuthorizationEntryEntityDTO ubiAccountAuthorizationEntryEntityDTO) {
        this.ubiAccountAuthorizationEntryEntityDTO = ubiAccountAuthorizationEntryEntityDTO;
    }

    public String getEmail() {
        return ubiAccountAuthorizationEntryEntityDTO.getEmail();
    }

    public String getEncodedPassword() {
        return ubiAccountAuthorizationEntryEntityDTO.getEncodedPassword();
    }

    public String getUbiProfileId() {
        return ubiAccountAuthorizationEntryEntityDTO.getUbiProfileId();
    }

    public String getUbiSessionId() {
        return ubiAccountAuthorizationEntryEntityDTO.getUbiSessionId();
    }

    public String getUbiSpaceId() {
        return ubiAccountAuthorizationEntryEntityDTO.getUbiSpaceId();
    }

    public String getUbiAuthTicket() {
        return ubiAccountAuthorizationEntryEntityDTO.getUbiAuthTicket();
    }

    public String getUbiRememberDeviceTicket() {
        return ubiAccountAuthorizationEntryEntityDTO.getUbiRememberDeviceTicket();
    }

    public String getUbiRememberMeTicket() {
        return ubiAccountAuthorizationEntryEntityDTO.getUbiRememberMeTicket();
    }
}
