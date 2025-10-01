package github.ricemonger.item_stats_fetcher.databases.postgres.services.entity_mappers;

import github.ricemonger.item_stats_fetcher.databases.postgres.custom_entities.ItemMainFieldsEntity;
import github.ricemonger.item_stats_fetcher.databases.postgres.custom_entities.TagEntity;
import github.ricemonger.item_stats_fetcher.databases.postgres.repositories.CustomTagValuePostgresRepository;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemMainFieldsPostgresMapperTest {

    @Autowired
    private ItemMainFieldsPostgresMapper itemMainFieldsPostgresMapper;

    @MockBean
    private CustomTagValuePostgresRepository tagValueRepository;

    @Test
    public void createEntities_should_properly_map_entities() {
        Item item1 = new Item();
        item1.setItemId("itemId");
        item1.setAssetUrl("assetUrl");
        item1.setName("name");
        item1.setTags(List.of("tag"));
        item1.setRarity(ItemRarity.RARE);
        item1.setType(ItemType.WeaponSkin);
        item1.setMaxBuyPrice(1);
        item1.setBuyOrdersCount(2);
        item1.setMinSellPrice(3);
        item1.setSellOrdersCount(4);
        item1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        item1.setLastSoldPrice(5);
        item1.setMonthAveragePrice(6);
        item1.setMonthMedianPrice(7);
        item1.setMonthMaxPrice(8);
        item1.setMonthMinPrice(9);
        item1.setMonthSalesPerDay(10);
        item1.setMonthSales(11);
        item1.setDayAveragePrice(12);
        item1.setDayMedianPrice(13);
        item1.setDayMaxPrice(14);
        item1.setDayMinPrice(15);
        item1.setDaySales(16);
        item1.setPriorityToSellByMaxBuyPrice(17L);
        item1.setPriorityToSellByNextFancySellPrice(18L);
        item1.setPriorityToBuyByMinSellPrice(19L);
        item1.setPriorityToBuyIn1Hour(20L);
        item1.setPriorityToBuyIn6Hours(21L);
        item1.setPriorityToBuyIn24Hours(22L);
        item1.setPriorityToBuyIn168Hours(23L);
        item1.setPriorityToBuyIn720Hours(24L);
        item1.setPriceToBuyIn1Hour(25);
        item1.setPriceToBuyIn6Hours(26);
        item1.setPriceToBuyIn24Hours(27);
        item1.setPriceToBuyIn168Hours(28);
        item1.setPriceToBuyIn720Hours(29);

        Item item2 = new Item();
        item2.setItemId("itemId1");
        item2.setAssetUrl("assetUrl1");
        item2.setName("name1");
        item2.setTags(List.of("tag1"));
        item2.setRarity(ItemRarity.EPIC);
        item2.setType(ItemType.Charm);
        item2.setMaxBuyPrice(2);
        item2.setBuyOrdersCount(3);
        item2.setMinSellPrice(4);
        item2.setSellOrdersCount(5);
        item2.setLastSoldAt(LocalDateTime.of(2022, 1, 1, 1, 1));
        item2.setLastSoldPrice(6);

        TagEntity tagEntity1 = new TagEntity();
        tagEntity1.setValue("tag");
        TagEntity tagEntity2 = new TagEntity();
        tagEntity2.setValue("tag1");

        when(tagValueRepository.findAll()).thenReturn(List.of(tagEntity1, tagEntity2));

        ItemMainFieldsEntity entity1 = new ItemMainFieldsEntity();
        entity1.setItemId("itemId");
        entity1.setAssetUrl("assetUrl");
        entity1.setName("name");
        entity1.setTags(List.of(tagEntity1));
        entity1.setRarity(ItemRarity.RARE);
        entity1.setType(ItemType.WeaponSkin);
        entity1.setMaxBuyPrice(1);
        entity1.setBuyOrdersCount(2);
        entity1.setMinSellPrice(3);
        entity1.setSellOrdersCount(4);
        entity1.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        entity1.setLastSoldPrice(5);

        ItemMainFieldsEntity entity2 = new ItemMainFieldsEntity();
        entity2.setItemId("itemId1");
        entity2.setAssetUrl("assetUrl1");
        entity2.setName("name1");
        entity2.setTags(List.of(tagEntity2));
        entity2.setRarity(ItemRarity.EPIC);
        entity2.setType(ItemType.Charm);
        entity2.setMaxBuyPrice(2);
        entity2.setBuyOrdersCount(3);
        entity2.setMinSellPrice(4);
        entity2.setSellOrdersCount(5);
        entity2.setLastSoldAt(LocalDateTime.of(2022, 1, 1, 1, 1));
        entity2.setLastSoldPrice(6);

        List<ItemMainFieldsEntity> expected = List.of(entity1, entity2);

        List<ItemMainFieldsEntity> actual = itemMainFieldsPostgresMapper.mapToEntities(List.of(item1, item2));

        assertTrue(expected.stream().allMatch(ex -> actual.stream().anyMatch(ac -> ac.isFullyEqual(ex))) && expected.size() == actual.size());
    }
}