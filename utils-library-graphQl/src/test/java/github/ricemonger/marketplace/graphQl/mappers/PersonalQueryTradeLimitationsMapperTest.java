package github.ricemonger.marketplace.graphQl.mappers;

import github.ricemonger.marketplace.graphQl.GraphQlCommonValuesService;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.DTO.TradesLimitations;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.DTO.tradeLimitations.Buy;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.DTO.tradeLimitations.Sell;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.DTO.tradeLimitations.sell.ResaleLocks;
import github.ricemonger.marketplace.graphQl.personal_query_trades_limitations.PersonalQueryTradesLimitationsMapper;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.UserTradesLimitations;
import github.ricemonger.utils.DTOs.personal.UserTransactionsCount;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalLockedItemsMappingException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class PersonalQueryTradeLimitationsMapperTest {
    private final GraphQlCommonValuesService commonValuesService = mock(GraphQlCommonValuesService.class);

    private final PersonalQueryTradesLimitationsMapper personalQueryTradesLimitationsMapper = spy(new PersonalQueryTradesLimitationsMapper(commonValuesService));

    @Test
    public void mapLockedItems_should_map_each_item() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);
        LocalDateTime date2 = date.plusSeconds(1000);

        TradesLimitations tradesLimitations = new TradesLimitations();
        tradesLimitations.setBuy(new Buy());
        tradesLimitations.getBuy().setActiveTransactionCount(1);
        tradesLimitations.getBuy().setResolvedTransactionCount(2);
        tradesLimitations.setSell(new Sell());
        tradesLimitations.getSell().setActiveTransactionCount(3);
        tradesLimitations.getSell().setResolvedTransactionCount(4);
        tradesLimitations.getSell().setResaleLocks(new ArrayList<>());
        tradesLimitations.getSell().getResaleLocks().add(new ResaleLocks("1", dtf.format(date)));
        tradesLimitations.getSell().getResaleLocks().add(new ResaleLocks("2", dtf.format(date2)));

        ItemResaleLock expected1 = new ItemResaleLock("1", date);
        ItemResaleLock expected2 = new ItemResaleLock("2", date2);

        UserTradesLimitations userTradesLimitations = personalQueryTradesLimitationsMapper.mapTradesLimitationsForUser(tradesLimitations, "ubiProfileId");

        assertEquals("ubiProfileId", userTradesLimitations.getUbiProfileId());
        assertEquals(1, userTradesLimitations.getActiveBuyTransactionCount());
        assertEquals(2, userTradesLimitations.getResolvedBuyTransactionCount());
        assertEquals(3, userTradesLimitations.getActiveSellTransactionCount());
        assertEquals(4, userTradesLimitations.getResolvedSellTransactionCount());
        assertTrue(userTradesLimitations.getResaleLocks().contains(expected1) && userTradesLimitations.getResaleLocks().contains(expected2));
        verify(personalQueryTradesLimitationsMapper, times(2)).mapLockedItem(any());
    }

    @Test
    public void mapLockedItems_should_throw_exception_when_tradeLimitations_is_null() {
        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapTradesLimitationsForUser(null, "ubiProfileId");
        });
    }

    @Test
    public void mapLockedItems_should_throw_exception_when_sell_is_null() {
        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapTradesLimitationsForUser(new TradesLimitations(), "ubiProfileId");
        });
    }

    @Test
    public void mapLockedItems_should_throw_exception_when_resaleLocks_is_null() {
        TradesLimitations tradesLimitations = new TradesLimitations();
        tradesLimitations.setSell(new Sell());

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapTradesLimitationsForUser(tradesLimitations, "ubiProfileId");
        });
    }

    @Test
    public void mapLockedItem_should_map_item_with_valid_fields() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(commonValuesService.getDateFormat());
        LocalDateTime date = LocalDateTime.now().withNano(0);

        ResaleLocks resaleLocks = new ResaleLocks("1", dtf.format(date));

        ItemResaleLock expected = new ItemResaleLock("1", date);

        assertEquals(expected, personalQueryTradesLimitationsMapper.mapLockedItem(resaleLocks));
    }

    @Test
    public void mapLockedItem_should_map_item_with_invalid_expiresAt() {
        when(commonValuesService.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss");

        ResaleLocks resaleLocks = new ResaleLocks("1", "invalid date");

        ItemResaleLock expected = new ItemResaleLock("1", LocalDateTime.MIN);

        assertEquals(expected, personalQueryTradesLimitationsMapper.mapLockedItem(resaleLocks));
    }

    @Test
    public void mapLockedItem_should_throw_exception_when_resaleLocks_is_null() {
        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapLockedItem(null);
        });
    }

    @Test
    public void mapLockedItem_should_throw_exception_when_itemId_is_null() {
        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapLockedItem(new ResaleLocks(null, "2021-01-01"));
        });
    }

    @Test
    public void mapLockedItem_should_throw_exception_when_expiresAt_is_null() {
        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapLockedItem(new ResaleLocks("1", null));
        });
    }

    @Test
    public void mapUserTransactionsCount_should_map_user_transactions_count() {
        TradesLimitations tradesLimitations = createTradeLimitations();

        UserTransactionsCount expected = new UserTransactionsCount(1, 2, 3, 4);

        assertEquals(expected, personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations));
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_tradeLimitations_is_null() {
        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(null);
        });
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_buy_is_null() {
        TradesLimitations tradesLimitations = createTradeLimitations();
        tradesLimitations.setBuy(null);

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations);
        });
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_sell_is_null() {
        TradesLimitations tradesLimitations = createTradeLimitations();
        tradesLimitations.setSell(null);

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations);
        });
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_buy_activeTransactionCount_is_null() {
        TradesLimitations tradesLimitations = createTradeLimitations();
        tradesLimitations.getBuy().setActiveTransactionCount(null);

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations);
        });
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_buy_resolvedTransactionCount_is_null() {
        TradesLimitations tradesLimitations = createTradeLimitations();
        tradesLimitations.getBuy().setResolvedTransactionCount(null);

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations);
        });
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_sell_activeTransactionCount_is_null() {
        TradesLimitations tradesLimitations = createTradeLimitations();
        tradesLimitations.getSell().setActiveTransactionCount(null);

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations);
        });
    }

    @Test
    public void mapUserTransactionsCount_should_throw_exception_when_sell_resolvedTransactionCount_is_null() {
        TradesLimitations tradesLimitations = createTradeLimitations();
        tradesLimitations.getSell().setResolvedTransactionCount(null);

        assertThrows(GraphQlPersonalLockedItemsMappingException.class, () -> {
            personalQueryTradesLimitationsMapper.mapUserTransactionsCount(tradesLimitations);
        });
    }

    private TradesLimitations createTradeLimitations() {
        TradesLimitations tradesLimitations = new TradesLimitations();
        tradesLimitations.setBuy(new Buy());
        tradesLimitations.setSell(new Sell());
        tradesLimitations.getBuy().setResolvedTransactionCount(1);
        tradesLimitations.getBuy().setActiveTransactionCount(2);
        tradesLimitations.getSell().setResaleLocks(new ArrayList<>());
        tradesLimitations.getSell().setResolvedTransactionCount(3);
        tradesLimitations.getSell().setActiveTransactionCount(4);
        return tradesLimitations;
    }
}