package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.authorization.AuthorizationService;
import github.ricemonger.marketplace.services.abstractions.UbiUserDatabaseService;
import github.ricemonger.utils.dtos.AuthorizationDTO;
import github.ricemonger.utils.dtos.UbiUser;
import github.ricemonger.utils.exceptions.UbiUserAuthorizationClientErrorException;
import github.ricemonger.utils.exceptions.UbiUserAuthorizationServerErrorException;
import github.ricemonger.utils.exceptions.UbiUserDoesntExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UbiUserServiceTest {

    @MockBean
    private UbiUserDatabaseService ubiUserDatabaseService;

    @MockBean
    private  AuthorizationService authorizationService;

    @Autowired
    private UbiUserService ubiUserService;

    @Test
    public void getOwnedItemsIds_should_return_owned_items_ids() {
        String chatId = "chatId";
        String email = "email";

        List<String> ids = List.of("id1", "id2");

        when(ubiUserDatabaseService.getOwnedItemsIds(chatId, email)).thenReturn(ids);

        List<String> result = new ArrayList<>(ubiUserService.getOwnedItemsIds(chatId, email));

        assertTrue(ids.containsAll(result) && result.containsAll(ids));
    }

    @Test
    public void getOwnedItemsIds_should_throw_if_ubi_user_doesnt_exist(){
        when(ubiUserDatabaseService.getOwnedItemsIds("chatId", "email")).thenThrow(new UbiUserDoesntExistException());

        assertThrows(UbiUserDoesntExistException.class, () -> ubiUserService.getOwnedItemsIds("chatId", "email"));
    }

    @Test
    public void findAllByLinkedTelegramUserChatId_should_return_all_users_by_chat_id(){
        String chatId = "chatId";

        List<UbiUser> users = List.of(new UbiUser(), new UbiUser());

        when(ubiUserDatabaseService.findAllByChatId(chatId)).thenReturn(users);

        List<UbiUser> result = new ArrayList<>(ubiUserService.findAllByLinkedTelegramUserChatId(chatId));

        assertTrue(users.containsAll(result) && result.containsAll(users));
    }

    @Test
    public void deleteAllByLinkedTelegramUserChatId_should_delete_all_users_by_chat_id(){
        String chatId = "chatId";

        ubiUserService.deleteAllByLinkedTelegramUserChatId(chatId);

        verify(ubiUserDatabaseService).deleteAllByChatId(chatId);
    }

    @Test
    public void deleteByLinkedTelegramUserChatIdAndEmail_should_delete_user_by_chat_id_and_email(){
        String chatId = "chatId";
        String email = "email";

        ubiUserService.deleteByLinkedTelegramUserChatIdAndEmail(chatId, email);

        verify(ubiUserDatabaseService).deleteById(chatId, email);
    }

    @Test
    public void authorizeAndSaveUser_should_authorize_and_save_user(){
        String chatId = "chatId";
        String email = "email";
        String password = "password";
        when(authorizationService.authorizeAndGetDTO(email, password)).thenReturn(new AuthorizationDTO());

        ubiUserService.authorizeAndSaveUser(chatId, email, password);

        verify(authorizationService).authorizeAndGetDTO(email, password);
        verify(ubiUserDatabaseService).save(any());
    }

    @Test
    public void authorizeAndSaveUser_should_throw_if_authorization_client_error(){
        String chatId = "chatId";
        String email = "email";
        String password = "password";

        when(authorizationService.authorizeAndGetDTO(email, password)).thenThrow(new UbiUserAuthorizationClientErrorException());

        assertThrows(UbiUserAuthorizationClientErrorException.class, () -> ubiUserService.authorizeAndSaveUser(chatId, email, password));
    }

    @Test
    public void authorizeAndSaveUser_should_throw_if_authorization_server_error(){
        String chatId = "chatId";
        String email = "email";
        String password = "password";

        when(authorizationService.authorizeAndGetDTO(email, password)).thenThrow(new UbiUserAuthorizationServerErrorException());

        assertThrows(UbiUserAuthorizationServerErrorException.class, () -> ubiUserService.authorizeAndSaveUser(chatId, email, password));
    }

    @Test
    public void reauthorizeAllUbiUsersAndGetUnauthorizedList_should_reauthorize_all_users_and_return_unauthorized(){

        UbiUser user1 = new UbiUser();
        user1.setEmail("email1");
        user1.setEncodedPassword("encodedPassword1");
        UbiUser user2 = new UbiUser();
        user2.setEmail("email2");
        user2.setEncodedPassword("encodedPassword2");

        List<UbiUser> users = List.of(user1, user2);

        when(ubiUserDatabaseService.findAll()).thenReturn(users);

        List<UbiUser> unauthorizedUsers = List.of(users.get(0));

        when(authorizationService.authorizeAndGetDtoForEncodedPassword("email1","encodedPassword1")).thenThrow(new UbiUserAuthorizationClientErrorException());
        when(authorizationService.authorizeAndGetDtoForEncodedPassword("email2","encodedPassword2")).thenReturn(new AuthorizationDTO());

        List<UbiUser> result = new ArrayList<>(ubiUserService.reauthorizeAllUbiUsersAndGetUnauthorizedList());

        assertTrue(unauthorizedUsers.containsAll(result) && result.containsAll(unauthorizedUsers));
    }
}