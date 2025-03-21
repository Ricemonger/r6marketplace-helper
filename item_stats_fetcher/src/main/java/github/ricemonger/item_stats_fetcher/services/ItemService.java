package github.ricemonger.item_stats_fetcher.services;

import github.ricemonger.item_stats_fetcher.services.abstractions.ItemDatabaseService;
import github.ricemonger.item_stats_fetcher.services.abstractions.ItemSaleDatabaseService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemMainFieldsI;
import github.ricemonger.utils.DTOs.common.ItemMinSellPrice;
import github.ricemonger.utils.DTOs.common.SoldItemDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class ItemService {

    private final ItemDatabaseService itemDatabaseService;

    private final ItemSaleDatabaseService itemSaleDatabaseService;

    public void saveAllItemsMainFields(Collection<? extends ItemMainFieldsI> items) {
        itemDatabaseService.saveAllItemsMainFields(items);
    }

    public void updateAllItemsMainFieldsExceptTags(Collection<Item> items) {
        itemDatabaseService.updateAllItemsMainFieldsExceptTags(items);
    }

    public void updateAllItemsMinSellPrice(Collection<? extends ItemMinSellPrice> items) {
        itemDatabaseService.updateAllItemsMinSellPrice(items);
    }

    public void insertAllItemsLastSales(Collection<? extends SoldItemDetails> items) {
        itemSaleDatabaseService.insertAllItemsLastSales(items);
    }
}
