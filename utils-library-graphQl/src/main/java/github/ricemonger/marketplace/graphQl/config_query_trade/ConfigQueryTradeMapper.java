package github.ricemonger.marketplace.graphQl.config_query_trade;

import github.ricemonger.marketplace.graphQl.config_query_trade.DTO.TradesConfig;
import github.ricemonger.marketplace.graphQl.config_query_trade.DTO.tradesConfig.BuyLimit;
import github.ricemonger.marketplace.graphQl.config_query_trade.DTO.tradesConfig.SellLimit;
import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.exceptions.server.GraphQlConfigTradeMappingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigQueryTradeMapper {

    public ConfigTrades mapConfigTrades(TradesConfig tradesConfig) throws GraphQlConfigTradeMappingException {
        if (tradesConfig == null) {
            throw new GraphQlConfigTradeMappingException("Trades config is null");
        }

        ConfigTrades result = new ConfigTrades();
        BuyLimit buyLimit = tradesConfig.getBuyLimit();
        SellLimit sellLimit = tradesConfig.getSellLimit();

        if (tradesConfig.getSaleExpiresAfterMinutes() == null
            || tradesConfig.getBuySlots() == null
            || tradesConfig.getSellSlots() == null
            || tradesConfig.getResaleLockDurationInMinutes() == null
            || tradesConfig.getTwoFactorAuthenticationRule() == null
            || tradesConfig.getGameOwnershipRule() == null
            || tradesConfig.getBuyLimit() == null
            || tradesConfig.getSellLimit() == null
            || tradesConfig.getTransactionFeesConfig() == null || tradesConfig.getTransactionFeesConfig().length == 0
            || tradesConfig.getTransactionFeesConfig()[0].getPaymentItemId() == null || tradesConfig.getTransactionFeesConfig()[0].getFeePercentage() == null
            || buyLimit.getMaximumCount() == null || sellLimit.getMaximumCount() == null) {
            throw new GraphQlConfigTradeMappingException("One of trades config fields is null: " + tradesConfig);
        }

        result.setSaleExpiresAfterMinutes(tradesConfig.getSaleExpiresAfterMinutes());
        result.setBuySlots(tradesConfig.getBuySlots());
        result.setSellSlots(tradesConfig.getSellSlots());
        result.setResaleLockDurationInMinutes(tradesConfig.getResaleLockDurationInMinutes());
        result.setTwoFactorAuthenticationRule(tradesConfig.getTwoFactorAuthenticationRule());
        result.setGameOwnershipRule(tradesConfig.getGameOwnershipRule());

        result.setBuyLimit(buyLimit.getMaximumCount());
        result.setSellLimit(sellLimit.getMaximumCount());

        result.setPaymentItemId(tradesConfig.getTransactionFeesConfig()[0].getPaymentItemId());
        result.setFeePercentage(tradesConfig.getTransactionFeesConfig()[0].getFeePercentage());

        return result;
    }
}
