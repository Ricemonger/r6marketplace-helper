package github.ricemonger.ubi_users_stats_fetcher.postgres.repositories;

import github.ricemonger.ubi_users_stats_fetcher.postgres.dto_projections.UserAuthorizedUbiAccountProjection;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountEntryEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UbiAccountStatsEntity;
import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserAuthorizedUbiAccountEntryPostgresRepositoryTest {
    @Autowired
    private CustomUserUbiAccountEntryPostgresRepository customUserUbiAccountEntryPostgresRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    public void setUp() {
        customUserUbiAccountEntryPostgresRepository.deleteAll();
    }

    @Test
    @Transactional
    public void findAllUserAuthorizedUbiAccounts_should_return_projections_for_user_with_ubiAccountStats() {
        UserEntity userEntity1 = new UserEntity();
        entityManager.persist(userEntity1);

        ItemEntity itemEntity1 = new ItemEntity("itemId1");
        entityManager.persist(itemEntity1);
        ItemEntity itemEntity2 = new ItemEntity("itemId2");
        entityManager.persist(itemEntity2);

        UbiAccountEntryEntity ubiAccountEntryEntity1 = new UbiAccountEntryEntity();
        ubiAccountEntryEntity1.setUser(userEntity1);
        ubiAccountEntryEntity1.setEmail("email1");
        ubiAccountEntryEntity1.setUbiAuthTicket("ubiAuthTicket1");
        ubiAccountEntryEntity1.setUbiSpaceId("ubiSpaceId1");
        ubiAccountEntryEntity1.setUbiSessionId("ubiSessionId1");
        ubiAccountEntryEntity1.setUbiRememberDeviceTicket("ubiRememberDeviceTicket1");
        ubiAccountEntryEntity1.setUbiRememberMeTicket("ubiRememberMeTicket1");
        entityManager.persist(ubiAccountEntryEntity1);

        UbiAccountStatsEntity ubiAccountStatsEntity1 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity1.setUbiProfileId("ubiProfileId1");
        ubiAccountStatsEntity1.setCreditAmount(101);
        ubiAccountStatsEntity1.setOwnedItems(List.of(itemEntity1, itemEntity2));
        entityManager.persist(ubiAccountStatsEntity1);

        ubiAccountEntryEntity1.setUbiAccountStats(ubiAccountStatsEntity1);
        entityManager.merge(ubiAccountEntryEntity1);

        UserEntity userEntity2 = new UserEntity();
        entityManager.persist(userEntity2);

        UbiAccountEntryEntity ubiAccountEntryEntity2 = new UbiAccountEntryEntity();
        ubiAccountEntryEntity2.setUser(userEntity2);
        ubiAccountEntryEntity2.setEmail("email2");
        ubiAccountEntryEntity2.setUbiAuthTicket("ubiAuthTicket2");
        ubiAccountEntryEntity2.setUbiSpaceId("ubiSpaceId2");
        ubiAccountEntryEntity2.setUbiSessionId("ubiSessionId2");
        ubiAccountEntryEntity2.setUbiRememberDeviceTicket("ubiRememberDeviceTicket2");
        ubiAccountEntryEntity2.setUbiRememberMeTicket("ubiRememberMeTicket2");
        entityManager.persist(ubiAccountEntryEntity2);

        UbiAccountStatsEntity ubiAccountStatsEntity2 = new UbiAccountStatsEntity();
        ubiAccountStatsEntity2.setUbiProfileId("ubiProfileId2");
        ubiAccountStatsEntity2.setCreditAmount(102);
        ubiAccountStatsEntity2.setOwnedItems(List.of());
        entityManager.persist(ubiAccountStatsEntity2);

        ubiAccountEntryEntity2.setUbiAccountStats(ubiAccountStatsEntity2);
        entityManager.merge(ubiAccountEntryEntity2);

        UserEntity userEntity3 = new UserEntity();
        entityManager.persist(userEntity3);

        UbiAccountEntryEntity ubiAccountEntryEntity3 = new UbiAccountEntryEntity();
        ubiAccountEntryEntity3.setUser(userEntity3);
        ubiAccountEntryEntity3.setEmail("email3");
        ubiAccountEntryEntity3.setUbiAuthTicket("ubiAuthTicket3");
        ubiAccountEntryEntity3.setUbiSpaceId("ubiSpaceId3");
        ubiAccountEntryEntity3.setUbiSessionId("ubiSessionId3");
        ubiAccountEntryEntity3.setUbiRememberDeviceTicket("ubiRememberDeviceTicket3");
        ubiAccountEntryEntity3.setUbiRememberMeTicket("ubiRememberMeTicket3");
        entityManager.persist(ubiAccountEntryEntity3);

        entityManager.flush();

        List<UserAuthorizedUbiAccountProjection> userAuthorizedUbiAccountProjections = customUserUbiAccountEntryPostgresRepository.findAllUserAuthorizedUbiAccounts();

        UserAuthorizedUbiAccountProjection projection1 = new UserAuthorizedUbiAccountProjection();
        projection1.setUserId(userEntity1.getId());
        projection1.setProfileId("ubiProfileId1");
        projection1.setCreditAmount(101);
        projection1.setOwnedItemsIdsCount(2);
        projection1.setTicket("ubiAuthTicket1");
        projection1.setSpaceId("ubiSpaceId1");
        projection1.setSessionId("ubiSessionId1");
        projection1.setRememberDeviceTicket("ubiRememberDeviceTicket1");
        projection1.setRememberMeTicket("ubiRememberMeTicket1");

        UserAuthorizedUbiAccountProjection projection2 = new UserAuthorizedUbiAccountProjection();
        projection2.setUserId(userEntity2.getId());
        projection2.setProfileId("ubiProfileId2");
        projection2.setCreditAmount(102);
        projection2.setOwnedItemsIdsCount(0);
        projection2.setTicket("ubiAuthTicket2");
        projection2.setSpaceId("ubiSpaceId2");
        projection2.setSessionId("ubiSessionId2");
        projection2.setRememberDeviceTicket("ubiRememberDeviceTicket2");
        projection2.setRememberMeTicket("ubiRememberMeTicket2");

        assertEquals(2, userAuthorizedUbiAccountProjections.size());
        assertTrue(userAuthorizedUbiAccountProjections.contains(projection1));
        assertTrue(userAuthorizedUbiAccountProjections.contains(projection2));
    }
}