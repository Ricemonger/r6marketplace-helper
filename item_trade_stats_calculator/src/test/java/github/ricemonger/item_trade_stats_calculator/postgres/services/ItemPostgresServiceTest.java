package github.ricemonger.item_trade_stats_calculator.postgres.services;


import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemCurrentPricesRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemHistoryFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.dto_projections.ItemRecalculationRequiredFieldsProjection;
import github.ricemonger.item_trade_stats_calculator.postgres.repositories.CustomItemPostgresRepository;
import github.ricemonger.item_trade_stats_calculator.postgres.services.entity_mappers.item.ItemEntitiesMapper;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemCurrentPricesRecalculationRequiredFields;
import github.ricemonger.item_trade_stats_calculator.services.DTOs.ItemRecalculationRequiredFields;
import github.ricemonger.utils.DTOs.common.ItemHistoryFieldsI;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemPostgresServiceTest {
    @Autowired
    private ItemPostgresService itemPostgresService;
    @MockBean
    private CustomItemPostgresRepository customItemPostgresRepository;
    @MockBean
    private ItemEntitiesMapper itemEntitiesMapper;

    @Test
    public void updateAllItemsHistoryFields_should_update_mapped_entitiesItem() {
        ItemHistoryFieldsI dto1 = Mockito.mock(ItemHistoryFieldsI.class);
        ItemHistoryFieldsI dto2 = Mockito.mock(ItemHistoryFieldsI.class);

        ItemHistoryFieldsProjection proj1 = Mockito.mock(ItemHistoryFieldsProjection.class);
        ItemHistoryFieldsProjection proj2 = Mockito.mock(ItemHistoryFieldsProjection.class);

        when(itemEntitiesMapper.createHistoryFieldsDtoProjection(dto1)).thenReturn(proj1);
        when(itemEntitiesMapper.createHistoryFieldsDtoProjection(dto2)).thenReturn(proj2);

        itemPostgresService.updateAllItemsHistoryFields(List.of(dto1, dto2));

        verify(customItemPostgresRepository).updateAllItemsHistoryFields(argThat(iterable -> {
            List arg = (List) iterable;

            return arg.size() == 2 && arg.contains(proj1) && arg.contains(proj2);
        }));
    }

    @Test
    public void findAllItemsRecalculationRequiredFields_should_return_mapped_dtos() {
        ItemRecalculationRequiredFieldsProjection proj1 = Mockito.mock(ItemRecalculationRequiredFieldsProjection.class);
        ItemRecalculationRequiredFieldsProjection proj2 = Mockito.mock(ItemRecalculationRequiredFieldsProjection.class);

        ItemRecalculationRequiredFields dto1 = Mockito.mock(ItemRecalculationRequiredFields.class);
        ItemRecalculationRequiredFields dto2 = Mockito.mock(ItemRecalculationRequiredFields.class);

        when(customItemPostgresRepository.findAllItemsRecalculationRequiredFields()).thenReturn(List.of(proj1, proj2));
        when(itemEntitiesMapper.createRecalculationRequiredFieldsDTO(proj1)).thenReturn(dto1);
        when(itemEntitiesMapper.createRecalculationRequiredFieldsDTO(proj2)).thenReturn(dto2);

        List<ItemRecalculationRequiredFields> result = itemPostgresService.findAllItemsRecalculationRequiredFields();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    public void updateAllItemsCurrentPricesHistoryFields_should_handle_to_repository_mapped_projections() {
        ItemHistoryFieldsI item1 = Mockito.mock(ItemHistoryFieldsI.class);
        ItemHistoryFieldsI item2 = Mockito.mock(ItemHistoryFieldsI.class);

        ItemCurrentPricesHistoryFieldsProjection proj1 = Mockito.mock(ItemCurrentPricesHistoryFieldsProjection.class);
        ItemCurrentPricesHistoryFieldsProjection proj2 = Mockito.mock(ItemCurrentPricesHistoryFieldsProjection.class);

        when(itemEntitiesMapper.createCurrentPricesHistoryFieldsProjection(item1)).thenReturn(proj1);
        when(itemEntitiesMapper.createCurrentPricesHistoryFieldsProjection(item2)).thenReturn(proj2);

        itemPostgresService.updateAllItemsCurrentPricesHistoryFields(List.of(item1, item2));

        verify(customItemPostgresRepository).updateAllItemsCurrentPricesHistoryFields(argThat(iterable -> {
            List arg = (List) iterable;

            return arg.size() == 2 && arg.contains(proj1) && arg.contains(proj2);
        }));
    }

    @Test
    public void findAllItemsCurrentPricesRecalculationRequiredFields_should_return_mapped_dtos() {
        ItemCurrentPricesRecalculationRequiredFieldsProjection proj1 = Mockito.mock(ItemCurrentPricesRecalculationRequiredFieldsProjection.class);
        ItemCurrentPricesRecalculationRequiredFieldsProjection proj2 = Mockito.mock(ItemCurrentPricesRecalculationRequiredFieldsProjection.class);

        ItemCurrentPricesRecalculationRequiredFields dto1 = Mockito.mock(ItemCurrentPricesRecalculationRequiredFields.class);
        ItemCurrentPricesRecalculationRequiredFields dto2 = Mockito.mock(ItemCurrentPricesRecalculationRequiredFields.class);

        when(customItemPostgresRepository.findAllItemsCurrentPricesRecalculationRequiredFields()).thenReturn(List.of(proj1, proj2));
        when(itemEntitiesMapper.createCurrentPricesRecalculationRequiredFields(proj1)).thenReturn(dto1);
        when(itemEntitiesMapper.createCurrentPricesRecalculationRequiredFields(proj2)).thenReturn(dto2);

        List<ItemCurrentPricesRecalculationRequiredFields> result = itemPostgresService.findAllItemsCurrentPricesRecalculationRequiredFields();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }
}