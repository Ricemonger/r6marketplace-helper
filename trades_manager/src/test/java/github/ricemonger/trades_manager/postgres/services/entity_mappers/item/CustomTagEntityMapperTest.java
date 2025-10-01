package github.ricemonger.trades_manager.postgres.services.entity_mappers.item;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomTagEntity;
import github.ricemonger.utils.DTOs.common.Tag;
import github.ricemonger.utils.enums.TagGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomTagEntityMapperTest {
    @Autowired
    private TagEntityMapper tagEntityMapper;

    @Test
    public void createDTO_should_properly_map_dto() {
        CustomTagEntity tagEntity = new CustomTagEntity("value", "name", TagGroup.Rarity);

        assertEquals(new Tag("value", "name", TagGroup.Rarity), tagEntityMapper.createDTO(tagEntity));
    }
}