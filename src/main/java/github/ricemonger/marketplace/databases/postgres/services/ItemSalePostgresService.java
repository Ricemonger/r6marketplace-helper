package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemSaleEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.ItemSalePostgresRepository;
import github.ricemonger.marketplace.services.abstractions.ItemSaleDatabaseService;
import github.ricemonger.utils.dtos.Item;
import github.ricemonger.utils.dtos.ItemSale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemSalePostgresService implements ItemSaleDatabaseService {

    private final ItemSalePostgresRepository itemSaleRepository;

    public void saveAll(Collection<Item> items) {
        if (items != null) {
            itemSaleRepository.saveAll(items.stream().map(ItemSaleEntity::new).toList());
        }
    }

    public List<ItemSale> findAll() {
        return itemSaleRepository.findAll().stream().map(ItemSaleEntity::toItemSale).toList();
    }
}
