package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastTradeCommand;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastTradeManagerCommandType;
import github.ricemonger.marketplace.graphQl.personal_mutation_cancel.PersonalMutationCancelGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_create.PersonalMutationSellCreateGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_update.PersonalMutationSellUpdateGraphQlClientService;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
class TradeCommandsExecutorTest {
    @Autowired
    private TradeCommandsExecutor tradeCommandsExecutor;
    @MockBean
    private PersonalMutationSellCreateGraphQlClientService personalMutationSellCreateGraphQlClientService;
    @MockBean
    private PersonalMutationSellUpdateGraphQlClientService personalMutationSellUpdateGraphQlClientService;
    @MockBean
    private PersonalMutationCancelGraphQlClientService personalMutationCancelGraphQlClientService;

    @Test
    public void executeCommand_should_properly_execute_sell_order_cancel_command() {
        AuthorizationDTO dto = Mockito.mock(AuthorizationDTO.class);

        String tradeId = "tradeId";
        String itemId = "itemId";
        Integer newPrice = 100;

        FastTradeCommand command = new FastTradeCommand(dto, FastTradeManagerCommandType.SELL_ORDER_CANCEL, itemId, tradeId);

        tradeCommandsExecutor.executeCommand(command);

        verify(personalMutationCancelGraphQlClientService).cancelOrderForUser(dto, tradeId);
    }

    @Test
    public void executeCommand_should_properly_execute_sell_order_update_command() {
        AuthorizationDTO dto = Mockito.mock(AuthorizationDTO.class);

        String tradeId = "tradeId";
        String itemId = "itemId";
        Integer newPrice = 100;

        FastTradeCommand command = new FastTradeCommand(dto, FastTradeManagerCommandType.SELL_ORDER_UPDATE, itemId, tradeId, newPrice);

        tradeCommandsExecutor.executeCommand(command);

        verify(personalMutationSellUpdateGraphQlClientService).updateSellOrderForUser(dto, tradeId, newPrice);
    }

    @Test
    public void executeCommand_should_properly_execute_sell_order_create_command() {
        AuthorizationDTO dto = Mockito.mock(AuthorizationDTO.class);

        String tradeId = "tradeId";
        String itemId = "itemId";
        Integer newPrice = 100;

        FastTradeCommand command = new FastTradeCommand(dto, FastTradeManagerCommandType.SELL_ORDER_CREATE, itemId, newPrice);

        tradeCommandsExecutor.executeCommand(command);

        verify(personalMutationSellCreateGraphQlClientService).createSellOrderForUser(dto, itemId, newPrice);
    }
}