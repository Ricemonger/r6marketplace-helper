package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemDaySalesUbiStatsEntity;
import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.ItemDaySalesUbiStatsPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.ItemPostgresRepository;
import github.ricemonger.marketplace.services.abstractions.ItemSaleUbiStatsService;
import github.ricemonger.utils.dtos.ItemDaySales;
import github.ricemonger.utils.dtos.ItemSaleUbiStats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemDaySalesUbiStatsPostgresService implements ItemSaleUbiStatsService {

    private final ItemDaySalesUbiStatsPostgresRepository itemDaySalesUbiStatsRepository;

    private final ItemPostgresRepository itemRepository;

    @Transactional
    public void saveAll(Collection<ItemSaleUbiStats> statsList) {
        if (statsList == null) {
            return;
        }

        List<String> itemNames = itemRepository.findAllItemIds();

        for (ItemSaleUbiStats stats : statsList) {
            try {
                if (!itemNames.contains(stats.getItemId())) {
                    log.error("Item with id {} not found, day sales parsing for this item skipped", stats.getItemId());
                    continue;
                }
                ItemEntity itemEntity = itemRepository.getReferenceById(stats.getItemId());
                List<ItemDaySalesUbiStatsEntity> salesEntities = new ArrayList<>();
                for (ItemDaySales itemDaySales : stats.getLast30DaysSales()) {
                    ItemDaySalesUbiStatsEntity salesEntity = new ItemDaySalesUbiStatsEntity(itemEntity, itemDaySales);
                    salesEntities.add(salesEntity);
                }
                itemDaySalesUbiStatsRepository.saveAll(salesEntities);
            } catch (Throwable e) {
                log.error("Item with id {} not found", stats.getItemId());
            }
        }
    }
}
