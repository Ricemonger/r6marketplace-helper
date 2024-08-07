package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.ItemPostgresRepository;
import github.ricemonger.utils.dtos.Item;
import github.ricemonger.utils.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemPostgresServiceTest {
    @Autowired
    private ItemPostgresService itemService;
    @Autowired
    private ItemPostgresRepository itemRepository;

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    public void saveAll_should_create_new_item_in_db_if_doesnt_exist() {

        List<Item> items = List.of(
                createItem("1"),
                createItem("2")
        );

        itemService.saveAll(items);

        assertEquals(2, itemRepository.count());
    }

    @Test
    public void saveAll_should_update_existing_item_in_db() {

        Item item1 = new Item();
        item1.setItemId("1");
        item1.setName("old name");

        itemRepository.save(new ItemEntity(item1));

        Item item2 = new Item();
        item2.setItemId("1");
        item2.setName("new name");

        itemService.saveAll(List.of(item2));

        assertEquals(1, itemRepository.count());
        assertEquals("new name", itemRepository.findById("1").get().getName());
    }

    @Test
    public void findById_should_return_item_by_id() {
        Item item = createItem("1");
        itemRepository.save(new ItemEntity(item));

        assertEquals(item, itemService.findById("1"));
    }

    @Test
    public void findById_should_throw_exception_if_item_doesnt_exist() {
        assertThrows(ItemNotFoundException.class, () -> itemService.findById("1"));
    }

    @Test
    public void findAll_should_return_all_items() {
        Item item1 = createItem("1");
        Item item2 = createItem("2");
        itemRepository.save(new ItemEntity(item1));
        itemRepository.save(new ItemEntity(item2));

        List<Item> result = itemService.findAll();

        assertTrue(List.of(item1, item2).containsAll(result) && result.containsAll(List.of(item1, item2)));
    }

    private Item createItem(String itemId) {
        Item item = new Item();
        item.setItemId(itemId);
        return item;
    }
}