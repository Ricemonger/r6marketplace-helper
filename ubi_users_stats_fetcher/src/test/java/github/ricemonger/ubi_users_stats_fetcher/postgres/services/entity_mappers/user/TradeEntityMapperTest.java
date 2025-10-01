package github.ricemonger.ubi_users_stats_fetcher.postgres.services.entity_mappers.user;

import github.ricemonger.ubi_users_stats_fetcher.postgres.repositories.CustomItemPostgresRepository;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.Trade;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import github.ricemonger.utils.exceptions.client.ItemDoesntExistException;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.TradeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class TradeEntityMapperTest {
    @Autowired
    private TradeEntityMapper tradeEntityMapper;
    @MockBean
    private CustomItemPostgresRepository customItemPostgresRepository;

    @Test
    public void createEntity_should_return_expected_result_for_existing_item() {
        Trade ubiTrade = new Trade();
        ubiTrade.setTradeId("tradeId");
        ubiTrade.setState(TradeState.Created);
        ubiTrade.setCategory(TradeCategory.Sell);
        ubiTrade.setExpiresAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        ubiTrade.setLastModifiedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        ubiTrade.setItem(new Item("itemId1"));
        ubiTrade.setSuccessPaymentPrice(1);
        ubiTrade.setSuccessPaymentFee(2);
        ubiTrade.setProposedPaymentPrice(3);
        ubiTrade.setProposedPaymentFee(4);

        List<String> existingItems = List.of("itemId1", "itemId2");
        ItemEntity itemEntity = new ItemEntity("itemId1");
        when(customItemPostgresRepository.getReferenceById("itemId1")).thenReturn(itemEntity);

        TradeEntity entity = new TradeEntity();
        entity.setTradeId("tradeId");
        entity.setState(TradeState.Created);
        entity.setCategory(TradeCategory.Sell);
        entity.setExpiresAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        entity.setLastModifiedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        entity.setItem(itemEntity);
        entity.setSuccessPaymentPrice(1);
        entity.setSuccessPaymentFee(2);
        entity.setProposedPaymentPrice(3);
        entity.setProposedPaymentFee(4);

        TradeEntity result = tradeEntityMapper.createEntity(ubiTrade, existingItems);

        assertEquals(entity.getTradeId(), result.getTradeId());
        assertEquals(entity.getState(), result.getState());
        assertEquals(entity.getCategory(), result.getCategory());
        assertEquals(entity.getExpiresAt(), result.getExpiresAt());
        assertEquals(entity.getLastModifiedAt(), result.getLastModifiedAt());
        assertEquals(entity.getItem(), result.getItem());
        assertEquals(entity.getSuccessPaymentPrice(), result.getSuccessPaymentPrice());
        assertEquals(entity.getSuccessPaymentFee(), result.getSuccessPaymentFee());
        assertEquals(entity.getProposedPaymentPrice(), result.getProposedPaymentPrice());
        assertEquals(entity.getProposedPaymentFee(), result.getProposedPaymentFee());
    }

    @Test
    public void createEntity_should_throw_for_non_existing_item() {
        Trade ubiTrade = new Trade();
        ubiTrade.setTradeId("tradeId");
        ubiTrade.setState(TradeState.Created);
        ubiTrade.setCategory(TradeCategory.Sell);
        ubiTrade.setExpiresAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        ubiTrade.setLastModifiedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        ubiTrade.setItem(new Item("itemId3"));
        ubiTrade.setSuccessPaymentPrice(1);
        ubiTrade.setSuccessPaymentFee(2);
        ubiTrade.setProposedPaymentPrice(3);
        ubiTrade.setProposedPaymentFee(4);

        List<String> existingItems = List.of("itemId1", "itemId2");

        assertThrows(ItemDoesntExistException.class, () -> tradeEntityMapper.createEntity(ubiTrade, existingItems));
    }
}