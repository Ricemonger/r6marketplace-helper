package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.repositories.ItemDaySalesUbiStatsPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.item.ItemDaySalesUbiStatsEntityMapper;
import github.ricemonger.marketplace.services.abstractions.ItemSaleUbiStatsService;
import github.ricemonger.utils.DTOs.common.GroupedItemDaySalesUbiStats;
import github.ricemonger.utils.DTOs.common.ItemDaySalesUbiStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemDaySalesUbiStatsPostgresService implements ItemSaleUbiStatsService {

    private final ItemDaySalesUbiStatsPostgresRepository itemDaySalesUbiStatsRepository;

    private final ItemDaySalesUbiStatsEntityMapper itemDaySalesUbiStatsEntityMapper;

    @Override
    @Transactional
    public void saveAll(Collection<GroupedItemDaySalesUbiStats> groupedItemDaySalesUbiStatsList) {
        itemDaySalesUbiStatsRepository.saveAll(itemDaySalesUbiStatsEntityMapper.createEntities(groupedItemDaySalesUbiStatsList));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDaySalesUbiStats> findAllForLastMonth() {
        return itemDaySalesUbiStatsRepository.findAllForLastMonth().stream().map(itemDaySalesUbiStatsEntityMapper::createDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDaySalesUbiStats> findAll() {
        return itemDaySalesUbiStatsRepository.findAll().stream().map(itemDaySalesUbiStatsEntityMapper::createDTO).toList();
    }
}
