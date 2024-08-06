package github.ricemonger.marketplace.graphQl;

import github.ricemonger.marketplace.services.CommonValuesService;
import github.ricemonger.utils.dtos.AuthorizationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@SpringBootTest
public class GraphQlClientFactoryTest {
    @Autowired
    private GraphQlClientFactory graphQlClientFactory;
    @MockBean
    private CommonValuesService commonValuesService;

    @Test
    public void createMainClient_should_create_client_with_main_user_headers() {
        graphQlClientFactory.createMainUserClient();

        verify(commonValuesService).getContentType();
        verify(commonValuesService).getUbiAppId();
        verify(commonValuesService).getUbiRegionId();
        verify(commonValuesService).getUbiLocaleCode();
        verify(commonValuesService).getUserAgent();
        verify(commonValuesService).getGraphqlUrl();

        verify(commonValuesService).getMainUserAuthorizationToken();
        verify(commonValuesService).getMainUserSessionId();
        verify(commonValuesService).getMainUserProfileId();
        verify(commonValuesService).getUbiGameSpaceId();
    }

    @Test
    public void createAuthorizedClient_should_create_client_with_authorized_user_headers() {
        AuthorizationDTO authorizationDTO = mock(AuthorizationDTO.class);

        graphQlClientFactory.createAuthorizedUserClient(authorizationDTO);

        verify(commonValuesService).getContentType();
        verify(commonValuesService).getUbiAppId();
        verify(commonValuesService).getUbiRegionId();
        verify(commonValuesService).getUbiLocaleCode();
        verify(commonValuesService).getUserAgent();
        verify(commonValuesService).getGraphqlUrl();

        verify(commonValuesService, times(0)).getMainUserAuthorizationToken();
        verify(commonValuesService, times(0)).getMainUserSessionId();
        verify(commonValuesService, times(0)).getMainUserProfileId();
        verify(commonValuesService, times(0)).getUbiGameSpaceId();

        verify(authorizationDTO).getTicket();
        verify(authorizationDTO).getSessionId();
        verify(authorizationDTO).getProfileId();
        verify(authorizationDTO).getSpaceId();
    }
}
