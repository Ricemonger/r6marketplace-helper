package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastSellCommand;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastSellManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.PotentialTrade;
import github.ricemonger.fast_sell_trade_manager.services.factories.PotentialTradeFactory;
import github.ricemonger.fast_sell_trade_manager.services.factories.TradeManagementCommandsFactory;
import github.ricemonger.marketplace.graphQl.common_query_items_prices.CommonQueryItemsPricesGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.FastUserUbiStats;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFastSellTradesManager {
    private final PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService;
    private final CommonQueryItemsPricesGraphQlClientService commonQueryItemsPricesGraphQlClientService;

    private final CommonValuesService commonValuesService;

    private final PotentialTradeFactory potentialTradeFactory;

    private final TradeManagementCommandsFactory tradeManagementCommandsFactory;
    private final TradeManagementCommandsExecutor fastTradeManagementCommandExecutor;

    private final List<CompletableFuture<?>> createFastSellCommandsTasks = Collections.synchronizedList(new LinkedList<>());
    private final Set<FastSellCommand> fastSellCommands = Collections.synchronizedSortedSet((new TreeSet<>()));

    private FastUserUbiStats savedUserStats;

    public void submitCreateCommandsTaskByFetchedUserStats(FastSellManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        if (fastSellCommands.isEmpty()) {
            CompletableFuture<?> task = CompletableFuture.supplyAsync(() -> {
                List<FastSellCommand> newCommands = fetchAndUpdateUserStatsAndCreateCommandsByThem(managedUser, itemsMedianPriceAndRarity, sellLimit, sellSlots);

                if (fastSellCommands.isEmpty() && !newCommands.isEmpty()) {
                    savedUserStats = null;
                    fastSellCommands.addAll(newCommands);
                }

                return newCommands;
            });
            createFastSellCommandsTasks.add(task);
        }
    }

    private List<FastSellCommand> fetchAndUpdateUserStatsAndCreateCommandsByThem(FastSellManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            FastUserUbiStats userStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(managedUser.toAuthorizationDTO(), commonValuesService.getFastTradeOwnedItemsLimit());

            this.savedUserStats = userStats;

            List<PotentialTrade> items = potentialTradeFactory.createPotentialTradesForUser(userStats.getItemsCurrentPrices(), itemsMedianPriceAndRarity, commonValuesService.getMinMedianPriceDifference(), commonValuesService.getMinMedianPriceDifferencePercentage());

            return tradeManagementCommandsFactory.createFastSellCommandsForUser(managedUser, userStats.getCurrentSellOrders(), userStats.getItemsCurrentPrices(), itemsMedianPriceAndRarity, items, sellLimit, sellSlots);
        } catch (Exception e) {
            log.error("Error while creating fast sell commands for user with id: {} : {}", managedUser.getUbiProfileId(), e.getMessage());
            return List.of();
        }
    }

    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(FastSellManagedUser managedUser, AuthorizationDTO authorizationDTO, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        if (fastSellCommands.isEmpty()) {
            CompletableFuture<?> task = CompletableFuture.supplyAsync(() -> {
                List<FastSellCommand> newCommands = fetchItemsCurrentStatsAndCreateCommandsByThemAndSavedUserStats(managedUser, authorizationDTO, itemsMedianPriceAndRarity, sellLimit, sellSlots);

                if (fastSellCommands.isEmpty() && !newCommands.isEmpty()) {
                    savedUserStats = null;
                    fastSellCommands.addAll(newCommands);
                }

                return newCommands;
            });
            createFastSellCommandsTasks.add(task);
        }
    }

    private List<FastSellCommand> fetchItemsCurrentStatsAndCreateCommandsByThemAndSavedUserStats(FastSellManagedUser managedUser, AuthorizationDTO authorizationDTO, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            if (savedUserStats == null) {
                log.info("Saved user stats is null while creating fast sell commands for user with id: {}", managedUser.getUbiProfileId());
                return List.of();
            }

            List<ItemCurrentPrices> fetchedItemCurrentPrices = commonQueryItemsPricesGraphQlClientService.fetchLimitedItemsStats(authorizationDTO, commonValuesService.getFetchUsersItemsLimit(), commonValuesService.getFetchUsersItemsOffset());

            List<ItemCurrentPrices> ownedItemsCurrentPrices = fetchedItemCurrentPrices.stream().filter(fetched -> savedUserStats.getItemsCurrentPrices().stream().anyMatch(saved -> saved.getItemId().equals(fetched.getItemId()))).toList();

            log.debug("Sorted fetched items current prices size: {}", ownedItemsCurrentPrices.size());

            List<PotentialTrade> items = potentialTradeFactory.createPotentialTradesForUser(ownedItemsCurrentPrices, itemsMedianPriceAndRarity, commonValuesService.getMinMedianPriceDifference(), commonValuesService.getMinMedianPriceDifferencePercentage());

            return tradeManagementCommandsFactory.createFastSellCommandsForUser(managedUser, savedUserStats.getCurrentSellOrders(), ownedItemsCurrentPrices, itemsMedianPriceAndRarity, items, sellLimit, sellSlots);
        } catch (Exception e) {
            log.error("Error while creating fast sell commands for user with id: {} : {}", managedUser.getUbiProfileId(), e.getMessage());
            return List.of();
        }
    }

    public void executeFastSellCommands() {
        if (!fastSellCommands.isEmpty()) {
            cancelAllCreateFastSellCommandsTasks();

            executeCommandsInOrder(fastSellCommands);

            fastSellCommands.clear();
        }
    }

    public void createAndExecuteCommandsToKeepOneSellSlotUnused(FastSellManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            FastUserUbiStats userStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(managedUser.toAuthorizationDTO(), commonValuesService.getFastTradeOwnedItemsLimit());

            List<FastSellCommand> keepUnusedSellSlotCommands = tradeManagementCommandsFactory.createKeepUnusedSlotCommandForUser(managedUser, userStats.getCurrentSellOrders(), userStats.getItemsCurrentPrices(), itemsMedianPriceAndRarity, sellLimit, sellSlots);

            executeCommandsInOrder(keepUnusedSellSlotCommands);

        } catch (Exception e) {
            log.error("Error while keeping unused sell slot for user with id: {} : {}", managedUser.getUbiProfileId(), e.getMessage());
        }
    }

    private void executeCommandsInOrder(Collection<FastSellCommand> commands) {
        for (FastSellCommand command : commands.stream().sorted().toList()) {
            fastTradeManagementCommandExecutor.executeCommand(command);
            log.info("Executed command: {}", command.toLogString());
        }
    }

    private void cancelAllCreateFastSellCommandsTasks() {
        for (Future<?> future : createFastSellCommandsTasks) {
            future.cancel(true);
        }
        createFastSellCommandsTasks.clear();
    }
}
