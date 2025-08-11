package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchUsersService {

    private List<AuthorizationDTO> authorizationDTOs;
    private int nextFetchUserIndex = 0;

    public void saveFetchUsersAuthorizationDTOs(List<AuthorizationDTO> fetchUsersAuthorizationDTOs) {
        this.authorizationDTOs = fetchUsersAuthorizationDTOs;
    }

    public AuthorizationDTO nextFetchUsersAuthorizationDTO() {
        if (authorizationDTOs.isEmpty()) {
            throw new FetchUsersNotFoundException("No fetch users found in the database");
        } else {
            if (nextFetchUserIndex >= authorizationDTOs.size()) {
                nextFetchUserIndex = 0;
            }
            return authorizationDTOs.get(nextFetchUserIndex++);
        }
    }

}
