package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.*;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TradeCommandsFactoryTest {
    @Autowired
    private TradeCommandsFactory tradeCommandsFactory;

    @Test
    public void createTradeCommandsForUser_should_return_expected_commands() {
        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setResaleLocks(List.of("lockedItemId1"));
        user.setSoldIn24h(16);

        SellTrade updateSellTrade = new SellTrade();
        updateSellTrade.setItemId("updateSellTradeItemId");
        updateSellTrade.setTradeId("updateSellTradeId");
        updateSellTrade.setPrice(100);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        SellTrade alreadySameSellTrade = new SellTrade();
        alreadySameSellTrade.setItemId("alreadySameSellTradeItemId");
        alreadySameSellTrade.setTradeId("alreadySameSellTradeId");
        alreadySameSellTrade.setPrice(50);

        SellTrade leaveSellTrade = new SellTrade();
        leaveSellTrade.setItemId("leaveSellTradeItemId");
        leaveSellTrade.setTradeId("leaveSellTradeId");
        leaveSellTrade.setPrice(50);

        List<SellTrade> sellTrades = List.of(updateSellTrade, cancelSellTrade, alreadySameSellTrade, leaveSellTrade);

        PotentialTradeItem updateExistingPotentialTradeItem = new PotentialTradeItem();
        updateExistingPotentialTradeItem.setItemId("updateSellTradeItemId");
        updateExistingPotentialTradeItem.setPrice(98);
        updateExistingPotentialTradeItem.setMonthMedianPriceDifference(2);
        updateExistingPotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem createNewPotentialTradeItem = new PotentialTradeItem();
        createNewPotentialTradeItem.setItemId("createNewPotentialTradeItemId");
        createNewPotentialTradeItem.setPrice(50);
        createNewPotentialTradeItem.setMonthMedianPriceDifference(2);
        createNewPotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem alreadySamePotentialTradeItem = new PotentialTradeItem();
        alreadySamePotentialTradeItem.setItemId("alreadySameSellTradeItemId");
        alreadySamePotentialTradeItem.setPrice(49);
        alreadySamePotentialTradeItem.setMonthMedianPriceDifference(2);
        alreadySamePotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem skipCauseResaleLockPotentialTradeItem = new PotentialTradeItem();
        skipCauseResaleLockPotentialTradeItem.setItemId("lockedItemId1");
        skipCauseResaleLockPotentialTradeItem.setPrice(1000);
        skipCauseResaleLockPotentialTradeItem.setMonthMedianPriceDifference(200);
        skipCauseResaleLockPotentialTradeItem.setMonthMedianPriceDifferencePercentage(200);

        List<PotentialTradeItem> items = List.of(updateExistingPotentialTradeItem, createNewPotentialTradeItem, alreadySamePotentialTradeItem, skipCauseResaleLockPotentialTradeItem);

        List<ItemCurrentPrices> currentPrices = List.of();

        ItemMedianPriceAndRarity cancelMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        cancelMedianPriceAndRarity.setItemId("cancelSellTradeItemId");
        cancelMedianPriceAndRarity.setMonthMedianPrice(48);

        ItemMedianPriceAndRarity leaveMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveMedianPriceAndRarity.setItemId("leaveSellTradeItemId");
        leaveMedianPriceAndRarity.setMonthMedianPrice(49);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(cancelMedianPriceAndRarity, leaveMedianPriceAndRarity);

        int sellLimit = 20;
        int sellSlots = 4;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsForUser(user, sellTrades, currentPrices, medianPriceAndRarities, items, sellLimit, sellSlots);

        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setProfileId("profileId");

        FastTradeCommand updateExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_UPDATE, "updateSellTradeItemId", "updateSellTradeId", 98);
        FastTradeCommand createExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CREATE, "createNewPotentialTradeItemId", 50);
        FastTradeCommand cancelExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CANCEL, "cancelSellTradeItemId", "cancelSellTradeId");

        System.out.println("Result:");
        result.forEach(System.out::println);

        assertEquals(3, result.size());
        assertTrue(result.contains(updateExpectedCommand));
        assertTrue(result.contains(createExpectedCommand));
        assertTrue(result.contains(cancelExpectedCommand));
    }

    @Test
    public void createTradeCommandsForUser_should_return_expected_commands_if_too_many_potentialTrades() {
        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setResaleLocks(List.of("lockedItemId1"));
        user.setSoldIn24h(19);

        SellTrade updateSellTrade = new SellTrade();
        updateSellTrade.setItemId("updateSellTradeItemId");
        updateSellTrade.setTradeId("updateSellTradeId");
        updateSellTrade.setPrice(100);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        SellTrade alreadySameSellTrade = new SellTrade();
        alreadySameSellTrade.setItemId("alreadySameSellTradeItemId");
        alreadySameSellTrade.setTradeId("alreadySameSellTradeId");
        alreadySameSellTrade.setPrice(50);

        SellTrade leaveSellTrade = new SellTrade();
        leaveSellTrade.setItemId("leaveSellTradeItemId");
        leaveSellTrade.setTradeId("leaveSellTradeId");
        leaveSellTrade.setPrice(50);

        List<SellTrade> sellTrades = List.of(updateSellTrade, cancelSellTrade, alreadySameSellTrade, leaveSellTrade);

        PotentialTradeItem updateExistingPotentialTradeItem = new PotentialTradeItem();
        updateExistingPotentialTradeItem.setItemId("updateSellTradeItemId");
        updateExistingPotentialTradeItem.setPrice(98);
        updateExistingPotentialTradeItem.setMonthMedianPriceDifference(2);
        updateExistingPotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem createNewPotentialTradeItem = new PotentialTradeItem();
        createNewPotentialTradeItem.setItemId("createNewPotentialTradeItemId");
        createNewPotentialTradeItem.setPrice(50);
        createNewPotentialTradeItem.setMonthMedianPriceDifference(2);
        createNewPotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem createNewPotentialTradeItemByBuyPrice = new PotentialTradeItem();
        createNewPotentialTradeItemByBuyPrice.setItemId("createNewPotentialTradeByBuyPriceItemId");
        createNewPotentialTradeItemByBuyPrice.setPrice(1);
        createNewPotentialTradeItemByBuyPrice.setMonthMedianPriceDifference(1);
        createNewPotentialTradeItemByBuyPrice.setMonthMedianPriceDifferencePercentage(1);
        createNewPotentialTradeItemByBuyPrice.setSellByMaxBuyPrice(true);

        PotentialTradeItem createNewPotentialTradeItemLowerProfit = new PotentialTradeItem();
        createNewPotentialTradeItemLowerProfit.setItemId("createNewPotentialTradeLowerProfitItemId");
        createNewPotentialTradeItemLowerProfit.setPrice(50);
        createNewPotentialTradeItemLowerProfit.setMonthMedianPriceDifference(1);
        createNewPotentialTradeItemLowerProfit.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem alreadySamePotentialTradeItem = new PotentialTradeItem();
        alreadySamePotentialTradeItem.setItemId("alreadySameSellTradeItemId");
        alreadySamePotentialTradeItem.setPrice(49);
        alreadySamePotentialTradeItem.setMonthMedianPriceDifference(2);
        alreadySamePotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem skipCauseResaleLockPotentialTradeItem = new PotentialTradeItem();
        skipCauseResaleLockPotentialTradeItem.setItemId("lockedItemId1");
        skipCauseResaleLockPotentialTradeItem.setPrice(1000);
        skipCauseResaleLockPotentialTradeItem.setMonthMedianPriceDifference(200);
        skipCauseResaleLockPotentialTradeItem.setMonthMedianPriceDifferencePercentage(200);

        List<PotentialTradeItem> items = List.of(updateExistingPotentialTradeItem, createNewPotentialTradeItem, alreadySamePotentialTradeItem,
                skipCauseResaleLockPotentialTradeItem, createNewPotentialTradeItemLowerProfit, createNewPotentialTradeItemByBuyPrice);

        ItemCurrentPrices cancelCurrentPrices = new ItemCurrentPrices();
        cancelCurrentPrices.setItemId("cancelSellTradeItemId");
        cancelCurrentPrices.setMinSellPrice(49);

        ItemCurrentPrices leaveCurrentPrices = new ItemCurrentPrices();
        leaveCurrentPrices.setItemId("leaveSellTradeItemId");
        leaveCurrentPrices.setMinSellPrice(48);

        List<ItemCurrentPrices> currentPrices = List.of(cancelCurrentPrices, leaveCurrentPrices);

        ItemMedianPriceAndRarity cancelMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        cancelMedianPriceAndRarity.setItemId("cancelSellTradeItemId");
        cancelMedianPriceAndRarity.setMonthMedianPrice(50);

        ItemMedianPriceAndRarity leaveMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveMedianPriceAndRarity.setItemId("leaveSellTradeItemId");
        leaveMedianPriceAndRarity.setMonthMedianPrice(47);

        ItemMedianPriceAndRarity updateMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        updateMedianPriceAndRarity.setItemId("updateSellTradeItemId");
        updateMedianPriceAndRarity.setMonthMedianPrice(90);

        ItemMedianPriceAndRarity alreadySameMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        alreadySameMedianPriceAndRarity.setItemId("alreadySameSellTradeItemId");
        alreadySameMedianPriceAndRarity.setMonthMedianPrice(40);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(cancelMedianPriceAndRarity, leaveMedianPriceAndRarity, updateMedianPriceAndRarity, alreadySameMedianPriceAndRarity);

        int sellLimit = 20;
        int sellSlots = 4;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsForUser(user, sellTrades, currentPrices, medianPriceAndRarities, items, sellLimit, sellSlots);

        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setProfileId("profileId");

        FastTradeCommand updateExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_UPDATE, "updateSellTradeItemId", "updateSellTradeId", 98);
        FastTradeCommand createExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CREATE, "createNewPotentialTradeItemId", 50);
        FastTradeCommand createExpectedCommandByBuyPrice = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CREATE, "createNewPotentialTradeByBuyPriceItemId", 1);
        FastTradeCommand cancelExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CANCEL, "cancelSellTradeItemId", "cancelSellTradeId");
        FastTradeCommand cancelLeftExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CANCEL, "leaveSellTradeItemId", "leaveSellTradeId");

        System.out.println("Result:");
        result.forEach(System.out::println);

        assertEquals(5, result.size());
        assertTrue(result.contains(updateExpectedCommand));
        assertTrue(result.contains(createExpectedCommand));
        assertTrue(result.contains(createExpectedCommandByBuyPrice));
        assertTrue(result.contains(cancelExpectedCommand));
        assertTrue(result.contains(cancelLeftExpectedCommand));
    }

    @Test
    public void createTradeCommandsForUser_should_return_only_update_list_if_day_limit_exceeded() {
        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setResaleLocks(List.of("lockedItemId1"));
        user.setSoldIn24h(20);

        SellTrade updateSellTrade = new SellTrade();
        updateSellTrade.setItemId("updateSellTradeItemId");
        updateSellTrade.setTradeId("updateSellTradeId");
        updateSellTrade.setPrice(100);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        SellTrade alreadySameSellTrade = new SellTrade();
        alreadySameSellTrade.setItemId("alreadySameSellTradeItemId");
        alreadySameSellTrade.setTradeId("alreadySameSellTradeId");
        alreadySameSellTrade.setPrice(50);

        SellTrade leaveSellTrade = new SellTrade();
        leaveSellTrade.setItemId("leaveSellTradeItemId");
        leaveSellTrade.setTradeId("leaveSellTradeId");
        leaveSellTrade.setPrice(50);

        List<SellTrade> sellTrades = List.of(updateSellTrade, cancelSellTrade, alreadySameSellTrade, leaveSellTrade);

        PotentialTradeItem updateExistingPotentialTradeItem = new PotentialTradeItem();
        updateExistingPotentialTradeItem.setItemId("updateSellTradeItemId");
        updateExistingPotentialTradeItem.setPrice(98);
        updateExistingPotentialTradeItem.setMonthMedianPriceDifference(2);
        updateExistingPotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem createNewPotentialTradeItem = new PotentialTradeItem();
        createNewPotentialTradeItem.setItemId("createNewPotentialTradeItemId");
        createNewPotentialTradeItem.setPrice(50);
        createNewPotentialTradeItem.setMonthMedianPriceDifference(2);
        createNewPotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem createNewPotentialTradeItemLowerProfit = new PotentialTradeItem();
        createNewPotentialTradeItemLowerProfit.setItemId("createNewPotentialTradeLowerProfitItemId");
        createNewPotentialTradeItemLowerProfit.setPrice(50);
        createNewPotentialTradeItemLowerProfit.setMonthMedianPriceDifference(1);
        createNewPotentialTradeItemLowerProfit.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem alreadySamePotentialTradeItem = new PotentialTradeItem();
        alreadySamePotentialTradeItem.setItemId("alreadySameSellTradeItemId");
        alreadySamePotentialTradeItem.setPrice(49);
        alreadySamePotentialTradeItem.setMonthMedianPriceDifference(2);
        alreadySamePotentialTradeItem.setMonthMedianPriceDifferencePercentage(2);

        PotentialTradeItem skipCauseResaleLockPotentialTradeItem = new PotentialTradeItem();
        skipCauseResaleLockPotentialTradeItem.setItemId("lockedItemId1");
        skipCauseResaleLockPotentialTradeItem.setPrice(1000);
        skipCauseResaleLockPotentialTradeItem.setMonthMedianPriceDifference(200);
        skipCauseResaleLockPotentialTradeItem.setMonthMedianPriceDifferencePercentage(200);

        List<PotentialTradeItem> items = List.of(updateExistingPotentialTradeItem, createNewPotentialTradeItem, alreadySamePotentialTradeItem,
                skipCauseResaleLockPotentialTradeItem, createNewPotentialTradeItemLowerProfit);

        List<ItemCurrentPrices> currentPrices = List.of();

        ItemMedianPriceAndRarity cancelMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        cancelMedianPriceAndRarity.setItemId("cancelSellTradeItemId");
        cancelMedianPriceAndRarity.setMonthMedianPrice(48);

        ItemMedianPriceAndRarity leaveMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveMedianPriceAndRarity.setItemId("leaveSellTradeItemId");
        leaveMedianPriceAndRarity.setMonthMedianPrice(49);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(cancelMedianPriceAndRarity, leaveMedianPriceAndRarity);

        int sellLimit = 20;
        int sellSlots = 4;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsForUser(user, sellTrades, currentPrices, medianPriceAndRarities, items, sellLimit, sellSlots);

        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setProfileId("profileId");

        FastTradeCommand updateExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_UPDATE, "updateSellTradeItemId", "updateSellTradeId", 98);

        System.out.println("Result:");
        result.forEach(System.out::println);

        assertEquals(1, result.size());
        assertTrue(result.contains(updateExpectedCommand));
    }

    @Test
    public void createTradeCommandsToKeepUnusedSlotForUser_should_return_expected_commands_by_trade_prices_diff() {
        SellTrade leaveSellTrade1 = new SellTrade();
        leaveSellTrade1.setItemId("leaveSellTradeItemId1");
        leaveSellTrade1.setTradeId("leaveSellTradeId1");
        leaveSellTrade1.setPrice(50);

        SellTrade leaveSellTrade2 = new SellTrade();
        leaveSellTrade2.setItemId("leaveSellTradeItemId2");
        leaveSellTrade2.setTradeId("leaveSellTradeId2");
        leaveSellTrade2.setPrice(51);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setSoldIn24h(19);

        ItemCurrentPrices leaveCurrentPrices1 = new ItemCurrentPrices();
        leaveCurrentPrices1.setItemId("leaveSellTradeItemId1");
        leaveCurrentPrices1.setMinSellPrice(50);

        ItemCurrentPrices leaveCurrentPrices2 = new ItemCurrentPrices();
        leaveCurrentPrices2.setItemId("leaveSellTradeItemId2");
        leaveCurrentPrices2.setMinSellPrice(50);

        ItemCurrentPrices cancelCurrentPrices = new ItemCurrentPrices();
        cancelCurrentPrices.setItemId("cancelSellTradeItemId");
        cancelCurrentPrices.setMinSellPrice(49);

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrices1,  leaveCurrentPrices2, cancelCurrentPrices);

        int sellLimit = 20;
        int sellSlots = 3;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(user, List.of(leaveSellTrade1, leaveSellTrade2, cancelSellTrade), currentPrices, sellLimit, sellSlots);

        assertEquals(1, result.size());
        assertEquals(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, "cancelSellTradeItemId", "cancelSellTradeId"), result.get(0));
    }

    @Test
    public void createTradeCommandsToKeepUnusedSlotForUser_should_return_expected_commands_by_trade_and_min_prices_diff() {
        SellTrade leaveSellTrade1 = new SellTrade();
        leaveSellTrade1.setItemId("leaveSellTradeItemId1");
        leaveSellTrade1.setTradeId("leaveSellTradeId1");
        leaveSellTrade1.setPrice(50);

        SellTrade leaveSellTrade2 = new SellTrade();
        leaveSellTrade2.setItemId("leaveSellTradeItemId2");
        leaveSellTrade2.setTradeId("leaveSellTradeId2");
        leaveSellTrade2.setPrice(52);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(51);

        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setSoldIn24h(19);

        ItemCurrentPrices leaveCurrentPrices1 = new ItemCurrentPrices();
        leaveCurrentPrices1.setItemId("leaveSellTradeItemId1");
        leaveCurrentPrices1.setMinSellPrice(50);

        ItemCurrentPrices leaveCurrentPrices2 = new ItemCurrentPrices();
        leaveCurrentPrices2.setItemId("leaveSellTradeItemId2");
        leaveCurrentPrices2.setMinSellPrice(51);

        ItemCurrentPrices cancelCurrentPrices = new ItemCurrentPrices();
        cancelCurrentPrices.setItemId("cancelSellTradeItemId");
        cancelCurrentPrices.setMinSellPrice(50);

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrices1, leaveCurrentPrices2, cancelCurrentPrices);

        int sellLimit = 20;
        int sellSlots = 3;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(user, List.of(leaveSellTrade1, leaveSellTrade2, cancelSellTrade), currentPrices, sellLimit, sellSlots);

        assertEquals(1, result.size());
        assertEquals(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, "cancelSellTradeItemId", "cancelSellTradeId"), result.get(0));
    }

    @Test
    public void createTradeCommandsToKeepUnusedSlotForUser_should_return_expected_commands_by_absent_current_price() {
        SellTrade leaveSellTrade1 = new SellTrade();
        leaveSellTrade1.setItemId("leaveSellTradeItemId1");
        leaveSellTrade1.setTradeId("leaveSellTradeId1");
        leaveSellTrade1.setPrice(100);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setSoldIn24h(19);

        ItemCurrentPrices leaveCurrentPrice = new ItemCurrentPrices();
        leaveCurrentPrice.setItemId("leaveSellTradeItemId1");
        leaveCurrentPrice.setMinSellPrice(2);

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrice);

        int sellLimit = 20;
        int sellSlots = 2;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(user, List.of(leaveSellTrade1, cancelSellTrade), currentPrices, sellLimit, sellSlots);

        assertEquals(1, result.size());
        assertEquals(new FastTradeCommand(user.toAuthorizationDTO(), FastTradeManagerCommandType.SELL_ORDER_CANCEL, "cancelSellTradeItemId", "cancelSellTradeId"), result.get(0));
    }

    @Test
    public void createTradeCommandsToKeepUnusedSlotForUser_should_return_empty_if_no_trades_with_bigger_then_min_sell_prices() {
        SellTrade leaveSellTrade1 = new SellTrade();
        leaveSellTrade1.setItemId("leaveSellTradeItemId1");
        leaveSellTrade1.setTradeId("leaveSellTradeId1");
        leaveSellTrade1.setPrice(50);

        SellTrade leaveSellTrade2 = new SellTrade();
        leaveSellTrade2.setItemId("leaveSellTradeItemId2");
        leaveSellTrade2.setTradeId("leaveSellTradeId2");
        leaveSellTrade2.setPrice(50);

        SellTrade leaveSellTrade3 = new SellTrade();
        leaveSellTrade3.setItemId("leaveSellTradeItemId3");
        leaveSellTrade3.setTradeId("leaveSellTradeId3");
        leaveSellTrade3.setPrice(50);

        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setSoldIn24h(19);

        ItemCurrentPrices leaveCurrentPrices1 = new ItemCurrentPrices();
        leaveCurrentPrices1.setItemId("leaveSellTradeItemId1");
        leaveCurrentPrices1.setMinSellPrice(50);

        ItemCurrentPrices leaveCurrentPrices2 = new ItemCurrentPrices();
        leaveCurrentPrices2.setItemId("leaveSellTradeItemId2");
        leaveCurrentPrices2.setMinSellPrice(50);

        ItemCurrentPrices laveCurrentPrices3 = new ItemCurrentPrices();
        laveCurrentPrices3.setItemId("leaveSellTradeItemId3");
        laveCurrentPrices3.setMinSellPrice(50);

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrices1, leaveCurrentPrices2, laveCurrentPrices3);

        int sellLimit = 20;
        int sellSlots = 3;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(user, List.of(leaveSellTrade1, leaveSellTrade2, leaveSellTrade3), currentPrices, sellLimit, sellSlots);

        assertEquals(0, result.size());
    }

    @Test
    public void createTradeCommandsToKeepUnusedSlotForUser_should_return_empty_if_slots_available() {
        SellTrade leaveSellTrade1 = new SellTrade();
        leaveSellTrade1.setItemId("leaveSellTradeItemId1");
        leaveSellTrade1.setTradeId("leaveSellTradeId1");
        leaveSellTrade1.setPrice(50);

        SellTrade leaveSellTrade2 = new SellTrade();
        leaveSellTrade2.setItemId("leaveSellTradeItemId2");
        leaveSellTrade2.setTradeId("leaveSellTradeId2");
        leaveSellTrade2.setPrice(50);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setSoldIn24h(19);

        ItemCurrentPrices leaveCurrentPrices1 = new ItemCurrentPrices();
        leaveCurrentPrices1.setItemId("leaveSellTradeItemId1");
        leaveCurrentPrices1.setMinSellPrice(50);

        ItemCurrentPrices leaveCurrentPrices2 = new ItemCurrentPrices();
        leaveCurrentPrices2.setItemId("leaveSellTradeItemId2");
        leaveCurrentPrices2.setMinSellPrice(50);

        ItemCurrentPrices cancelCurrentPrices = new ItemCurrentPrices();
        cancelCurrentPrices.setItemId("cancelSellTradeItemId");
        cancelCurrentPrices.setMinSellPrice(49);

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrices1, leaveCurrentPrices2, cancelCurrentPrices);

        int sellLimit = 20;
        int sellSlots = 4;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(user, List.of(leaveSellTrade1, leaveSellTrade2, cancelSellTrade), currentPrices, sellLimit, sellSlots);

        assertEquals(0, result.size());
    }

    @Test
    public void createTradeCommandsToKeepUnusedSlotForUser_should_return_empty_if_sell_limit_exceeded() {
        SellTrade leaveSellTrade1 = new SellTrade();
        leaveSellTrade1.setItemId("leaveSellTradeItemId1");
        leaveSellTrade1.setTradeId("leaveSellTradeId1");
        leaveSellTrade1.setPrice(50);

        SellTrade leaveSellTrade2 = new SellTrade();
        leaveSellTrade2.setItemId("leaveSellTradeItemId2");
        leaveSellTrade2.setTradeId("leaveSellTradeId2");
        leaveSellTrade2.setPrice(50);

        SellTrade cancelSellTrade = new SellTrade();
        cancelSellTrade.setItemId("cancelSellTradeItemId");
        cancelSellTrade.setTradeId("cancelSellTradeId");
        cancelSellTrade.setPrice(50);

        ManagedUser user = new ManagedUser();
        user.setUbiProfileId("profileId");
        user.setSoldIn24h(20);

        ItemCurrentPrices leaveCurrentPrices1 = new ItemCurrentPrices();
        leaveCurrentPrices1.setItemId("leaveSellTradeItemId1");
        leaveCurrentPrices1.setMinSellPrice(50);

        ItemCurrentPrices leaveCurrentPrices2 = new ItemCurrentPrices();
        leaveCurrentPrices2.setItemId("leaveSellTradeItemId2");
        leaveCurrentPrices2.setMinSellPrice(50);

        ItemCurrentPrices cancelCurrentPrices = new ItemCurrentPrices();
        cancelCurrentPrices.setItemId("cancelSellTradeItemId");
        cancelCurrentPrices.setMinSellPrice(49);

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrices1, leaveCurrentPrices2, cancelCurrentPrices);

        int sellLimit = 20;
        int sellSlots = 3;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsToKeepUnusedSlotForUser(user, List.of(leaveSellTrade1, leaveSellTrade2, cancelSellTrade), currentPrices, sellLimit, sellSlots);

        assertEquals(0, result.size());
    }
}