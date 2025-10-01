package github.ricemonger.utils.DTOs.common;

import java.util.Objects;

public interface ItemHistoryFieldsI extends ItemIdFieldI {
    Integer getMonthAveragePrice();

    void setMonthAveragePrice(Integer monthAveragePrice);

    Integer getMonthMedianPrice();

    void setMonthMedianPrice(Integer monthMedianPrice);

    Integer getMonthMaxPrice();

    void setMonthMaxPrice(Integer monthMaxPrice);

    Integer getMonthMinPrice();

    void setMonthMinPrice(Integer monthMinPrice);

    Integer getMonthSalesPerDay();

    void setMonthSalesPerDay(Integer monthSalesPerDay);

    Integer getMonthSales();

    void setMonthSales(Integer monthSales);

    Integer getDayAveragePrice();

    void setDayAveragePrice(Integer dayAveragePrice);

    Integer getDayMedianPrice();

    void setDayMedianPrice(Integer dayMedianPrice);

    Integer getDayMaxPrice();

    void setDayMaxPrice(Integer dayMaxPrice);

    Integer getDayMinPrice();

    void setDayMinPrice(Integer dayMinPrice);

    Integer getDaySales();

    void setDaySales(Integer daySales);

    Long getPriorityToSellByMaxBuyPrice();

    void setPriorityToSellByMaxBuyPrice(Long priorityToSellByMaxBuyPrice);

    Long getPriorityToSellByNextFancySellPrice();

    void setPriorityToSellByNextFancySellPrice(Long priorityToSellByNextFancySellPrice);

    Long getPriorityToBuyByMinSellPrice();

    void setPriorityToBuyByMinSellPrice(Long priorityToBuyByMinSellPrice);

    Long getPriorityToBuyIn1Hour();

    void setPriorityToBuyIn1Hour(Long priorityToBuyIn1Hour);

    Long getPriorityToBuyIn6Hours();

    void setPriorityToBuyIn6Hours(Long priorityToBuyIn6Hours);

    Long getPriorityToBuyIn24Hours();

    void setPriorityToBuyIn24Hours(Long priorityToBuyIn24Hours);

    Long getPriorityToBuyIn168Hours();

    void setPriorityToBuyIn168Hours(Long priorityToBuyIn168Hours);

    Long getPriorityToBuyIn720Hours();

    void setPriorityToBuyIn720Hours(Long priorityToBuyIn720Hours);

    Integer getPriceToBuyIn1Hour();

    void setPriceToBuyIn1Hour(Integer priceToBuyIn1Hour);

    Integer getPriceToBuyIn6Hours();

    void setPriceToBuyIn6Hours(Integer priceToBuyIn6Hours);

    Integer getPriceToBuyIn24Hours();

    void setPriceToBuyIn24Hours(Integer priceToBuyIn24Hours);

    Integer getPriceToBuyIn168Hours();

    void setPriceToBuyIn168Hours(Integer priceToBuyIn168Hours);

    Integer getPriceToBuyIn720Hours();

    void setPriceToBuyIn720Hours(Integer priceToBuyIn720Hours);

    default void updateCurrentPricesPriorities(Long priorityToSellByMaxBuyPrice,
                                               Long priorityToSellByNextFancySellPrice,
                                               Long priorityToBuyByMinSellPrice) {
        setPriorityToSellByMaxBuyPrice(priorityToSellByMaxBuyPrice);
        setPriorityToSellByNextFancySellPrice(priorityToSellByNextFancySellPrice);
        setPriorityToBuyByMinSellPrice(priorityToBuyByMinSellPrice);
    }

