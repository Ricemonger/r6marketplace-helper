package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.services.DTOs.ManageableUser;
import github.ricemonger.trades_manager.services.DTOs.TradeByFiltersManager;
import github.ricemonger.trades_manager.services.DTOs.UbiAccountStats;
import github.ricemonger.trades_manager.services.configurations.ItemTradePriorityByExpressionCalculatorConfiguration;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utilspostgresschema.full_entities.user.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ManageableUserFactoryTest {
    @Autowired
    private ManageableUserFactory manageableUserFactory;
    @MockBean
    private TradeByFiltersManagerEntityMapper tradeByFiltersManagerEntityMapper;
    @MockBean
    private TradeByItemIdManagerEntityMapper tradeByItemIdManagerEntityMapper;
    @MockBean
    private UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;
    @MockBean
    private ItemTradePriorityByExpressionCalculatorConfiguration itemTradePriorityByExpressionCalculatorConfiguration;

    @Test
    public void createManageableUser_FromPostgresEntity_should_return_expected_result_with_expressions() {
        TradeByFiltersManagerEntity tradeByFiltersManagerEntity1 = Mockito.mock(TradeByFiltersManagerEntity.class);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntity2 = Mockito.mock(TradeByFiltersManagerEntity.class);

        TradeByFiltersManager tradeByFiltersManager1 = Mockito.mock(TradeByFiltersManager.class);
        TradeByFiltersManager tradeByFiltersManager2 = Mockito.mock(TradeByFiltersManager.class);

        when(tradeByFiltersManagerEntityMapper.createDTO(tradeByFiltersManagerEntity1)).thenReturn(tradeByFiltersManager1);
        when(tradeByFiltersManagerEntityMapper.createDTO(tradeByFiltersManagerEntity2)).thenReturn(tradeByFiltersManager2);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity1 = Mockito.mock(TradeByItemIdManagerEntity.class);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntity2 = Mockito.mock(TradeByItemIdManagerEntity.class);

        TradeByItemIdManager tradeByItemIdManager1 = Mockito.mock(TradeByItemIdManager.class);
        TradeByItemIdManager tradeByItemIdManager2 = Mockito.mock(TradeByItemIdManager.class);

        when(tradeByItemIdManagerEntityMapper.createDTO(tradeByItemIdManagerEntity1)).thenReturn(tradeByItemIdManager1);
        when(tradeByItemIdManagerEntityMapper.createDTO(tradeByItemIdManagerEntity2)).thenReturn(tradeByItemIdManager2);

        UbiAccountStatsEntity ubiAccountStatsEntity = Mockito.mock(UbiAccountStatsEntity.class);

        UbiAccountStats ubiAccountStats = Mockito.mock(UbiAccountStats.class);

        when(ubiAccountStatsEntityMapper.createDTO(ubiAccountStatsEntity)).thenReturn(ubiAccountStats);

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUbiAccountEntry(new UbiAccountEntryEntity());
        entity.getUbiAccountEntry().setUbiAccountStats(ubiAccountStatsEntity);
        entity.getUbiAccountEntry().setUbiAuthTicket("ubiAuthTicket");
        entity.getUbiAccountEntry().setUbiSpaceId("ubiSpaceId");
        entity.getUbiAccountEntry().setUbiSessionId("ubiSessionId");
        entity.getUbiAccountEntry().setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        entity.getUbiAccountEntry().setUbiRememberMeTicket("ubiRememberMeTicket");
        entity.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity1, tradeByFiltersManagerEntity2));
        entity.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity1, tradeByItemIdManagerEntity2));
        entity.setSellTradesManagingEnabledFlag(true);
        entity.setSellTradePriorityExpression("sellTradePriorityExpression");
        entity.setBuyTradesManagingEnabledFlag(false);
        entity.setBuyTradePriorityExpression("buyTradePriorityExpression");

        ManageableUser result = manageableUserFactory.createManageableUserFromPostgresEntity(entity);

        assertEquals(1L, result.getId());
        assertSame(ubiAccountStats, result.getUbiAccountStats());
        assertEquals("ubiAuthTicket", result.getUbiAuthTicket());
        assertEquals("ubiSpaceId", result.getUbiSpaceId());
        assertEquals("ubiSessionId", result.getUbiSessionId());
        assertEquals("ubiRememberDeviceTicket", result.getUbiRememberDeviceTicket());
        assertEquals("ubiRememberMeTicket", result.getUbiRememberMeTicket());
        assertTrue(result.getTradeByFiltersManagers().stream().anyMatch(tm -> tm == tradeByFiltersManager1));
        assertTrue(result.getTradeByFiltersManagers().stream().anyMatch(tm -> tm == tradeByFiltersManager2));
        assertTrue(result.getTradeByItemIdManagers().stream().anyMatch(tm -> tm == tradeByItemIdManager1));
        assertTrue(result.getTradeByItemIdManagers().stream().anyMatch(tm -> tm == tradeByItemIdManager2));
        assertTrue(result.getSellTradesManagingEnabledFlag());
        assertEquals("sellTradePriorityExpression", result.getSellTradePriorityExpression());
        assertFalse(result.getBuyTradesManagingEnabledFlag());
        assertEquals("buyTradePriorityExpression", result.getBuyTradePriorityExpression());
    }

    @Test
    public void createManageableUser_FromPostgresEntity_should_return_expected_result_with_no_expressions_provided() {
        TradeByFiltersManagerEntity tradeByFiltersManagerEntity1 = Mockito.mock(TradeByFiltersManagerEntity.class);
        TradeByFiltersManagerEntity tradeByFiltersManagerEntity2 = Mockito.mock(TradeByFiltersManagerEntity.class);

        TradeByFiltersManager tradeByFiltersManager1 = Mockito.mock(TradeByFiltersManager.class);
        TradeByFiltersManager tradeByFiltersManager2 = Mockito.mock(TradeByFiltersManager.class);

        when(tradeByFiltersManagerEntityMapper.createDTO(tradeByFiltersManagerEntity1)).thenReturn(tradeByFiltersManager1);
        when(tradeByFiltersManagerEntityMapper.createDTO(tradeByFiltersManagerEntity2)).thenReturn(tradeByFiltersManager2);

        TradeByItemIdManagerEntity tradeByItemIdManagerEntity1 = Mockito.mock(TradeByItemIdManagerEntity.class);
        TradeByItemIdManagerEntity tradeByItemIdManagerEntity2 = Mockito.mock(TradeByItemIdManagerEntity.class);

        TradeByItemIdManager tradeByItemIdManager1 = Mockito.mock(TradeByItemIdManager.class);
        TradeByItemIdManager tradeByItemIdManager2 = Mockito.mock(TradeByItemIdManager.class);

        when(tradeByItemIdManagerEntityMapper.createDTO(tradeByItemIdManagerEntity1)).thenReturn(tradeByItemIdManager1);
        when(tradeByItemIdManagerEntityMapper.createDTO(tradeByItemIdManagerEntity2)).thenReturn(tradeByItemIdManager2);

        UbiAccountStatsEntity ubiAccountStatsEntity = Mockito.mock(UbiAccountStatsEntity.class);

        UbiAccountStats ubiAccountStats = Mockito.mock(UbiAccountStats.class);

        when(ubiAccountStatsEntityMapper.createDTO(ubiAccountStatsEntity)).thenReturn(ubiAccountStats);

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUbiAccountEntry(new UbiAccountEntryEntity());
        entity.getUbiAccountEntry().setUbiAccountStats(ubiAccountStatsEntity);
        entity.getUbiAccountEntry().setUbiAuthTicket("ubiAuthTicket");
        entity.getUbiAccountEntry().setUbiSpaceId("ubiSpaceId");
        entity.getUbiAccountEntry().setUbiSessionId("ubiSessionId");
        entity.getUbiAccountEntry().setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        entity.getUbiAccountEntry().setUbiRememberMeTicket("ubiRememberMeTicket");
        entity.setTradeByFiltersManagers(List.of(tradeByFiltersManagerEntity1, tradeByFiltersManagerEntity2));
        entity.setTradeByItemIdManagers(List.of(tradeByItemIdManagerEntity1, tradeByItemIdManagerEntity2));
        entity.setSellTradesManagingEnabledFlag(true);
        entity.setSellTradePriorityExpression(null);
        entity.setBuyTradesManagingEnabledFlag(false);
        entity.setBuyTradePriorityExpression(null);

        when(itemTradePriorityByExpressionCalculatorConfiguration.getDefaultSellTradePriorityExpression()).thenReturn("sellTradePriorityExpression");
        when(itemTradePriorityByExpressionCalculatorConfiguration.getDefaultBuyTradePriorityExpression()).thenReturn("buyTradePriorityExpression");

        ManageableUser result = manageableUserFactory.createManageableUserFromPostgresEntity(entity);

        assertEquals(1L, result.getId());
        assertSame(ubiAccountStats, result.getUbiAccountStats());
        assertEquals("ubiAuthTicket", result.getUbiAuthTicket());
        assertEquals("ubiSpaceId", result.getUbiSpaceId());
        assertEquals("ubiSessionId", result.getUbiSessionId());
        assertEquals("ubiRememberDeviceTicket", result.getUbiRememberDeviceTicket());
        assertEquals("ubiRememberMeTicket", result.getUbiRememberMeTicket());
        assertTrue(result.getTradeByFiltersManagers().stream().anyMatch(tm -> tm == tradeByFiltersManager1));
        assertTrue(result.getTradeByFiltersManagers().stream().anyMatch(tm -> tm == tradeByFiltersManager2));
        assertTrue(result.getTradeByItemIdManagers().stream().anyMatch(tm -> tm == tradeByItemIdManager1));
        assertTrue(result.getTradeByItemIdManagers().stream().anyMatch(tm -> tm == tradeByItemIdManager2));
        assertTrue(result.getSellTradesManagingEnabledFlag());
        assertEquals("sellTradePriorityExpression", result.getSellTradePriorityExpression());
        assertFalse(result.getBuyTradesManagingEnabledFlag());
        assertEquals("buyTradePriorityExpression", result.getBuyTradePriorityExpression());
    }
}