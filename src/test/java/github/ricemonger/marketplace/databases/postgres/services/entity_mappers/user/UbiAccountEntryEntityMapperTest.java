package github.ricemonger.marketplace.databases.postgres.services.entity_mappers.user;

import github.ricemonger.marketplace.databases.postgres.entities.item.ItemEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.TelegramUserEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountEntryEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UbiAccountStatsEntity;
import github.ricemonger.marketplace.databases.postgres.entities.user.UserEntity;
import github.ricemonger.marketplace.databases.postgres.repositories.UbiAccountStatsEntityPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.repositories.UserPostgresRepository;
import github.ricemonger.utils.DTOs.personal.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UbiAccountEntryEntityMapperTest {
    @Autowired
    private UbiAccountEntryEntityMapper ubiAccountEntryEntityMapper;
    @MockBean
    private UserPostgresRepository userPostgresRepository;
    @MockBean
    private UbiAccountStatsEntityPostgresRepository ubiAccountStatsEntityPostgresRepository;
    @MockBean
    private UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;

    @Test
    public void createEntityForTelegramUser_should_save_new_ubi_stats_entity_if_doesnt_exist() {
        when(userPostgresRepository.findByTelegramUserChatId("chatId")).thenReturn(new UserEntity(1L));
        when(ubiAccountStatsEntityPostgresRepository.findById("ubiProfileId")).thenReturn(Optional.empty());

        ubiAccountEntryEntityMapper.createEntityForTelegramUser("chatId", new UbiAccountAuthorizationEntry());

        verify(ubiAccountStatsEntityPostgresRepository).save(any(UbiAccountStatsEntity.class));
    }

    @Test
    public void createEntityForTelegramUser_should_properly_map_entity_for_existing_ubi_stats() {
        when(userPostgresRepository.findByTelegramUserChatId("chatId")).thenReturn(new UserEntity(1L));
        when(ubiAccountStatsEntityPostgresRepository.findById("ubiProfileId")).thenReturn(Optional.of(new UbiAccountStatsEntity("ubiProfileId")));

        UbiAccountAuthorizationEntry account = new UbiAccountAuthorizationEntry();
        account.setUbiProfileId("ubiProfileId");
        account.setEmail("email");
        account.setEncodedPassword("encodedPassword");
        account.setUbiSessionId("ubiSessionId");
        account.setUbiSpaceId("ubiSpaceId");
        account.setUbiAuthTicket("ubiAuthTicket");
        account.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        account.setUbiRememberMeTicket("ubiRememberMeTicket");

        UbiAccountEntryEntity expected = new UbiAccountEntryEntity();
        expected.setUser(new UserEntity(1L));
        expected.setEmail("email");
        expected.setEncodedPassword("encodedPassword");
        expected.setUbiSessionId("ubiSessionId");
        expected.setUbiSpaceId("ubiSpaceId");
        expected.setUbiAuthTicket("ubiAuthTicket");
        expected.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        expected.setUbiRememberMeTicket("ubiRememberMeTicket");
        expected.setUbiAccountStats(new UbiAccountStatsEntity("ubiProfileId"));

        assertTrue(expected.isFullyEqual(ubiAccountEntryEntityMapper.createEntityForTelegramUser("chatId", account)));
    }

    @Test
    public void createEntityForTelegramUser_should_properly_map_entity_for_non_existing_ubi_stats() {
        when(userPostgresRepository.findByTelegramUserChatId("chatId")).thenReturn(new UserEntity(1L));
        when(ubiAccountStatsEntityPostgresRepository.findById("ubiProfileId")).thenReturn(Optional.empty());
        UbiAccountStatsEntity ubiAccountStatsEntity = new UbiAccountStatsEntity("ubiProfileId");
        when(ubiAccountStatsEntityPostgresRepository.save(any())).thenReturn(ubiAccountStatsEntity);

        UbiAccountAuthorizationEntry account = new UbiAccountAuthorizationEntry();
        account.setUbiProfileId("ubiProfileId");
        account.setEmail("email");
        account.setEncodedPassword("encodedPassword");
        account.setUbiSessionId("ubiSessionId");
        account.setUbiSpaceId("ubiSpaceId");
        account.setUbiAuthTicket("ubiAuthTicket");
        account.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        account.setUbiRememberMeTicket("ubiRememberMeTicket");

        UbiAccountEntryEntity expected = new UbiAccountEntryEntity();
        expected.setUser(new UserEntity(1L));
        expected.setEmail("email");
        expected.setEncodedPassword("encodedPassword");
        expected.setUbiSessionId("ubiSessionId");
        expected.setUbiSpaceId("ubiSpaceId");
        expected.setUbiAuthTicket("ubiAuthTicket");
        expected.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        expected.setUbiRememberMeTicket("ubiRememberMeTicket");
        expected.setUbiAccountStats(new UbiAccountStatsEntity("ubiProfileId"));

        UbiAccountEntryEntity actual = ubiAccountEntryEntityMapper.createEntityForTelegramUser("chatId", account);
        //verify(ubiAccountStatsEntityPostgresRepository).save(same(ubiAccountStatsEntity));

        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);

        assertTrue(expected.isFullyEqual(actual));
    }

    @Test
    public void createEntity() {
        UbiAccountAuthorizationEntry account = new UbiAccountAuthorizationEntry();
        account.setUbiProfileId("ubiProfileId");
        account.setEmail("email");
        account.setEncodedPassword("encodedPassword");
        account.setUbiSessionId("ubiSessionId");
        account.setUbiSpaceId("ubiSpaceId");
        account.setUbiAuthTicket("ubiAuthTicket");
        account.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        account.setUbiRememberMeTicket("ubiRememberMeTicket");

        UbiAccountEntryEntity expected = new UbiAccountEntryEntity();
        expected.setUser(new UserEntity(1L));
        expected.setEmail("email");
        expected.setEncodedPassword("encodedPassword");
        expected.setUbiSessionId("ubiSessionId");
        expected.setUbiSpaceId("ubiSpaceId");
        expected.setUbiAuthTicket("ubiAuthTicket");
        expected.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        expected.setUbiRememberMeTicket("ubiRememberMeTicket");
        expected.setUbiAccountStats(new UbiAccountStatsEntity("ubiProfileId"));

        assertTrue(expected.isFullyEqual(ubiAccountEntryEntityMapper.createEntity(new UserEntity(1L), new UbiAccountStatsEntity(
                "ubiProfileId"), account)));
    }

    @Test
    public void createUbiAccountAuthorizationEntryWithTelegram() {
        UbiAccountEntryEntity entity = new UbiAccountEntryEntity();
        entity.setUser(new UserEntity(1L));
        entity.getUser().setTelegramUser(new TelegramUserEntity("chatId", 1L));
        entity.getUser().setPrivateNotificationsEnabledFlag(true);
        entity.setEmail("email");
        entity.setEncodedPassword("encodedPassword");
        entity.setUbiSessionId("ubiSessionId");
        entity.setUbiSpaceId("ubiSpaceId");
        entity.setUbiAuthTicket("ubiAuthTicket");
        entity.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        entity.setUbiRememberMeTicket("ubiRememberMeTicket");
        entity.setUbiAccountStats(new UbiAccountStatsEntity("ubiProfileId"));

        UbiAccountAuthorizationEntry authorizationEntry = new UbiAccountAuthorizationEntry();
        authorizationEntry.setUbiProfileId("ubiProfileId");
        authorizationEntry.setEmail("email");
        authorizationEntry.setEncodedPassword("encodedPassword");
        authorizationEntry.setUbiSessionId("ubiSessionId");
        authorizationEntry.setUbiSpaceId("ubiSpaceId");
        authorizationEntry.setUbiAuthTicket("ubiAuthTicket");
        authorizationEntry.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        authorizationEntry.setUbiRememberMeTicket("ubiRememberMeTicket");

        UbiAccountAuthorizationEntryWithTelegram expected = new UbiAccountAuthorizationEntryWithTelegram();
        expected.setChatId("chatId");
        expected.setPrivateNotificationsEnabledFlag(true);
        expected.setUbiAccountAuthorizationEntry(authorizationEntry);

        assertEquals(expected, ubiAccountEntryEntityMapper.createUbiAccountAuthorizationEntryWithTelegram(entity));
    }

    @Test
    public void createUbiAccountEntryWithTelegram() {
        UbiAccountEntryEntity entity = new UbiAccountEntryEntity();
        entity.setUser(new UserEntity(1L));
        entity.getUser().setTelegramUser(new TelegramUserEntity("chatId", 1L));
        entity.getUser().setPrivateNotificationsEnabledFlag(true);
        entity.setEmail("email");
        entity.setEncodedPassword("encodedPassword");
        entity.setUbiSessionId("ubiSessionId");
        entity.setUbiSpaceId("ubiSpaceId");
        entity.setUbiAuthTicket("ubiAuthTicket");
        entity.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        entity.setUbiRememberMeTicket("ubiRememberMeTicket");
        entity.setUbiAccountStats(new UbiAccountStatsEntity("ubiProfileId"));
        entity.getUbiAccountStats().setCreditAmount(0);
        entity.getUbiAccountStats().setOwnedItems(List.of(new ItemEntity("itemId1"), new ItemEntity("itemId2")));

        UbiAccountEntry ubiAccountEntry = new UbiAccountEntry();
        UbiAccountAuthorizationEntry authorizationEntry = new UbiAccountAuthorizationEntry();
        authorizationEntry.setUbiProfileId("ubiProfileId");
        authorizationEntry.setEmail("email");
        authorizationEntry.setEncodedPassword("encodedPassword");
        authorizationEntry.setUbiSessionId("ubiSessionId");
        authorizationEntry.setUbiSpaceId("ubiSpaceId");
        authorizationEntry.setUbiAuthTicket("ubiAuthTicket");
        authorizationEntry.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        authorizationEntry.setUbiRememberMeTicket("ubiRememberMeTicket");
        UbiAccountStatsEntityDTO ubiAccountStatsEntityDTO = new UbiAccountStatsEntityDTO();
        ubiAccountStatsEntityDTO.setUbiProfileId("ubiProfileId");
        ubiAccountStatsEntityDTO.setCreditAmount(0);
        ubiAccountStatsEntityDTO.setOwnedItemsIds(List.of("itemId1", "itemId2"));
        ubiAccountEntry.setUbiAccountStatsEntityDTO(ubiAccountStatsEntityDTO);
        ubiAccountEntry.setUbiAccountAuthorizationEntry(authorizationEntry);

        when(ubiAccountStatsEntityMapper.createDTO(any())).thenReturn(ubiAccountStatsEntityDTO);

        UbiAccountEntryWithTelegram expected = new UbiAccountEntryWithTelegram();
        expected.setChatId("chatId");
        expected.setPrivateNotificationsEnabledFlag(true);
        expected.setUbiAccountEntry(ubiAccountEntry);

        assertEquals(expected, ubiAccountEntryEntityMapper.createUbiAccountEntryWithTelegram(entity));
    }

    @Test
    public void createUbiAccountAuthorizationEntry() {
        UbiAccountEntryEntity entity = new UbiAccountEntryEntity();
        entity.setUser(new UserEntity(1L));
        entity.setEmail("email");
        entity.setEncodedPassword("encodedPassword");
        entity.setUbiSessionId("ubiSessionId");
        entity.setUbiSpaceId("ubiSpaceId");
        entity.setUbiAuthTicket("ubiAuthTicket");
        entity.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        entity.setUbiRememberMeTicket("ubiRememberMeTicket");
        entity.setUbiAccountStats(new UbiAccountStatsEntity("ubiProfileId"));

        UbiAccountAuthorizationEntry authorizationEntry = new UbiAccountAuthorizationEntry();
        authorizationEntry.setUbiProfileId("ubiProfileId");
        authorizationEntry.setEmail("email");
        authorizationEntry.setEncodedPassword("encodedPassword");
        authorizationEntry.setUbiSessionId("ubiSessionId");
        authorizationEntry.setUbiSpaceId("ubiSpaceId");
        authorizationEntry.setUbiAuthTicket("ubiAuthTicket");
        authorizationEntry.setUbiRememberDeviceTicket("ubiRememberDeviceTicket");
        authorizationEntry.setUbiRememberMeTicket("ubiRememberMeTicket");

        assertEquals(authorizationEntry, ubiAccountEntryEntityMapper.createUbiAccountAuthorizationEntry(entity));
    }
}