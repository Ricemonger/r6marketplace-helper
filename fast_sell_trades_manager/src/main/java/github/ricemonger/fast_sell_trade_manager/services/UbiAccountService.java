package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.abstractions.UbiAccountEntryDatabaseService;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UbiAccountService {

    private final UbiAccountEntryDatabaseService ubiAccountEntryDatabaseService;

    public ManagedUser getFastSellManagedUserById(Long userId, String email) {
        return ubiAccountEntryDatabaseService.getFastSellManagedUserById(userId, email);
    }

    public List<ItemMedianPriceAndRarity> getOwnedItemsMedianPriceAndRarity(String ubiProfileId) {
        return ubiAccountEntryDatabaseService.getOwnedItemsMedianPriceAndRarity(ubiProfileId);
    }

    public List<AuthorizationDTO> getAllFetchAccountsAuthorizationDTOs() {
        return ubiAccountEntryDatabaseService.getAllFetchAccountsAuthorizationDTOs();
    }
}
