package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.*;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.DTOs.personal.SellTrade;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TradeCommandsFactoryTest {
    @Autowired
    private TradeCommandsFactory tradeCommandsFactory;

    @Test
    public void createTradeCommandsForUser_should_return_expected_commands_and_add_untouched_updated_created_trades_to_already_managed_list() {
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

        SellTrade leaveCauseAlreadyManagedTrade = new SellTrade();
        leaveCauseAlreadyManagedTrade.setItemId("leaveCauseAlreadyManagedTradeItemId");
        leaveCauseAlreadyManagedTrade.setTradeId("leaveCauseAlreadyManagedTradeId");
        leaveCauseAlreadyManagedTrade.setPrice(50);

        List<SellTrade> sellTrades = List.of(updateSellTrade, cancelSellTrade, alreadySameSellTrade, leaveSellTrade, leaveCauseAlreadyManagedTrade);

        PotentialTrade updateExistingPotentialTrade = new PotentialTrade();
        updateExistingPotentialTrade.setItemId("updateSellTradeItemId");
        updateExistingPotentialTrade.setPrice(98);
        updateExistingPotentialTrade.setMonthMedianPriceDifference(2);
        updateExistingPotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade createNewPotentialTrade = new PotentialTrade();
        createNewPotentialTrade.setItemId("createNewPotentialTradeItemId");
        createNewPotentialTrade.setPrice(50);
        createNewPotentialTrade.setMonthMedianPriceDifference(3);
        createNewPotentialTrade.setMonthMedianPriceDifferencePercentage(3);

        PotentialTrade alreadySamePotentialTrade = new PotentialTrade();
        alreadySamePotentialTrade.setItemId("alreadySameSellTradeItemId");
        alreadySamePotentialTrade.setPrice(49);
        alreadySamePotentialTrade.setMonthMedianPriceDifference(2);
        alreadySamePotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade skipCauseResaleLockPotentialTrade = new PotentialTrade();
        skipCauseResaleLockPotentialTrade.setItemId("lockedItemId1");
        skipCauseResaleLockPotentialTrade.setPrice(1000);
        skipCauseResaleLockPotentialTrade.setMonthMedianPriceDifference(200);
        skipCauseResaleLockPotentialTrade.setMonthMedianPriceDifferencePercentage(200);

        List<PotentialTrade> items = List.of(updateExistingPotentialTrade, createNewPotentialTrade, alreadySamePotentialTrade, skipCauseResaleLockPotentialTrade);

        List<ItemCurrentPrices> currentPrices = List.of();

        ItemMedianPriceAndRarity cancelMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        cancelMedianPriceAndRarity.setItemId("cancelSellTradeItemId");
        cancelMedianPriceAndRarity.setMonthMedianPrice(49);

        ItemMedianPriceAndRarity leaveCauseAlreadyManagedMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveCauseAlreadyManagedMedianPriceAndRarity.setItemId("leaveCauseAlreadyManagedTradeItemId");
        leaveCauseAlreadyManagedMedianPriceAndRarity.setMonthMedianPrice(1000);

        ItemMedianPriceAndRarity leaveMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveMedianPriceAndRarity.setItemId("leaveSellTradeItemId");
        leaveMedianPriceAndRarity.setMonthMedianPrice(48);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(cancelMedianPriceAndRarity, leaveCauseAlreadyManagedMedianPriceAndRarity, leaveMedianPriceAndRarity);

        Set<String> alreadyManagedItems = new HashSet<>();
        alreadyManagedItems.add("leaveCauseAlreadyManagedTradeItemId");

        int sellLimit = 20;
        int sellSlots = 5;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsForUser(user, sellTrades, currentPrices, medianPriceAndRarities, items, alreadyManagedItems, sellLimit, sellSlots);

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

        System.out.println("Already Managed Items:");
        alreadyManagedItems.forEach(System.out::println);

        assertEquals(4, alreadyManagedItems.size());
        assertTrue(alreadyManagedItems.contains("leaveCauseAlreadyManagedTradeItemId"));
        assertTrue(alreadyManagedItems.contains("createNewPotentialTradeItemId"));
        assertTrue(alreadyManagedItems.contains("alreadySameSellTradeItemId"));
        assertTrue(alreadyManagedItems.contains("updateSellTradeItemId"));
    }

    @Test
    public void createTradeCommandsForUser_should_return_expected_commands_if_too_many_potentialTrades_and_add_untouched_updated_created_trades_to_already_managed_list() {
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

        SellTrade leaveCauseAlreadyManagedTrade = new SellTrade();
        leaveCauseAlreadyManagedTrade.setItemId("leaveCauseAlreadyManagedTradeItemId");
        leaveCauseAlreadyManagedTrade.setTradeId("leaveCauseAlreadyManagedTradeId");
        leaveCauseAlreadyManagedTrade.setPrice(50);

        List<SellTrade> sellTrades = List.of(updateSellTrade, cancelSellTrade, alreadySameSellTrade, leaveSellTrade, leaveCauseAlreadyManagedTrade);

        PotentialTrade updateExistingPotentialTrade = new PotentialTrade();
        updateExistingPotentialTrade.setItemId("updateSellTradeItemId");
        updateExistingPotentialTrade.setPrice(98);
        updateExistingPotentialTrade.setMonthMedianPriceDifference(2);
        updateExistingPotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade createNewPotentialTrade = new PotentialTrade();
        createNewPotentialTrade.setItemId("createNewPotentialTradeItemId");
        createNewPotentialTrade.setPrice(50);
        createNewPotentialTrade.setMonthMedianPriceDifference(2);
        createNewPotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade createNewPotentialTradeByBuyPrice = new PotentialTrade();
        createNewPotentialTradeByBuyPrice.setItemId("createNewPotentialTradeByBuyPriceItemId");
        createNewPotentialTradeByBuyPrice.setPrice(1);
        createNewPotentialTradeByBuyPrice.setMonthMedianPriceDifference(1);
        createNewPotentialTradeByBuyPrice.setMonthMedianPriceDifferencePercentage(1);
        createNewPotentialTradeByBuyPrice.setSellByMaxBuyPrice(true);

        PotentialTrade createNewPotentialTradeLowerProfit = new PotentialTrade();
        createNewPotentialTradeLowerProfit.setItemId("createNewPotentialTradeLowerProfitItemId");
        createNewPotentialTradeLowerProfit.setPrice(50);
        createNewPotentialTradeLowerProfit.setMonthMedianPriceDifference(2);
        createNewPotentialTradeLowerProfit.setMonthMedianPriceDifferencePercentage(1);

        PotentialTrade alreadySamePotentialTrade = new PotentialTrade();
        alreadySamePotentialTrade.setItemId("alreadySameSellTradeItemId");
        alreadySamePotentialTrade.setPrice(49);
        alreadySamePotentialTrade.setMonthMedianPriceDifference(2);
        alreadySamePotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade skipCauseResaleLockPotentialTrade = new PotentialTrade();
        skipCauseResaleLockPotentialTrade.setItemId("lockedItemId1");
        skipCauseResaleLockPotentialTrade.setPrice(1000);
        skipCauseResaleLockPotentialTrade.setMonthMedianPriceDifference(200);
        skipCauseResaleLockPotentialTrade.setMonthMedianPriceDifferencePercentage(200);

        List<PotentialTrade> items = List.of(updateExistingPotentialTrade, createNewPotentialTrade, alreadySamePotentialTrade,
                skipCauseResaleLockPotentialTrade, createNewPotentialTradeLowerProfit, createNewPotentialTradeByBuyPrice);

        ItemCurrentPrices cancelCurrentPrices = new ItemCurrentPrices();
        cancelCurrentPrices.setItemId("cancelSellTradeItemId");
        cancelCurrentPrices.setMinSellPrice(49);

        ItemCurrentPrices leaveCurrentPrices = new ItemCurrentPrices();
        leaveCurrentPrices.setItemId("leaveSellTradeItemId");
        leaveCurrentPrices.setMinSellPrice(50);

        List<ItemCurrentPrices> currentPrices = List.of(cancelCurrentPrices, leaveCurrentPrices);

        ItemMedianPriceAndRarity cancelMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        cancelMedianPriceAndRarity.setItemId("cancelSellTradeItemId");
        cancelMedianPriceAndRarity.setMonthMedianPrice(50);

        ItemMedianPriceAndRarity leaveMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveMedianPriceAndRarity.setItemId("leaveSellTradeItemId");
        leaveMedianPriceAndRarity.setMonthMedianPrice(47);

        ItemMedianPriceAndRarity leaveCauseAlreadyManagedMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveCauseAlreadyManagedMedianPriceAndRarity.setItemId("leaveCauseAlreadyManagedTradeItemId");
        leaveCauseAlreadyManagedMedianPriceAndRarity.setMonthMedianPrice(40);

        ItemMedianPriceAndRarity updateMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        updateMedianPriceAndRarity.setItemId("updateSellTradeItemId");
        updateMedianPriceAndRarity.setMonthMedianPrice(90);

        ItemMedianPriceAndRarity alreadySameMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        alreadySameMedianPriceAndRarity.setItemId("alreadySameSellTradeItemId");
        alreadySameMedianPriceAndRarity.setMonthMedianPrice(40);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(cancelMedianPriceAndRarity, leaveMedianPriceAndRarity, updateMedianPriceAndRarity, alreadySameMedianPriceAndRarity, leaveCauseAlreadyManagedMedianPriceAndRarity);

        Set<String> alreadyManagedItems = new HashSet<>();
        alreadyManagedItems.add("leaveCauseAlreadyManagedTradeItemId");

        int sellLimit = 20;
        int sellSlots = 5;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsForUser(user, sellTrades, currentPrices, medianPriceAndRarities, items, alreadyManagedItems, sellLimit, sellSlots);

        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setProfileId("profileId");

        FastTradeCommand createExpectedCommandByBuyPrice = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CREATE, "createNewPotentialTradeByBuyPriceItemId", 1);
        FastTradeCommand cancelExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_CANCEL, "cancelSellTradeItemId", "cancelSellTradeId");
        FastTradeCommand updateExpectedCommand = new FastTradeCommand(authorizationDTO, FastTradeManagerCommandType.SELL_ORDER_UPDATE, "updateSellTradeItemId", "updateSellTradeId", 98);

        System.out.println("Result:");
        result.forEach(System.out::println);

        assertEquals(3, result.size());
        assertTrue(result.contains(createExpectedCommandByBuyPrice));
        assertTrue(result.contains(cancelExpectedCommand));
        assertTrue(result.contains(updateExpectedCommand));

        System.out.println("Already Managed Items:");
        alreadyManagedItems.forEach(System.out::println);

        assertEquals(4, alreadyManagedItems.size());
        assertTrue(alreadyManagedItems.contains("leaveCauseAlreadyManagedTradeItemId"));
        assertTrue(alreadyManagedItems.contains("createNewPotentialTradeByBuyPriceItemId"));
        assertTrue(alreadyManagedItems.contains("alreadySameSellTradeItemId"));
        assertTrue(alreadyManagedItems.contains("updateSellTradeItemId"));
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

        PotentialTrade updateExistingPotentialTrade = new PotentialTrade();
        updateExistingPotentialTrade.setItemId("updateSellTradeItemId");
        updateExistingPotentialTrade.setPrice(98);
        updateExistingPotentialTrade.setMonthMedianPriceDifference(2);
        updateExistingPotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade createNewPotentialTrade = new PotentialTrade();
        createNewPotentialTrade.setItemId("createNewPotentialTradeItemId");
        createNewPotentialTrade.setPrice(50);
        createNewPotentialTrade.setMonthMedianPriceDifference(2);
        createNewPotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade createNewPotentialTradeLowerProfit = new PotentialTrade();
        createNewPotentialTradeLowerProfit.setItemId("createNewPotentialTradeLowerProfitItemId");
        createNewPotentialTradeLowerProfit.setPrice(50);
        createNewPotentialTradeLowerProfit.setMonthMedianPriceDifference(1);
        createNewPotentialTradeLowerProfit.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade alreadySamePotentialTrade = new PotentialTrade();
        alreadySamePotentialTrade.setItemId("alreadySameSellTradeItemId");
        alreadySamePotentialTrade.setPrice(49);
        alreadySamePotentialTrade.setMonthMedianPriceDifference(2);
        alreadySamePotentialTrade.setMonthMedianPriceDifferencePercentage(2);

        PotentialTrade skipCauseResaleLockPotentialTrade = new PotentialTrade();
        skipCauseResaleLockPotentialTrade.setItemId("lockedItemId1");
        skipCauseResaleLockPotentialTrade.setPrice(1000);
        skipCauseResaleLockPotentialTrade.setMonthMedianPriceDifference(200);
        skipCauseResaleLockPotentialTrade.setMonthMedianPriceDifferencePercentage(200);

        List<PotentialTrade> items = List.of(updateExistingPotentialTrade, createNewPotentialTrade, alreadySamePotentialTrade,
                skipCauseResaleLockPotentialTrade, createNewPotentialTradeLowerProfit);

        List<ItemCurrentPrices> currentPrices = List.of();

        ItemMedianPriceAndRarity cancelMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        cancelMedianPriceAndRarity.setItemId("cancelSellTradeItemId");
        cancelMedianPriceAndRarity.setMonthMedianPrice(48);

        ItemMedianPriceAndRarity leaveMedianPriceAndRarity = new ItemMedianPriceAndRarity();
        leaveMedianPriceAndRarity.setItemId("leaveSellTradeItemId");
        leaveMedianPriceAndRarity.setMonthMedianPrice(49);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(cancelMedianPriceAndRarity, leaveMedianPriceAndRarity);

        Set<String> alreadyManagedItems = new HashSet<>();

        int sellLimit = 20;
        int sellSlots = 4;

        List<FastTradeCommand> result = tradeCommandsFactory.createTradeCommandsForUser(user, sellTrades, currentPrices, medianPriceAndRarities, items, alreadyManagedItems, sellLimit, sellSlots);

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

        List<ItemCurrentPrices> currentPrices = List.of(leaveCurrentPrices1, leaveCurrentPrices2, cancelCurrentPrices);

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