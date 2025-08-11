package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.FastTradeCommand;
import github.ricemonger.marketplace.graphQl.personal_mutation_cancel.PersonalMutationCancelGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_create.PersonalMutationSellCreateGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_update.PersonalMutationSellUpdateGraphQlClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeCommandsExecutor {
    private final PersonalMutationSellCreateGraphQlClientService personalMutationSellCreateGraphQlClientService;
    private final PersonalMutationSellUpdateGraphQlClientService personalMutationSellUpdateGraphQlClientService;
    private final PersonalMutationCancelGraphQlClientService personalMutationCancelGraphQlClientService;

    public void executeCommand(FastTradeCommand command) {
        try {
            switch (command.getCommandType()) {
                case SELL_ORDER_CANCEL -> {
                    personalMutationCancelGraphQlClientService.cancelOrderForUser(command.getAuthorizationDTO(), command.getTradeId());
                }
                case SELL_ORDER_UPDATE -> {
                    personalMutationSellUpdateGraphQlClientService.updateSellOrderForUser(command.getAuthorizationDTO(), command.getTradeId(), command.getNewPrice());
                }
                case SELL_ORDER_CREATE -> {
                    personalMutationSellCreateGraphQlClientService.createSellOrderForUser(command.getAuthorizationDTO(), command.getItemId(), command.getNewPrice());
                }
            }
        } catch (Exception e) {
            log.error("Exception while executing command: {}", command, e);
        }
    }
}
