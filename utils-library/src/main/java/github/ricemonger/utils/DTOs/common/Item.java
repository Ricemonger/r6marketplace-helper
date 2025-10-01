package github.ricemonger.utils.DTOs.common;

import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements ItemMainFieldsI, ItemHistoryFieldsI {
    private String itemId;
    private String assetUrl;
    private String name;
    private List<String> tags;

    private ItemRarity rarity;

    private ItemType type;

    private Integer maxBuyPrice;
    private Integer buyOrdersCount;

    private Integer minSellPrice;
    private Integer sellOrdersCount;

    private LocalDateTime lastSoldAt;
    private Integer lastSoldPrice;

    //history fields

    private Integer monthAveragePrice;
    private Integer monthMedianPrice;
    private Integer monthMaxPrice;
    private Integer monthMinPrice;
    private Integer monthSalesPerDay;
    private Integer monthSales;

    private Integer dayAveragePrice;
    private Integer dayMedianPrice;
    private Integer dayMaxPrice;
    private Integer dayMinPrice;
    private Integer daySales;

    private Long priorityToSellByMaxBuyPrice; //updated with every item stats update, not recalculation
    private Long priorityToSellByNextFancySellPrice; //updated with every item stats update, not recalculation

    private Long priorityToBuyByMinSellPrice; //updated with every item stats update, not recalculation

    private Long priorityToBuyIn1Hour;
    private Long priorityToBuyIn6Hours;
    private Long priorityToBuyIn24Hours;
    private Long priorityToBuyIn168Hours;
    private Long priorityToBuyIn720Hours;

    private Integer priceToBuyIn1Hour;
    private Integer priceToBuyIn6Hours;
    private Integer priceToBuyIn24Hours;
    private Integer priceToBuyIn168Hours;
    private Integer priceToBuyIn720Hours;

    public Item(String itemId) {
        this.itemId = itemId;
    }

    public Item(Item item) {
        setMainFields(item);
        setHistoryFields(item);
    }

    public Item(ItemMainFieldsI mainFields) {
        setMainFields(mainFields);
    }

    public Item(ItemHistoryFieldsI historyFields) {
        setHistoryFields(historyFields);
    }

    public Item(ItemMainFieldsI mainFields, ItemHistoryFieldsI historyFields) {
        setMainFields(mainFields);
        setHistoryFields(historyFields);
    }

    public boolean isFullyEquals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Item item)) {
            return false;
        }
        return itemMainFieldsAreEqual(item) && itemHistoryFieldsAreEqual(item);
    }

    public int hashCode() {
        return Objects.hash(itemId);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Item item)) {
            return false;
        }
        if (item.getItemId() == null || this.getItemId() == null) {
            return false;
        }
        return Objects.equals(item.getItemId(), this.getItemId());
    }
}
