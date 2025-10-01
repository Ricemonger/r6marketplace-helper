package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private UbiTrade ubiTrade = new UbiTrade();

    private Integer minutesToTrade;

    private Long tradePriority;

    public Trade(String tradeId, TradeState state, TradeCategory category, LocalDateTime expiresAt, LocalDateTime lastModifiedAt, Item item, Integer successPaymentPrice, Integer successPaymentFee, Integer proposedPaymentPrice, Integer proposedPaymentFee, Integer minutesToTrade, Long tradePriority) {
        this.ubiTrade = new UbiTrade(tradeId, state, category, expiresAt, lastModifiedAt, item, successPaymentPrice, successPaymentFee, proposedPaymentPrice, proposedPaymentFee);
        this.minutesToTrade = minutesToTrade;
        this.tradePriority = tradePriority;
    }

    public String getTradeId() {
        return ubiTrade == null ? null : ubiTrade.getTradeId();
    }

    public void setTradeId(String buyTradeId2) {
        ubiTrade.setTradeId(buyTradeId2);
    }

    public TradeState getState() {
        return ubiTrade == null ? null : ubiTrade.getState();
    }

    public TradeCategory getCategory() {
        return ubiTrade == null ? null : ubiTrade.getCategory();
    }

    public void setCategory(TradeCategory tradeCategory) {
        ubiTrade.setCategory(tradeCategory);
    }

    public LocalDateTime getExpiresAt() {
        return ubiTrade == null ? null : ubiTrade.getExpiresAt();
    }

    public LocalDateTime getLastModifiedAt() {
        return ubiTrade == null ? null : ubiTrade.getLastModifiedAt();
    }

    public Item getItem() {
        return ubiTrade == null ? null : ubiTrade.getItem();
    }

    public void setItem(Item item) {
        ubiTrade.setItem(item);
    }

    public Integer getSuccessPaymentPrice() {
        return ubiTrade == null ? null : ubiTrade.getSuccessPaymentPrice();
    }

    public Integer getSuccessPaymentFee() {
        return ubiTrade == null ? null : ubiTrade.getSuccessPaymentFee();
    }

    public Integer getProposedPaymentPrice() {
        return ubiTrade == null ? null : ubiTrade.getProposedPaymentPrice();
    }

    public void setProposedPaymentPrice(Integer i) {
        ubiTrade.setProposedPaymentPrice(i);
    }

    public Integer getProposedPaymentFee() {
        return ubiTrade == null ? null : ubiTrade.getProposedPaymentFee();
    }

    public String getItemId() {
        return ubiTrade == null ? null : ubiTrade.getItemId();
    }

    public String getItemName() {
        return ubiTrade == null ? null : ubiTrade.getItemName();
    }

    public boolean isFullyEqual(Trade existingTrade) {
        if (existingTrade == null) {
            return false;
        } else {
            return Objects.equals(minutesToTrade, existingTrade.minutesToTrade) &&
                   Objects.equals(tradePriority, existingTrade.tradePriority) &&
                   ubiTrade.isFullyEqual(existingTrade.ubiTrade);
        }
    }
}
