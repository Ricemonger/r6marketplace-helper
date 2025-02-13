package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

class ItemTradePriorityByExpressionCalculatorTest {

    private ItemTradePriorityByExpressionCalculator itemTradePriorityByExpressionCalculator = spy(new ItemTradePriorityByExpressionCalculator());

    @Test
    public void calculateTradePriority_should_return_expected_result_first_return() {
        Item item = new Item();

        Integer price = 100;

        Integer time = 1000;

        String expression1 = "if price == 100 then return 101$if time == 1000 then return 1001$return 10001";

        Long result1 = itemTradePriorityByExpressionCalculator.calculateTradePriority(expression1, item, price, time);

        assertEquals(101, result1);

        String expression2 = "if time == 1000 then return 1001$if price == 100 then return 101$return 10001";

        Long result2 = itemTradePriorityByExpressionCalculator.calculateTradePriority(expression2, item, price, time);

        assertEquals(1001, result2);

        String expression3 = "if time != 1000 then return 1001$if price > 100 then return 101$return 10001";

        Long result3 = itemTradePriorityByExpressionCalculator.calculateTradePriority(expression3, item, price, time);

        assertEquals(10001, result3);
    }

    @Test
    public void calculateTradePriority_should_return_expected_result_user_variables() {
        Item item = new Item();

        Integer price = 100;

        Integer time = 1000;

        String expression1 = "a = 10$b = 20$if a < 11 then b = 30$ return b+a";

        Long result1 = itemTradePriorityByExpressionCalculator.calculateTradePriority(expression1, item, price, time);

        assertEquals(40, result1);
    }

    @Test
    public void calculateTradePriority_should_return_expected_result_item_variables() {
        Item item = new Item();
        item.setMaxBuyPrice(2);

        Integer price = 100;

        Integer time = 1000;

        String expression1 = "a = 3$b = 20$if a < 11 then b = 30$ return maxBuyPrice^a";

        Long result1 = itemTradePriorityByExpressionCalculator.calculateTradePriority(expression1, item, price, time);

        assertEquals(8, result1);
    }
}