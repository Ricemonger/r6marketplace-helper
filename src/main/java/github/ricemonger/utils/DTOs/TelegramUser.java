package github.ricemonger.utils.DTOs;

import github.ricemonger.telegramBot.InputGroup;
import github.ricemonger.telegramBot.InputState;
import github.ricemonger.utils.DTOs.items.ItemFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {
    private String chatId;

    private InputState inputState;
    private InputGroup inputGroup;

    private boolean publicNotificationsEnabledFlag;

    private Integer itemShowMessagesLimit;
    private boolean itemShowFewInMessageFlag;

    private boolean itemShowNameFlag;
    private boolean itemShowItemTypeFlag;
    private boolean itemShowMaxBuyPrice;
    private boolean itemShowBuyOrdersCountFlag;
    private boolean itemShowMinSellPriceFlag;
    private boolean itemsShowSellOrdersCountFlag;
    private boolean itemShowPictureFlag;

    private List<ItemFilter> itemShowAppliedFilters;

    private boolean newManagersAreActiveFlag;
    private boolean managingEnabledFlag;

    public TelegramUser(Long chatId) {
        this.chatId = String.valueOf(chatId);
    }
}
