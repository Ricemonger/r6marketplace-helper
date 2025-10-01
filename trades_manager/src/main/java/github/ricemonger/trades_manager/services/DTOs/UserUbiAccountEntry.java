package github.ricemonger.trades_manager.services.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUbiAccountEntry {
    private Long userId;

    private String profileId;

    private Integer creditAmount;

    private String ticket;
    private String spaceId;
    private String sessionId;
    private String rememberDeviceTicket;
    private String rememberMeTicket;

}
