package github.ricemonger.telegramBot.update_consumer;

import github.ricemonger.marketplace.services.CommonValuesService;
import github.ricemonger.marketplace.services.DTOs.TelegramUserInput;
import github.ricemonger.marketplace.services.DTOs.TradeByFiltersManager;
import github.ricemonger.marketplace.services.ItemService;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TradeManagerFromInputsMapperTest {
    @Autowired
    private TradeManagerFromInputsMapper tradeManagerFromInputsMapper;
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private ItemService itemService;

    @Test
    public void mapToTradeByItemIdManager_should_map_inputs_to_manager_with_valid_inputs() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID, "itemId"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE, "120"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE, "360"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_PRIORITY, "2"));

        TradeByItemIdManager expected = new TradeByItemIdManager();
        expected.setTradeOperationType(TradeOperationType.BUY);
        expected.setItemId("itemId");
        expected.setEnabled(true);
        expected.setSellBoundaryPrice(120);
        expected.setBuyBoundaryPrice(360);
        expected.setPriorityMultiplier(2);

        Item item = new Item();
        item.setMinSellPrice(0);
        item.setMaxBuyPrice(120);
        item.setRarity(ItemRarity.UNCOMMON);

        when(itemService.getItemById("itemId")).thenReturn(item);
        when(commonValuesService.getMinimumPriceByRarity(item.getRarity())).thenReturn(0);
        when(commonValuesService.getMaximumPriceByRarity(item.getRarity())).thenReturn(1000);

        assertEquals(expected, tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, TradeOperationType.BUY, true));
    }

    @Test
    public void mapToTradeByItemIdManager_should_map_inputs_to_manager_with_invalid_inputs() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID, "itemId"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_PRIORITY, ""));

        TradeByItemIdManager expected = new TradeByItemIdManager();
        expected.setTradeOperationType(TradeOperationType.SELL);
        expected.setItemId("itemId");
        expected.setEnabled(false);
        expected.setSellBoundaryPrice(120);
        expected.setBuyBoundaryPrice(100000);
        expected.setPriorityMultiplier(1);

        Item item = new Item();
        item.setMinSellPrice(150);
        item.setMaxBuyPrice(0);
        item.setRarity(ItemRarity.UNCOMMON);

        when(itemService.getItemById("itemId")).thenReturn(item);
        when(commonValuesService.getMinimumPriceByRarity(item.getRarity())).thenReturn(120);
        when(commonValuesService.getMaximumPriceByRarity(item.getRarity())).thenReturn(100000);

        TradeByItemIdManager actual = tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, TradeOperationType.SELL, false);

        assertEquals(expected, actual);
    }

    @Test
    public void mapToTradeByItemIdManager_should_map_inputs_to_manager_with_wrong_conjunctions_of_prices() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID, "itemId"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE, "161"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE, "152"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_PRIORITY, "1"));

        TradeByItemIdManager expected = new TradeByItemIdManager();
        expected.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        expected.setItemId("itemId");
        expected.setEnabled(false);
        expected.setSellBoundaryPrice(161);
        expected.setBuyBoundaryPrice(152);
        expected.setPriorityMultiplier(1);

        Item item = new Item();
        item.setMinSellPrice(150);
        item.setMaxBuyPrice(0);
        item.setRarity(ItemRarity.UNCOMMON);

        when(itemService.getItemById("itemId")).thenReturn(item);
        when(commonValuesService.getMinimumPriceByRarity(item.getRarity())).thenReturn(0);
        when(commonValuesService.getMaximumPriceByRarity(item.getRarity())).thenReturn(360);

        TradeByItemIdManager actual = tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, TradeOperationType.BUY_AND_SELL, false);

        assertEquals(expected, actual);
    }

    @Test
    public void mapToTradeByItemIdManager_should_map_inputs_to_manager_with_out_of_limit_low_prices() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID, "itemId"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE, "110"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE, "110"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_PRIORITY, "1"));

        TradeByItemIdManager expected = new TradeByItemIdManager();
        expected.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        expected.setItemId("itemId");
        expected.setEnabled(true);
        expected.setSellBoundaryPrice(120);
        expected.setBuyBoundaryPrice(120);
        expected.setPriorityMultiplier(1);

        Item item = new Item();
        item.setMinSellPrice(150);
        item.setMaxBuyPrice(0);
        item.setRarity(ItemRarity.UNCOMMON);

        when(itemService.getItemById("itemId")).thenReturn(item);
        when(commonValuesService.getMinimumPriceByRarity(item.getRarity())).thenReturn(120);
        when(commonValuesService.getMaximumPriceByRarity(item.getRarity())).thenReturn(360);

        TradeByItemIdManager actual = tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, TradeOperationType.BUY_AND_SELL, true);

        assertEquals(expected, actual);
    }

    @Test
    public void mapToTradeByItemIdManager_should_map_inputs_to_manager_with_out_of_limit_high_prices() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID, "itemId"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE, "151000"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE, "151000"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_PRIORITY, "1"));

        TradeByItemIdManager expected = new TradeByItemIdManager();
        expected.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        expected.setItemId("itemId");
        expected.setEnabled(true);
        expected.setSellBoundaryPrice(150_000);
        expected.setBuyBoundaryPrice(150_000);
        expected.setPriorityMultiplier(1);

        Item item = new Item();
        item.setMinSellPrice(150);
        item.setMaxBuyPrice(0);
        item.setRarity(ItemRarity.UNCOMMON);

        when(itemService.getItemById("itemId")).thenReturn(item);
        when(commonValuesService.getMinimumPriceByRarity(item.getRarity())).thenReturn(0);
        when(commonValuesService.getMaximumPriceByRarity(item.getRarity())).thenReturn(150_000);

        TradeByItemIdManager actual = tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, TradeOperationType.BUY_AND_SELL, true);

        assertEquals(expected, actual);
    }

    @Test
    public void mapToTradeByItemIdManager_should_map_inputs_to_manager_with_out_of_limit_priority() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_ITEM_ID, "itemId"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE, "120"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_BUY_PRICE, "360"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_ITEM_ID_MANAGER_PRIORITY, "0"));

        TradeByItemIdManager expected = new TradeByItemIdManager();
        expected.setTradeOperationType(TradeOperationType.BUY);
        expected.setItemId("itemId");
        expected.setEnabled(true);
        expected.setSellBoundaryPrice(120);
        expected.setBuyBoundaryPrice(360);
        expected.setPriorityMultiplier(1);

        Item item = new Item();
        item.setMinSellPrice(0);
        item.setMaxBuyPrice(120);
        item.setRarity(ItemRarity.UNCOMMON);

        when(itemService.getItemById("itemId")).thenReturn(item);
        when(commonValuesService.getMinimumPriceByRarity(item.getRarity())).thenReturn(0);
        when(commonValuesService.getMaximumPriceByRarity(item.getRarity())).thenReturn(1000);

        assertEquals(expected, tradeManagerFromInputsMapper.generateTradeByItemIdManagerByUserInput(inputs, TradeOperationType.BUY, true));
    }

    @Test
    public void mapToTradeByFiltersManager_should_map_inputs_to_manager_with_valid_inputs() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME, "name"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_TRADE_TYPE, Callbacks.TRADE_BY_FILTERS_MANAGER_TYPE_BUY_EDIT));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_BUY_SELL_PROFIT, "100"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_MEDIAN_PRICE_DIFFERENCE_PERCENT, "30"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_PRIORITY, "2"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES, "filter1,filter2|filter3"));

        TradeByFiltersManager expected = new TradeByFiltersManager();
        expected.setName("name");
        expected.setEnabled(true);
        expected.setTradeOperationType(TradeOperationType.BUY);
        expected.setAppliedFilters(List.of("filter1", "filter2", "filter3"));
        expected.setMinDifferenceFromMedianPrice(100);
        expected.setMinDifferenceFromMedianPricePercent(30);
        expected.setPriorityMultiplier(2);

        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(1000);

        assertEquals(expected, tradeManagerFromInputsMapper.generateTradeByFiltersManagerByUserInput(inputs, true));
    }

    @Test
    public void mapToTradeByFiltersManager_should_map_inputs_to_manager_with_invalid_inputs() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_TRADE_TYPE, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_BUY_SELL_PROFIT, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_MEDIAN_PRICE_DIFFERENCE_PERCENT, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_PRIORITY, ""));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES, ""));

        TradeByFiltersManager expected = new TradeByFiltersManager();
        expected.setName("");
        expected.setEnabled(false);
        expected.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        expected.setAppliedFilters(List.of());
        expected.setMinDifferenceFromMedianPrice(50);
        expected.setMinDifferenceFromMedianPricePercent(10);
        expected.setPriorityMultiplier(1);

        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(1000);

        assertEquals(expected, tradeManagerFromInputsMapper.generateTradeByFiltersManagerByUserInput(inputs, false));
    }

    @Test
    public void mapToTradeByFiltersManager_should_map_inputs_to_manager_with_minimum_values() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME, null));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_TRADE_TYPE, Callbacks.TRADE_BY_FILTERS_MANAGER_TYPE_SELL_EDIT));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES, null));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_BUY_SELL_PROFIT, "-150001"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_MEDIAN_PRICE_DIFFERENCE_PERCENT, "-2147483647"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_PRIORITY, "0"));

        TradeByFiltersManager expected = new TradeByFiltersManager();
        expected.setName(null);
        expected.setEnabled(true);
        expected.setTradeOperationType(TradeOperationType.SELL);
        expected.setAppliedFilters(List.of());
        expected.setMinDifferenceFromMedianPrice(-150_000);
        expected.setMinDifferenceFromMedianPricePercent(-2147483647);
        expected.setPriorityMultiplier(1);

        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(150_000);

        assertEquals(expected, tradeManagerFromInputsMapper.generateTradeByFiltersManagerByUserInput(inputs, true));
    }

    @Test
    public void mapToTradeByFiltersManager_should_map_inputs_to_manager_with_maximum_values() {
        String chatId = "chatId";
        List<TelegramUserInput> inputs = new ArrayList<>();
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_NAME, "name"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_TRADE_TYPE,
                Callbacks.TRADE_BY_FILTERS_MANAGER_TYPE_BUY_AND_SELL_EDIT));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES, null));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_BUY_SELL_PROFIT, "150001"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_MIN_MEDIAN_PRICE_DIFFERENCE_PERCENT, "2147483647"));
        inputs.add(new TelegramUserInput(chatId, InputState.TRADE_BY_FILTERS_MANAGER_PRIORITY, "2147483647"));

        TradeByFiltersManager expected = new TradeByFiltersManager();
        expected.setName("name");
        expected.setEnabled(true);
        expected.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        expected.setAppliedFilters(List.of());
        expected.setMinDifferenceFromMedianPrice(150_000);
        expected.setMinDifferenceFromMedianPricePercent(Integer.MAX_VALUE);
        expected.setPriorityMultiplier(Integer.MAX_VALUE);

        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(150_000);

        assertEquals(expected, tradeManagerFromInputsMapper.generateTradeByFiltersManagerByUserInput(inputs, true));
    }
}