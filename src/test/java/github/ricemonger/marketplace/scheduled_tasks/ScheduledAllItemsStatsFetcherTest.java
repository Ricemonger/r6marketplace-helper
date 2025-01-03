package github.ricemonger.marketplace.scheduled_tasks;

import github.ricemonger.marketplace.graphQl.GraphQlClientService;
import github.ricemonger.marketplace.services.CommonValuesService;
import github.ricemonger.marketplace.services.ItemService;
import github.ricemonger.telegramBot.TelegramBotService;
import github.ricemonger.utils.DTOs.common.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ScheduledAllItemsStatsFetcherTest {

    @MockBean
    private GraphQlClientService graphQlClientService;

    @MockBean
    private TelegramBotService telegramBotService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommonValuesService commonValuesService;

    @Autowired
    private ScheduledAllItemsStatsFetcher scheduledAllItemsStatsFetcher;

    @Test
    public void fetchAllItemStats_should_save_items_from_graphql_and_calculate() {
        List<Item> itemMainFields = new ArrayList<>();

        when(graphQlClientService.fetchAllItemStats()).thenReturn(itemMainFields);

        scheduledAllItemsStatsFetcher.fetchAllItemStats();

        verify(itemService).saveAllItemsMainFields(same(itemMainFields));
        verify(itemService).saveAllItemLastSales(same(itemMainFields));
    }

    @Test
    public void fetchAllItemStats_should_call_services_when_item_amount_increased() {
        List<Item> items = new ArrayList<>();
        items.add(new Item());

        when(graphQlClientService.fetchAllItemStats()).thenReturn(items);
        when(commonValuesService.getExpectedItemCount()).thenReturn(0);

        scheduledAllItemsStatsFetcher.fetchAllItemStats();

        verify(itemService).saveAllItemsMainFields(same(items));
        verify(itemService).saveAllItemLastSales(same(items));
        verify(commonValuesService).setExpectedItemCount(1);
        verify(telegramBotService).notifyAllUsersAboutItemAmountIncrease(0, 1);
    }
}
