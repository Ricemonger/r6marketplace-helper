package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.authorization.AuthorizationService;
import github.ricemonger.marketplace.services.abstractions.TelegramUserUbiAccountEntryDatabaseService;
import github.ricemonger.utils.DTOs.personal.UbiAccountAuthorizationEntry;
import github.ricemonger.utils.DTOs.personal.UbiAccountAuthorizationEntryWithTelegram;
import github.ricemonger.utils.DTOs.personal.UbiAccountEntryWithTelegram;
import github.ricemonger.utils.DTOs.personal.UbiAccountStatsEntityDTO;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import github.ricemonger.utils.exceptions.client.TelegramUserDoesntExistException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryAlreadyExistsException;
import github.ricemonger.utils.exceptions.client.UbiAccountEntryDoesntExistException;
import github.ricemonger.utils.exceptions.client.UbiUserAuthorizationClientErrorException;
import github.ricemonger.utils.exceptions.server.UbiUserAuthorizationServerErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TelegramUserUbiAccountEntryServiceTest {
    @Autowired
    private TelegramUserUbiAccountEntryService telegramUserUbiAccountEntryService;
    @MockBean
    private AuthorizationService authorizationService;
    @MockBean
    private TelegramUserUbiAccountEntryDatabaseService telegramUserUbiAccountEntryDatabaseService;

    @Test
    public void authorizeAndSaveUser_should_handle_to_services() {
        String email = "email";
        String password = "password";
        String twoFaCode = "twoFaCode";
        String encodedPassword = "encodedPassword";
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setTicket("ticket");
        when(authorizationService.authorizeAndGet2FaAuthorizedDTO(email, password, twoFaCode)).thenReturn(dto);
        when(authorizationService.encodePassword(password)).thenReturn(encodedPassword);

        telegramUserUbiAccountEntryService.authorizeAndSaveUser("chatId", email, password, twoFaCode);

        verify(authorizationService).authorizeAndGet2FaAuthorizedDTO(email, password, twoFaCode);

        verify(telegramUserUbiAccountEntryDatabaseService).saveAuthorizationInfo("chatId", buildUbiAccount(email, encodedPassword, dto));
    }

    @Test
    public void reauthorizeAndSaveExistingUserBy2FACode_should_reauthorize_using_email_and_encodedPassword_from_db_and_twoFaCode() {
        String email = "email";
        String password = "password";
        String twoFaCode = "twoFaCode";
        String encodedPassword = "encodedPassword";
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setTicket("ticket");
        UbiAccountAuthorizationEntry authEntry = new UbiAccountAuthorizationEntry();
        authEntry.setEmail(email);
        authEntry.setEncodedPassword(encodedPassword);
        when(telegramUserUbiAccountEntryDatabaseService.findAuthorizationInfoByChatId(any())).thenReturn(authEntry);
        when(authorizationService.authorizeAndGet2FaAuthorizedDTOForEncodedPassword(email, encodedPassword, twoFaCode)).thenReturn(dto);

        telegramUserUbiAccountEntryService.reauthorizeAndSaveExistingUserBy2FACode("chatId", twoFaCode);

        verify(telegramUserUbiAccountEntryDatabaseService).saveAuthorizationInfo("chatId", buildUbiAccount(email, encodedPassword, dto));
    }

    @Test
    public void saveAll_UbiAccountStats_should_handle_to_service() {
        List<UbiAccountStatsEntityDTO> updatedUbiAccounts = new ArrayList<>();
        telegramUserUbiAccountEntryService.saveAllUbiAccountStats(updatedUbiAccounts);

        verify(telegramUserUbiAccountEntryDatabaseService).saveAllUbiAccountStats(same(updatedUbiAccounts));
    }

    @Test
    public void deleteByChatId_should_handle_to_service() {
        telegramUserUbiAccountEntryService.deleteByChatId("chatId");

        verify(telegramUserUbiAccountEntryDatabaseService).deleteAuthorizationInfoByChatId("chatId");
    }

    @Test
    public void deleteByChatId_should_throw_if_user_doesnt_exist() {
        doThrow(TelegramUserDoesntExistException.class).when(telegramUserUbiAccountEntryDatabaseService).deleteAuthorizationInfoByChatId(any());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserUbiAccountEntryService.deleteByChatId("chatId"));
    }

    @Test
    public void reauthorizeAllUbiUsersAndGetUnauthorizedList_should_reauthorize_all_users_and_return_unauthorized() {
        UbiAccountAuthorizationEntry authorizedEntry = new UbiAccountAuthorizationEntry();
        authorizedEntry.setEmail("email");
        authorizedEntry.setEncodedPassword("encodedPassword");
        authorizedEntry.setUbiRememberDeviceTicket("rememberDeviceTicket");

        UbiAccountAuthorizationEntry clientErrorEntry = new UbiAccountAuthorizationEntry();
        clientErrorEntry.setEmail("email1");
        clientErrorEntry.setEncodedPassword("encodedPassword1");
        clientErrorEntry.setUbiRememberDeviceTicket("rememberDeviceTicket1");

        UbiAccountAuthorizationEntry serverErrorEntry = new UbiAccountAuthorizationEntry();
        serverErrorEntry.setEmail("email2");
        serverErrorEntry.setEncodedPassword("encodedPassword2");
        serverErrorEntry.setUbiRememberDeviceTicket("rememberDeviceTicket2");

        UbiAccountAuthorizationEntry invalidRememberDeviceTicketEntry = new UbiAccountAuthorizationEntry();
        invalidRememberDeviceTicketEntry.setEmail("email3");
        invalidRememberDeviceTicketEntry.setEncodedPassword("encodedPassword3");
        invalidRememberDeviceTicketEntry.setUbiRememberDeviceTicket("rememberDeviceTicket3");

        List<UbiAccountAuthorizationEntryWithTelegram> authUsers = new ArrayList<>();

        authUsers.add(new UbiAccountAuthorizationEntryWithTelegram("1", true, authorizedEntry));
        authUsers.add(new UbiAccountAuthorizationEntryWithTelegram("2", false, authorizedEntry));
        authUsers.add(new UbiAccountAuthorizationEntryWithTelegram("3", null, authorizedEntry));

        List<UbiAccountAuthorizationEntryWithTelegram> authUsersWithDatabaseExceptions = new ArrayList<>();

        authUsersWithDatabaseExceptions.add(new UbiAccountAuthorizationEntryWithTelegram("6", true, authorizedEntry));
        authUsersWithDatabaseExceptions.add(new UbiAccountAuthorizationEntryWithTelegram("7", false, authorizedEntry));

        AuthorizationDTO validAuthDTO = new AuthorizationDTO("ticket", "profileId", "spaceId", "sessionId", "rememberDeviceTicket", "rememberMeTicket");

        AuthorizationDTO invalidAuthDTO = new AuthorizationDTO("ticket", null, "spaceId", "sessionId", "rememberDeviceTicket", "rememberMeTicket");

        when(authorizationService.reauthorizeAndGet2FaAuthorizedDtoForEncodedPasswordWithRememberDeviceTicket(authorizedEntry.getEmail(),
                authorizedEntry.getEncodedPassword(), authorizedEntry.getUbiRememberDeviceTicket())).thenReturn(validAuthDTO);
        when(authorizationService.reauthorizeAndGet2FaAuthorizedDtoForEncodedPasswordWithRememberDeviceTicket(invalidRememberDeviceTicketEntry.getEmail(),
                invalidRememberDeviceTicketEntry.getEncodedPassword(), invalidRememberDeviceTicketEntry.getUbiRememberDeviceTicket())).thenReturn(invalidAuthDTO);

        doThrow(TelegramUserDoesntExistException.class).when(telegramUserUbiAccountEntryDatabaseService).saveAuthorizationInfo(eq("6"), eq(authorizedEntry));
        doThrow(UbiAccountEntryAlreadyExistsException.class).when(telegramUserUbiAccountEntryDatabaseService).saveAuthorizationInfo(eq("7"), eq(authorizedEntry));

        List<UbiAccountAuthorizationEntryWithTelegram> unAuthUsers = new ArrayList<>();
        unAuthUsers.add(new UbiAccountAuthorizationEntryWithTelegram("4", null, clientErrorEntry));
        unAuthUsers.add(new UbiAccountAuthorizationEntryWithTelegram("5", true, serverErrorEntry));
        unAuthUsers.add(new UbiAccountAuthorizationEntryWithTelegram("8", false, invalidRememberDeviceTicketEntry));

        doThrow(UbiUserAuthorizationClientErrorException.class).when(authorizationService).reauthorizeAndGet2FaAuthorizedDtoForEncodedPasswordWithRememberDeviceTicket(clientErrorEntry.getEmail(), clientErrorEntry.getEncodedPassword(), clientErrorEntry.getUbiRememberDeviceTicket());
        doThrow(UbiUserAuthorizationServerErrorException.class).when(authorizationService).reauthorizeAndGet2FaAuthorizedDtoForEncodedPasswordWithRememberDeviceTicket(serverErrorEntry.getEmail(), serverErrorEntry.getEncodedPassword(), serverErrorEntry.getUbiRememberDeviceTicket());

        List<UbiAccountAuthorizationEntryWithTelegram> allUsers = new ArrayList<>();
        allUsers.addAll(authUsers);
        allUsers.addAll(authUsersWithDatabaseExceptions);
        allUsers.addAll(unAuthUsers);

        when(telegramUserUbiAccountEntryDatabaseService.findAllAuthorizationInfoForTelegram()).thenReturn(allUsers);

        List<UbiAccountAuthorizationEntryWithTelegram> result = telegramUserUbiAccountEntryService.reauthorizeAllUbiUsersAndGetUnauthorizedList();

        assertTrue(result.containsAll(unAuthUsers) && unAuthUsers.containsAll(result));

        verify(telegramUserUbiAccountEntryDatabaseService).findAllAuthorizationInfoForTelegram();

        verify(authorizationService, times(8)).reauthorizeAndGet2FaAuthorizedDtoForEncodedPasswordWithRememberDeviceTicket(any(), any(), any());

        UbiAccountAuthorizationEntry authorizedEntryToSave = new UbiAccountAuthorizationEntry();
        authorizedEntryToSave.setEmail("email");
        authorizedEntryToSave.setEncodedPassword("encodedPassword");
        authorizedEntryToSave.setUbiProfileId("profileId");
        authorizedEntryToSave.setUbiSessionId("sessionId");
        authorizedEntryToSave.setUbiAuthTicket("ticket");
        authorizedEntryToSave.setUbiSpaceId("spaceId");
        authorizedEntryToSave.setUbiRememberMeTicket("rememberMeTicket");
        authorizedEntryToSave.setUbiRememberDeviceTicket("rememberDeviceTicket");

        verify(telegramUserUbiAccountEntryDatabaseService, times(5)).saveAuthorizationInfo(any(), eq(authorizedEntryToSave));
        verify(telegramUserUbiAccountEntryDatabaseService, times(0)).saveAuthorizationInfo(any(), eq(clientErrorEntry));
        verify(telegramUserUbiAccountEntryDatabaseService, times(0)).saveAuthorizationInfo(any(), eq(serverErrorEntry));
    }

    @Test
    public void findByChatId_should_return_ubi_account_entry() {
        String chatId = "chatId";
        UbiAccountAuthorizationEntry entry = new UbiAccountAuthorizationEntry();
        entry.setEmail("email");
        when(telegramUserUbiAccountEntryDatabaseService.findAuthorizationInfoByChatId(chatId)).thenReturn(entry);

        assertEquals(entry, telegramUserUbiAccountEntryService.findByChatId(chatId));

        verify(telegramUserUbiAccountEntryDatabaseService).findAuthorizationInfoByChatId(chatId);
    }

    @Test
    public void findByChatId_should_throw_if_user_doesnt_exist() {
        doThrow(TelegramUserDoesntExistException.class).when(telegramUserUbiAccountEntryDatabaseService).findAuthorizationInfoByChatId(any());

        assertThrows(TelegramUserDoesntExistException.class, () -> telegramUserUbiAccountEntryService.findByChatId("chatId"));
    }

    @Test
    public void findByChatId_should_throw_if_ubi_account_entry_doesnt_exist() {
        doThrow(UbiAccountEntryDoesntExistException.class).when(telegramUserUbiAccountEntryDatabaseService).findAuthorizationInfoByChatId(any());

        assertThrows(UbiAccountEntryDoesntExistException.class, () -> telegramUserUbiAccountEntryService.findByChatId("chatId"));
    }

    @Test
    public void findAll_WithTelegram_should_return_service_result() {
        List<UbiAccountEntryWithTelegram> mockList = new ArrayList<>();
        when(telegramUserUbiAccountEntryDatabaseService.findAllForTelegram()).thenReturn(mockList);

        assertSame(mockList, telegramUserUbiAccountEntryService.findAllFUbiAccountEntriesWithTelegram());
    }

    private UbiAccountAuthorizationEntry buildUbiAccount(String email, String password, AuthorizationDTO authorizationDTO) {
        UbiAccountAuthorizationEntry user = new UbiAccountAuthorizationEntry();
        user.setEmail(email);
        user.setEncodedPassword(password);

        user.setUbiProfileId(authorizationDTO.getProfileId());
        user.setUbiSessionId(authorizationDTO.getSessionId());
        user.setUbiAuthTicket(authorizationDTO.getTicket());
        user.setUbiSpaceId(authorizationDTO.getSpaceId());
        user.setUbiRememberMeTicket(authorizationDTO.getRememberMeTicket());
        user.setUbiRememberDeviceTicket(authorizationDTO.getRememberDeviceTicket());
        return user;
    }
}