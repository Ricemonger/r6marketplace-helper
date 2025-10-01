package github.ricemonger.trades_manager.scheduled_tasks;

import github.ricemonger.trades_manager.services.CommonValuesService;
import github.ricemonger.trades_manager.services.DTOs.*;
import github.ricemonger.trades_manager.services.ItemService;
import github.ricemonger.trades_manager.services.TradeManagementCommandsExecutor;
import github.ricemonger.trades_manager.services.UserService;
import github.ricemonger.trades_manager.services.factories.PersonalItemFactory;
import github.ricemonger.trades_manager.services.factories.PotentialTradeFactory;
import github.ricemonger.trades_manager.services.factories.TradeManagerCommandsFactory;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.DTOs.common.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledAllUbiUsersManager {
    private final CommonValuesService commonValuesService;
    private final ItemService itemService;
    private final UserService userService;
    private final PersonalItemFactory personalItemFactory;
    private final PotentialTradeFactory potentialTradeFactory;
    private final TradeManagerCommandsFactory tradeManagerCommandsFactory;
    private final TradeManagementCommandsExecutor tradeManagementCommandsExecutor;

    @Scheduled(fixedRateString = "${app.scheduling.fixedRate}", initialDelayString = "${app.scheduling.initialDelay}")
    public void manageAllUsersTrades() {
        ConfigTrades configTrades = commonValuesService.getConfigTrades();

        List<Item> items = itemService.getAllItems();

        List<ManageableUser> manageableUsers = userService.getAllManageableUsers();

        for (ManageableUser manageableUser : manageableUsers) {
            try {
                manageUserTrades(manageableUser, configTrades, items);
            } catch (Exception e) {
                log.error("Exception while managing trades for user: {}", manageableUser, e);
            }
        }
    }

    public void manageUserTrades(ManageableUser manageableUser, ConfigTrades configTrades, Collection<Item> existingItems) {

        List<Trade> currentSellTrades = manageableUser.getCurrentSellTrades();
        List<Trade> currentBuyTrades = manageableUser.getCurrentBuyTrades();

        Set<PersonalItem> personalItems = personalItemFactory.getPersonalItemsForUser(
                manageableUser.getTradeByFiltersManagers(),
                manageableUser.getTradeByItemIdManagers(),
                currentSellTrades,
                currentBuyTrades,
                manageableUser.getOwnedItemsIds(),
                existingItems);

        List<PotentialPersonalSellTrade> resultingSellTrades;
        if (manageableUser.getSellTradesManagingEnabledFlag() != null && manageableUser.getSellTradesManagingEnabledFlag()) {
            resultingSellTrades = potentialTradeFactory.getResultingPersonalSellTrades(
                    personalItems,
                    manageableUser.getResaleLocks(),
                    manageableUser.getSoldIn24h(),
                    configTrades.getSellSlots(),
                    configTrades.getSellLimit());
        } else {
            resultingSellTrades = new ArrayList<>();
        }

        List<PotentialPersonalBuyTrade> resultingBuyTrades;
        if (manageableUser.getBuyTradesManagingEnabledFlag() != null && manageableUser.getBuyTradesManagingEnabledFlag()) {
            resultingBuyTrades = potentialTradeFactory.getResultingPersonalBuyTrades(
                    personalItems,
                    manageableUser.getCreditAmount(),
                    manageableUser.getBoughtIn24h(),
                    configTrades.getBuySlots(),
                    configTrades.getBuyLimit());
        } else {
            resultingBuyTrades = new ArrayList<>();
        }

        List<TradeManagerCommand> commands = new ArrayList<>(tradeManagerCommandsFactory.createTradeManagerCommandsForUser(
                resultingSellTrades,
                currentSellTrades,
                resultingBuyTrades,
                currentBuyTrades,
                manageableUser.getId(),
                manageableUser.toAuthorizationDTO(),
                configTrades));

        for (TradeManagerCommand command : commands.stream().sorted().toList()) {
            tradeManagementCommandsExecutor.executeCommand(command);
        }
    }
}
