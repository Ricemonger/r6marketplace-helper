package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.services.abstractions.UserDatabaseService;
import github.ricemonger.utils.DTOs.UserForCentralTradeManager;
import github.ricemonger.utils.DTOs.items.ItemEntityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserDatabaseService userDatabaseService;

    @Test
    public void getAllManageableUsers_should_return_service_result() {
        List<ItemEntityDTO> existingItems = List.of(new ItemEntityDTO());

        List expected = List.of(new UserForCentralTradeManager());
        when(userDatabaseService.getAllManageableUsers(existingItems)).thenReturn(expected);

        assertSame(expected, userService.getAllManageableUsers(existingItems));
    }

    @Test
    public void getAllManageableUsers_should_throw_if_service_throws() {
        List<ItemEntityDTO> existingItems = List.of(new ItemEntityDTO());

        when(userDatabaseService.getAllManageableUsers(existingItems)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> userService.getAllManageableUsers(existingItems));
    }
}