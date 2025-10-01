package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemEntity;
import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomTradeEntity;
import github.ricemonger.trades_manager.postgres.services.entity_mappers.item.ItemEntityMapper;
import github.ricemonger.trades_manager.services.DTOs.Trade;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomTradeEntityMapperTest {
    @Autowired
    private TradeEntityMapper tradeEntityMapper;
    @MockBean
    private ItemEntityMapper itemEntityMapper;

    @Test
    public void createDTO_should_return_expected_result() {
        CustomItemEntity itemEntity = mock(CustomItemEntity.class);

        Item itemDTO = new Item();

        when(itemEntityMapper.createDTO(itemEntity)).thenReturn(itemDTO);

        CustomTradeEntity entity = new CustomTradeEntity();
        entity.setTradeId("tradeId");
        entity.setState(TradeState.Created);
        entity.setCategory(TradeCategory.Buy);
        entity.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        entity.setLastModifiedAt(LocalDateTime.of(2022, 1, 1, 1, 1));
        entity.setItem(itemEntity);
        entity.setSuccessPaymentPrice(1);
        entity.setSuccessPaymentFee(2);
        entity.setProposedPaymentPrice(3);
        entity.setProposedPaymentFee(4);
        entity.setMinutesToTrade(5);
        entity.setTradePriority(6L);

        Trade result = tradeEntityMapper.createDTO(entity);

        assertEquals("tradeId", result.getTradeId());
        assertEquals(TradeState.Created, result.getState());
        assertEquals(TradeCategory.Buy, result.getCategory());
        assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), result.getExpiresAt());
        assertEquals(LocalDateTime.of(2022, 1, 1, 1, 1), result.getLastModifiedAt());
        assertSame(itemDTO, result.getItem());
        assertEquals(1, result.getSuccessPaymentPrice());
        assertEquals(2, result.getSuccessPaymentFee());
        assertEquals(3, result.getProposedPaymentPrice());
        assertEquals(4, result.getProposedPaymentFee());
        assertEquals(5, result.getMinutesToTrade());
        assertEquals(6L, result.getTradePriority());
    }
}