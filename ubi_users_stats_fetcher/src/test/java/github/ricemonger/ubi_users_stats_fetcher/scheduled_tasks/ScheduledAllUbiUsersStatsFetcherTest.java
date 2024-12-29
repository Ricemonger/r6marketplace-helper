package github.ricemonger.ubi_users_stats_fetcher.scheduled_tasks;

import github.ricemonger.marketplace.graphQl.personal_query_credits_amount.PersonalQueryCreditAmountGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_current_orders.PersonalQueryCurrentOrdersGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_finished_orders.PersonalQueryFinishedOrdersGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_locked_items.PersonalQueryLockedItemsGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_query_owned_items.PersonalQueryOwnedItemsGraphQlClientService;
import github.ricemonger.ubi_users_stats_fetcher.services.CommonValuesService;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UbiAccountStats;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UserUbiAccount;
import github.ricemonger.ubi_users_stats_fetcher.services.TelegramBotService;
import github.ricemonger.ubi_users_stats_fetcher.services.UbiAccountService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.DTOs.personal.UserTradesLimitations;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.enums.TradeCategory;
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
class ScheduledAllUbiUsersStatsFetcherTest {
    @Autowired
    private ScheduledAllUbiUsersStatsFetcher scheduledAllUbiUsersStatsFetcher;
    @MockBean
    private UbiAccountService ubiAccountService;
    @MockBean
    private TelegramBotService telegramBotService;
    @MockBean
    private PersonalQueryCreditAmountGraphQlClientService personalQueryCreditAmountGraphQlClientService;
    @MockBean
    private PersonalQueryCurrentOrdersGraphQlClientService personalQueryCurrentOrdersGraphQlClientService;
    @MockBean
    private PersonalQueryFinishedOrdersGraphQlClientService personalQueryFinishedOrdersGraphQlClientService;
    @MockBean
    private PersonalQueryOwnedItemsGraphQlClientService personalQueryOwnedItemsGraphQlClientService;
    @MockBean
    private PersonalQueryLockedItemsGraphQlClientService personalQueryLockedItemsGraphQlClientService;
    @MockBean
    private CommonValuesService commonValuesService;

