package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.configurations.FastSellManagementConfiguration;
import github.ricemonger.fast_sell_trade_manager.services.configurations.UbiServiceConfiguration;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.abstract_services.CommonValuesDatabaseService;
import github.ricemonger.utils.services.calculators.PricesCommonValuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonValuesService implements PricesCommonValuesService {

    private final CommonValuesDatabaseService commonValuesDatabaseService;

    private final UbiServiceConfiguration ubiServiceConfiguration;

    private final FastSellManagementConfiguration fastSellManagementConfiguration;

    public ConfigTrades getConfigTrades() {
        return commonValuesDatabaseService.getConfigTrades();
    }

    public Integer getMinimumUncommonPrice() {
        return ubiServiceConfiguration.getMinUncommonPrice();
    }

    public Integer getMaximumUncommonPrice() {
        return ubiServiceConfiguration.getMaxUncommonPrice();
    }

    public Integer getMinimumRarePrice() {
        return ubiServiceConfiguration.getMinRarePrice();
    }

    public Integer getMaximumRarePrice() {
        return ubiServiceConfiguration.getMaxRarePrice();
    }

    public Integer getMinimumEpicPrice() {
        return ubiServiceConfiguration.getMinEpicPrice();
    }

    public Integer getMaximumEpicPrice() {
        return ubiServiceConfiguration.getMaxEpicPrice();
    }

    public Integer getMinimumLegendaryPrice() {
        return ubiServiceConfiguration.getMinLegendaryPrice();
    }

    public Integer getMaximumLegendaryPrice() {
        return ubiServiceConfiguration.getMaxLegendaryPrice();
    }

    public Integer getMinimumMarketplacePrice() {
        return Math.min(ubiServiceConfiguration.getMinUncommonPrice(), Math.min(ubiServiceConfiguration.getMinRarePrice(), Math.min(ubiServiceConfiguration.getMinEpicPrice(), ubiServiceConfiguration.getMinLegendaryPrice())));
    }

    public Integer getMaximumMarketplacePrice() {
        return Math.max(ubiServiceConfiguration.getMaxUncommonPrice(), Math.max(ubiServiceConfiguration.getMaxRarePrice(), Math.max(ubiServiceConfiguration.getMaxEpicPrice(), ubiServiceConfiguration.getMaxLegendaryPrice())));
    }

    public Long getFastSellManagedUserId() {
        return fastSellManagementConfiguration.getUserId();
    }

    public String getFastSellManagedUserEmail() {
        return fastSellManagementConfiguration.getEmail();
    }

    public Integer getMinMedianPriceDifference() {
        return fastSellManagementConfiguration.getMinMedianPriceDifference();
    }

    public Integer getMinMedianPriceDifferencePercentage() {
        return fastSellManagementConfiguration.getMinMedianPriceDifferencePercentage();
    }

    public Integer getFastTradeOwnedItemsLimit() {
        return fastSellManagementConfiguration.getOwnedItemsLimit();
    }

    public Integer getExpectedItemCount() {
        return commonValuesDatabaseService.getExpectedItemCount();
    }

    public Integer getFetchUsersItemsLimit() {
        return fastSellManagementConfiguration.getFetchUsersItemsLimit();
    }

    public Integer getFetchUsersItemsOffset() {
        return fastSellManagementConfiguration.getFetchUsersItemsOffset();
    }

    public Integer getSleepAfterCommandsExecutionTime() {
        return fastSellManagementConfiguration.getSleepAfterExecutionTime();
    }
}
