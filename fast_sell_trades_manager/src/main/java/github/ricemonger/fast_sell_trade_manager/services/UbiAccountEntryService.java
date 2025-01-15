package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastSellManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.abstractions.UbiAccountEntryDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UbiAccountEntryService {

    private final UbiAccountEntryDatabaseService ubiAccountEntryDatabaseService;

    public FastSellManagedUser getFastSellManagedUserById(Long userId, String email) {
        return ubiAccountEntryDatabaseService.getFastSellManagedUserById(userId, email);
    }

    public List<ItemMedianPriceAndRarity> getOwnedItemsMedianPriceAndRarity(String ubiProfileId) {
        return ubiAccountEntryDatabaseService.getOwnedItemsMedianPriceAndRarity(ubiProfileId);
    }
}