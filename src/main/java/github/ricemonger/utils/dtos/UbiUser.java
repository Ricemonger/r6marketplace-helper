package github.ricemonger.utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbiUser {
    private String chatId;

    private String email;
    private String password;
    private String ubiProfileId;
    private String ubiSessionId;
    private String ubiSpaceId;
    private String ubiAuthTicket;
    private String ubiTwoFactorAuthTicket;
    private String ubiRememberDeviceTicket;
    private String ubiRememberMeTicket;

    private List<String> ownedItemsIds;
}