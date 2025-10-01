package github.ricemonger.trades_manager.postgres.services;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemEntity;
import github.ricemonger.trades_manager.postgres.repositories.CustomItemPostgresRepository;
import github.ricemonger.trades_manager.postgres.services.entity_mappers.item.ItemEntityMapper;
import github.ricemonger.utils.DTOs.common.Item;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ItemPostgresServiceTest {
    @Autowired
    private ItemPostgresService itemPostgresService;
    @MockBean
    private CustomItemPostgresRepository itemRepository;
    @MockBean
    private ItemEntityMapper itemEntityMapper;

    @Test
    public void findAll_should_return_mapped_repository_result() {
        CustomItemEntity entity1 = Mockito.mock(CustomItemEntity.class);
        CustomItemEntity entity2 = Mockito.mock(CustomItemEntity.class);

        Item item1 = Mockito.mock(Item.class);
        Item item2 = Mockito.mock(Item.class);

        Mockito.when(itemRepository.findAll()).thenReturn(List.of(entity1, entity2));

        Mockito.when(itemEntityMapper.createDTO(entity1)).thenReturn(item1);
        Mockito.when(itemEntityMapper.createDTO(entity2)).thenReturn(item2);

        List<Item> items = itemPostgresService.findAll();

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(item -> item == item1));
        assertTrue(items.stream().anyMatch(item -> item == item2));
    }
}