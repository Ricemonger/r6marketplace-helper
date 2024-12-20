package github.ricemonger.utils.DTOs.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemShownFieldsSettings {
    private Boolean itemShowNameFlag;
    private Boolean itemShowItemTypeFlag;
    private Boolean itemShowMaxBuyPrice;
    private Boolean itemShowBuyOrdersCountFlag;
    private Boolean itemShowMinSellPriceFlag;
    private Boolean itemsShowSellOrdersCountFlag;
    private Boolean itemShowPictureFlag;

    public int getActiveFieldsCount() {
        int count = 1;
        if (itemShowNameFlag) count++;
        if (itemShowItemTypeFlag) count++;
        if (itemShowMaxBuyPrice) count++;
        if (itemShowBuyOrdersCountFlag) count++;
        if (itemShowMinSellPriceFlag) count++;
        if (itemsShowSellOrdersCountFlag) count++;
        if (itemShowPictureFlag) count++;
        return count;
    }

    public String toString() {
        return "Show Item Name: " + itemShowNameFlag + "\n" +
               "Show Item Type: " + itemShowItemTypeFlag + "\n" +
               "Show Max Buy Price: " + itemShowMaxBuyPrice + "\n" +
               "Show Buy Orders Count: " + itemShowBuyOrdersCountFlag + "\n" +
               "Show Min Sell Price: " + itemShowMinSellPriceFlag + "\n" +
               "Show Sell Orders Count: " + itemsShowSellOrdersCountFlag + "\n" +
               "Show Item Picture: " + itemShowPictureFlag + "\n";
    }
}