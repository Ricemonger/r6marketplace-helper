package github.ricemonger.marketplace.graphQl.personal_query_trades_limitations;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.DTO.TradesLimitations;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.DTO.tradeLimitations.sell.ResaleLocks;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.UserTradesLimitations;
import github.ricemonger.utils.DTOs.personal.UserTransactionsCount;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalLockedItemsMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@RequiredArgsConstructor
public class PersonalQueryTradesLimitationsMapper {

    private final GraphQlCommonValuesService graphQlCommonValuesService;

    public UserTradesLimitations mapTradesLimitationsForUser(TradesLimitations tradesLimitations, String ubiProfileId) throws GraphQlPersonalLockedItemsMappingException {
        if (tradesLimitations == null) {
            throw new GraphQlPersonalLockedItemsMappingException("Trade limitations is null");
        }

        UserTradesLimitations userTradesLimitations = new UserTradesLimitations();
        userTradesLimitations.setUbiProfileId(ubiProfileId);

        if (tradesLimitations.getSell() == null || tradesLimitations.getSell().getResaleLocks() == null) {
            throw new GraphQlPersonalLockedItemsMappingException("Sell or resale locks is null in trade limitations-" + tradesLimitations);
        } else {
            userTradesLimitations.setResaleLocks(tradesLimitations.getSell().getResaleLocks().stream()
                    .map(this::mapLockedItem)
                    .toList());
        }

        UserTransactionsCount userTransactionsCount = mapUserTransactionsCount(tradesLimitations);
        userTradesLimitations.setUserTransactionsCount(userTransactionsCount);

        return userTradesLimitations;
    }

    public ItemResaleLock mapLockedItem(ResaleLocks resaleLocks) throws GraphQlPersonalLockedItemsMappingException {
        if (resaleLocks == null || resaleLocks.getItemId() == null || resaleLocks.getExpiresAt() == null) {
            throw new GraphQlPersonalLockedItemsMappingException("Resale locks or one of it's fields is null:" + resaleLocks);
        }
        ItemResaleLock result = new ItemResaleLock();
        result.setItemId(resaleLocks.getItemId());
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(graphQlCommonValuesService.getDateFormat());
            result.setExpiresAt(LocalDateTime.parse(resaleLocks.getExpiresAt(), dtf));

        } catch (DateTimeParseException e) {
            result.setExpiresAt(LocalDateTime.MIN);
            log.error("Error parsing date: {}", e.getMessage());
        }

        return result;
    }

    public UserTransactionsCount mapUserTransactionsCount(TradesLimitations tradesLimitations) throws GraphQlPersonalLockedItemsMappingException {
        if (tradesLimitations == null
            || tradesLimitations.getBuy() == null
            || tradesLimitations.getSell() == null
            || tradesLimitations.getBuy().getActiveTransactionCount() == null || tradesLimitations.getBuy().getResolvedTransactionCount() == null
            || tradesLimitations.getSell().getActiveTransactionCount() == null || tradesLimitations.getSell().getResolvedTransactionCount() == null) {
            throw new GraphQlPersonalLockedItemsMappingException("Trade limitation or one of its fields is null:" + tradesLimitations);
        }

        UserTransactionsCount result = new UserTransactionsCount();
        result.setBuyResolvedTransactionCount(tradesLimitations.getBuy().getResolvedTransactionCount());
        result.setBuyActiveTransactionCount(tradesLimitations.getBuy().getActiveTransactionCount());
        result.setSellResolvedTransactionCount(tradesLimitations.getSell().getResolvedTransactionCount());
        result.setSellActiveTransactionCount(tradesLimitations.getSell().getActiveTransactionCount());

        return result;
    }
}
