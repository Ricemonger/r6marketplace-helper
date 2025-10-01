package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.marketplace.graphQl.personal_query_user_stats.PersonalQueryUserStatsGraphQlClientService;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UbiAccountStats;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UserAuthorizedUbiAccount;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.*;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UbiUsersStatsFetchingServiceTest {
    @Autowired
    private UbiUsersStatsFetchingService ubiUsersStatsFetchingService;
    @MockBean
    private UbiAccountService ubiAccountService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private PersonalQueryUserStatsGraphQlClientService personalQueryUserStatsGraphQlClientService;
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private TradeService tradeService;

    @Test
    public void fetchAllUbiUsersStatsAndManageTrades_should_update_Authorized_ubiStats() {
        UserAuthorizedUbiAccount noNotificationUbiAccount = new UserAuthorizedUbiAccount();
        noNotificationUbiAccount.setUserId(1L);
        noNotificationUbiAccount.setProfileId("profileId1");
        noNotificationUbiAccount.setCreditAmount(100);
        noNotificationUbiAccount.setOwnedItemsIdsCount(1);
        noNotificationUbiAccount.setTicket("ticket1");
        noNotificationUbiAccount.setSpaceId("spaceId1");
        noNotificationUbiAccount.setSessionId("sessionId1");
        noNotificationUbiAccount.setRememberDeviceTicket("rememberDeviceTicket1");
        noNotificationUbiAccount.setRememberMeTicket("rememberMeTicket1");

        AuthorizationDTO authDTO1 = noNotificationUbiAccount.toAuthorizationDTO();

        UbiTrade currentSellTrade11 = new UbiTrade();
        currentSellTrade11.setTradeId("currentSellTrade11");
        currentSellTrade11.setCategory(TradeCategory.Sell);

        UbiTrade currentSellTrade12 = new UbiTrade();
        currentSellTrade12.setTradeId("currentSellTrade12");
        currentSellTrade12.setCategory(TradeCategory.Sell);

        UbiTrade currentBuyTrade11 = new UbiTrade();
        currentBuyTrade11.setTradeId("currentBuyTrade11");
        currentBuyTrade11.setCategory(TradeCategory.Buy);

        UbiTrade currentBuyTrade12 = new UbiTrade();
        currentBuyTrade12.setTradeId("currentBuyTrade12");
        currentBuyTrade12.setCategory(TradeCategory.Buy);

        UbiTrade finishedSellTrade1 = new UbiTrade();
        finishedSellTrade1.setState(TradeState.Succeeded);
        finishedSellTrade1.setCategory(TradeCategory.Sell);
        finishedSellTrade1.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        UbiTrade finishedBuyTrade1 = new UbiTrade();
        finishedBuyTrade1.setState(TradeState.Succeeded);
        finishedBuyTrade1.setCategory(TradeCategory.Buy);
        finishedBuyTrade1.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        Trade currentSellTrade11Prioritized = new Trade();
        currentSellTrade11Prioritized.setTradeId("currentSellTrade11");
        Trade currentSellTrade12Prioritized = new Trade();
        currentSellTrade12Prioritized.setTradeId("currentSellTrade12");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentSellTrade11, currentSellTrade12))).thenReturn(List.of(currentSellTrade11Prioritized, currentSellTrade12Prioritized));

        Trade currentBuyTrade11Prioritized = new Trade();
        currentBuyTrade11Prioritized.setTradeId("currentBuyTrade11");
        Trade currentBuyTrade12Prioritized = new Trade();
        currentBuyTrade12Prioritized.setTradeId("currentBuyTrade12");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentBuyTrade11, currentBuyTrade12))).thenReturn(List.of(currentBuyTrade11Prioritized, currentBuyTrade12Prioritized));

        List<String> ownedItemsIds1 = List.of("ownedItemId1", "ownedItemId2");

        ItemResaleLock resaleLock11 = new ItemResaleLock();
        resaleLock11.setItemId("resaleLock11");
        resaleLock11.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));

        ItemResaleLock resaleLock12 = new ItemResaleLock();
        resaleLock12.setItemId("resaleLock12");
        resaleLock12.setExpiresAt(LocalDateTime.of(2021, 1, 2, 1, 1));

        List<ItemResaleLock> resaleLocks1 = List.of(resaleLock11, resaleLock12);

        UserTradesLimitations userTradesLimitations1 = new UserTradesLimitations();
        userTradesLimitations1.setResolvedSellTransactionCount(11);
        userTradesLimitations1.setResolvedBuyTransactionCount(12);
        userTradesLimitations1.setResaleLocks(resaleLocks1);

        UbiUserStats stats = new UbiUserStats();
        stats.setCreditAmount(100);
        stats.setCurrentOrders(List.of(currentSellTrade11, currentSellTrade12, currentBuyTrade11, currentBuyTrade12));
        stats.setFinishedOrders(List.of(finishedSellTrade1, finishedBuyTrade1));
        stats.setUserTradesLimitations(userTradesLimitations1);
        stats.setOwnedItemsIds(ownedItemsIds1);

        when(personalQueryUserStatsGraphQlClientService.fetchUbiUserStatsForUser(authDTO1, 1)).thenReturn(stats);

        UserAuthorizedUbiAccount creditAmountNotificationUbiAccount = new UserAuthorizedUbiAccount();
        creditAmountNotificationUbiAccount.setUserId(2L);
        creditAmountNotificationUbiAccount.setProfileId("profileId2");
        creditAmountNotificationUbiAccount.setCreditAmount(200);
        creditAmountNotificationUbiAccount.setOwnedItemsIdsCount(2);
        creditAmountNotificationUbiAccount.setTicket("ticket2");
        creditAmountNotificationUbiAccount.setSpaceId("spaceId2");
        creditAmountNotificationUbiAccount.setSessionId("sessionId2");
        creditAmountNotificationUbiAccount.setRememberDeviceTicket("rememberDeviceTicket2");
        creditAmountNotificationUbiAccount.setRememberMeTicket("rememberMeTicket2");

        AuthorizationDTO authDTO2 = creditAmountNotificationUbiAccount.toAuthorizationDTO();

        UbiTrade currentSellTrade21 = new UbiTrade();
        currentSellTrade21.setTradeId("currentSellTrade21");
        currentSellTrade21.setCategory(TradeCategory.Sell);

        UbiTrade currentSellTrade22 = new UbiTrade();
        currentSellTrade22.setTradeId("currentSellTrade22");
        currentSellTrade22.setCategory(TradeCategory.Sell);

        UbiTrade currentBuyTrade21 = new UbiTrade();
        currentBuyTrade21.setTradeId("currentBuyTrade21");
        currentBuyTrade21.setCategory(TradeCategory.Buy);

        UbiTrade currentBuyTrade22 = new UbiTrade();
        currentBuyTrade22.setTradeId("currentBuyTrade22");
        currentBuyTrade22.setCategory(TradeCategory.Buy);

        UbiTrade finishedSellTrade2 = new UbiTrade();
        finishedSellTrade2.setState(TradeState.Succeeded);
        finishedSellTrade2.setCategory(TradeCategory.Sell);
        finishedSellTrade2.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        UbiTrade finishedBuyTrade2 = new UbiTrade();
        finishedBuyTrade2.setState(TradeState.Succeeded);
        finishedBuyTrade2.setCategory(TradeCategory.Buy);
        finishedBuyTrade2.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        Trade currentSellTrade21Prioritized = new Trade();
        currentSellTrade21Prioritized.setTradeId("currentSellTrade21");
        Trade currentSellTrade22Prioritized = new Trade();
        currentSellTrade22Prioritized.setTradeId("currentSellTrade22");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentSellTrade21, currentSellTrade22))).thenReturn(List.of(currentSellTrade21Prioritized, currentSellTrade22Prioritized));

        Trade currentBuyTrade21Prioritized = new Trade();
        currentBuyTrade21Prioritized.setTradeId("currentBuyTrade21");
        Trade currentBuyTrade22Prioritized = new Trade();
        currentBuyTrade22Prioritized.setTradeId("currentBuyTrade22");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentBuyTrade21, currentBuyTrade22))).thenReturn(List.of(currentBuyTrade21Prioritized, currentBuyTrade22Prioritized));

        List<String> ownedItemsIds2 = List.of("ownedItemId3", "ownedItemId4");

        ItemResaleLock resaleLock21 = new ItemResaleLock();
        resaleLock21.setItemId("resaleLock21");
        resaleLock21.setExpiresAt(LocalDateTime.of(2021, 2, 1, 1, 1));

        ItemResaleLock resaleLock22 = new ItemResaleLock();
        resaleLock22.setItemId("resaleLock22");
        resaleLock22.setExpiresAt(LocalDateTime.of(2021, 2, 2, 1, 1));

        List<ItemResaleLock> resaleLocks2 = List.of(resaleLock21, resaleLock22);

        UserTradesLimitations userTradesLimitations2 = new UserTradesLimitations();
        userTradesLimitations2.setResolvedSellTransactionCount(21);
        userTradesLimitations2.setResolvedBuyTransactionCount(22);
        userTradesLimitations2.setResaleLocks(resaleLocks2);

        UbiUserStats stats2 = new UbiUserStats();
        stats2.setCreditAmount(201);
        stats2.setCurrentOrders(List.of(currentSellTrade21, currentSellTrade22, currentBuyTrade21, currentBuyTrade22));
        stats2.setFinishedOrders(List.of(finishedSellTrade2, finishedBuyTrade2));
        stats2.setUserTradesLimitations(userTradesLimitations2);
        stats2.setOwnedItemsIds(ownedItemsIds2);

        when(personalQueryUserStatsGraphQlClientService.fetchUbiUserStatsForUser(authDTO2, 2)).thenReturn(stats2);

        UserAuthorizedUbiAccount soldIn24hNotificationUbiAccount = new UserAuthorizedUbiAccount();
        soldIn24hNotificationUbiAccount.setUserId(3L);
        soldIn24hNotificationUbiAccount.setProfileId("profileId3");
        soldIn24hNotificationUbiAccount.setCreditAmount(300);
        soldIn24hNotificationUbiAccount.setOwnedItemsIdsCount(3);
        soldIn24hNotificationUbiAccount.setTicket("ticket3");
        soldIn24hNotificationUbiAccount.setSpaceId("spaceId3");
        soldIn24hNotificationUbiAccount.setSessionId("sessionId3");
        soldIn24hNotificationUbiAccount.setRememberDeviceTicket("rememberDeviceTicket3");
        soldIn24hNotificationUbiAccount.setRememberMeTicket("rememberMeTicket3");

        AuthorizationDTO authDTO3 = soldIn24hNotificationUbiAccount.toAuthorizationDTO();

        UbiTrade currentSellTrade3 = new UbiTrade();
        currentSellTrade3.setTradeId("currentSellTrade31");
        currentSellTrade3.setCategory(TradeCategory.Sell);

        UbiTrade currentBuyTrade3 = new UbiTrade();
        currentBuyTrade3.setTradeId("currentBuyTrade31");
        currentBuyTrade3.setCategory(TradeCategory.Buy);

        UbiTrade finishedSellTrade3 = new UbiTrade();
        finishedSellTrade3.setState(TradeState.Succeeded);
        finishedSellTrade3.setCategory(TradeCategory.Sell);
        finishedSellTrade3.setItem(new Item());
        finishedSellTrade3.setLastModifiedAt(LocalDateTime.now().minusMinutes(8));

        UbiTrade finishedBuyTrade3 = new UbiTrade();
        finishedBuyTrade3.setState(TradeState.Succeeded);
        finishedBuyTrade3.setCategory(TradeCategory.Buy);
        finishedBuyTrade3.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        Trade currentSellTrade3Prioritized = new Trade();
        currentSellTrade3Prioritized.setTradeId("currentSellTrade31");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentSellTrade3))).thenReturn(List.of(currentSellTrade3Prioritized));

        Trade currentBuyTrade3Prioritized = new Trade();
        currentBuyTrade3Prioritized.setTradeId("currentBuyTrade31");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentBuyTrade3))).thenReturn(List.of(currentBuyTrade3Prioritized));

        List<String> ownedItemsIds3 = List.of("ownedItemId5", "ownedItemId6");

        ItemResaleLock resaleLock3 = new ItemResaleLock();
        resaleLock3.setItemId("resaleLock3");
        resaleLock3.setExpiresAt(LocalDateTime.of(2021, 3, 1, 1, 1));

        UserTradesLimitations userTradesLimitations3 = new UserTradesLimitations();
        userTradesLimitations3.setResolvedSellTransactionCount(31);
        userTradesLimitations3.setResolvedBuyTransactionCount(32);
        userTradesLimitations3.setResaleLocks(List.of(resaleLock3));

        UbiUserStats stats3 = new UbiUserStats();
        stats3.setCreditAmount(300);
        stats3.setCurrentOrders(List.of(currentSellTrade3, currentBuyTrade3));
        stats3.setFinishedOrders(List.of(finishedSellTrade3, finishedBuyTrade3));
        stats3.setUserTradesLimitations(userTradesLimitations3);
        stats3.setOwnedItemsIds(ownedItemsIds3);

        when(personalQueryUserStatsGraphQlClientService.fetchUbiUserStatsForUser(authDTO3, 3)).thenReturn(stats3);

        UserAuthorizedUbiAccount boughtIn24hNotificationUbiAccount = new UserAuthorizedUbiAccount();
        boughtIn24hNotificationUbiAccount.setUserId(4L);
        boughtIn24hNotificationUbiAccount.setProfileId("profileId4");
        boughtIn24hNotificationUbiAccount.setCreditAmount(400);
        boughtIn24hNotificationUbiAccount.setOwnedItemsIdsCount(4);
        boughtIn24hNotificationUbiAccount.setTicket("ticket4");
        boughtIn24hNotificationUbiAccount.setSpaceId("spaceId4");
        boughtIn24hNotificationUbiAccount.setSessionId("sessionId4");
        boughtIn24hNotificationUbiAccount.setRememberDeviceTicket("rememberDeviceTicket4");
        boughtIn24hNotificationUbiAccount.setRememberMeTicket("rememberMeTicket4");

        AuthorizationDTO authDTO4 = boughtIn24hNotificationUbiAccount.toAuthorizationDTO();

        UbiTrade currentSellTrade4 = new UbiTrade();
        currentSellTrade4.setTradeId("currentSellTrade41");
        currentSellTrade4.setCategory(TradeCategory.Sell);

        UbiTrade currentBuyTrade4 = new UbiTrade();
        currentBuyTrade4.setTradeId("currentBuyTrade41");
        currentBuyTrade4.setCategory(TradeCategory.Buy);

        UbiTrade finishedSellTrade4 = new UbiTrade();
        finishedSellTrade4.setState(TradeState.Succeeded);
        finishedSellTrade4.setCategory(TradeCategory.Sell);
        finishedSellTrade4.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        UbiTrade finishedBuyTrade4 = new UbiTrade();
        finishedBuyTrade4.setState(TradeState.Succeeded);
        finishedBuyTrade4.setCategory(TradeCategory.Buy);
        finishedBuyTrade4.setItem(new Item());
        finishedBuyTrade4.setLastModifiedAt(LocalDateTime.now().minusMinutes(8));

        Trade currentSellTrade4Prioritized = new Trade();
        currentSellTrade4Prioritized.setTradeId("currentSellTrade41");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentSellTrade4))).thenReturn(List.of(currentSellTrade4Prioritized));

        Trade currentBuyTrade4Prioritized = new Trade();
        currentBuyTrade4Prioritized.setTradeId("currentBuyTrade41");
        when(tradeService.calculateTradeStatsForUbiTrades(List.of(currentBuyTrade4))).thenReturn(List.of(currentBuyTrade4Prioritized));

        List<String> ownedItemsIds4 = List.of("ownedItemId7", "ownedItemId8");

        ItemResaleLock resaleLock4 = new ItemResaleLock();
        resaleLock4.setItemId("resaleLock4");
        resaleLock4.setExpiresAt(LocalDateTime.of(2021, 4, 1, 1, 1));

        UserTradesLimitations userTradesLimitations4 = new UserTradesLimitations();
        userTradesLimitations4.setResolvedSellTransactionCount(41);
        userTradesLimitations4.setResolvedBuyTransactionCount(42);
        userTradesLimitations4.setResaleLocks(List.of(resaleLock4));

        UbiUserStats stats4 = new UbiUserStats();
        stats4.setCreditAmount(400);
        stats4.setCurrentOrders(List.of(currentSellTrade4, currentBuyTrade4));
        stats4.setFinishedOrders(List.of(finishedSellTrade4, finishedBuyTrade4));
        stats4.setUserTradesLimitations(userTradesLimitations4);
        stats4.setCurrentOrders(List.of(currentSellTrade4, currentBuyTrade4));

        when(personalQueryUserStatsGraphQlClientService.fetchUbiUserStatsForUser(authDTO4, 4)).thenReturn(stats4);

        List<UserAuthorizedUbiAccount> existingUsersUbiAccounts = List.of(noNotificationUbiAccount, creditAmountNotificationUbiAccount, soldIn24hNotificationUbiAccount, boughtIn24hNotificationUbiAccount);

        when(ubiAccountService.findAllUsersUbiAccountEntries()).thenReturn(existingUsersUbiAccounts);
        when(commonValuesService.getLastUbiUsersStatsFetchTime()).thenReturn(LocalDateTime.now().minusMinutes(10));

        ubiUsersStatsFetchingService.fetchAllAuthorizedUbiUsersStats();

        UbiAccountStats noNotificationUbiAccountStats = new UbiAccountStats();
        noNotificationUbiAccountStats.setUbiProfileId("profileId1");
        noNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations1.getResolvedSellTransactionCount());
        noNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations1.getResolvedBuyTransactionCount());
        noNotificationUbiAccountStats.setCreditAmount(100);
        noNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds1);
        noNotificationUbiAccountStats.setResaleLocks(resaleLocks1);
        noNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade11Prioritized, currentSellTrade12Prioritized));
        noNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade11Prioritized, currentBuyTrade12Prioritized));

        UbiAccountStats creditAmountNotificationUbiAccountStats = new UbiAccountStats();
        creditAmountNotificationUbiAccountStats.setUbiProfileId("profileId2");
        creditAmountNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations2.getResolvedSellTransactionCount());
        creditAmountNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations2.getResolvedBuyTransactionCount());
        creditAmountNotificationUbiAccountStats.setCreditAmount(201);
        creditAmountNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds2);
        creditAmountNotificationUbiAccountStats.setResaleLocks(resaleLocks2);
        creditAmountNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade21Prioritized, currentSellTrade22Prioritized));
        creditAmountNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade21Prioritized, currentBuyTrade22Prioritized));

        UbiAccountStats soldIn24hNotificationUbiAccountStats = new UbiAccountStats();
        soldIn24hNotificationUbiAccountStats.setUbiProfileId("profileId3");
        soldIn24hNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations3.getResolvedSellTransactionCount());
        soldIn24hNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations3.getResolvedBuyTransactionCount());
        soldIn24hNotificationUbiAccountStats.setCreditAmount(300);
        soldIn24hNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds3);
        soldIn24hNotificationUbiAccountStats.setResaleLocks(List.of(resaleLock3));
        soldIn24hNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade3Prioritized));
        soldIn24hNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade3Prioritized));

        UbiAccountStats boughtIn24hNotificationUbiAccountStats = new UbiAccountStats();
        boughtIn24hNotificationUbiAccountStats.setUbiProfileId("profileId4");
        boughtIn24hNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations4.getResolvedSellTransactionCount());
        boughtIn24hNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations4.getResolvedBuyTransactionCount());
        boughtIn24hNotificationUbiAccountStats.setCreditAmount(400);
        boughtIn24hNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds4);
        boughtIn24hNotificationUbiAccountStats.setResaleLocks(List.of(resaleLock4));
        boughtIn24hNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade4Prioritized));
        boughtIn24hNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade4Prioritized));

        List<UbiAccountStats> expectedUpdatedUbiAccountsStats = List.of(noNotificationUbiAccountStats, creditAmountNotificationUbiAccountStats, soldIn24hNotificationUbiAccountStats, boughtIn24hNotificationUbiAccountStats);

        verify(commonValuesService).setLastUbiUsersStatsFetchTime(argThat(time -> Duration.between(time, LocalDateTime.now()).getSeconds() < 30));

        verify(ubiAccountService).saveAllUbiAccountStats(argThat(arg -> arg.containsAll(expectedUpdatedUbiAccountsStats) && arg.size() == 4));

        verify(notificationService, times(0)).sendUbiStatsUpdatedNotification(eq(noNotificationUbiAccount.getUserId()), anyString());
        verify(notificationService, times(1)).sendUbiStatsUpdatedNotification(eq(creditAmountNotificationUbiAccount.getUserId()), anyString());
        verify(notificationService, times(1)).sendUbiStatsUpdatedNotification(eq(soldIn24hNotificationUbiAccount.getUserId()), anyString());
        verify(notificationService, times(1)).sendUbiStatsUpdatedNotification(eq(boughtIn24hNotificationUbiAccount.getUserId()), anyString());
    }
}