package github.ricemonger.fast_sell_trade_manager.services.abstractions;


import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UbiAccountEntryDatabaseService {
    ManagedUser getFastSellManagedUserById(Long fastSellManagedUserId, String email);

    List<ItemMedianPriceAndRarity> getOwnedItemsMedianPriceAndRarity(String ubiProfileId);

    @Transactional(readOnly = true)
    List<AuthorizationDTO> getAllFetchAccountsAuthorizationDTOs();
}
