package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastTradeCommand;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.PotentialTradeItem;
import github.ricemonger.fast_sell_trade_manager.services.factories.PotentialTradeItemsFactory;
import github.ricemonger.fast_sell_trade_manager.services.factories.TradeCommandsFactory;
import github.ricemonger.marketplace.graphQl.common_query_items_prices.CommonQueryItemsPricesGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.FastUbiUserStats;
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
public class UserFastTradesManager {
    private final PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService;

    private final CommonQueryItemsPricesGraphQlClientService commonQueryItemsPricesGraphQlClientService;

    private final CommonValuesService commonValuesService;

    private final PotentialTradeItemsFactory potentialTradeItemsFactory;

    private final TradeCommandsFactory tradeCommandsFactory;
    private final TradeCommandsExecutor tradeCommandExecutor;

    private final List<CompletableFuture<?>> createCommandsTasks = Collections.synchronizedList(new LinkedList<>());
    private final Set<FastTradeCommand> commands = Collections.synchronizedSortedSet((new TreeSet<>()));

    private FastUbiUserStats savedUserStats;

    public void submitCreateCommandsTaskByFetchedUserStats(ManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        if (commands.isEmpty()) {
            CompletableFuture<?> task = CompletableFuture.supplyAsync(() -> {
                List<FastTradeCommand> newCommands = fetchAndUpdateUserStatsAndCreateCommandsByThem(managedUser, itemsMedianPriceAndRarity, sellLimit, sellSlots);

                if (commands.isEmpty() && !newCommands.isEmpty()) {
                    savedUserStats = null;
                    commands.addAll(newCommands);
                }

                return newCommands;
            });
            createCommandsTasks.add(task);
        }
    }

    private List<FastTradeCommand> fetchAndUpdateUserStatsAndCreateCommandsByThem(ManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            FastUbiUserStats userStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(managedUser.toAuthorizationDTO(), commonValuesService.getFastTradeOwnedItemsLimit());

            this.savedUserStats = userStats;

            List<PotentialTradeItem> potentialTradeItems = potentialTradeItemsFactory.createPotentialTradeItemsForUser(userStats.getOwnedItemsCurrentPrices(), itemsMedianPriceAndRarity, commonValuesService.getMinMedianPriceDifference(), commonValuesService.getMinMedianPriceDifferencePercentage());

            return tradeCommandsFactory.createTradeCommandsForUser(managedUser, userStats.getCurrentSellOrders(), userStats.getOwnedItemsCurrentPrices(), itemsMedianPriceAndRarity, potentialTradeItems, sellLimit, sellSlots);
        } catch (Exception e) {
            log.error("Error while creating fast sell commands for user with id: {} : {}", managedUser.getUbiProfileId(), e.toString());
            return List.of();
        }
    }

    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(ManagedUser managedUser, AuthorizationDTO authorizationDTO, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        if (commands.isEmpty()) {
            CompletableFuture<?> task = CompletableFuture.supplyAsync(() -> {
                List<FastTradeCommand> newCommands = fetchItemsCurrentStatsAndCreateCommandsByThemAndSavedUserStats(managedUser, authorizationDTO, itemsMedianPriceAndRarity, sellLimit, sellSlots);

                if (commands.isEmpty() && !newCommands.isEmpty()) {
                    savedUserStats = null;
                    commands.addAll(newCommands);
                }

                return newCommands;
            });
            createCommandsTasks.add(task);
        }
    }

    private List<FastTradeCommand> fetchItemsCurrentStatsAndCreateCommandsByThemAndSavedUserStats(ManagedUser managedUser, AuthorizationDTO authorizationDTO, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            if (savedUserStats == null) {
                log.debug("Saved user stats is null while creating fast sell commands for user with id: {}", managedUser.getUbiProfileId());
                return List.of();
            }

            List<ItemCurrentPrices> fetchedItemCurrentPrices = commonQueryItemsPricesGraphQlClientService.fetchLimitedItemsStats(authorizationDTO, commonValuesService.getFetchUsersItemsLimit(), commonValuesService.getFetchUsersItemsOffset());

            List<ItemCurrentPrices> ownedItemsCurrentPrices = fetchedItemCurrentPrices.stream().filter(fetched -> savedUserStats.getOwnedItemsCurrentPrices().stream().anyMatch(saved -> saved.getItemId().equals(fetched.getItemId()))).toList();

            log.debug("Sorted fetched items current prices size: {}", ownedItemsCurrentPrices.size());

            List<PotentialTradeItem> items = potentialTradeItemsFactory.createPotentialTradeItemsForUser(ownedItemsCurrentPrices, itemsMedianPriceAndRarity, commonValuesService.getMinMedianPriceDifference(), commonValuesService.getMinMedianPriceDifferencePercentage());

            return tradeCommandsFactory.createTradeCommandsForUser(managedUser, savedUserStats.getCurrentSellOrders(), ownedItemsCurrentPrices, itemsMedianPriceAndRarity, items, sellLimit, sellSlots);
        } catch (Exception e) {
            log.error("Error while creating fast sell commands for user with id: {} : {}", managedUser.getUbiProfileId(), e.toString());
            return List.of();
        }
    }

    public void executeFastSellCommands() {
        if (!commands.isEmpty()) {
            cancelAllCreateCommandsTasks();

            synchronized (commands) {
                executeCommandsInOrder(commands);
            }

            commands.clear();
        }
    }

    public void createAndExecuteCommandsToKeepOneSellSlotUnused(ManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            FastUbiUserStats userStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(managedUser.toAuthorizationDTO(), commonValuesService.getFastTradeOwnedItemsLimit());

            this.savedUserStats = userStats;

            List<FastTradeCommand> keepUnusedSellSlotCommands = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(managedUser, userStats.getCurrentSellOrders(), userStats.getOwnedItemsCurrentPrices(), itemsMedianPriceAndRarity, sellLimit, sellSlots);

            executeCommandsInOrder(keepUnusedSellSlotCommands);

        } catch (Exception e) {
            log.error("Error while keeping unused sell slot for user with id: {} : {}", managedUser.getUbiProfileId(), e.toString());
        }
    }

    private void executeCommandsInOrder(Collection<FastTradeCommand> commands) {
        for (FastTradeCommand command : commands.stream().sorted().toList()) {
            tradeCommandExecutor.executeCommand(command);
            log.info("Executed command: {}", command.toLogString());
        }
    }

    private void cancelAllCreateCommandsTasks() {
        synchronized (createCommandsTasks) {
            for (Future<?> future : createCommandsTasks) {
                future.cancel(true);
            }
            createCommandsTasks.clear();
        }
    }
}
