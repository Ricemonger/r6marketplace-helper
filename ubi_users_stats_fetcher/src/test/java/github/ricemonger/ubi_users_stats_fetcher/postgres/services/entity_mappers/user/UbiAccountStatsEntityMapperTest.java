package github.ricemonger.ubi_users_stats_fetcher.postgres.services.entity_mappers.user;


import github.ricemonger.ubi_users_stats_fetcher.postgres.repositories.CustomItemPostgresRepository;
import github.ricemonger.ubi_users_stats_fetcher.postgres.repositories.UbiAccountStatsPostgresRepository;
import github.ricemonger.ubi_users_stats_fetcher.services.DTOs.UbiAccountStats;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.DTOs.personal.Trade;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.ItemResaleLockEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.TradeEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountStatsEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class UbiAccountStatsEntityMapperTest {
    @Autowired
    private UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;
    @MockBean
    private CustomItemPostgresRepository itemIdPostgresRepository;
    @MockBean
    private TradeEntityMapper TradeEntityMapper;
    @MockBean
    private UbiAccountStatsPostgresRepository ubiAccountStatsPostgresRepository;

    @Test
    public void createEntities_should_return_expected_result_if_user_doesnt_exist() {
        List<String> existingItems = List.of("itemId1", "itemId2");
        when(itemIdPostgresRepository.findAllItemIds()).thenReturn(existingItems);
        when(itemIdPostgresRepository.getReferenceById("itemId1")).thenReturn(new ItemEntity("itemId1"));
        when(itemIdPostgresRepository.getReferenceById("itemId2")).thenReturn(new ItemEntity("itemId2"));

        when(ubiAccountStatsPostgresRepository.existsById("ubiProfileId1")).thenReturn(false);
        when(ubiAccountStatsPostgresRepository.existsById("ubiProfileId2")).thenReturn(false);

        Trade currentSellTrade11 = Mockito.mock(Trade.class);
        Trade currentSellTrade12 = Mockito.mock(Trade.class);

        Trade currentBuyTrade11 = Mockito.mock(Trade.class);

        TradeEntity currentSellTradeEntity11 = new TradeEntity();
        currentSellTradeEntity11.setTradeId("currentSellTradeEntity11");

        TradeEntity currentSellTradeEntity12 = new TradeEntity();
        currentSellTradeEntity12.setTradeId("currentSellTradeEntity12");

        TradeEntity currentBuyTradeEntity11 = new TradeEntity();
        currentBuyTradeEntity11.setTradeId("currentBuyTradeEntity11");

        when(TradeEntityMapper.createEntity(currentSellTrade11, existingItems)).thenReturn(currentSellTradeEntity11);
        when(TradeEntityMapper.createEntity(currentSellTrade12, existingItems)).thenReturn(currentSellTradeEntity12);
        when(TradeEntityMapper.createEntity(currentBuyTrade11, existingItems)).thenReturn(currentBuyTradeEntity11);

        UbiAccountStats dto1 = new UbiAccountStats();
        dto1.setUbiProfileId("ubiProfileId1");
        dto1.setCreditAmount(100);
        dto1.setSoldIn24h(11);
        dto1.setBoughtIn24h(12);
        dto1.setOwnedItemsIds(List.of("itemId1", "itemId2", "itemId3"));
        ItemResaleLock resaleLock11 = new ItemResaleLock("itemId3", LocalDateTime.of(2021, 1, 1, 0, 0));
        ItemResaleLock resaleLock12 = new ItemResaleLock("itemId4", LocalDateTime.of(2021, 2, 1, 0, 0));
        dto1.setResaleLocks(List.of(resaleLock11, resaleLock12));
        dto1.setCurrentSellTrades(List.of(currentSellTrade11, currentSellTrade12));
        dto1.setCurrentBuyTrades(List.of(currentBuyTrade11));

        UbiAccountStatsEntity entity1 = new UbiAccountStatsEntity();
        entity1.setUbiProfileId("ubiProfileId1");
        entity1.setCreditAmount(100);
        entity1.setSoldIn24h(11);
        entity1.setBoughtIn24h(12);
        entity1.setOwnedItems(List.of(new ItemEntity("itemId1"), new ItemEntity("itemId2")));
        entity1.setResaleLocks(List.of());
        entity1.setCurrentSellTrades(List.of(currentSellTradeEntity11, currentSellTradeEntity12));
        entity1.setCurrentBuyTrades(List.of(currentBuyTradeEntity11));

        Trade currentSellTrade21 = Mockito.mock(Trade.class);

        Trade currentBuyTrade21 = Mockito.mock(Trade.class);
        Trade currentBuyTrade22 = Mockito.mock(Trade.class);

        TradeEntity currentSellTradeEntity21 = new TradeEntity();
        currentSellTradeEntity21.setTradeId("currentSellTradeEntity21");

        TradeEntity currentBuyTradeEntity21 = new TradeEntity();
        currentBuyTradeEntity21.setTradeId("currentBuyTradeEntity21");

        TradeEntity currentBuyTradeEntity22 = new TradeEntity();
        currentBuyTradeEntity22.setTradeId("currentBuyTradeEntity22");

        when(TradeEntityMapper.createEntity(currentSellTrade21, existingItems)).thenReturn(currentSellTradeEntity21);
        when(TradeEntityMapper.createEntity(currentBuyTrade21, existingItems)).thenReturn(currentBuyTradeEntity21);
        when(TradeEntityMapper.createEntity(currentBuyTrade22, existingItems)).thenReturn(currentBuyTradeEntity22);

        UbiAccountStats dto2 = new UbiAccountStats();
        dto2.setUbiProfileId("ubiProfileId2");
        dto2.setCreditAmount(200);
        dto2.setSoldIn24h(21);
        dto2.setBoughtIn24h(22);
        dto2.setOwnedItemsIds(List.of("itemId3", "itemId4"));
        ItemResaleLock resaleLock21 = new ItemResaleLock("itemId1", LocalDateTime.of(2022, 1, 1, 0, 0));
        ItemResaleLock resaleLock22 = new ItemResaleLock("itemId2", LocalDateTime.of(2022, 2, 1, 0, 0));
        dto2.setResaleLocks(List.of(resaleLock21, resaleLock22));
        dto2.setCurrentSellTrades(List.of(currentSellTrade21));
        dto2.setCurrentBuyTrades(List.of(currentBuyTrade21, currentBuyTrade22));

        UbiAccountStatsEntity entity2 = new UbiAccountStatsEntity();
        entity2.setUbiProfileId("ubiProfileId2");
        entity2.setCreditAmount(200);
        entity2.setSoldIn24h(21);
        entity2.setBoughtIn24h(22);
        ItemResaleLockEntity resaleLockEntity21 = new ItemResaleLockEntity(entity2, new ItemEntity("itemId1"), LocalDateTime.of(2022, 1, 1, 0, 0));
        ItemResaleLockEntity resaleLockEntity22 = new ItemResaleLockEntity(entity2, new ItemEntity("itemId2"), LocalDateTime.of(2022, 2, 1, 0, 0));
        entity2.setOwnedItems(List.of());
        entity2.setResaleLocks(List.of(resaleLockEntity21, resaleLockEntity22));
        entity2.setCurrentSellTrades(List.of(currentSellTradeEntity21));
        entity2.setCurrentBuyTrades(List.of(currentBuyTradeEntity21, currentBuyTradeEntity22));

        List<UbiAccountStats> ubiAccounts = List.of(dto1, dto2);

        List<UbiAccountStatsEntity> result = ubiAccountStatsEntityMapper.createEntities(ubiAccounts);

        System.out.println("Expected: ");
        System.out.println(entity1);
        System.out.println(entity2);

        System.out.println("Result: ");
        result.forEach(System.out::println);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(entity -> entity.isFullyEqual(entity1)));
        assertTrue(result.stream().anyMatch(entity -> entity.isFullyEqual(entity2)));
    }

    @Test
    public void createEntities_should_return_expected_result_if_user_already_exists() {
        List<String> existingItems = List.of("itemId1", "itemId2");
        when(itemIdPostgresRepository.findAllItemIds()).thenReturn(existingItems);
        when(itemIdPostgresRepository.getReferenceById("itemId1")).thenReturn(new ItemEntity("itemId1"));
        when(itemIdPostgresRepository.getReferenceById("itemId2")).thenReturn(new ItemEntity("itemId2"));

        when(ubiAccountStatsPostgresRepository.existsById("ubiProfileId11")).thenReturn(true);
        when(ubiAccountStatsPostgresRepository.existsById("ubiProfileId22")).thenReturn(true);

        List<ItemEntity> itemIdSpy1 = spy(new ArrayList<>());
        List<ItemResaleLockEntity> resaleLockSpy1 = spy(new ArrayList<>());
        List<TradeEntity> currentSellTradeSpy1 = spy(new ArrayList<>());
        List<TradeEntity> currentBuyTradeSpy1 = spy(new ArrayList<>());

        UbiAccountStatsEntity existingEntity1 = new UbiAccountStatsEntity();
        existingEntity1.setUbiProfileId("ubiProfileId1");
        existingEntity1.setOwnedItems(itemIdSpy1);
        existingEntity1.setResaleLocks(resaleLockSpy1);
        existingEntity1.setCurrentSellTrades(currentSellTradeSpy1);
        existingEntity1.setCurrentBuyTrades(currentBuyTradeSpy1);

        List<ItemEntity> itemIdSpy2 = spy(new ArrayList<>());
        List<ItemResaleLockEntity> resaleLockSpy2 = spy(new ArrayList<>());
        List<TradeEntity> currentSellTradeSpy2 = spy(new ArrayList<>());
        List<TradeEntity> currentBuyTradeSpy2 = spy(new ArrayList<>());

        UbiAccountStatsEntity existingEntity2 = new UbiAccountStatsEntity();
        existingEntity2.setUbiProfileId("ubiProfileId2");
        existingEntity2.setOwnedItems(itemIdSpy2);
        existingEntity2.setResaleLocks(resaleLockSpy2);
        existingEntity2.setCurrentSellTrades(currentSellTradeSpy2);
        existingEntity2.setCurrentBuyTrades(currentBuyTradeSpy2);

        when(ubiAccountStatsPostgresRepository.findById("ubiProfileId11")).thenReturn(Optional.of(existingEntity1));
        when(ubiAccountStatsPostgresRepository.findById("ubiProfileId22")).thenReturn(Optional.of(existingEntity2));

        Trade currentSellTrade11 = Mockito.mock(Trade.class);
        Trade currentSellTrade12 = Mockito.mock(Trade.class);

        Trade currentBuyTrade11 = Mockito.mock(Trade.class);

        TradeEntity currentSellTradeEntity11 = new TradeEntity();
        currentSellTradeEntity11.setTradeId("currentSellTradeEntity11");

        TradeEntity currentSellTradeEntity12 = new TradeEntity();
        currentSellTradeEntity12.setTradeId("currentSellTradeEntity12");

        TradeEntity currentBuyTradeEntity11 = new TradeEntity();
        currentBuyTradeEntity11.setTradeId("currentBuyTradeEntity11");

        when(TradeEntityMapper.createEntity(currentSellTrade11, existingItems)).thenReturn(currentSellTradeEntity11);
        when(TradeEntityMapper.createEntity(currentSellTrade12, existingItems)).thenReturn(currentSellTradeEntity12);
        when(TradeEntityMapper.createEntity(currentBuyTrade11, existingItems)).thenReturn(currentBuyTradeEntity11);

        UbiAccountStats dto1 = new UbiAccountStats();
        dto1.setUbiProfileId("ubiProfileId11");
        dto1.setCreditAmount(100);
        dto1.setSoldIn24h(11);
        dto1.setBoughtIn24h(12);
        dto1.setOwnedItemsIds(List.of("itemId1", "itemId2", "itemId3"));
        ItemResaleLock resaleLock11 = new ItemResaleLock("itemId3", LocalDateTime.of(2021, 1, 1, 0, 0));
        ItemResaleLock resaleLock12 = new ItemResaleLock("itemId4", LocalDateTime.of(2021, 2, 1, 0, 0));
        dto1.setResaleLocks(List.of(resaleLock11, resaleLock12));
        dto1.setCurrentSellTrades(List.of(currentSellTrade11, currentSellTrade12));
        dto1.setCurrentBuyTrades(List.of(currentBuyTrade11));

        UbiAccountStatsEntity entity1 = new UbiAccountStatsEntity();
        entity1.setUbiProfileId("ubiProfileId1");
        entity1.setCreditAmount(100);
        entity1.setSoldIn24h(11);
        entity1.setBoughtIn24h(12);
        entity1.setOwnedItems(List.of(new ItemEntity("itemId1"), new ItemEntity("itemId2")));
        entity1.setResaleLocks(List.of());
        entity1.setCurrentSellTrades(List.of(currentSellTradeEntity11, currentSellTradeEntity12));
        entity1.setCurrentBuyTrades(List.of(currentBuyTradeEntity11));

        Trade currentSellTrade21 = Mockito.mock(Trade.class);

        Trade currentBuyTrade21 = Mockito.mock(Trade.class);
        Trade currentBuyTrade22 = Mockito.mock(Trade.class);

        TradeEntity currentSellTradeEntity21 = new TradeEntity();
        currentSellTradeEntity21.setTradeId("currentSellTradeEntity21");

        TradeEntity currentBuyTradeEntity21 = new TradeEntity();
        currentBuyTradeEntity21.setTradeId("currentBuyTradeEntity21");

        TradeEntity currentBuyTradeEntity22 = new TradeEntity();
        currentBuyTradeEntity22.setTradeId("currentBuyTradeEntity22");

        when(TradeEntityMapper.createEntity(currentSellTrade21, existingItems)).thenReturn(currentSellTradeEntity21);
        when(TradeEntityMapper.createEntity(currentBuyTrade21, existingItems)).thenReturn(currentBuyTradeEntity21);
        when(TradeEntityMapper.createEntity(currentBuyTrade22, existingItems)).thenReturn(currentBuyTradeEntity22);

        UbiAccountStats dto2 = new UbiAccountStats();
        dto2.setUbiProfileId("ubiProfileId22");
        dto2.setCreditAmount(200);
        dto2.setSoldIn24h(21);
        dto2.setBoughtIn24h(22);
        dto2.setOwnedItemsIds(List.of("itemId3", "itemId4"));
        ItemResaleLock resaleLock21 = new ItemResaleLock("itemId1", LocalDateTime.of(2022, 1, 1, 0, 0));
        ItemResaleLock resaleLock22 = new ItemResaleLock("itemId2", LocalDateTime.of(2022, 2, 1, 0, 0));
        dto2.setResaleLocks(List.of(resaleLock21, resaleLock22));
        dto2.setCurrentSellTrades(List.of(currentSellTrade21));
        dto2.setCurrentBuyTrades(List.of(currentBuyTrade21, currentBuyTrade22));

        UbiAccountStatsEntity entity2 = new UbiAccountStatsEntity();
        entity2.setUbiProfileId("ubiProfileId2");
        entity2.setCreditAmount(200);
        entity2.setSoldIn24h(21);
        entity2.setBoughtIn24h(22);
        ItemResaleLockEntity resaleLockEntity21 = new ItemResaleLockEntity(entity2, new ItemEntity("itemId1"), LocalDateTime.of(2022, 1, 1, 0, 0));
        ItemResaleLockEntity resaleLockEntity22 = new ItemResaleLockEntity(entity2, new ItemEntity("itemId2"), LocalDateTime.of(2022, 2, 1, 0, 0));
        entity2.setOwnedItems(List.of());
        entity2.setResaleLocks(List.of(resaleLockEntity21, resaleLockEntity22));
        entity2.setCurrentSellTrades(List.of(currentSellTradeEntity21));
        entity2.setCurrentBuyTrades(List.of(currentBuyTradeEntity21, currentBuyTradeEntity22));

        List<UbiAccountStats> ubiAccounts = List.of(dto1, dto2);

        List<UbiAccountStatsEntity> result = ubiAccountStatsEntityMapper.createEntities(ubiAccounts);

        System.out.println("Expected: ");
        System.out.println(entity1);
        System.out.println(entity2);

        System.out.println("Result: ");
        result.forEach(System.out::println);

        assertEquals(2, result.size());

        verify(itemIdSpy1).clear();
        verify(resaleLockSpy1).clear();
        verify(currentSellTradeSpy1).clear();
        verify(currentBuyTradeSpy1).clear();

        verify(itemIdSpy2).clear();
        verify(resaleLockSpy2).clear();
        verify(currentSellTradeSpy2).clear();
        verify(currentBuyTradeSpy2).clear();

        assertTrue(result.stream().anyMatch(entity -> entity.isFullyEqual(entity1)));
        assertTrue(result.stream().anyMatch(entity -> entity.isFullyEqual(entity2)));
    }
}