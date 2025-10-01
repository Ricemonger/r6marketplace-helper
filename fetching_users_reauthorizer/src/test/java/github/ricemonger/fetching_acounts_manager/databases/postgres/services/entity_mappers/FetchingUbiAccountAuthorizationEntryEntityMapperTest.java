package github.ricemonger.fetching_acounts_manager.databases.postgres.services.entity_mappers;

import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utilspostgresschema.full_entities.fetch.FetchingUbiAccountAuthorizationEntryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FetchingUbiAccountAuthorizationEntryEntityMapperTest {
    @Autowired
    FetchingUbiAccountAuthorizationEntryEntityMapper mapper;

    @Test
    public void mapToEntities_should_return_expected_result() {
        AuthorizationDTO authorizationDTO1 = new AuthorizationDTO();
        authorizationDTO1.setProfileId("profileId1");
        authorizationDTO1.setTicket("ticket1");
        authorizationDTO1.setSpaceId("spaceId1");
        authorizationDTO1.setSessionId("sessionId1");
        authorizationDTO1.setRememberDeviceTicket("rememberDeviceTicket1");
        authorizationDTO1.setRememberMeTicket("rememberMeTicket1");

        AuthorizationDTO authorizationDTO2 = new AuthorizationDTO();
        authorizationDTO2.setProfileId("profileId2");
        authorizationDTO2.setTicket("ticket2");
        authorizationDTO2.setSpaceId("spaceId2");
        authorizationDTO2.setSessionId("sessionId2");
        authorizationDTO2.setRememberDeviceTicket("rememberDeviceTicket2");
        authorizationDTO2.setRememberMeTicket("rememberMeTicket2");

        List<FetchingUbiAccountAuthorizationEntryEntity> entities = mapper.mapToEntities(List.of(authorizationDTO1, authorizationDTO2));

        assertEquals(2, entities.size());
        assertTrue(
                entities.stream().anyMatch(
                        entity -> entity.getUbiProfileId().equals("profileId1") &&
                                  entity.getUbiAuthTicket().equals("ticket1") &&
                                  entity.getUbiSpaceId().equals("spaceId1") &&
                                  entity.getUbiSessionId().equals("sessionId1") &&
                                  entity.getUbiRememberDeviceTicket().equals("rememberDeviceTicket1") &&
                                  entity.getUbiRememberMeTicket().equals("rememberMeTicket1")
                )
        );
        assertTrue(
                entities.stream().anyMatch(
                        entity -> entity.getUbiProfileId().equals("profileId2") &&
                                  entity.getUbiAuthTicket().equals("ticket2") &&
                                  entity.getUbiSpaceId().equals("spaceId2") &&
                                  entity.getUbiSessionId().equals("sessionId2") &&
                                  entity.getUbiRememberDeviceTicket().equals("rememberDeviceTicket2") &&
                                  entity.getUbiRememberMeTicket().equals("rememberMeTicket2")
                )
        );
    }
}