package github.ricemonger.marketplace.scheduled_tasks;

import github.ricemonger.marketplace.graphQl.GraphQlClientService;
import github.ricemonger.marketplace.services.CentralTradeManager;
import github.ricemonger.marketplace.services.CommonValuesService;
import github.ricemonger.marketplace.services.ItemService;
import github.ricemonger.marketplace.services.TelegramUserUbiAccountEntryService;
import github.ricemonger.telegramBot.TelegramBotService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.*;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.enums.TradeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduledAllUbiUsersManagerTest {
    @Autowired
    private ScheduledAllUbiUsersManager scheduledAllUbiUsersManager;
    @MockBean
    private TelegramUserUbiAccountEntryService telegramUserUbiAccountEntryService;
    @MockBean
    private TelegramBotService telegramBotService;
    @MockBean
    private GraphQlClientService graphQlClientService;
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private CentralTradeManager centralTradeManager;

    @Test
    public void fetchAllUbiUsersStatsAndManageTrades_should_update_ubiStats_and_manage_trades() {
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO1 = new UbiAccountAuthorizationEntry("ubiAuthProfileId1", "email1",
                "encodedPassword1",
                "ubiSessionId1", "ubiSpaceId1", "ubiAuthTicket1", "ubiRememberDeviceTicket1", "ubiRememberMeTicket1");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO1 = new UbiAccountStatsEntityDTO("ubiProfileId1", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO1 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO1, ubiAccountStatsEntityDTO1);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram1 = new UbiAccountEntryWithTelegram("chatId1", false, ubiAccountEntryEntityDTO1);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO2 = new UbiAccountAuthorizationEntry("ubiAuthProfileId2", "email2",
                "encodedPassword2",
                "ubiSessionId2", "ubiSpaceId2", "ubiAuthTicket2", "ubiRememberDeviceTicket2", "ubiRememberMeTicket2");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO2 = new UbiAccountStatsEntityDTO("ubiProfileId2", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO2 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO2, ubiAccountStatsEntityDTO2);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram2 = new UbiAccountEntryWithTelegram("chatId2", false, ubiAccountEntryEntityDTO2);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO3 = new UbiAccountAuthorizationEntry("ubiAuthProfileId3", "email3",
                "encodedPassword3",
                "ubiSessionId3", "ubiSpaceId3", "ubiAuthTicket3", "ubiRememberDeviceTicket3", "ubiRememberMeTicket3");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO3 = new UbiAccountStatsEntityDTO("ubiProfileId3", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO3 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO3, ubiAccountStatsEntityDTO3);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram3 = new UbiAccountEntryWithTelegram("chatId3", false, ubiAccountEntryEntityDTO3);

        List<UbiAccountEntryWithTelegram> ubiAccountAuthorizationEntriesWithTelegram = List.of(
                ubiAccountEntryEntityDTOWithTelegram1,
                ubiAccountEntryEntityDTOWithTelegram2,
                ubiAccountEntryEntityDTOWithTelegram3);

        when(telegramUserUbiAccountEntryService.findAllFUbiAccountEntriesWithTelegram()).thenReturn(ubiAccountAuthorizationEntriesWithTelegram);

        int creditAmount1 = 100;
        when(graphQlClientService.fetchCreditAmountForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(creditAmount1);
        UbiTrade currentBuyTrade1 = new UbiTrade();
        currentBuyTrade1.setCategory(TradeCategory.Buy);
        currentBuyTrade1.setTradeId("currentBuyTrade1");
        UbiTrade currentSellTrade1 = new UbiTrade();
        currentSellTrade1.setCategory(TradeCategory.Sell);
        currentSellTrade1.setTradeId("currentSellTrade1");
        when(graphQlClientService.fetchCurrentOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(List.of(currentBuyTrade1, currentSellTrade1));
        UbiTrade finishedBuyTrade1 = new UbiTrade();
        finishedBuyTrade1.setCategory(TradeCategory.Buy);
        finishedBuyTrade1.setLastModifiedAt(LocalDateTime.now());
        finishedBuyTrade1.setTradeId("finishedBuyTrade1");
        UbiTrade finishedSellTrade1 = new UbiTrade();
        finishedSellTrade1.setCategory(TradeCategory.Sell);
        finishedSellTrade1.setLastModifiedAt(LocalDateTime.now());
        finishedSellTrade1.setTradeId("finishedSellTrade1");
        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(List.of(finishedSellTrade1, finishedBuyTrade1));
        ItemResaleLockWithUbiAccount itemResaleLockWithUbiAccount1 = new ItemResaleLockWithUbiAccount();
        itemResaleLockWithUbiAccount1.setItemId("itemId1");
        ItemResaleLockWithUbiAccount itemResaleLockWithUbiAccount2 = new ItemResaleLockWithUbiAccount();
        itemResaleLockWithUbiAccount2.setItemId("itemId2");

        UserTradesLimitations userTradesLimitations1 = new UserTradesLimitations();
        userTradesLimitations1.setResolvedBuyTransactionCount(1);
        userTradesLimitations1.setResolvedSellTransactionCount(1);
        userTradesLimitations1.setResaleLocks(List.of(itemResaleLockWithUbiAccount1, itemResaleLockWithUbiAccount2));

        when(graphQlClientService.fetchTradesLimitationsForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(userTradesLimitations1);
        when(graphQlClientService.fetchAllOwnedItemsIdsForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(List.of("itemId3",
                "itemId4"));

        int creditAmount2 = 200;
        when(graphQlClientService.fetchCreditAmountForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram2))).thenReturn(creditAmount2);
        UbiTrade currentBuyTrade2 = new UbiTrade();
        currentBuyTrade2.setCategory(TradeCategory.Buy);
        currentBuyTrade2.setTradeId("currentBuyTrade2");
        when(graphQlClientService.fetchCurrentOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram2))).thenReturn(List.of(currentBuyTrade2));
        UbiTrade finishedBuyTrade2 = new UbiTrade();
        finishedBuyTrade2.setCategory(TradeCategory.Buy);
        finishedBuyTrade2.setLastModifiedAt(LocalDateTime.now().minusDays(2));
        finishedBuyTrade2.setTradeId("finishedBuyTrade2");
        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram2))).thenReturn(List.of(finishedBuyTrade2));
        ItemResaleLockWithUbiAccount itemResaleLockWithUbiAccount3 = new ItemResaleLockWithUbiAccount();
        itemResaleLockWithUbiAccount3.setItemId("itemId3");

        UserTradesLimitations userTradesLimitations2 = new UserTradesLimitations();
        userTradesLimitations2.setResolvedBuyTransactionCount(1);
        userTradesLimitations2.setResolvedSellTransactionCount(0);
        userTradesLimitations2.setResaleLocks(List.of(itemResaleLockWithUbiAccount3));

        when(graphQlClientService.fetchTradesLimitationsForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram2))).thenReturn(userTradesLimitations2);
        when(graphQlClientService.fetchAllOwnedItemsIdsForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram2))).thenReturn(List.of("itemId5",
                "itemId6"));

        int creditAmount3 = 300;
        when(graphQlClientService.fetchCreditAmountForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram3))).thenReturn(creditAmount3);
        UbiTrade currentSellTrade3 = new UbiTrade();
        currentSellTrade3.setCategory(TradeCategory.Sell);
        currentSellTrade3.setTradeId("currentSellTrade3");
        when(graphQlClientService.fetchCurrentOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram3))).thenReturn(List.of(currentSellTrade3));
        UbiTrade finishedSellTrade3 = new UbiTrade();
        finishedSellTrade3.setCategory(TradeCategory.Sell);
        finishedSellTrade3.setLastModifiedAt(LocalDateTime.now().minusDays(2));
        finishedSellTrade3.setTradeId("finishedSellTrade3");

        UserTradesLimitations userTradesLimitations3 = new UserTradesLimitations();
        userTradesLimitations3.setResolvedBuyTransactionCount(0);
        userTradesLimitations3.setResolvedSellTransactionCount(1);

        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram3))).thenReturn(List.of(finishedSellTrade3));
        when(graphQlClientService.fetchTradesLimitationsForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram3))).thenReturn(userTradesLimitations3);
        when(graphQlClientService.fetchAllOwnedItemsIdsForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram3))).thenReturn(List.of());


        when(commonValuesService.getLastUbiUsersStatsFetchTime()).thenReturn(LocalDateTime.now().minusDays(15));

        scheduledAllUbiUsersManager.fetchAllUbiUsersStatsAndManageTrades();

        UbiAccountStats ubiAccountStats1 = new UbiAccountStats("ubiProfileId1", 1, 1, creditAmount1, List.of("itemId3", "itemId4"), List.of(itemResaleLockWithUbiAccount1, itemResaleLockWithUbiAccount2), List.of(currentSellTrade1), List.of(currentBuyTrade1));
        UbiAccountStats ubiAccountStats2 = new UbiAccountStats("ubiProfileId2", 0, 1, creditAmount2, List.of("itemId5", "itemId6"), List.of(itemResaleLockWithUbiAccount3), List.of(), List.of(currentBuyTrade2));
        UbiAccountStats ubiAccountStats3 = new UbiAccountStats("ubiProfileId3", 1, 0, creditAmount3, List.of(), List.of(), List.of(currentSellTrade3), List.of());
        List<UbiAccountStats> expectedUbiAccountStats = List.of(ubiAccountStats1, ubiAccountStats2, ubiAccountStats3);

        UbiAccountStatsEntityDTO expectedUbiAccountStatsEntityDTO1 = new UbiAccountStatsEntityDTO("ubiProfileId1", creditAmount1, List.of("itemId3", "itemId4"));
        UbiAccountStatsEntityDTO expectedUbiAccountStatsEntityDTO2 = new UbiAccountStatsEntityDTO("ubiProfileId2", creditAmount2, List.of("itemId5", "itemId6"));
        UbiAccountStatsEntityDTO expectedUbiAccountStatsEntityDTO3 = new UbiAccountStatsEntityDTO("ubiProfileId3", creditAmount3, List.of());
        List<UbiAccountStatsEntityDTO> expectedUpdatedUbiAccountStatEntityDTOS = List.of(expectedUbiAccountStatsEntityDTO1, expectedUbiAccountStatsEntityDTO2, expectedUbiAccountStatsEntityDTO3);

        verify(telegramUserUbiAccountEntryService).saveAllUbiAccountStats(argThat(arg -> {
            for (UbiAccountStatsEntityDTO actual : arg) {
                UbiAccountStatsEntityDTO expected = expectedUpdatedUbiAccountStatEntityDTOS.stream()
                        .filter(e -> e.getUbiProfileId().equals(actual.getUbiProfileId()))
                        .findFirst().orElse(null);
                if (expected == null || !expected.isFullyEqual(actual)) {
                    return false;
                }
            }
            return true;
        }));

        System.out.println("Expected managed:");
        for (UbiAccountStats expected : expectedUbiAccountStats) {
            System.out.println(expected);
        }

        System.out.println("Actual managed:");

        verify(centralTradeManager).manageAllUsersTrades(argThat(arg -> {
            for (UbiAccountStats actual : arg) {
                System.out.println(actual);
            }
            for (UbiAccountStats actual : arg) {
                UbiAccountStats expected = expectedUbiAccountStats.stream()
                        .filter(e -> e.getUbiProfileId().equals(actual.getUbiProfileId()))
                        .findFirst().orElse(null);
                if (expected == null || !expected.isFullyEqual(actual)) {
                    return false;
                }
            }
            return true;
        }));
    }

    @Test
    public void fetchAllUbiUsersStatsAndManageTrades_should_notify_users_on_credits_amount() {
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO1 = new UbiAccountAuthorizationEntry("ubiAuthProfileId1", "email1",
                "encodedPassword1",
                "ubiSessionId1", "ubiSpaceId1", "ubiAuthTicket1", "ubiRememberDeviceTicket1", "ubiRememberMeTicket1");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO1 = new UbiAccountStatsEntityDTO("ubiProfileId1", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO1 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO1, ubiAccountStatsEntityDTO1);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram1 = new UbiAccountEntryWithTelegram("chatId1", true, ubiAccountEntryEntityDTO1);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO2 = new UbiAccountAuthorizationEntry("ubiAuthProfileId2", "email2",
                "encodedPassword2",
                "ubiSessionId2", "ubiSpaceId2", "ubiAuthTicket2", "ubiRememberDeviceTicket2", "ubiRememberMeTicket2");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO2 = new UbiAccountStatsEntityDTO("ubiProfileId2", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO2 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO2, ubiAccountStatsEntityDTO2);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram2 = new UbiAccountEntryWithTelegram("chatId2", false, ubiAccountEntryEntityDTO2);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO3 = new UbiAccountAuthorizationEntry("ubiAuthProfileId3", "email3",
                "encodedPassword3",
                "ubiSessionId3", "ubiSpaceId3", "ubiAuthTicket3", "ubiRememberDeviceTicket3", "ubiRememberMeTicket3");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO3 = new UbiAccountStatsEntityDTO("ubiProfileId3", 100, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO3 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO3, ubiAccountStatsEntityDTO3);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram3 = new UbiAccountEntryWithTelegram("chatId3", true, ubiAccountEntryEntityDTO3);

        List<UbiAccountEntryWithTelegram> ubiAccountAuthorizationEntriesWithTelegram = List.of(
                ubiAccountEntryEntityDTOWithTelegram1,
                ubiAccountEntryEntityDTOWithTelegram2,
                ubiAccountEntryEntityDTOWithTelegram3);

        when(telegramUserUbiAccountEntryService.findAllFUbiAccountEntriesWithTelegram()).thenReturn(ubiAccountAuthorizationEntriesWithTelegram);

        int creditAmount = 100;
        when(graphQlClientService.fetchCreditAmountForUser(any())).thenReturn(creditAmount);
        when(graphQlClientService.fetchCurrentOrdersForUser(any())).thenReturn(List.of());
        when(graphQlClientService.fetchLastFinishedOrdersForUser(any())).thenReturn(List.of());
        when(graphQlClientService.fetchTradesLimitationsForUser(any())).thenReturn(new UserTradesLimitations());
        when(graphQlClientService.fetchAllOwnedItemsIdsForUser(any())).thenReturn(List.of());

        when(commonValuesService.getLastUbiUsersStatsFetchTime()).thenReturn(LocalDateTime.now().minusDays(15));

        scheduledAllUbiUsersManager.fetchAllUbiUsersStatsAndManageTrades();

        verify(telegramBotService, times(1)).sendNotificationToUser(eq("chatId1"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId2"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId3"), anyString());
    }

    @Test
    public void fetchAllUbiUsersStatsAndManageTrades_should_notify_users_on_new_sells() {
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO1 = new UbiAccountAuthorizationEntry("ubiAuthProfileId1", "email1",
                "encodedPassword1",
                "ubiSessionId1", "ubiSpaceId1", "ubiAuthTicket1", "ubiRememberDeviceTicket1", "ubiRememberMeTicket1");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO1 = new UbiAccountStatsEntityDTO("ubiProfileId1", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO1 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO1, ubiAccountStatsEntityDTO1);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram1 = new UbiAccountEntryWithTelegram("chatId1", true, ubiAccountEntryEntityDTO1);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO2 = new UbiAccountAuthorizationEntry("ubiAuthProfileId2", "email2",
                "encodedPassword2",
                "ubiSessionId2", "ubiSpaceId2", "ubiAuthTicket2", "ubiRememberDeviceTicket2", "ubiRememberMeTicket2");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO2 = new UbiAccountStatsEntityDTO("ubiProfileId2", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO2 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO2, ubiAccountStatsEntityDTO2);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram2 = new UbiAccountEntryWithTelegram("chatId2", false, ubiAccountEntryEntityDTO2);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO3 = new UbiAccountAuthorizationEntry("ubiAuthProfileId3", "email3",
                "encodedPassword3",
                "ubiSessionId3", "ubiSpaceId3", "ubiAuthTicket3", "ubiRememberDeviceTicket3", "ubiRememberMeTicket3");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO3 = new UbiAccountStatsEntityDTO("ubiProfileId3", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO3 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO3, ubiAccountStatsEntityDTO3);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram3 = new UbiAccountEntryWithTelegram("chatId3", true, ubiAccountEntryEntityDTO3);
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO4 = new UbiAccountAuthorizationEntry("ubiAuthProfileId3", "email3",
                "encodedPassword3",
                "ubiSessionId3", "ubiSpaceId3", "ubiAuthTicket3", "ubiRememberDeviceTicket3", "ubiRememberMeTicket3");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO4 = new UbiAccountStatsEntityDTO("ubiProfileId3", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO4 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO4, ubiAccountStatsEntityDTO4);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram4 = new UbiAccountEntryWithTelegram("chatId4", true, ubiAccountEntryEntityDTO4);


        List<UbiAccountEntryWithTelegram> ubiAccountAuthorizationEntriesWithTelegram = List.of(
                ubiAccountEntryEntityDTOWithTelegram1,
                ubiAccountEntryEntityDTOWithTelegram2,
                ubiAccountEntryEntityDTOWithTelegram3);

        when(telegramUserUbiAccountEntryService.findAllFUbiAccountEntriesWithTelegram()).thenReturn(ubiAccountAuthorizationEntriesWithTelegram);

        UbiTrade finishedSellTradeValid = new UbiTrade();
        finishedSellTradeValid.setItem(new Item());
        finishedSellTradeValid.setCategory(TradeCategory.Sell);
        finishedSellTradeValid.setLastModifiedAt(LocalDateTime.now().minusHours(3));
        finishedSellTradeValid.setTradeId("finishedSellTradeValid");
        finishedSellTradeValid.setSuccessPaymentPrice(100);
        finishedSellTradeValid.setSuccessPaymentFee(10);

        UbiTrade finishedSellTradeInvalid = new UbiTrade();
        finishedSellTradeInvalid.setItem(new Item());
        finishedSellTradeInvalid.setCategory(TradeCategory.Sell);
        finishedSellTradeInvalid.setLastModifiedAt(LocalDateTime.now().minusHours(6));
        finishedSellTradeInvalid.setTradeId("finishedSellTradeInvalid");
        finishedSellTradeInvalid.setSuccessPaymentPrice(100);
        finishedSellTradeInvalid.setSuccessPaymentFee(10);

        when(graphQlClientService.fetchCreditAmountForUser(any())).thenReturn(0);
        when(graphQlClientService.fetchCurrentOrdersForUser(any())).thenReturn(List.of());

        when(graphQlClientService.fetchLastFinishedOrdersForUser(any())).thenReturn(List.of());
        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(List.of(finishedSellTradeValid));
        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram4))).thenReturn(List.of(finishedSellTradeInvalid));

        when(graphQlClientService.fetchTradesLimitationsForUser(any())).thenReturn(new UserTradesLimitations());
        when(graphQlClientService.fetchAllOwnedItemsIdsForUser(any())).thenReturn(List.of());

        when(commonValuesService.getLastUbiUsersStatsFetchTime()).thenReturn(LocalDateTime.now().minusHours(5));

        scheduledAllUbiUsersManager.fetchAllUbiUsersStatsAndManageTrades();

        verify(telegramBotService, times(1)).sendNotificationToUser(eq("chatId1"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId2"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId3"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId4"), anyString());
    }

    @Test
    public void fetchAllUbiUsersStatsAndManageTrades_should_notify_users_on_new_buys() {
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO1 = new UbiAccountAuthorizationEntry("ubiAuthProfileId1", "email1",
                "encodedPassword1",
                "ubiSessionId1", "ubiSpaceId1", "ubiAuthTicket1", "ubiRememberDeviceTicket1", "ubiRememberMeTicket1");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO1 = new UbiAccountStatsEntityDTO("ubiProfileId1", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO1 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO1, ubiAccountStatsEntityDTO1);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram1 = new UbiAccountEntryWithTelegram("chatId1", true, ubiAccountEntryEntityDTO1);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO2 = new UbiAccountAuthorizationEntry("ubiAuthProfileId2", "email2",
                "encodedPassword2",
                "ubiSessionId2", "ubiSpaceId2", "ubiAuthTicket2", "ubiRememberDeviceTicket2", "ubiRememberMeTicket2");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO2 = new UbiAccountStatsEntityDTO("ubiProfileId2", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO2 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO2, ubiAccountStatsEntityDTO2);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram2 = new UbiAccountEntryWithTelegram("chatId2", false, ubiAccountEntryEntityDTO2);

        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO3 = new UbiAccountAuthorizationEntry("ubiAuthProfileId3", "email3",
                "encodedPassword3",
                "ubiSessionId3", "ubiSpaceId3", "ubiAuthTicket3", "ubiRememberDeviceTicket3", "ubiRememberMeTicket3");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO3 = new UbiAccountStatsEntityDTO("ubiProfileId3", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO3 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO3, ubiAccountStatsEntityDTO3);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram3 = new UbiAccountEntryWithTelegram("chatId3", true, ubiAccountEntryEntityDTO3);
        UbiAccountAuthorizationEntry ubiAccountAuthorizationEntryEntityDTO4 = new UbiAccountAuthorizationEntry("ubiAuthProfileId3", "email3",
                "encodedPassword3",
                "ubiSessionId3", "ubiSpaceId3", "ubiAuthTicket3", "ubiRememberDeviceTicket3", "ubiRememberMeTicket3");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO4 = new UbiAccountStatsEntityDTO("ubiProfileId3", 0, List.of());
        UbiAccountEntry ubiAccountEntryEntityDTO4 = new UbiAccountEntry(ubiAccountAuthorizationEntryEntityDTO4, ubiAccountStatsEntityDTO4);
        UbiAccountEntryWithTelegram ubiAccountEntryEntityDTOWithTelegram4 = new UbiAccountEntryWithTelegram("chatId4", true, ubiAccountEntryEntityDTO4);


        List<UbiAccountEntryWithTelegram> ubiAccountAuthorizationEntriesWithTelegram = List.of(
                ubiAccountEntryEntityDTOWithTelegram1,
                ubiAccountEntryEntityDTOWithTelegram2,
                ubiAccountEntryEntityDTOWithTelegram3);

        when(telegramUserUbiAccountEntryService.findAllFUbiAccountEntriesWithTelegram()).thenReturn(ubiAccountAuthorizationEntriesWithTelegram);

        UbiTrade finishedSellTradeValid = new UbiTrade();
        finishedSellTradeValid.setItem(new Item());
        finishedSellTradeValid.setCategory(TradeCategory.Buy);
        finishedSellTradeValid.setLastModifiedAt(LocalDateTime.now().minusHours(3));
        finishedSellTradeValid.setTradeId("finishedSellTradeValid");
        finishedSellTradeValid.setSuccessPaymentPrice(100);

        UbiTrade finishedSellTradeInvalid = new UbiTrade();
        finishedSellTradeInvalid.setItem(new Item());
        finishedSellTradeInvalid.setCategory(TradeCategory.Buy);
        finishedSellTradeInvalid.setLastModifiedAt(LocalDateTime.now().minusHours(6));
        finishedSellTradeInvalid.setTradeId("finishedSellTradeInvalid");
        finishedSellTradeInvalid.setSuccessPaymentPrice(100);

        when(graphQlClientService.fetchCreditAmountForUser(any())).thenReturn(0);
        when(graphQlClientService.fetchCurrentOrdersForUser(any())).thenReturn(List.of());

        when(graphQlClientService.fetchLastFinishedOrdersForUser(any())).thenReturn(List.of());
        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram1))).thenReturn(List.of(finishedSellTradeValid));
        when(graphQlClientService.fetchLastFinishedOrdersForUser(new AuthorizationDTO(ubiAccountEntryEntityDTOWithTelegram4))).thenReturn(List.of(finishedSellTradeInvalid));

        when(graphQlClientService.fetchTradesLimitationsForUser(any())).thenReturn(new UserTradesLimitations());
        when(graphQlClientService.fetchAllOwnedItemsIdsForUser(any())).thenReturn(List.of());

        when(commonValuesService.getLastUbiUsersStatsFetchTime()).thenReturn(LocalDateTime.now().minusHours(5));

        scheduledAllUbiUsersManager.fetchAllUbiUsersStatsAndManageTrades();

        verify(telegramBotService, times(1)).sendNotificationToUser(eq("chatId1"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId2"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId3"), anyString());
        verify(telegramBotService, times(0)).sendNotificationToUser(eq("chatId4"), anyString());
    }
}