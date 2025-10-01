package github.ricemonger.trades_manager.services;

import github.ricemonger.marketplace.graphQl.personal_mutation_buy_create.PersonalMutationBuyCreateGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_buy_update.PersonalMutationBuyUpdateGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_cancel.PersonalMutationCancelGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_create.PersonalMutationSellCreateGraphQlClientService;
import github.ricemonger.marketplace.graphQl.personal_mutation_sell_update.PersonalMutationSellUpdateGraphQlClientService;
import github.ricemonger.trades_manager.services.DTOs.TradeManagerCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeManagementCommandsExecutor {
    private final PersonalMutationBuyCreateGraphQlClientService personalMutationBuyCreateGraphQlClientService;
    private final PersonalMutationBuyUpdateGraphQlClientService personalMutationBuyUpdateGraphQlClientService;
    private final PersonalMutationSellCreateGraphQlClientService personalMutationSellCreateGraphQlClientService;
    private final PersonalMutationSellUpdateGraphQlClientService personalMutationSellUpdateGraphQlClientService;
    private final PersonalMutationCancelGraphQlClientService personalMutationCancelGraphQlClientService;
    private final NotificationService notificationService;

    public void executeCommand(TradeManagerCommand command) {
        try {
            switch (command.getCommandType()) {
                case BUY_ORDER_CANCEL -> {
                    personalMutationCancelGraphQlClientService.cancelOrderForUser(command.getAuthorizationDTO(), command.getTradeId());
                    notificationService.sendTradeManagerNotification(command.getUserId(), String.format("Your buy order for item %s with price %d has been" +
                                                                                                        " cancelled", command.getItemName(), command.getOldPrice()));
                }
                case BUY_ORDER_UPDATE -> {
                    personalMutationBuyUpdateGraphQlClientService.updateBuyOrderForUser(command.getAuthorizationDTO(), command.getTradeId(), command.getNewPrice());
                    notificationService.sendTradeManagerNotification(command.getUserId(), String.format("Your buy order price for item %s has been updated" +
                                                                                                        " from %d to %d", command.getItemName(), command.getOldPrice(), command.getNewPrice()));
                }
                case BUY_ORDER_CREATE -> {
                    personalMutationBuyCreateGraphQlClientService.createBuyOrderForUser(command.getAuthorizationDTO(), command.getItemId(), command.getNewPrice());
                    notificationService.sendTradeManagerNotification(command.getUserId(), String.format("Your buy order for item %s with price %d has been" +
                                                                                                        " created", command.getItemName(), command.getNewPrice()));
                }
                case SELL_ORDER_CANCEL -> {
                    personalMutationCancelGraphQlClientService.cancelOrderForUser(command.getAuthorizationDTO(), command.getTradeId());
                    notificationService.sendTradeManagerNotification(command.getUserId(), String.format("Your sell order for item %s with price %d has been" +
                                                                                                        " cancelled", command.getItemName(), command.getOldPrice()));

                }
                case SELL_ORDER_UPDATE -> {
                    personalMutationSellUpdateGraphQlClientService.updateSellOrderForUser(command.getAuthorizationDTO(), command.getTradeId(), command.getNewPrice());
                    notificationService.sendTradeManagerNotification(command.getUserId(), String.format("Your sell order price for item %s has been updated" +
                                                                                                        " from %d to %d", command.getItemName(), command.getOldPrice(), command.getNewPrice()));
                }
                case SELL_ORDER_CREATE -> {
                    personalMutationSellCreateGraphQlClientService.createSellOrderForUser(command.getAuthorizationDTO(), command.getItemId(), command.getNewPrice());
                    notificationService.sendTradeManagerNotification(command.getUserId(), String.format("Your sell order for item %s with price %d has been" +
                                                                                                        " created", command.getItemName(), command.getNewPrice()));
                }
            }
        } catch (Exception e) {
            log.error("Exception while executing command: {}", command, e);
        }
    }
}
