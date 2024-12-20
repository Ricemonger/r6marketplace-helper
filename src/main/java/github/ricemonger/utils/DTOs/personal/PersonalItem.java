package github.ricemonger.utils.DTOs.personal;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.TradeOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalItem {
    private Item item;
    private Integer sellBoundaryPrice;
    private Integer buyBoundaryPrice;
    private Integer minMedianPriceDifference;
    private Integer minMedianPriceDifferencePercent;
    private TradeOperationType tradeOperationType;

    private Integer priorityMultiplier;
    private Boolean isOwned;

    private Boolean tradeAlreadyExists;
    private UbiTrade existingTrade;

    public String getItemId() {
        return item == null ? null : item.getItemId();
    }

    public String getName() {
        return item == null ? null : item.getName();
    }

    public ItemRarity getRarity() {
        return item == null ? null : item.getRarity();
    }

    public LocalDateTime getLastSoldAt() {
        return item == null ? null : item.getLastSoldAt();
    }

    public Integer getMonthMedianPrice() {
        return item == null ? null : item.getMonthMedianPrice();
    }

    public Integer getProposedPaymentPrice() {
        return existingTrade == null ? null : existingTrade.getProposedPaymentPrice();
    }

    public String getTradeId() {
        return existingTrade == null ? null : existingTrade.getTradeId();
    }

    public int hashCode() {
        return Objects.hash(item, tradeOperationType);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PersonalItem itemForTradeDTO)) {
            return false;
        }
        return Objects.equals(item, itemForTradeDTO.item) && Objects.equals(tradeOperationType, itemForTradeDTO.tradeOperationType);
    }
}