    @Test
    public void fetchAllUbiUsersStatsAndManageTrades_should_update_Authorized_ubiStats() {
        UserUbiAccount noNotificationUbiAccount = new UserUbiAccount();
        noNotificationUbiAccount.setUserId(1L);
        noNotificationUbiAccount.setProfileId("profileId1");
        noNotificationUbiAccount.setCreditAmount(100);
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
        finishedSellTrade1.setCategory(TradeCategory.Sell);
        finishedSellTrade1.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        UbiTrade finishedBuyTrade1 = new UbiTrade();
        finishedBuyTrade1.setCategory(TradeCategory.Buy);
        finishedBuyTrade1.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

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

        when(personalQueryCreditAmountGraphQlClientService.fetchCreditAmountForUser(authDTO1)).thenReturn(100);
        when(personalQueryCurrentOrdersGraphQlClientService.fetchCurrentOrdersForUser(authDTO1)).thenReturn(List.of(currentSellTrade11, currentSellTrade12, currentBuyTrade11, currentBuyTrade12));
        when(personalQueryFinishedOrdersGraphQlClientService.fetchLastFinishedOrdersForUser(authDTO1)).thenReturn(List.of(finishedSellTrade1, finishedBuyTrade1));
        when(personalQueryOwnedItemsGraphQlClientService.fetchAllOwnedItemsIdsForUser(authDTO1)).thenReturn(ownedItemsIds1);
        when(personalQueryLockedItemsGraphQlClientService.fetchTradesLimitationsForUser(authDTO1)).thenReturn(userTradesLimitations1);

        UserUbiAccount creditAmountNotificationUbiAccount = new UserUbiAccount();
        creditAmountNotificationUbiAccount.setUserId(2L);
        creditAmountNotificationUbiAccount.setProfileId("profileId2");
        creditAmountNotificationUbiAccount.setCreditAmount(200);
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
        finishedSellTrade2.setCategory(TradeCategory.Sell);
        finishedSellTrade2.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        UbiTrade finishedBuyTrade2 = new UbiTrade();
        finishedBuyTrade2.setCategory(TradeCategory.Buy);
        finishedBuyTrade2.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

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

        when(personalQueryCreditAmountGraphQlClientService.fetchCreditAmountForUser(authDTO2)).thenReturn(201);
        when(personalQueryCurrentOrdersGraphQlClientService.fetchCurrentOrdersForUser(authDTO2)).thenReturn(List.of(currentSellTrade21, currentSellTrade22, currentBuyTrade21, currentBuyTrade22));
        when(personalQueryFinishedOrdersGraphQlClientService.fetchLastFinishedOrdersForUser(authDTO2)).thenReturn(List.of(finishedSellTrade2, finishedBuyTrade2));
        when(personalQueryOwnedItemsGraphQlClientService.fetchAllOwnedItemsIdsForUser(authDTO2)).thenReturn(ownedItemsIds2);
        when(personalQueryLockedItemsGraphQlClientService.fetchTradesLimitationsForUser(authDTO2)).thenReturn(userTradesLimitations2);

        UserUbiAccount soldIn24hNotificationUbiAccount = new UserUbiAccount();
        soldIn24hNotificationUbiAccount.setUserId(3L);
        soldIn24hNotificationUbiAccount.setProfileId("profileId3");
        soldIn24hNotificationUbiAccount.setCreditAmount(300);
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
        finishedSellTrade3.setCategory(TradeCategory.Sell);
        finishedSellTrade3.setItem(new Item());
        finishedSellTrade3.setLastModifiedAt(LocalDateTime.now().minusMinutes(8));

        UbiTrade finishedBuyTrade3 = new UbiTrade();
        finishedBuyTrade3.setCategory(TradeCategory.Buy);
        finishedBuyTrade3.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        List<String> ownedItemsIds3 = List.of("ownedItemId5", "ownedItemId6");

        ItemResaleLock resaleLock3 = new ItemResaleLock();
        resaleLock3.setItemId("resaleLock3");
        resaleLock3.setExpiresAt(LocalDateTime.of(2021, 3, 1, 1, 1));

        UserTradesLimitations userTradesLimitations3 = new UserTradesLimitations();
        userTradesLimitations3.setResolvedSellTransactionCount(31);
        userTradesLimitations3.setResolvedBuyTransactionCount(32);
        userTradesLimitations3.setResaleLocks(List.of(resaleLock3));

        when(personalQueryCreditAmountGraphQlClientService.fetchCreditAmountForUser(authDTO3)).thenReturn(300);
        when(personalQueryCurrentOrdersGraphQlClientService.fetchCurrentOrdersForUser(authDTO3)).thenReturn(List.of(currentSellTrade3, currentBuyTrade3));
        when(personalQueryFinishedOrdersGraphQlClientService.fetchLastFinishedOrdersForUser(authDTO3)).thenReturn(List.of(finishedSellTrade3, finishedBuyTrade3));
        when(personalQueryOwnedItemsGraphQlClientService.fetchAllOwnedItemsIdsForUser(authDTO3)).thenReturn(ownedItemsIds3);
        when(personalQueryLockedItemsGraphQlClientService.fetchTradesLimitationsForUser(authDTO3)).thenReturn(userTradesLimitations3);

        UserUbiAccount boughtIn24hNotificationUbiAccount = new UserUbiAccount();
        boughtIn24hNotificationUbiAccount.setUserId(4L);
        boughtIn24hNotificationUbiAccount.setProfileId("profileId4");
        boughtIn24hNotificationUbiAccount.setCreditAmount(400);
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
        finishedSellTrade4.setCategory(TradeCategory.Sell);
        finishedSellTrade4.setLastModifiedAt(LocalDateTime.now().minusMinutes(15));

        UbiTrade finishedBuyTrade4 = new UbiTrade();
        finishedBuyTrade4.setCategory(TradeCategory.Buy);
        finishedBuyTrade4.setItem(new Item());
        finishedBuyTrade4.setLastModifiedAt(LocalDateTime.now().minusMinutes(8));

        List<String> ownedItemsIds4 = List.of("ownedItemId7", "ownedItemId8");

        ItemResaleLock resaleLock4 = new ItemResaleLock();
        resaleLock4.setItemId("resaleLock4");
        resaleLock4.setExpiresAt(LocalDateTime.of(2021, 4, 1, 1, 1));

        UserTradesLimitations userTradesLimitations4 = new UserTradesLimitations();
        userTradesLimitations4.setResolvedSellTransactionCount(41);
        userTradesLimitations4.setResolvedBuyTransactionCount(42);
        userTradesLimitations4.setResaleLocks(List.of(resaleLock4));

        when(personalQueryCreditAmountGraphQlClientService.fetchCreditAmountForUser(authDTO4)).thenReturn(400);
        when(personalQueryCurrentOrdersGraphQlClientService.fetchCurrentOrdersForUser(authDTO4)).thenReturn(List.of(currentSellTrade4, currentBuyTrade4));
        when(personalQueryFinishedOrdersGraphQlClientService.fetchLastFinishedOrdersForUser(authDTO4)).thenReturn(List.of(finishedSellTrade4, finishedBuyTrade4));
        when(personalQueryOwnedItemsGraphQlClientService.fetchAllOwnedItemsIdsForUser(authDTO4)).thenReturn(ownedItemsIds4);
        when(personalQueryLockedItemsGraphQlClientService.fetchTradesLimitationsForUser(authDTO4)).thenReturn(userTradesLimitations4);

        List<UserUbiAccount> existingUsersUbiAccounts = List.of(noNotificationUbiAccount, creditAmountNotificationUbiAccount, soldIn24hNotificationUbiAccount, boughtIn24hNotificationUbiAccount);

        when(ubiAccountService.findAllUsersUbiAccountEntries()).thenReturn(existingUsersUbiAccounts);
        when(commonValuesService.getLastUbiUsersStatsFetchTime()).thenReturn(LocalDateTime.now().minusMinutes(10));

        scheduledAllUbiUsersStatsFetcher.fetchAllAuthorizedUbiUsersStats();

        UbiAccountStats noNotificationUbiAccountStats = new UbiAccountStats();
        noNotificationUbiAccountStats.setUbiProfileId("profileId1");
        noNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations1.getResolvedSellTransactionCount());
        noNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations1.getResolvedBuyTransactionCount());
        noNotificationUbiAccountStats.setCreditAmount(100);
        noNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds1);
        noNotificationUbiAccountStats.setResaleLocks(resaleLocks1);
        noNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade11, currentSellTrade12));
        noNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade11, currentBuyTrade12));

        UbiAccountStats creditAmountNotificationUbiAccountStats = new UbiAccountStats();
        creditAmountNotificationUbiAccountStats.setUbiProfileId("profileId2");
        creditAmountNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations2.getResolvedSellTransactionCount());
        creditAmountNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations2.getResolvedBuyTransactionCount());
        creditAmountNotificationUbiAccountStats.setCreditAmount(201);
        creditAmountNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds2);
        creditAmountNotificationUbiAccountStats.setResaleLocks(resaleLocks2);
        creditAmountNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade21, currentSellTrade22));
        creditAmountNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade21, currentBuyTrade22));

        UbiAccountStats soldIn24hNotificationUbiAccountStats = new UbiAccountStats();
        soldIn24hNotificationUbiAccountStats.setUbiProfileId("profileId3");
        soldIn24hNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations3.getResolvedSellTransactionCount());
        soldIn24hNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations3.getResolvedBuyTransactionCount());
        soldIn24hNotificationUbiAccountStats.setCreditAmount(300);
        soldIn24hNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds3);
        soldIn24hNotificationUbiAccountStats.setResaleLocks(List.of(resaleLock3));
        soldIn24hNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade3));
        soldIn24hNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade3));

        UbiAccountStats boughtIn24hNotificationUbiAccountStats = new UbiAccountStats();
        boughtIn24hNotificationUbiAccountStats.setUbiProfileId("profileId4");
        boughtIn24hNotificationUbiAccountStats.setSoldIn24h(userTradesLimitations4.getResolvedSellTransactionCount());
        boughtIn24hNotificationUbiAccountStats.setBoughtIn24h(userTradesLimitations4.getResolvedBuyTransactionCount());
        boughtIn24hNotificationUbiAccountStats.setCreditAmount(400);
        boughtIn24hNotificationUbiAccountStats.setOwnedItemsIds(ownedItemsIds4);
        boughtIn24hNotificationUbiAccountStats.setResaleLocks(List.of(resaleLock4));
        boughtIn24hNotificationUbiAccountStats.setCurrentSellTrades(List.of(currentSellTrade4));
        boughtIn24hNotificationUbiAccountStats.setCurrentBuyTrades(List.of(currentBuyTrade4));

        List<UbiAccountStats> expectedUpdatedUbiAccountsStats = List.of(noNotificationUbiAccountStats, creditAmountNotificationUbiAccountStats, soldIn24hNotificationUbiAccountStats, boughtIn24hNotificationUbiAccountStats);

        verify(commonValuesService).setLastUbiUsersStatsFetchTime(argThat(time -> Duration.between(time, LocalDateTime.now()).getSeconds() < 30));

        verify(ubiAccountService).saveAllUbiAccountStats(argThat(arg -> arg.containsAll(expectedUpdatedUbiAccountsStats) && arg.size() == 4));

        verify(telegramBotService, times(0)).sendPrivateNotification(eq(noNotificationUbiAccount.getUserId()), anyString());
        verify(telegramBotService, times(1)).sendPrivateNotification(eq(creditAmountNotificationUbiAccount.getUserId()), anyString());
        verify(telegramBotService, times(1)).sendPrivateNotification(eq(soldIn24hNotificationUbiAccount.getUserId()), anyString());
        verify(telegramBotService, times(1)).sendPrivateNotification(eq(boughtIn24hNotificationUbiAccount.getUserId()), anyString());
    }
}