    default boolean itemHistoryFieldsAreEqual(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ItemHistoryFieldsI item) {
            return Objects.equals(item.getItemId(), this.getItemId()) &&
                   Objects.equals(item.getMonthAveragePrice(), this.getMonthAveragePrice()) &&
                   Objects.equals(item.getMonthMedianPrice(), this.getMonthMedianPrice()) &&
                   Objects.equals(item.getMonthMaxPrice(), this.getMonthMaxPrice()) &&
                   Objects.equals(item.getMonthMinPrice(), this.getMonthMinPrice()) &&
                   Objects.equals(item.getMonthSalesPerDay(), this.getMonthSalesPerDay()) &&
                   Objects.equals(item.getMonthSales(), this.getMonthSales()) &&
                   Objects.equals(item.getDayAveragePrice(), this.getDayAveragePrice()) &&
                   Objects.equals(item.getDayMedianPrice(), this.getDayMedianPrice()) &&
                   Objects.equals(item.getDayMaxPrice(), this.getDayMaxPrice()) &&
                   Objects.equals(item.getDayMinPrice(), this.getDayMinPrice()) &&
                   Objects.equals(item.getDaySales(), this.getDaySales()) &&
                   Objects.equals(item.getPriorityToSellByMaxBuyPrice(), this.getPriorityToSellByMaxBuyPrice()) &&
                   Objects.equals(item.getPriorityToSellByNextFancySellPrice(), this.getPriorityToSellByNextFancySellPrice()) &&
                   Objects.equals(item.getPriorityToBuyByMinSellPrice(), this.getPriorityToBuyByMinSellPrice()) &&
                   Objects.equals(item.getPriorityToBuyIn1Hour(), this.getPriorityToBuyIn1Hour()) &&
                   Objects.equals(item.getPriorityToBuyIn6Hours(), this.getPriorityToBuyIn6Hours()) &&
                   Objects.equals(item.getPriorityToBuyIn24Hours(), this.getPriorityToBuyIn24Hours()) &&
                   Objects.equals(item.getPriorityToBuyIn168Hours(), this.getPriorityToBuyIn168Hours()) &&
                   Objects.equals(item.getPriorityToBuyIn720Hours(), this.getPriorityToBuyIn720Hours()) &&
                   Objects.equals(item.getPriceToBuyIn1Hour(), this.getPriceToBuyIn1Hour()) &&
                   Objects.equals(item.getPriceToBuyIn6Hours(), this.getPriceToBuyIn6Hours()) &&
                   Objects.equals(item.getPriceToBuyIn24Hours(), this.getPriceToBuyIn24Hours()) &&
                   Objects.equals(item.getPriceToBuyIn168Hours(), this.getPriceToBuyIn168Hours()) &&
                   Objects.equals(item.getPriceToBuyIn720Hours(), this.getPriceToBuyIn720Hours());
        }
        return false;
    }

    default void setHistoryFields(ItemHistoryFieldsI historyFieldsI) {
        this.setMonthAveragePrice(historyFieldsI.getMonthAveragePrice());
        this.setMonthMedianPrice(historyFieldsI.getMonthMedianPrice());
        this.setMonthMaxPrice(historyFieldsI.getMonthMaxPrice());
        this.setMonthMinPrice(historyFieldsI.getMonthMinPrice());
        this.setMonthSalesPerDay(historyFieldsI.getMonthSalesPerDay());
        this.setMonthSales(historyFieldsI.getMonthSales());

        this.setDayAveragePrice(historyFieldsI.getDayAveragePrice());
        this.setDayMedianPrice(historyFieldsI.getDayMedianPrice());
        this.setDayMaxPrice(historyFieldsI.getDayMaxPrice());
        this.setDayMinPrice(historyFieldsI.getDayMinPrice());
        this.setDaySales(historyFieldsI.getDaySales());

        this.setPriorityToSellByMaxBuyPrice(historyFieldsI.getPriorityToSellByMaxBuyPrice());
        this.setPriorityToSellByNextFancySellPrice(historyFieldsI.getPriorityToSellByNextFancySellPrice());

        this.setPriorityToBuyByMinSellPrice(historyFieldsI.getPriorityToBuyByMinSellPrice());

        this.setPriorityToBuyIn1Hour(historyFieldsI.getPriorityToBuyIn1Hour());
        this.setPriorityToBuyIn6Hours(historyFieldsI.getPriorityToBuyIn6Hours());
        this.setPriorityToBuyIn24Hours(historyFieldsI.getPriorityToBuyIn24Hours());
        this.setPriorityToBuyIn168Hours(historyFieldsI.getPriorityToBuyIn168Hours());
        this.setPriorityToBuyIn720Hours(historyFieldsI.getPriorityToBuyIn720Hours());

        this.setPriceToBuyIn1Hour(historyFieldsI.getPriceToBuyIn1Hour());
        this.setPriceToBuyIn6Hours(historyFieldsI.getPriceToBuyIn6Hours());
        this.setPriceToBuyIn24Hours(historyFieldsI.getPriceToBuyIn24Hours());
        this.setPriceToBuyIn168Hours(historyFieldsI.getPriceToBuyIn168Hours());
        this.setPriceToBuyIn720Hours(historyFieldsI.getPriceToBuyIn720Hours());
    }
}
