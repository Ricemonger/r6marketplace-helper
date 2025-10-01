package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomItemFilterEntity;
import github.ricemonger.trades_manager.postgres.services.entity_mappers.item.TagEntityMapper;
import github.ricemonger.utils.DTOs.common.Tag;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.enums.FilterType;
import github.ricemonger.utils.enums.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemFilterEntityMapper {

    private final TagEntityMapper tagEntityMapper;

    public ItemFilter createDTO(CustomItemFilterEntity entity) {
        String name = entity.getName();
        FilterType filterType = entity.getFilterType();

        List<String> namePatterns = new ArrayList<>();
        if (entity.getItemNamePatterns() != null && !entity.getItemNamePatterns().isEmpty()) {
            namePatterns = Arrays.stream(entity.getItemNamePatterns().split("[,|]")).map(String::trim).toList();
        }

        List<ItemType> itemTypes = new ArrayList<>();
        if (entity.getItemTypes() != null && !entity.getItemTypes().isEmpty()) {
            String[] split = entity.getItemTypes().split("[,|]");
            for (String s : split) {
                try {
                    itemTypes.add(ItemType.valueOf(s));
                } catch (IllegalArgumentException e) {
                    log.error("Unknown item type: " + s);
                }
            }
        }

        List<Tag> tags = new ArrayList<>();
        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
            tags = entity.getTags().stream().map(tagEntityMapper::createDTO).toList();
        }

        Integer minSellPrice = entity.getMinSellPrice();
        Integer maxBuyPrice = entity.getMaxBuyPrice();

        return new ItemFilter(name, filterType, namePatterns, itemTypes, tags, minSellPrice, maxBuyPrice);
    }
}
