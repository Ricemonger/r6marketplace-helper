package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.repositories.ItemSalePostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_factories.item.ItemSaleEntityFactory;
import github.ricemonger.marketplace.services.abstractions.ItemSaleDatabaseService;
import github.ricemonger.utils.DTOs.items.ItemSale;
import github.ricemonger.utils.DTOs.items.SoldItemDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSalePostgresService implements ItemSaleDatabaseService {

    private final ItemSalePostgresRepository itemSaleRepository;

    private ItemSaleEntityFactory itemSaleEntityFactory;

    @Override
    @Transactional
    public void saveAll(Collection<? extends SoldItemDetails> soldItems) {
        itemSaleRepository.saveAll(itemSaleEntityFactory.createEntities(soldItems));
    }

    @Override
    public List<ItemSale> findAll() {
        return itemSaleRepository.findAll().stream().map(itemSaleEntityFactory::createDTO).toList();
    }

    @Override
    public List<ItemSale> findAllForLastMonth() {
        return itemSaleRepository.findAllForLastMonth().stream().map(itemSaleEntityFactory::createDTO).toList();
    }
}

