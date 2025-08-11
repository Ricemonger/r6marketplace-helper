package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastTradeCommand;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.factories.PotentialTradeItemsFactory;
import github.ricemonger.fast_sell_trade_manager.services.factories.TradeCommandsFactory;
import github.ricemonger.marketplace.graphQl.common_query_items_prices.CommonQueryItemsPricesGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items_prices_and_current_sell_orders.PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.FastUbiUserStats;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserFastTradesManagerTest {
    @SpyBean
    private UserFastTradesManager userFastTradesManager;
    @MockBean
    private PersonalQueryOwnedItemsPricesAndCurrentSellOrdersGraphQlClientService personalGraphQlClientService;
    @MockBean
    private CommonQueryItemsPricesGraphQlClientService commonGraphQlClientService;
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private PotentialTradeItemsFactory potentialTradeItemsFactory;
    @MockBean
    private TradeCommandsFactory tradeCommandsFactory;
    @MockBean
    private TradeCommandsExecutor fastTradeManagementCommandExecutor;

    @Test
    public void submitCreateCommandsTaskByFetchedUserStats_should_add_commands_to_commands_list_and_set_saved_users_to_null_if_list_is_empty() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);

        AuthorizationDTO dto = mock(AuthorizationDTO.class);

        when(user.toAuthorizationDTO()).thenReturn(dto);

        List itemsMedianPriceAndRarity = mock(List.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        FastUbiUserStats fetchedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId3", 5, 6),
                new ItemCurrentPrices("itemId4", 7, 8)
        ));

        when(commonValuesService.getFastTradeOwnedItemsLimit()).thenReturn(200);

        when(personalGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(same(dto), eq(200))).thenReturn(fetchedUserStats);

        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(same(fetchedUserStats.getOwnedItemsCurrentPrices()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);

        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(fetchedUserStats.getCurrentSellOrders()), same(fetchedUserStats.getOwnedItemsCurrentPrices()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskByFetchedUserStats(user, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 1);

        tasks.get(0).get();

        assertTrue(commands.size() == 2);
        assertTrue(commands.stream().anyMatch(c -> c == command1));
        assertTrue(commands.stream().anyMatch(c -> c == command2));
        assertNull(ReflectionTestUtils.getField(userFastTradesManager, "savedUserStats"));
    }

    @Test
    public void submitCreateCommandsTaskByFetchedUserStats_should_additionally_check_commands_list_before_adding_new_commands() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);

        AuthorizationDTO dto = mock(AuthorizationDTO.class);

        when(user.toAuthorizationDTO()).thenReturn(dto);

        List itemsMedianPriceAndRarity = mock(List.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        FastUbiUserStats fetchedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId3", 5, 6),
                new ItemCurrentPrices("itemId4", 7, 8)
        ));

        when(commonValuesService.getFastTradeOwnedItemsLimit()).thenReturn(200);

        when(personalGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(same(dto), eq(200))).then(new Answer<FastUbiUserStats>() {
            @Override
            public FastUbiUserStats answer(InvocationOnMock invocation) throws Throwable {
                Thread.sleep(100);
                return fetchedUserStats;
            }
        });

        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(same(fetchedUserStats.getOwnedItemsCurrentPrices()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);

        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(fetchedUserStats.getCurrentSellOrders()), same(fetchedUserStats.getOwnedItemsCurrentPrices()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskByFetchedUserStats(user, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 1);

        FastTradeCommand command3 = mock(FastTradeCommand.class);
        commands.add(command3);

        tasks.get(0).get();

        assertTrue(commands.size() == 1);
        assertTrue(commands.stream().noneMatch(c -> c == command1));
        assertTrue(commands.stream().noneMatch(c -> c == command2));
        assertTrue(commands.stream().anyMatch(c -> c == command3));
    }

    @Test
    public void submitCreateCommandsTaskByFetchedUserStats_should_do_nothing_if_commands_list_is_not_empty() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);

        AuthorizationDTO dto = mock(AuthorizationDTO.class);

        when(user.toAuthorizationDTO()).thenReturn(dto);

        List itemsMedianPriceAndRarity = mock(List.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        commands.add(mock(FastTradeCommand.class));
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        FastUbiUserStats fetchedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId3", 5, 6),
                new ItemCurrentPrices("itemId4", 7, 8)
        ));

        when(commonValuesService.getFastTradeOwnedItemsLimit()).thenReturn(200);

        when(personalGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(same(dto), eq(200))).thenReturn(fetchedUserStats);
        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(same(fetchedUserStats.getOwnedItemsCurrentPrices()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);

        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(fetchedUserStats.getCurrentSellOrders()), same(fetchedUserStats.getOwnedItemsCurrentPrices()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskByFetchedUserStats(user, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 0);

        verify(fastTradeManagementCommandExecutor, never()).executeCommand(any());
        verify(commonGraphQlClientService, never()).fetchLimitedItemsStats(any(), anyInt(), anyInt());
        verify(potentialTradeItemsFactory, never()).createPotentialTradeItemsForUser(any(), any(), anyInt(), anyInt());
        verify(tradeCommandsFactory, never()).createTradeCommandsForUser(any(), any(), any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices_should_add_commands_to_commands_list_and_set_saved_users_to_null_if_list_is_empty() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);
        List itemsMedianPriceAndRarity = mock(List.class);

        AuthorizationDTO authorizationDTO = mock(AuthorizationDTO.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        when(commonValuesService.getFetchUsersItemsLimit()).thenReturn(200);
        when(commonValuesService.getFetchUsersItemsOffset()).thenReturn(20);

        List<ItemCurrentPrices> fetchedCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6),
                new ItemCurrentPrices("itemId3", 7, 8)
        );

        when(commonGraphQlClientService.fetchLimitedItemsStats(same(authorizationDTO), eq(200), eq(20))).thenReturn(fetchedCurrentPrices);

        List<ItemCurrentPrices> ownedItemsCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6)
        );

        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);


        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(savedUserStats.getCurrentSellOrders()), argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(user, authorizationDTO, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 1);

        tasks.get(0).get();

        assertTrue(commands.size() == 2);
        assertTrue(commands.stream().anyMatch(c -> c == command1));
        assertTrue(commands.stream().anyMatch(c -> c == command2));
        assertNull(ReflectionTestUtils.getField(userFastTradesManager, "savedUserStats"));
    }

    @Test
    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices_should_add_empty_list_if_savedUserStats_is_null() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);
        List itemsMedianPriceAndRarity = mock(List.class);

        AuthorizationDTO authorizationDTO = mock(AuthorizationDTO.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", null);

        when(commonValuesService.getFetchUsersItemsLimit()).thenReturn(200);
        when(commonValuesService.getFetchUsersItemsOffset()).thenReturn(20);

        List<ItemCurrentPrices> fetchedCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6),
                new ItemCurrentPrices("itemId3", 7, 8)
        );

        when(commonGraphQlClientService.fetchLimitedItemsStats(same(authorizationDTO), eq(200), eq(20))).thenReturn(fetchedCurrentPrices);

        List<ItemCurrentPrices> ownedItemsCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6)
        );

        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);


        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(savedUserStats.getCurrentSellOrders()), argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(user, authorizationDTO, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 1);

        tasks.get(0).get();

        assertTrue(commands.size() == 0);
    }

    @Test
    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices_should_additionally_check_if_commands_list_empty_before_adding_new() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);
        List itemsMedianPriceAndRarity = mock(List.class);

        AuthorizationDTO authorizationDTO = mock(AuthorizationDTO.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        when(commonValuesService.getFetchUsersItemsLimit()).thenReturn(200);
        when(commonValuesService.getFetchUsersItemsOffset()).thenReturn(20);

        List<ItemCurrentPrices> fetchedCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6),
                new ItemCurrentPrices("itemId3", 7, 8)
        );

        when(commonGraphQlClientService.fetchLimitedItemsStats(same(authorizationDTO), eq(200), eq(20))).then(new Answer<List<ItemCurrentPrices>>() {
            @Override
            public List<ItemCurrentPrices> answer(InvocationOnMock invocation) throws Throwable {
                Thread.sleep(100);
                return fetchedCurrentPrices;
            }
        });

        List<ItemCurrentPrices> ownedItemsCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6)
        );

        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);


        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(savedUserStats.getCurrentSellOrders()), argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(user, authorizationDTO, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 1);

        FastTradeCommand command3 = mock(FastTradeCommand.class);
        commands.add(command3);

        tasks.get(0).get();

        assertTrue(commands.size() == 1);
        assertTrue(commands.stream().noneMatch(c -> c == command1));
        assertTrue(commands.stream().noneMatch(c -> c == command2));
        assertTrue(commands.stream().anyMatch(c -> c == command3));
    }

    @Test
    public void submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices_should_do_nothing_if_commands_is_not_empty() throws Exception {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        ManagedUser user = mock(ManagedUser.class);
        List itemsMedianPriceAndRarity = mock(List.class);

        AuthorizationDTO authorizationDTO = mock(AuthorizationDTO.class);

        Set<FastTradeCommand> commands = new TreeSet<>();
        commands.add(mock(FastTradeCommand.class));
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId1", 1, 2),
                new ItemCurrentPrices("itemId2", 3, 4)
        ));

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);
        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        when(commonValuesService.getFetchUsersItemsLimit()).thenReturn(200);
        when(commonValuesService.getFetchUsersItemsOffset()).thenReturn(20);

        List<ItemCurrentPrices> fetchedCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6),
                new ItemCurrentPrices("itemId3", 7, 8)
        );

        when(commonGraphQlClientService.fetchLimitedItemsStats(same(authorizationDTO), eq(200), eq(20))).thenReturn(fetchedCurrentPrices);

        List<ItemCurrentPrices> ownedItemsCurrentPrices = List.of(
                new ItemCurrentPrices("itemId1", 5, 6)
        );

        List potentialTrades = mock(List.class);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        when(potentialTradeItemsFactory.createPotentialTradeItemsForUser(argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), eq(1), eq(2))).thenReturn(potentialTrades);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);

        List<FastTradeCommand> createdCommands = List.of(command1, command2);


        when(tradeCommandsFactory.createTradeCommandsForUser(same(user), same(savedUserStats.getCurrentSellOrders()), argThat(arg -> arg.containsAll(ownedItemsCurrentPrices) && arg.size() == ownedItemsCurrentPrices.size()), same(itemsMedianPriceAndRarity), same(potentialTrades), eq(3), eq(4))).thenReturn(createdCommands);

        userFastTradesManager.submitCreateCommandsTaskBySavedUserStatsAndFetchedCurrentPrices(user, authorizationDTO, itemsMedianPriceAndRarity, 3, 4);

        assertTrue(tasks.size() == 0);

        verify(fastTradeManagementCommandExecutor, never()).executeCommand(any());
        verify(commonGraphQlClientService, never()).fetchLimitedItemsStats(any(), anyInt(), anyInt());
        verify(potentialTradeItemsFactory, never()).createPotentialTradeItemsForUser(any(), any(), anyInt(), anyInt());
        verify(tradeCommandsFactory, never()).createTradeCommandsForUser(any(), any(), any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    public void executeFastSellCommands_should_execute_commands_and_clear_tasks_in_commands_list_is_not_empty() {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        FastTradeCommand command1 = mock(FastTradeCommand.class);
        FastTradeCommand command2 = mock(FastTradeCommand.class);
        Set commands = new TreeSet();
        commands.add(command1);
        commands.add(command2);

        Future future1 = mock(Future.class);
        Future future2 = mock(Future.class);
        List tasks = new ArrayList<>();
        tasks.add(future1);
        tasks.add(future2);

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);

        userFastTradesManager.executeFastSellCommands();

        Mockito.verify(fastTradeManagementCommandExecutor).executeCommand(same(command1));
        Mockito.verify(fastTradeManagementCommandExecutor).executeCommand(same(command2));

        verify(future1).cancel(true);
        verify(future2).cancel(true);

        assertTrue(commands.isEmpty());
    }

    @Test
    public void executeFastSellCommands_should_do_nothing_is_commands_is_empty() {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        Set commands = new TreeSet();

        Future future1 = mock(Future.class);
        Future future2 = mock(Future.class);
        List tasks = List.of(future1, future2);

        ReflectionTestUtils.setField(userFastTradesManager, "commands", commands);
        ReflectionTestUtils.setField(userFastTradesManager, "createCommandsTasks", tasks);

        userFastTradesManager.executeFastSellCommands();

        Mockito.verify(fastTradeManagementCommandExecutor, never()).executeCommand(any());

        verify(future1, never()).cancel(anyBoolean());
        verify(future2, never()).cancel(anyBoolean());
    }

    @Test
    public void createAndExecuteCommandsToKeepOneSellSlotUnused_should_fetch_info_and_execute_commands_and_save_ubi_user_stats() {
        userFastTradesManager = new UserFastTradesManager(personalGraphQlClientService, commonGraphQlClientService, commonValuesService, potentialTradeItemsFactory, tradeCommandsFactory, fastTradeManagementCommandExecutor);

        List itemCurrentPrices = Mockito.mock(List.class);
        List sellTrades = Mockito.mock(List.class);

        AuthorizationDTO authorizationDTO = Mockito.mock(AuthorizationDTO.class);

        ManagedUser user = Mockito.mock(ManagedUser.class);
        when(user.toAuthorizationDTO()).thenReturn(authorizationDTO);
        when(user.getUbiProfileId()).thenReturn("profileId");

        when(commonValuesService.getFastTradeOwnedItemsLimit()).thenReturn(120);

        FastUbiUserStats savedUserStats = new FastUbiUserStats();
        savedUserStats.setOwnedItemsCurrentPrices(itemCurrentPrices);
        savedUserStats.setCurrentSellOrders(sellTrades);

        ReflectionTestUtils.setField(userFastTradesManager, "savedUserStats", savedUserStats);

        FastUbiUserStats fetchedUserStats = new FastUbiUserStats();
        savedUserStats.setCurrentSellOrders(mock(List.class));
        savedUserStats.setOwnedItemsCurrentPrices(List.of(
                new ItemCurrentPrices("itemId3", 5, 6),
                new ItemCurrentPrices("itemId4", 7, 8)
        ));

        when(personalGraphQlClientService.fetchOwnedItemsCurrentPricesAndSellOrdersForUser(same(authorizationDTO), eq(120))).thenReturn(fetchedUserStats);

        when(commonValuesService.getMinMedianPriceDifference()).thenReturn(1);
        when(commonValuesService.getMinMedianPriceDifferencePercentage()).thenReturn(2);

        FastTradeCommand command1 = Mockito.mock(FastTradeCommand.class);
        FastTradeCommand command2 = Mockito.mock(FastTradeCommand.class);

        List commands = List.of(command1, command2);

        when(tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(same(user), same(fetchedUserStats.getCurrentSellOrders()), same(fetchedUserStats.getOwnedItemsCurrentPrices()), eq(3), eq(4))).thenReturn(commands);

        userFastTradesManager.createAndExecuteCommandsToKeepOneSellSlotUnused(user, 3, 4);

        Mockito.verify(fastTradeManagementCommandExecutor).executeCommand(same(command1));
        Mockito.verify(fastTradeManagementCommandExecutor).executeCommand(same(command2));
        assertEquals(ReflectionTestUtils.getField(userFastTradesManager, "savedUserStats"), fetchedUserStats);
    }
}