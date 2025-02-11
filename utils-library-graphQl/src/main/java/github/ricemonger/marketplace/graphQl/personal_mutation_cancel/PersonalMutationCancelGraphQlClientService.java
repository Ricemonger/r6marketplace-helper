package github.ricemonger.marketplace.graphQl.personal_mutation_cancel;

import github.ricemonger.marketplace.graphQl.GraphQlClientFactory;
import github.ricemonger.marketplace.graphQl.GraphQlDocuments;
import github.ricemonger.marketplace.graphQl.GraphQlVariablesService;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;

@RequiredArgsConstructor
public class PersonalMutationCancelGraphQlClientService {
    private final GraphQlClientFactory graphQlClientFactory;

    private final GraphQlVariablesService graphQlVariablesService;

    public void cancelOrderForUser(AuthorizationDTO authorizationDTO, String tradeId) {
        HttpGraphQlClient client = graphQlClientFactory.createAuthorizedUserClient(authorizationDTO);

        client.document(GraphQlDocuments.MUTATION_ORDER_CANCEL_DOCUMENT)
                .variables(graphQlVariablesService.getCancelOrderVariables(tradeId))
                .execute().block();
    }

    public void nonBlockingCancelOrderForUser(AuthorizationDTO authorizationDTO, String tradeId) {
        HttpGraphQlClient client = graphQlClientFactory.createAuthorizedUserClient(authorizationDTO);

        client.document(GraphQlDocuments.MUTATION_ORDER_CANCEL_DOCUMENT)
                .variables(graphQlVariablesService.getCancelOrderVariables(tradeId))
                .execute().subscribe();
    }
}
