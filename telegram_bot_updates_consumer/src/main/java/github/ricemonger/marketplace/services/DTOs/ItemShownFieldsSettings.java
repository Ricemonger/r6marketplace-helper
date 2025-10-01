package github.ricemonger.marketplace.services.DTOs;

import github.ricemonger.utils.DTOs.common.ItemMainFieldsI;
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
        if (itemShowNameFlag != null && itemShowNameFlag) count++;
        if (itemShowItemTypeFlag != null && itemShowItemTypeFlag) count++;
        if (itemShowMaxBuyPrice != null && itemShowMaxBuyPrice) count++;
        if (itemShowBuyOrdersCountFlag != null && itemShowBuyOrdersCountFlag) count++;
        if (itemShowMinSellPriceFlag != null && itemShowMinSellPriceFlag) count++;
        if (itemsShowSellOrdersCountFlag != null && itemsShowSellOrdersCountFlag) count++;
        if (itemShowPictureFlag != null && itemShowPictureFlag) count++;
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

    public String showItem(ItemMainFieldsI item) {
        StringBuilder sb = new StringBuilder();

        sb.append("Id: ").append(item.getItemId()).append("\n");

        if (this.getItemShowNameFlag() != null && this.getItemShowNameFlag()) {
            sb.append("Name: ").append(item.getName()).append("\n");
        }
        if (this.getItemShowItemTypeFlag() != null && this.getItemShowItemTypeFlag()) {
            sb.append("Type: ").append(item.getType()).append("\n");
        }
        if (this.getItemShowMaxBuyPrice() != null && this.getItemShowMaxBuyPrice()) {
            sb.append("Max buy price: ").append(item.getMaxBuyPrice()).append("\n");
        }
        if (this.getItemShowBuyOrdersCountFlag() != null && this.getItemShowBuyOrdersCountFlag()) {
            sb.append("Buy orders: ").append(item.getBuyOrdersCount()).append("\n");
        }
        if (this.getItemShowMinSellPriceFlag() != null && this.getItemShowMinSellPriceFlag()) {
            sb.append("Min sell price: ").append(item.getMinSellPrice()).append("\n");
        }
        if (this.getItemsShowSellOrdersCountFlag() != null && this.getItemsShowSellOrdersCountFlag()) {
            sb.append("Sell orders: ").append(item.getSellOrdersCount()).append("\n");
        }
        if (this.getItemShowPictureFlag() != null && this.getItemShowPictureFlag()) {
            sb.append("Picture: ").append(item.getAssetUrl()).append("\n");
        }
        return sb.toString();
    }
}
