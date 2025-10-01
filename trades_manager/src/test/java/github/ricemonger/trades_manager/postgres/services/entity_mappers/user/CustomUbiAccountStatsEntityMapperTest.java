package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomItemResaleLockEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomTradeEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomUbiAccountStatsEntity;
import github.ricemonger.trades_manager.services.DTOs.Trade;
import github.ricemonger.trades_manager.services.DTOs.UbiAccountStats;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CustomUbiAccountStatsEntityMapperTest {
    @Autowired
    private UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;
    @MockBean
    private ItemResaleLockEntityMapper itemResaleLockEntityMapper;
    @MockBean
    private TradeEntityMapper tradeEntityMapper;

    @Test
    public void createDTO_should_return_expected_result() {
        CustomItemResaleLockEntity resaleLockEntity1 = Mockito.mock(CustomItemResaleLockEntity.class);
        CustomItemResaleLockEntity resaleLockEntity2 = Mockito.mock(CustomItemResaleLockEntity.class);

        ItemResaleLock resaleLock1 = Mockito.mock(ItemResaleLock.class);
        ItemResaleLock resaleLock2 = Mockito.mock(ItemResaleLock.class);

        Mockito.when(itemResaleLockEntityMapper.createDTO(resaleLockEntity1)).thenReturn(resaleLock1);
        Mockito.when(itemResaleLockEntityMapper.createDTO(resaleLockEntity2)).thenReturn(resaleLock2);

        CustomTradeEntity buyTradeEntity1 = Mockito.mock(CustomTradeEntity.class);
        CustomTradeEntity buyTradeEntity2 = Mockito.mock(CustomTradeEntity.class);

        Trade buyTrade1 = Mockito.mock(Trade.class);
        Trade buyTrade2 = Mockito.mock(Trade.class);

        Mockito.when(tradeEntityMapper.createDTO(buyTradeEntity1)).thenReturn(buyTrade1);
        Mockito.when(tradeEntityMapper.createDTO(buyTradeEntity2)).thenReturn(buyTrade2);

        CustomTradeEntity sellTradeEntity1 = Mockito.mock(CustomTradeEntity.class);
        CustomTradeEntity sellTradeEntity2 = Mockito.mock(CustomTradeEntity.class);

        Trade sellTrade1 = Mockito.mock(Trade.class);
        Trade sellTrade2 = Mockito.mock(Trade.class);

        Mockito.when(tradeEntityMapper.createDTO(sellTradeEntity1)).thenReturn(sellTrade1);
        Mockito.when(tradeEntityMapper.createDTO(sellTradeEntity2)).thenReturn(sellTrade2);

        CustomUbiAccountStatsEntity entity = new CustomUbiAccountStatsEntity();
        entity.setUbiProfileId("ubiProfileId");
        entity.setSoldIn24h(1);
        entity.setBoughtIn24h(2);
        entity.setCreditAmount(3);
        entity.setOwnedItems(List.of(new CustomItemIdEntity("itemId1"), new CustomItemIdEntity("itemId2")));
        entity.setResaleLocks(List.of(resaleLockEntity1, resaleLockEntity2));
        entity.setCurrentSellTrades(List.of(sellTradeEntity1, sellTradeEntity2));
        entity.setCurrentBuyTrades(List.of(buyTradeEntity1, buyTradeEntity2));

        UbiAccountStats result = ubiAccountStatsEntityMapper.createDTO(entity);

        assertEquals("ubiProfileId", result.getUbiProfileId());
        assertEquals(1, result.getSoldIn24h());
        assertEquals(2, result.getBoughtIn24h());
        assertEquals(3, result.getCreditAmount());

        assertEquals(2, result.getOwnedItemsIds().size());
        assertTrue(result.getOwnedItemsIds().contains("itemId1"));
        assertTrue(result.getOwnedItemsIds().contains("itemId2"));

        assertEquals(2, result.getResaleLocks().size());
        assertTrue(result.getResaleLocks().stream().anyMatch(lock -> lock == resaleLock1));
        assertTrue(result.getResaleLocks().stream().anyMatch(lock -> lock == resaleLock2));

        assertEquals(2, result.getCurrentSellTrades().size());
        assertTrue(result.getCurrentSellTrades().stream().anyMatch(trade -> trade == sellTrade1));
        assertTrue(result.getCurrentSellTrades().stream().anyMatch(trade -> trade == sellTrade2));

        assertEquals(2, result.getCurrentBuyTrades().size());
        assertTrue(result.getCurrentBuyTrades().stream().anyMatch(trade -> trade == buyTrade1));
        assertTrue(result.getCurrentBuyTrades().stream().anyMatch(trade -> trade == buyTrade2));

    }
}