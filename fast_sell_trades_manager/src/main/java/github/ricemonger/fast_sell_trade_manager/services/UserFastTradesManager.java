package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastTradeCommand;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.PotentialTrade;
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

    private final List<CompletableFuture<?>> createCommandsTasks = new LinkedList<>();
    private final Set<FastTradeCommand> commands = new TreeSet<>();
    private final Set<String> alreadyManagedItems = new HashSet<>();

    private FastUbiUserStats savedUserStats;

    public void submitCreateCommandsTaskByFetchedUserStats(ManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
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

    private List<FastTradeCommand> fetchAndUpdateUserStatsAndCreateCommandsByThem(ManagedUser managedUser, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            FastUbiUserStats userStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(managedUser.toAuthorizationDTO(), commonValuesService.getFastTradeOwnedItemsLimit());

            this.savedUserStats = userStats;

            List<PotentialTrade> potentialTrades = potentialTradeItemsFactory.createPotentialTradeItemsForUser(userStats.getOwnedItemsCurrentPrices(), itemsMedianPriceAndRarity, commonValuesService.getMinMedianPriceDifference(), commonValuesService.getMinMedianPriceDifferencePercentage());

            return tradeCommandsFactory.createTradeCommandsForUser(managedUser, userStats.getCurrentSellOrders(), userStats.getOwnedItemsCurrentPrices(), itemsMedianPriceAndRarity, potentialTrades, alreadyManagedItems, sellLimit, sellSlots);
        } catch (Exception e) {
            log.error("Error while creating fast sell commands for user with id: {} : {}", managedUser.getUbiProfileId(), e.toString());
            return List.of();
        }
    }

    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(ManagedUser managedUser, AuthorizationDTO authorizationDTO, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
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

    private List<FastTradeCommand> fetchItemsCurrentStatsAndCreateCommandsByThemAndSavedUserStats(ManagedUser managedUser, AuthorizationDTO authorizationDTO, List<ItemMedianPriceAndRarity> itemsMedianPriceAndRarity, int sellLimit, int sellSlots) {
        try {
            if (savedUserStats == null) {
                log.debug("Saved user stats is null while creating fast sell commands for user with id: {}", managedUser.getUbiProfileId());
                return List.of();
            }

            List<ItemCurrentPrices> fetchedItemCurrentPrices = commonQueryItemsPricesGraphQlClientService.fetchLimitedItemsStats(authorizationDTO, commonValuesService.getFetchUsersItemsLimit(), commonValuesService.getFetchUsersItemsOffset());

            List<ItemCurrentPrices> ownedItemsCurrentPrices = fetchedItemCurrentPrices.stream().filter(fetched -> savedUserStats.getOwnedItemsCurrentPrices().stream().anyMatch(saved -> saved.getItemId().equals(fetched.getItemId()))).toList();

            log.debug("Sorted fetched items current prices size: {}", ownedItemsCurrentPrices.size());

            List<PotentialTrade> potentialTrades = potentialTradeItemsFactory.createPotentialTradeItemsForUser(ownedItemsCurrentPrices, itemsMedianPriceAndRarity, commonValuesService.getMinMedianPriceDifference(), commonValuesService.getMinMedianPriceDifferencePercentage());

            return tradeCommandsFactory.createTradeCommandsForUser(managedUser, savedUserStats.getCurrentSellOrders(), ownedItemsCurrentPrices, itemsMedianPriceAndRarity, potentialTrades, alreadyManagedItems, sellLimit, sellSlots);
        } catch (Exception e) {
            log.error("Error while creating fast sell commands for user with id: {} : {}", managedUser.getUbiProfileId(), e.toString());
            return List.of();
        }
    }

    public void executeFastSellCommands() {
        if (!commands.isEmpty()) {
            cancelAllCreateCommandsTasks();
            executeCommandsInOrder(commands);
            commands.clear();
            log.info("Executed fast sell commands, sleeping for {} ms...", commonValuesService.getSleepAfterCommandsExecutionTime());
            try {
                Thread.sleep(commonValuesService.getSleepAfterCommandsExecutionTime());
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void cancelAllCreateCommandsTasks() {
        for (Future<?> future : createCommandsTasks) {
            future.cancel(true);
        }
        createCommandsTasks.clear();
    }

    public void createAndExecuteCommandsToKeepOneSellSlotUnused(ManagedUser managedUser, int sellLimit, int sellSlots) {
        try {
            FastUbiUserStats userStats = personalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(managedUser.toAuthorizationDTO(), commonValuesService.getExpectedItemCount());

            this.savedUserStats = userStats;

            List<FastTradeCommand> keepUnusedSellSlotCommands = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(managedUser, userStats.getCurrentSellOrders(), userStats.getOwnedItemsCurrentPrices(), sellLimit, sellSlots);

            executeCommandsInOrder(keepUnusedSellSlotCommands);

            alreadyManagedItems.clear();

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
}
