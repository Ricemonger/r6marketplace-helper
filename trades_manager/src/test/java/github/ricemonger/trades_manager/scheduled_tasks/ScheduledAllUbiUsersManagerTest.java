package github.ricemonger.trades_manager.scheduled_tasks;

import github.ricemonger.trades_manager.services.CommonValuesService;
import github.ricemonger.trades_manager.services.DTOs.ManageableUser;
import github.ricemonger.trades_manager.services.DTOs.TradeManagerCommand;
import github.ricemonger.trades_manager.services.DTOs.UbiAccountStats;
import github.ricemonger.trades_manager.services.ItemService;
import github.ricemonger.trades_manager.services.TradeManagementCommandsExecutor;
import github.ricemonger.trades_manager.services.UserService;
import github.ricemonger.trades_manager.services.factories.PersonalItemFactory;
import github.ricemonger.trades_manager.services.factories.PotentialTradeFactory;
import github.ricemonger.trades_manager.services.factories.TradeManagerCommandsFactory;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.enums.CentralTradeManagerCommandType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduledAllUbiUsersManagerTest {
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private PersonalItemFactory personalItemFactory;
    @MockBean
    private PotentialTradeFactory potentialTradeFactory;
    @MockBean
    private TradeManagerCommandsFactory tradeManagerCommandsFactory;
    @MockBean
    private TradeManagementCommandsExecutor tradeManagementCommandsExecutor;
    @Autowired
    private ScheduledAllUbiUsersManager scheduledAllUbiUsersManager;

    @Test
    public void manageAllUsersTrades_should_create_and_execute_commands_for_all_manageable_users() {
        ConfigTrades configTrades = new ConfigTrades();
        configTrades.setBuySlots(1);
        configTrades.setBuyLimit(2);
        configTrades.setSellSlots(3);
        configTrades.setSellLimit(4);

        List<Item> existingItems = mock(List.class);

        List tradeByFiltersManagers1 = mock(List.class);
        List tradeByItemIdManagers1 = mock(List.class);
        List ownedItemsIds1 = mock(List.class);
        List resaleLocks1 = mock(List.class);
        List currentSellTrades1 = mock(List.class);
        List currentBuyTrades1 = mock(List.class);

        ManageableUser manageableUser1 = new ManageableUser();
        manageableUser1.setId(1L);
        AuthorizationDTO authDTO1 = new AuthorizationDTO("ticket1", "profileId1", "spaceId1", "sessionId1", "rememberDeviceTicket1", "rememberMeTicket1");
        manageableUser1.setUbiAuthTicket("ticket1");
        manageableUser1.setUbiSpaceId("spaceId1");
        manageableUser1.setUbiSessionId("sessionId1");
        manageableUser1.setUbiRememberDeviceTicket("rememberDeviceTicket1");
        manageableUser1.setUbiRememberMeTicket("rememberMeTicket1");
        manageableUser1.setUbiAccountStats(new UbiAccountStats());
        manageableUser1.getUbiAccountStats().setUbiProfileId("ubiProfiledId1");
        manageableUser1.getUbiAccountStats().setCreditAmount(1000);
        manageableUser1.setTradeByFiltersManagers(tradeByFiltersManagers1);
        manageableUser1.setTradeByItemIdManagers(tradeByItemIdManagers1);
        manageableUser1.getUbiAccountStats().setOwnedItemsIds(ownedItemsIds1);
        manageableUser1.getUbiAccountStats().setResaleLocks(resaleLocks1);
        manageableUser1.getUbiAccountStats().setSoldIn24h(1);
        manageableUser1.getUbiAccountStats().setBoughtIn24h(10);
        manageableUser1.getUbiAccountStats().setCurrentSellTrades(currentSellTrades1);
        manageableUser1.getUbiAccountStats().setCurrentBuyTrades(currentBuyTrades1);
        manageableUser1.setSellTradesManagingEnabledFlag(true);
        manageableUser1.setBuyTradesManagingEnabledFlag(true);

        Set personalItems1 = mock(Set.class);
        when(personalItemFactory.getPersonalItemsForUser(same(tradeByFiltersManagers1), same(tradeByItemIdManagers1), same(currentSellTrades1), same(currentBuyTrades1), same(ownedItemsIds1), same(existingItems))).thenReturn(personalItems1);

        List resultingSellTrades1 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalSellTrades(same(personalItems1), same(resaleLocks1), same(1), eq(configTrades.getSellSlots()), same(configTrades.getSellLimit()))).thenReturn(resultingSellTrades1);

        List resultingBuyTrades1 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalBuyTrades(same(personalItems1), eq(1000), eq(10), eq(configTrades.getBuySlots()), eq(configTrades.getBuyLimit()))).thenReturn(resultingBuyTrades1);

        TradeManagerCommand buyCancelCommand1 = mock(TradeManagerCommand.class);
        when(buyCancelCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CANCEL);
        when(buyCancelCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(buyCancelCommand1.getTradeId()).thenReturn("buyCancelTradeId1");
        when(buyCancelCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand buyCancelCommand12 = mock(TradeManagerCommand.class);
        when(buyCancelCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CANCEL);
        when(buyCancelCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(buyCancelCommand12.getTradeId()).thenReturn("buyCancelTradeId12");
        when(buyCancelCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand buyUpdateCommand1 = mock(TradeManagerCommand.class);
        when(buyUpdateCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_UPDATE);
        when(buyUpdateCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(buyUpdateCommand1.getTradeId()).thenReturn("buyUpdateTradeId1");
        when(buyUpdateCommand1.getNewPrice()).thenReturn(1);
        when(buyUpdateCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand buyUpdateCommand12 = mock(TradeManagerCommand.class);
        when(buyUpdateCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_UPDATE);
        when(buyUpdateCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(buyUpdateCommand12.getTradeId()).thenReturn("buyUpdateTradeId12");
        when(buyUpdateCommand12.getNewPrice()).thenReturn(2);
        when(buyUpdateCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand buyCreateCommand1 = mock(TradeManagerCommand.class);
        when(buyCreateCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CREATE);
        when(buyCreateCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(buyCreateCommand1.getItemId()).thenReturn("buyCreateItemId1");
        when(buyCreateCommand1.getNewPrice()).thenReturn(1);
        when(buyCreateCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand buyCreateCommand12 = mock(TradeManagerCommand.class);
        when(buyCreateCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CREATE);
        when(buyCreateCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(buyCreateCommand12.getItemId()).thenReturn("buyCreateItemId12");
        when(buyCreateCommand12.getNewPrice()).thenReturn(2);
        when(buyCreateCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCancelCommand1 = mock(TradeManagerCommand.class);
        when(sellCancelCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CANCEL);
        when(sellCancelCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCancelCommand1.getTradeId()).thenReturn("sellCancelTradeId1");
        when(sellCancelCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCancelCommand12 = mock(TradeManagerCommand.class);
        when(sellCancelCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CANCEL);
        when(sellCancelCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCancelCommand12.getTradeId()).thenReturn("sellCancelTradeId12");
        when(sellCancelCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand sellUpdateCommand1 = mock(TradeManagerCommand.class);
        when(sellUpdateCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_UPDATE);
        when(sellUpdateCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellUpdateCommand1.getTradeId()).thenReturn("sellUpdateTradeId1");
        when(sellUpdateCommand1.getNewPrice()).thenReturn(1);
        when(sellUpdateCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand sellUpdateCommand12 = mock(TradeManagerCommand.class);
        when(sellUpdateCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_UPDATE);
        when(sellUpdateCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellUpdateCommand12.getTradeId()).thenReturn("sellUpdateTradeId12");
        when(sellUpdateCommand12.getNewPrice()).thenReturn(2);
        when(sellUpdateCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCreateCommand1 = mock(TradeManagerCommand.class);
        when(sellCreateCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CREATE);
        when(sellCreateCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCreateCommand1.getItemId()).thenReturn("sellCreateItemId1");
        when(sellCreateCommand1.getNewPrice()).thenReturn(1);
        when(sellCreateCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCreateCommand12 = mock(TradeManagerCommand.class);
        when(sellCreateCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CREATE);
        when(sellCreateCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCreateCommand12.getItemId()).thenReturn("sellCreateItemId12");
        when(sellCreateCommand12.getNewPrice()).thenReturn(2);
        when(sellCreateCommand12.getUserId()).thenReturn(1L);

        List<TradeManagerCommand> commands1 = List.of(buyCancelCommand1, buyCancelCommand12, buyUpdateCommand1, buyUpdateCommand12,
                buyCreateCommand1, buyCreateCommand12, sellCancelCommand1, sellCancelCommand12, sellUpdateCommand1, sellUpdateCommand12,
                sellCreateCommand1, sellCreateCommand12);

        AuthorizationDTO updatedAuthDTO1 = new AuthorizationDTO(authDTO1.getTicket(), manageableUser1.getUbiProfileId(), authDTO1.getSpaceId(), authDTO1.getSessionId(), authDTO1.getRememberDeviceTicket(), authDTO1.getRememberMeTicket());

        when(tradeManagerCommandsFactory.createTradeManagerCommandsForUser(same(resultingSellTrades1), same(currentSellTrades1),
                same(resultingBuyTrades1), same(currentBuyTrades1), eq(1L), eq(updatedAuthDTO1), eq(configTrades))).thenReturn(commands1);

        List tradeByFiltersManagers2 = mock(List.class);
        List tradeByItemIdManagers2 = mock(List.class);
        List ownedItemsIds2 = mock(List.class);
        List resaleLocks2 = mock(List.class);
        List currentSellTrades2 = mock(List.class);
        List currentBuyTrades2 = mock(List.class);

        ManageableUser manageableUser2 = new ManageableUser();
        manageableUser2.setId(2L);
        AuthorizationDTO authDTO2 = new AuthorizationDTO("ticket2", "profileId2", "spaceId2", "sessionId2", "rememberDeviceTicket2", "rememberMeTicket2");
        manageableUser2.setUbiAuthTicket("ticket2");
        manageableUser2.setUbiSpaceId("spaceId2");
        manageableUser2.setUbiSessionId("sessionId2");
        manageableUser2.setUbiRememberDeviceTicket("rememberDeviceTicket2");
        manageableUser2.setUbiRememberMeTicket("rememberMeTicket2");
        manageableUser2.setUbiAccountStats(new UbiAccountStats());
        manageableUser2.getUbiAccountStats().setUbiProfileId("ubiProfiledId2");
        manageableUser2.getUbiAccountStats().setCreditAmount(2000);
        manageableUser2.setTradeByFiltersManagers(tradeByFiltersManagers2);
        manageableUser2.setTradeByItemIdManagers(tradeByItemIdManagers2);
        manageableUser2.getUbiAccountStats().setOwnedItemsIds(ownedItemsIds2);
        manageableUser2.getUbiAccountStats().setResaleLocks(resaleLocks2);
        manageableUser2.getUbiAccountStats().setSoldIn24h(2);
        manageableUser2.getUbiAccountStats().setBoughtIn24h(20);
        manageableUser2.getUbiAccountStats().setCurrentSellTrades(currentSellTrades2);
        manageableUser2.getUbiAccountStats().setCurrentBuyTrades(currentBuyTrades2);
        manageableUser2.setSellTradesManagingEnabledFlag(true);
        manageableUser2.setBuyTradesManagingEnabledFlag(true);

        Set personalItems2 = mock(Set.class);
        when(personalItemFactory.getPersonalItemsForUser(same(tradeByFiltersManagers2), same(tradeByItemIdManagers2), same(currentSellTrades2), same(currentBuyTrades2), same(ownedItemsIds2), same(existingItems))).thenReturn(personalItems2);

        List resultingSellTrades2 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalSellTrades(same(personalItems2), same(resaleLocks2), same(2), eq(configTrades.getSellSlots()), same(configTrades.getSellLimit()))).thenReturn(resultingSellTrades2);

        List resultingBuyTrades2 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalBuyTrades(same(personalItems2), eq(2000), eq(20), eq(configTrades.getBuySlots()), eq(configTrades.getBuyLimit()))).thenReturn(resultingBuyTrades2);

        TradeManagerCommand buyCancelCommand2 = mock(TradeManagerCommand.class);
        when(buyCancelCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CANCEL);
        when(buyCancelCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(buyCancelCommand2.getTradeId()).thenReturn("buyCancelTradeId2");
        when(buyCancelCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand buyUpdateCommand2 = mock(TradeManagerCommand.class);
        when(buyUpdateCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_UPDATE);
        when(buyUpdateCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(buyUpdateCommand2.getTradeId()).thenReturn("buyUpdateTradeId2");
        when(buyUpdateCommand2.getNewPrice()).thenReturn(3);
        when(buyUpdateCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand buyCreateCommand2 = mock(TradeManagerCommand.class);
        when(buyCreateCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CREATE);
        when(buyCreateCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(buyCreateCommand2.getItemId()).thenReturn("buyCreateItemId2");
        when(buyCreateCommand2.getNewPrice()).thenReturn(3);
        when(buyCreateCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand sellCancelCommand2 = mock(TradeManagerCommand.class);
        when(sellCancelCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CANCEL);
        when(sellCancelCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(sellCancelCommand2.getTradeId()).thenReturn("sellCancelTradeId2");
        when(sellCancelCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand sellUpdateCommand2 = mock(TradeManagerCommand.class);
        when(sellUpdateCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_UPDATE);
        when(sellUpdateCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(sellUpdateCommand2.getTradeId()).thenReturn("sellUpdateTradeId2");
        when(sellUpdateCommand2.getNewPrice()).thenReturn(3);
        when(sellUpdateCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand sellCreateCommand2 = mock(TradeManagerCommand.class);
        when(sellCreateCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CREATE);
        when(sellCreateCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(sellCreateCommand2.getItemId()).thenReturn("sellCreateItemId2");
        when(sellCreateCommand2.getNewPrice()).thenReturn(3);
        when(sellCreateCommand2.getUserId()).thenReturn(2L);

        List<TradeManagerCommand> commands2 = List.of(buyCancelCommand2, buyUpdateCommand2, buyCreateCommand2, sellCancelCommand2, sellUpdateCommand2, sellCreateCommand2);

        AuthorizationDTO updatedAuthDTO2 = new AuthorizationDTO(authDTO2.getTicket(), manageableUser2.getUbiProfileId(), authDTO2.getSpaceId(), authDTO2.getSessionId(), authDTO2.getRememberDeviceTicket(), authDTO2.getRememberMeTicket());

        when(tradeManagerCommandsFactory.createTradeManagerCommandsForUser(same(resultingSellTrades2), same(currentSellTrades2), same(resultingBuyTrades2), same(currentBuyTrades2), eq(2L), eq(updatedAuthDTO2), eq(configTrades))).thenReturn(commands2);

        List<ManageableUser> manageableUsers = List.of(manageableUser1, manageableUser2);

        when(commonValuesService.getConfigTrades()).thenReturn(configTrades);
        when(itemService.getAllItems()).thenReturn(existingItems);
        when(userService.getAllManageableUsers()).thenReturn(manageableUsers);

        scheduledAllUbiUsersManager.manageAllUsersTrades();

        verify(tradeManagementCommandsExecutor).executeCommand(buyCancelCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(buyCancelCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(buyUpdateCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(buyUpdateCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(buyCreateCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(buyCreateCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCancelCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCancelCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(sellUpdateCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(sellUpdateCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCreateCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCreateCommand12);
    }

    @Test
    public void manageAllUsersTrades_should_create_and_execute_commands_for_all_manageable_users_trade_types_disalbed() {
        ConfigTrades configTrades = new ConfigTrades();
        configTrades.setBuySlots(1);
        configTrades.setBuyLimit(2);
        configTrades.setSellSlots(3);
        configTrades.setSellLimit(4);

        List<Item> existingItems = mock(List.class);

        List tradeByFiltersManagers1 = mock(List.class);
        List tradeByItemIdManagers1 = mock(List.class);
        List ownedItemsIds1 = mock(List.class);
        List resaleLocks1 = mock(List.class);
        List currentSellTrades1 = mock(List.class);
        List currentBuyTrades1 = mock(List.class);

        ManageableUser manageableUser1 = new ManageableUser();
        manageableUser1.setId(1L);
        AuthorizationDTO authDTO1 = new AuthorizationDTO("ticket1", "profileId1", "spaceId1", "sessionId1", "rememberDeviceTicket1", "rememberMeTicket1");
        manageableUser1.setUbiAuthTicket("ticket1");
        manageableUser1.setUbiSpaceId("spaceId1");
        manageableUser1.setUbiSessionId("sessionId1");
        manageableUser1.setUbiRememberDeviceTicket("rememberDeviceTicket1");
        manageableUser1.setUbiRememberMeTicket("rememberMeTicket1");
        manageableUser1.setUbiAccountStats(new UbiAccountStats());
        manageableUser1.getUbiAccountStats().setUbiProfileId("ubiProfiledId1");
        manageableUser1.getUbiAccountStats().setCreditAmount(1000);
        manageableUser1.setTradeByFiltersManagers(tradeByFiltersManagers1);
        manageableUser1.setTradeByItemIdManagers(tradeByItemIdManagers1);
        manageableUser1.getUbiAccountStats().setOwnedItemsIds(ownedItemsIds1);
        manageableUser1.getUbiAccountStats().setResaleLocks(resaleLocks1);
        manageableUser1.getUbiAccountStats().setSoldIn24h(1);
        manageableUser1.getUbiAccountStats().setBoughtIn24h(10);
        manageableUser1.getUbiAccountStats().setCurrentSellTrades(currentSellTrades1);
        manageableUser1.getUbiAccountStats().setCurrentBuyTrades(currentBuyTrades1);
        manageableUser1.setSellTradesManagingEnabledFlag(true);
        manageableUser1.setBuyTradesManagingEnabledFlag(false);

        Set personalItems1 = mock(Set.class);
        when(personalItemFactory.getPersonalItemsForUser(same(tradeByFiltersManagers1), same(tradeByItemIdManagers1), same(currentSellTrades1), same(currentBuyTrades1), same(ownedItemsIds1), same(existingItems))).thenReturn(personalItems1);

        List resultingSellTrades1 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalSellTrades(same(personalItems1), same(resaleLocks1), same(1), eq(configTrades.getSellSlots()), same(configTrades.getSellLimit()))).thenReturn(resultingSellTrades1);

        List resultingBuyTrades1 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalBuyTrades(same(personalItems1), eq(1000), eq(10), eq(configTrades.getBuySlots()), eq(configTrades.getBuyLimit()))).thenReturn(resultingBuyTrades1);

        TradeManagerCommand sellCancelCommand1 = mock(TradeManagerCommand.class);
        when(sellCancelCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CANCEL);
        when(sellCancelCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCancelCommand1.getTradeId()).thenReturn("sellCancelTradeId1");
        when(sellCancelCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCancelCommand12 = mock(TradeManagerCommand.class);
        when(sellCancelCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CANCEL);
        when(sellCancelCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCancelCommand12.getTradeId()).thenReturn("sellCancelTradeId12");
        when(sellCancelCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand sellUpdateCommand1 = mock(TradeManagerCommand.class);
        when(sellUpdateCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_UPDATE);
        when(sellUpdateCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellUpdateCommand1.getTradeId()).thenReturn("sellUpdateTradeId1");
        when(sellUpdateCommand1.getNewPrice()).thenReturn(1);
        when(sellUpdateCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand sellUpdateCommand12 = mock(TradeManagerCommand.class);
        when(sellUpdateCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_UPDATE);
        when(sellUpdateCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellUpdateCommand12.getTradeId()).thenReturn("sellUpdateTradeId12");
        when(sellUpdateCommand12.getNewPrice()).thenReturn(2);
        when(sellUpdateCommand12.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCreateCommand1 = mock(TradeManagerCommand.class);
        when(sellCreateCommand1.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CREATE);
        when(sellCreateCommand1.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCreateCommand1.getItemId()).thenReturn("sellCreateItemId1");
        when(sellCreateCommand1.getNewPrice()).thenReturn(1);
        when(sellCreateCommand1.getUserId()).thenReturn(1L);

        TradeManagerCommand sellCreateCommand12 = mock(TradeManagerCommand.class);
        when(sellCreateCommand12.getCommandType()).thenReturn(CentralTradeManagerCommandType.SELL_ORDER_CREATE);
        when(sellCreateCommand12.getAuthorizationDTO()).thenReturn(authDTO1);
        when(sellCreateCommand12.getItemId()).thenReturn("sellCreateItemId12");
        when(sellCreateCommand12.getNewPrice()).thenReturn(2);
        when(sellCreateCommand12.getUserId()).thenReturn(1L);

        List<TradeManagerCommand> commands1 = List.of(sellCancelCommand1, sellCancelCommand12, sellUpdateCommand1, sellUpdateCommand12, sellCreateCommand1, sellCreateCommand12);

        AuthorizationDTO updatedAuthDTO1 = new AuthorizationDTO(authDTO1.getTicket(), manageableUser1.getUbiProfileId(), authDTO1.getSpaceId(), authDTO1.getSessionId(), authDTO1.getRememberDeviceTicket(), authDTO1.getRememberMeTicket());

        when(tradeManagerCommandsFactory.createTradeManagerCommandsForUser(same(resultingSellTrades1), same(currentSellTrades1),
                argThat(arg -> arg != resultingBuyTrades1), same(currentBuyTrades1), eq(1L), eq(updatedAuthDTO1), eq(configTrades))).thenReturn(commands1);

        List tradeByFiltersManagers2 = mock(List.class);
        List tradeByItemIdManagers2 = mock(List.class);
        List ownedItemsIds2 = mock(List.class);
        List resaleLocks2 = mock(List.class);
        List currentSellTrades2 = mock(List.class);
        List currentBuyTrades2 = mock(List.class);

        ManageableUser manageableUser2 = new ManageableUser();
        manageableUser2.setId(2L);
        AuthorizationDTO authDTO2 = new AuthorizationDTO("ticket2", "profileId2", "spaceId2", "sessionId2", "rememberDeviceTicket2", "rememberMeTicket2");
        manageableUser2.setUbiAuthTicket("ticket2");
        manageableUser2.setUbiSpaceId("spaceId2");
        manageableUser2.setUbiSessionId("sessionId2");
        manageableUser2.setUbiRememberDeviceTicket("rememberDeviceTicket2");
        manageableUser2.setUbiRememberMeTicket("rememberMeTicket2");
        manageableUser2.setUbiAccountStats(new UbiAccountStats());
        manageableUser2.getUbiAccountStats().setUbiProfileId("ubiProfiledId2");
        manageableUser2.getUbiAccountStats().setCreditAmount(2000);
        manageableUser2.setTradeByFiltersManagers(tradeByFiltersManagers2);
        manageableUser2.setTradeByItemIdManagers(tradeByItemIdManagers2);
        manageableUser2.getUbiAccountStats().setOwnedItemsIds(ownedItemsIds2);
        manageableUser2.getUbiAccountStats().setResaleLocks(resaleLocks2);
        manageableUser2.getUbiAccountStats().setSoldIn24h(2);
        manageableUser2.getUbiAccountStats().setBoughtIn24h(20);
        manageableUser2.getUbiAccountStats().setCurrentSellTrades(currentSellTrades2);
        manageableUser2.getUbiAccountStats().setCurrentBuyTrades(currentBuyTrades2);
        manageableUser2.setSellTradesManagingEnabledFlag(false);
        manageableUser2.setBuyTradesManagingEnabledFlag(true);

        Set personalItems2 = mock(Set.class);
        when(personalItemFactory.getPersonalItemsForUser(same(tradeByFiltersManagers2), same(tradeByItemIdManagers2), same(currentSellTrades2), same(currentBuyTrades2), same(ownedItemsIds2), same(existingItems))).thenReturn(personalItems2);

        List resultingSellTrades2 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalSellTrades(same(personalItems2), same(resaleLocks2), same(2), eq(configTrades.getSellSlots()), same(configTrades.getSellLimit()))).thenReturn(resultingSellTrades2);

        List resultingBuyTrades2 = mock(List.class);
        when(potentialTradeFactory.getResultingPersonalBuyTrades(same(personalItems2), eq(2000), eq(20), eq(configTrades.getBuySlots()), eq(configTrades.getBuyLimit()))).thenReturn(resultingBuyTrades2);

        TradeManagerCommand buyCancelCommand2 = mock(TradeManagerCommand.class);
        when(buyCancelCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CANCEL);
        when(buyCancelCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(buyCancelCommand2.getTradeId()).thenReturn("buyCancelTradeId2");
        when(buyCancelCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand buyUpdateCommand2 = mock(TradeManagerCommand.class);
        when(buyUpdateCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_UPDATE);
        when(buyUpdateCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(buyUpdateCommand2.getTradeId()).thenReturn("buyUpdateTradeId2");
        when(buyUpdateCommand2.getNewPrice()).thenReturn(3);
        when(buyUpdateCommand2.getUserId()).thenReturn(2L);

        TradeManagerCommand buyCreateCommand2 = mock(TradeManagerCommand.class);
        when(buyCreateCommand2.getCommandType()).thenReturn(CentralTradeManagerCommandType.BUY_ORDER_CREATE);
        when(buyCreateCommand2.getAuthorizationDTO()).thenReturn(authDTO2);
        when(buyCreateCommand2.getItemId()).thenReturn("buyCreateItemId2");
        when(buyCreateCommand2.getNewPrice()).thenReturn(3);
        when(buyCreateCommand2.getUserId()).thenReturn(2L);

        List<TradeManagerCommand> commands2 = List.of(buyCancelCommand2, buyUpdateCommand2, buyCreateCommand2);

        AuthorizationDTO updatedAuthDTO2 = new AuthorizationDTO(authDTO2.getTicket(), manageableUser2.getUbiProfileId(), authDTO2.getSpaceId(), authDTO2.getSessionId(), authDTO2.getRememberDeviceTicket(), authDTO2.getRememberMeTicket());

        when(tradeManagerCommandsFactory.createTradeManagerCommandsForUser(argThat(arg -> arg != resultingSellTrades2), same(currentSellTrades2), same(resultingBuyTrades2), same(currentBuyTrades2), eq(2L), eq(updatedAuthDTO2), eq(configTrades))).thenReturn(commands2);

        List<ManageableUser> manageableUsers = List.of(manageableUser1, manageableUser2);

        when(commonValuesService.getConfigTrades()).thenReturn(configTrades);
        when(itemService.getAllItems()).thenReturn(existingItems);
        when(userService.getAllManageableUsers()).thenReturn(manageableUsers);

        scheduledAllUbiUsersManager.manageAllUsersTrades();

        verify(tradeManagementCommandsExecutor).executeCommand(sellCancelCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCancelCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(sellUpdateCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(sellUpdateCommand12);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCreateCommand1);
        verify(tradeManagementCommandsExecutor).executeCommand(sellCreateCommand12);
    }
}