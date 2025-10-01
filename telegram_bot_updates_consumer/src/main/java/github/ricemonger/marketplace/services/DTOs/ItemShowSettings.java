package github.ricemonger.marketplace.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemShowSettings {
    private Integer itemShowMessagesLimit;
    private Boolean itemShowFewInMessageFlag;

    private ItemShownFieldsSettings shownFieldsSettings = new ItemShownFieldsSettings();

    private Collection<ItemFilter> itemShowAppliedFilters = new ArrayList<>();

    public ItemShowSettings(Integer itemShowMessagesLimit,
                            Boolean itemShowFewInMessageFlag,
                            Boolean itemShowNameFlag,
                            Boolean itemShowItemTypeFlag,
                            Boolean itemShowMaxBuyPrice,
                            Boolean itemShowBuyOrdersCountFlag,
                            Boolean itemShowMinSellPriceFlag,
                            Boolean itemsShowSellOrdersCountFlag,
                            Boolean itemShowPictureFlag,
                            Collection<ItemFilter> itemShowAppliedFilters) {
        this.itemShowMessagesLimit = itemShowMessagesLimit;
        this.itemShowFewInMessageFlag = itemShowFewInMessageFlag;
        this.shownFieldsSettings = new ItemShownFieldsSettings(
                itemShowNameFlag,
                itemShowItemTypeFlag,
                itemShowMaxBuyPrice,
                itemShowBuyOrdersCountFlag,
                itemShowMinSellPriceFlag,
                itemsShowSellOrdersCountFlag,
                itemShowPictureFlag
        );
        this.itemShowAppliedFilters = itemShowAppliedFilters;
    }

    public Integer getActiveFieldsCount() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getActiveFieldsCount();
    }

    public String showItem(Item item) {
        return shownFieldsSettings == null ? null : shownFieldsSettings.showItem(item);
    }

    public String toHandsomeString() {
        String shownFields;
        if (shownFieldsSettings == null) {
            shownFields = "null";
        } else {
            shownFields = shownFieldsSettings.toString();
        }

        String appliedFilters;
        if (itemShowAppliedFilters == null) {
            appliedFilters = "null";
        } else {
            appliedFilters = itemShowAppliedFilters.stream().map(ItemFilter::getName).reduce((s, s2) -> s + "," + s2).orElse("");
        }

        return "Messages limit: " + itemShowMessagesLimit + "\n" +
               "Few items in message: " + itemShowFewInMessageFlag + "\n" +
               "Shown fields: \n" + shownFields + "\n" +
               "Applied filters: " + appliedFilters;
    }

    public Boolean getItemShowNameFlag() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemShowNameFlag();
    }

    public Boolean getItemShowItemTypeFlag() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemShowItemTypeFlag();
    }

    public Boolean getItemShowMaxBuyPrice() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemShowMaxBuyPrice();
    }

    public Boolean getItemShowBuyOrdersCountFlag() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemShowBuyOrdersCountFlag();
    }

    public Boolean getItemShowMinSellPriceFlag() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemShowMinSellPriceFlag();
    }

    public Boolean getItemShowSellOrdersCountFlag() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemsShowSellOrdersCountFlag();
    }

    public Boolean getItemShowPictureFlag() {
        return shownFieldsSettings == null ? null : shownFieldsSettings.getItemShowPictureFlag();
    }
}
