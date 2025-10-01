package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomItemFilterEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomManageableUserEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomTradeByFiltersManagerEntity;
import github.ricemonger.trades_manager.services.DTOs.TradeByFiltersManager;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomTradeByFiltersManagerEntityMapperTest {
    @Autowired
    private TradeByFiltersManagerEntityMapper tradeByFiltersManagerEntityMapper;
    @MockBean
    private ItemFilterEntityMapper itemFilterEntityMapper;

    @Test
    public void createDTO_should_properly_map_dto() {
        when(itemFilterEntityMapper.createDTO(any())).thenReturn(new ItemFilter());

        CustomTradeByFiltersManagerEntity entity = new CustomTradeByFiltersManagerEntity();
        entity.setUser(new CustomManageableUserEntity(1L));
        entity.setName("name");
        entity.setEnabled(true);
        entity.setTradeOperationType(TradeOperationType.BUY);
        entity.setAppliedFilters(List.of(new CustomItemFilterEntity()));
        entity.setMinDifferenceFromMedianPrice(10);
        entity.setMinDifferenceFromMedianPricePercent(11);
        entity.setPriorityMultiplier(12);

        TradeByFiltersManager expected = new TradeByFiltersManager();
        expected.setName("name");
        expected.setEnabled(true);
        expected.setTradeOperationType(TradeOperationType.BUY);
        expected.setAppliedFilters(List.of(new ItemFilter()));
        expected.setMinDifferenceFromMedianPrice(10);
        expected.setMinDifferenceFromMedianPricePercent(11);
        expected.setPriorityMultiplier(12);

        assertEquals(expected, tradeByFiltersManagerEntityMapper.createDTO(entity));
    }
}