package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.configurations.FastSellManagementConfiguration;
import github.ricemonger.fast_sell_trade_manager.services.configurations.UbiServiceConfiguration;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.abstract_services.CommonValuesDatabaseService;
import github.ricemonger.utils.enums.ItemRarity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommonValuesServiceTest {
    @Autowired
    private CommonValuesService commonValuesService;
    @MockBean
    private CommonValuesDatabaseService commonValuesDatabaseService;
    @MockBean
    private UbiServiceConfiguration ubiServiceConfiguration;
    @MockBean
    private FastSellManagementConfiguration fastSellManagementConfiguration;

    @Test
    public void getConfigTrades_should_return_service_result() {
        ConfigTrades configTrades = Mockito.mock(ConfigTrades.class);

        when(commonValuesDatabaseService.getConfigTrades()).thenReturn(configTrades);

        assertSame(configTrades, commonValuesService.getConfigTrades());
    }

    @Test
    public void getMinimumPriceByRarity_should_return_for_uncommon() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinUncommonPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumPriceByRarity(ItemRarity.UNCOMMON));
    }

    @Test
    public void getMinimumPriceByRarity_should_return_for_rare() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinRarePrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumPriceByRarity(ItemRarity.RARE));
    }

    @Test
    public void getMinimumPriceByRarity_should_return_for_epic() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinEpicPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumPriceByRarity(ItemRarity.EPIC));
    }

    @Test
    public void getMinimumPriceByRarity_should_return_for_legendary() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinLegendaryPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumPriceByRarity(ItemRarity.LEGENDARY));
    }

    @Test
    public void getMinimumPriceByRarity_should_return_default() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinLegendaryPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumPriceByRarity(ItemRarity.UNKNOWN));
    }

    @Test
    public void getMaximumPriceByRarity_should_for_uncommon() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxUncommonPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumPriceByRarity(ItemRarity.UNCOMMON));
    }

    @Test
    public void getMaximumPriceByRarity_should_for_rare() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxRarePrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumPriceByRarity(ItemRarity.RARE));
    }

    @Test
    public void getMaximumPriceByRarity_should_for_epic() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxEpicPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumPriceByRarity(ItemRarity.EPIC));
    }

    @Test
    public void getMaximumPriceByRarity_should_for_legendary() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxLegendaryPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumPriceByRarity(ItemRarity.LEGENDARY));
    }

    @Test
    public void getMaximumPriceByRarity_should_return_default() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxUncommonPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumPriceByRarity(ItemRarity.UNCOMMON));
    }

    @Test
    public void getMinimumUncommonPrice_should_handle_to_service() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinUncommonPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumUncommonPrice());
    }

    @Test
    public void getMaximumUncommonPrice_should_handle_to_service() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxUncommonPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumUncommonPrice());
    }

    @Test
    public void getMinimumRarePrice_should_handle_to_service() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinRarePrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumRarePrice());
    }

    @Test
    public void getMaximumRarePrice_should_handle_to_service() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxRarePrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumRarePrice());
    }

    @Test
    public void getMinimumEpicPrice_should_handle_to_service() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinEpicPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumEpicPrice());
    }

    @Test
    public void getMaximumEpicPrice_should_handle_to_service() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxEpicPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumEpicPrice());
    }

    @Test
    public void getMinimumLegendaryPrice_should_handle_to_service() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinLegendaryPrice()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinimumLegendaryPrice());
    }

    @Test
    public void getMaximumLegendaryPrice_should_handle_to_service() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxLegendaryPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumLegendaryPrice());
    }

    @Test
    public void getMinimumMarketplacePrice_should_handle_to_service() {
        Integer minPrice = 10;
        when(ubiServiceConfiguration.getMinUncommonPrice()).thenReturn(minPrice + 2);
        when(ubiServiceConfiguration.getMinRarePrice()).thenReturn(minPrice + 1);
        when(ubiServiceConfiguration.getMinEpicPrice()).thenReturn(minPrice);
        when(ubiServiceConfiguration.getMinLegendaryPrice()).thenReturn(minPrice + 3);

        assertEquals(minPrice, commonValuesService.getMinimumMarketplacePrice());
    }

    @Test
    public void getMaximumMarketplacePrice_should_handle_to_service() {
        Integer maxPrice = 10;
        when(ubiServiceConfiguration.getMaxLegendaryPrice()).thenReturn(maxPrice);

        assertEquals(maxPrice, commonValuesService.getMaximumMarketplacePrice());
    }

    @Test
    public void getFastSellManagedUserId_should_handle_to_service() {
        Long userId = 10L;
        when(fastSellManagementConfiguration.getUserId()).thenReturn(userId);

        assertEquals(userId, commonValuesService.getFastSellManagedUserId());
    }

    @Test
    public void getFastSellManagedUserEmail_should_handle_to_service() {
        String email = "email";
        when(fastSellManagementConfiguration.getEmail()).thenReturn(email);

        assertEquals(email, commonValuesService.getFastSellManagedUserEmail());
    }

    @Test
    public void getMinMedianPriceDifference_should_handle_to_service() {
        Integer minPrice = 10;
        when(fastSellManagementConfiguration.getMinMedianPriceDifference()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinMedianPriceDifference());
    }

    @Test
    public void getMinMedianPriceDifferencePercentage_should_handle_to_service() {
        Integer minPrice = 10;
        when(fastSellManagementConfiguration.getMinMedianPriceDifferencePercentage()).thenReturn(minPrice);

        assertEquals(minPrice, commonValuesService.getMinMedianPriceDifferencePercentage());
    }

    @Test
    public void getExpectedItemCount_should_handle_to_service() {
        Integer expectedItemCount = 10;
        when(commonValuesDatabaseService.getExpectedItemCount()).thenReturn(expectedItemCount);

        assertEquals(expectedItemCount, commonValuesService.getExpectedItemCount());
    }

    @Test
    public void getFastTradeOwnedItemsLimit_should_handle_to_service() {
        Integer limit = 10;
        when(fastSellManagementConfiguration.getOwnedItemsLimit()).thenReturn(limit);

        assertEquals(limit, commonValuesService.getFastTradeOwnedItemsLimit());
    }

    @Test
    public void getFetchUsersItemsLimit_should_handle_to_service() {
        Integer limit = 10;
        when(fastSellManagementConfiguration.getFetchUsersItemsLimit()).thenReturn(limit);

        assertEquals(limit, commonValuesService.getFetchUsersItemsLimit());
    }

    @Test
    public void getFetchUsersItemsOffset_should_handle_to_service() {
        Integer offset = 100;
        when(fastSellManagementConfiguration.getFetchUsersItemsOffset()).thenReturn(offset);

        assertEquals(offset, commonValuesService.getFetchUsersItemsOffset());
    }

    @Test
    public void getSleepAfterCommandsExecutionTime_should_handle_to_service(){
        Integer sleepAfterCommandsExecutionTime = 100;
        when(fastSellManagementConfiguration.getSleepAfterExecutionTime()).thenReturn(sleepAfterCommandsExecutionTime);

        assertEquals(sleepAfterCommandsExecutionTime, commonValuesService.getSleepAfterCommandsExecutionTime());
    }
}