package github.ricemonger.fast_sell_trade_manager.scheduled_tasks;

import github.ricemonger.fast_sell_trade_manager.services.CommonValuesService;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.UbiAccountService;
import github.ricemonger.fast_sell_trade_manager.services.UserFastTradesManager;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledOneUserFastSellTradeManager {
    private final UserFastTradesManager userFastTradesManager;

    private final CommonValuesService commonValuesService;
    private final UbiAccountService ubiAccountService;

    private int sellLimit;
    private int sellSlots;
    private ManagedUser managedUser;
    private List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity = new ArrayList<>();
    private List<AuthorizationDTO> fetchUsersAuthorizationDTOs;
    private int currentFetchUserIndex = 0;

    @Scheduled(fixedRateString = "${app.scheduling.management_update.fixedRate}", initialDelayString = "${app.scheduling.management_update.initialDelay}")
    public void submitCreateCommandsTaskByFetchedUserStats() {
        userFastTradesManager.submitCreateCommandsTaskByFetchedUserStats(managedUser, itemsMedianPriceAndRarity, sellLimit, sellSlots);
    }

    @Scheduled(fixedRateString = "${app.scheduling.management_fetch.fixedRate}", initialDelayString = "${app.scheduling.management_fetch.initialDelay}")
    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices() {
        if (fetchUsersAuthorizationDTOs.isEmpty()) {
            log.debug("No fetch users found in the database");
            return;
        } else {
            if (currentFetchUserIndex >= fetchUsersAuthorizationDTOs.size()) {
                currentFetchUserIndex = 0;
            }
            userFastTradesManager.submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(managedUser, fetchUsersAuthorizationDTOs.get(currentFetchUserIndex++), itemsMedianPriceAndRarity, sellLimit, sellSlots);
        }
    }

    @Scheduled(fixedRateString = "${app.scheduling.management_execute.fixedRate}", initialDelayString = "${app.scheduling.management_execute.initialDelay}")
    public void executeFastSellCommands() {
        userFastTradesManager.executeFastSellCommands();
    }

    @Scheduled(fixedRateString = "${app.scheduling.keep_unused_slot.fixedRate}", initialDelayString = "${app.scheduling.keep_unused_slot.initialDelay}")
    public void keepUnusedOneSellSlotForManagedUser() {
        userFastTradesManager.createAndExecuteCommandsToKeepOneSellSlotUnused(managedUser, itemsMedianPriceAndRarity, sellLimit, sellSlots);
    }

    @Scheduled(fixedRateString = "${app.scheduling.median_prices_fetch.fixedRate}", initialDelayString = "${app.scheduling.median_prices_fetch.initialDelay}")
    public void fetchItemMedianPriceAndConfigTradesFromDb() {
        ConfigTrades configTrades = commonValuesService.getConfigTrades();
        sellLimit = configTrades.getSellLimit();
        sellSlots = configTrades.getSellSlots();
        itemsMedianPriceAndRarity = ubiAccountService.getOwnedItemsMedianPriceAndRarity(managedUser.getUbiProfileId());
    }

    @Scheduled(fixedRateString = "${app.scheduling.user_fetch.fixedRate}", initialDelayString = "${app.scheduling.user_fetch.initialDelay}")
    public void fetchManagedUserAuthorizationFromDb() {
        managedUser = ubiAccountService.getFastSellManagedUserById(commonValuesService.getFastSellManagedUserId(), commonValuesService.getFastSellManagedUserEmail());
    }

    @Scheduled(fixedRateString = "${app.scheduling.user_fetch.fixedRate}", initialDelayString = "${app.scheduling.user_fetch.initialDelay}")
    public void fetchFetchUsersAuthorizationFromDb() {
        fetchUsersAuthorizationDTOs = ubiAccountService.getAllFetchAccountsAuthorizationDTOs();
    }
}
