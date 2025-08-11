package github.ricemonger.fast_sell_trade_manager.services.DTOs;


import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class FastTradeCommand implements Comparable<FastTradeCommand> {
    private final AuthorizationDTO authorizationDTO;
    private final FastTradeManagerCommandType commandType;
    private final String itemId;
    private final String tradeId;
    private final Integer newPrice;

    // CANCEL
    public FastTradeCommand(AuthorizationDTO authorizationDTO, FastTradeManagerCommandType commandType, String itemId, String tradeId) {
        this.authorizationDTO = authorizationDTO;
        this.commandType = commandType;
        this.itemId = itemId;
        this.tradeId = tradeId;
        this.newPrice = null;
    }

    // UPDATE
    public FastTradeCommand(AuthorizationDTO authorizationDTO, FastTradeManagerCommandType commandType, String itemId, String tradeId, Integer newPrice) {
        this.authorizationDTO = authorizationDTO;
        this.commandType = commandType;
        this.itemId = itemId;
        this.tradeId = tradeId;
        this.newPrice = newPrice;
    }

    // CREATE
    public FastTradeCommand(AuthorizationDTO authorizationDTO, FastTradeManagerCommandType commandType, String itemId, Integer newPrice) {
        this.authorizationDTO = authorizationDTO;
        this.commandType = commandType;
        this.itemId = itemId;
        this.tradeId = null;
        this.newPrice = newPrice;
    }

    @Override
    public int compareTo(FastTradeCommand o) {
        int typeCompare = this.commandType.compareTo(o.commandType);
        if (typeCompare != 0) {
            return typeCompare;
        } else {
            return this.itemId.compareTo(o.itemId);
        }
    }

    public String toLogString() {
        return "Command(Type=" + commandType + ", ItemId=" + itemId + ", TradeId=" + tradeId + ", Price=" + newPrice + ")";
    }
}
