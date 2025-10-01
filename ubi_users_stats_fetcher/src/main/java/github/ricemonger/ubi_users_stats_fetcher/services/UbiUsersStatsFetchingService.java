package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.marketplace.graphQl.personal_query_user_stats.PersonalQueryUserStatsGraphQlClientService;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UbiAccountStats;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UserAuthorizedUbiAccount;
import github.ricemonger.utils.DTOs.personal.*;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UbiUsersStatsFetchingService {

    private final UbiAccountService ubiAccountService;

    private final NotificationService notificationService;

    private final PersonalQueryUserStatsGraphQlClientService personalQueryUserStatsGraphQlClientService;

    private final CommonValuesService commonValuesService;

    private final TradeService tradeService;

    public void fetchAllAuthorizedUbiUsersStats() {
        List<UserAuthorizedUbiAccount> userAuthorizedUbiAccounts = ubiAccountService.findAllUsersUbiAccountEntries();

        List<UbiAccountStats> updatedUbiAccountsStats = new ArrayList<>();

        for (UserAuthorizedUbiAccount userAuthorizedUbiAccount : userAuthorizedUbiAccounts) {
            try {
                updatedUbiAccountsStats.add(fetchAndGetUserPersonalStats(userAuthorizedUbiAccount));
            } catch (Exception e) {
                log.error("Error while fetching user stats for user : {}", userAuthorizedUbiAccount, e);
            }
        }

        commonValuesService.setLastUbiUsersStatsFetchTime(LocalDateTime.now().withNano(0).plusSeconds(10));

        ubiAccountService.saveAllUbiAccountStats(updatedUbiAccountsStats);
    }

    private UbiAccountStats fetchAndGetUserPersonalStats(UserAuthorizedUbiAccount userAuthorizedUbiAccount) {
        AuthorizationDTO authorizationDTO = userAuthorizedUbiAccount.toAuthorizationDTO();

        UbiUserStats ubiUserStats = personalQueryUserStatsGraphQlClientService.fetchUbiUserStatsForUser(authorizationDTO, userAuthorizedUbiAccount.getOwnedItemsIdsCount());

        int creditAmount = ubiUserStats.getCreditAmount();
        List<UbiTrade> currentOrders = ubiUserStats.getCurrentOrders();
        List<UbiTrade> finishedOrders = ubiUserStats.getFinishedOrders();
        UserTradesLimitations userTradesLimitations = ubiUserStats.getUserTradesLimitations();
        List<String> ownedItemsIds = ubiUserStats.getOwnedItemsIds();
        List<ItemResaleLock> itemResaleLocks = userTradesLimitations.getResaleLocks();

        LocalDateTime lastDayPeriod = LocalDateTime.now().minusDays(1).withNano(0);

        List<UbiTrade> soldIn24h = finishedOrders.stream()
                .filter(order -> order.getState() == TradeState.Succeeded && order.getCategory().equals(TradeCategory.Sell) && order.getLastModifiedAt().isAfter(lastDayPeriod)).toList();

        List<UbiTrade> boughtIn24h = finishedOrders.stream()
                .filter(order -> order.getState() == TradeState.Succeeded && order.getCategory().equals(TradeCategory.Buy) && order.getLastModifiedAt().isAfter(lastDayPeriod)).toList();

        List<UbiTrade> currentSellTrades = currentOrders.stream().filter(order -> order.getCategory().equals(TradeCategory.Sell)).toList();
        List<UbiTrade> currentBuyTrades = currentOrders.stream().filter(order -> order.getCategory().equals(TradeCategory.Buy)).toList();

        List<Trade> calculatedCurrentSellTrades = tradeService.calculateTradeStatsForUbiTrades(currentSellTrades);
        List<Trade> calculatedCurrentBuyTrades = tradeService.calculateTradeStatsForUbiTrades(currentBuyTrades);

        notifyUser(userAuthorizedUbiAccount, creditAmount, soldIn24h, boughtIn24h);

        return new UbiAccountStats(
                userAuthorizedUbiAccount.getProfileId(),
                creditAmount,
                userTradesLimitations.getResolvedSellTransactionCount(),
                userTradesLimitations.getResolvedBuyTransactionCount(),
                ownedItemsIds,
                itemResaleLocks,
                calculatedCurrentSellTrades,
                calculatedCurrentBuyTrades);
    }

    private void notifyUser(UserAuthorizedUbiAccount userAuthorizedUbiAccount, int creditAmount, List<UbiTrade> soldIn24h, List<UbiTrade> boughtIn24h) {

        boolean creditsChanged = userAuthorizedUbiAccount.getCreditAmount() != null && userAuthorizedUbiAccount.getCreditAmount() != creditAmount;
        boolean soldIn24hChanged = soldIn24h.stream().anyMatch(trade -> trade.getLastModifiedAt().isAfter(commonValuesService.getLastUbiUsersStatsFetchTime()));
        boolean boughtIn24hChanged = boughtIn24h.stream().anyMatch(trade -> trade.getLastModifiedAt().isAfter(commonValuesService.getLastUbiUsersStatsFetchTime()));

        if (creditsChanged || soldIn24hChanged || boughtIn24hChanged) {
            StringBuilder message = new StringBuilder();

            if (creditsChanged) {
                message.append("Your credit amount has changed. Old: ").append(userAuthorizedUbiAccount.getCreditAmount()).append(" New : ").append(creditAmount).append("\n");
            }
            if (soldIn24hChanged) {
                message.append("You have additionally sold these items in the last 24 hours: \n");
                for (UbiTrade trade : soldIn24h.stream().filter(trade -> trade.getLastModifiedAt().isAfter(commonValuesService.getLastUbiUsersStatsFetchTime())).toList()) {
                    message.append(getFinishedTradeString(trade)).append("\n");
                }
            }
            if (boughtIn24hChanged) {
                message.append("You have additionally bought these items in the last 24 hours: \n");
                for (UbiTrade trade : boughtIn24h.stream().filter(trade -> trade.getLastModifiedAt().isAfter(commonValuesService.getLastUbiUsersStatsFetchTime())).toList()) {
                    message.append(getFinishedTradeString(trade)).append("\n");
                }
            }

            notificationService.sendUbiStatsUpdatedNotification(userAuthorizedUbiAccount.getUserId(), message.toString());
        }
    }

    private String getFinishedTradeString(UbiTrade trade) {
        return trade.getItem().getName() + " for " + trade.getSuccessPaymentPrice();
    }
}
