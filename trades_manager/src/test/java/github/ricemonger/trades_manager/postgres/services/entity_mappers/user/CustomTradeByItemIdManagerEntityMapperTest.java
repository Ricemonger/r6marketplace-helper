package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemIdEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomTradeByItemIdManagerEntity;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomTradeByItemIdManagerEntityMapperTest {
    @Autowired
    TradeByItemIdManagerEntityMapper tradeByItemIdManagerEntityMapper;

    @Test
    public void createDTO_should_return_expected_result() {
        CustomTradeByItemIdManagerEntity entity = new CustomTradeByItemIdManagerEntity();
        entity.setItem(new CustomItemIdEntity("itemId"));
        entity.setEnabled(true);
        entity.setTradeOperationType(TradeOperationType.SELL);
        entity.setSellBoundaryPrice(100);
        entity.setBuyBoundaryPrice(200);
        entity.setPriorityMultiplier(300);

        TradeByItemIdManager result = tradeByItemIdManagerEntityMapper.createDTO(entity);

        assertEquals(entity.getItemId_(), result.getItemId());
        assertEquals(entity.getEnabled(), result.getEnabled());
        assertEquals(entity.getTradeOperationType(), result.getTradeOperationType());
        assertEquals(entity.getSellBoundaryPrice(), result.getSellBoundaryPrice());
        assertEquals(entity.getBuyBoundaryPrice(), result.getBuyBoundaryPrice());
    }
}