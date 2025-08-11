package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class FetchUsersServiceTest {

    @Autowired
    FetchUsersService fetchUsersService;

    @BeforeEach
    void reflectionUtilsSetUp() {
        fetchUsersService = new FetchUsersService();
    }

    @Test
    public void saveFetchUsersAuthorizationDTOs_should_save_list() {
        List oldList = mock(List.class);

        ReflectionTestUtils.setField(fetchUsersService, "authorizationDTOs", oldList);

        List newList = mock(List.class);

        fetchUsersService.saveFetchUsersAuthorizationDTOs(newList);

        assertSame(newList, ReflectionTestUtils.getField(fetchUsersService, "authorizationDTOs"));
    }

    @Test
    public void nextFetchUsersAuthorizationDTO_should_throw_if_empty_list() {
        List<AuthorizationDTO> list = new ArrayList<>();

        ReflectionTestUtils.setField(fetchUsersService, "authorizationDTOs", list);

        assertThrows(FetchUsersNotFoundException.class, fetchUsersService::nextFetchUsersAuthorizationDTO);
    }

    @Test
    public void nextFetchUsersAuthorizationDTO_should_return_authorizationDTO_by_inex_and_update_index() {
        AuthorizationDTO dto0 = mock(AuthorizationDTO.class);
        AuthorizationDTO dto1 = mock(AuthorizationDTO.class);
        AuthorizationDTO dto2 = mock(AuthorizationDTO.class);
        AuthorizationDTO dto3 = mock(AuthorizationDTO.class);
        List<AuthorizationDTO> list = List.of(dto0, dto1, dto2, dto3);

        ReflectionTestUtils.setField(fetchUsersService, "authorizationDTOs", list);
        ReflectionTestUtils.setField(fetchUsersService, "nextFetchUserIndex", 0);

        AuthorizationDTO res0 = fetchUsersService.nextFetchUsersAuthorizationDTO();
        assertEquals(ReflectionTestUtils.getField(fetchUsersService, "nextFetchUserIndex"), 1);

        AuthorizationDTO res1 = fetchUsersService.nextFetchUsersAuthorizationDTO();
        assertEquals(ReflectionTestUtils.getField(fetchUsersService, "nextFetchUserIndex"), 2);

        AuthorizationDTO res2 = fetchUsersService.nextFetchUsersAuthorizationDTO();
        assertEquals(ReflectionTestUtils.getField(fetchUsersService, "nextFetchUserIndex"), 3);

        AuthorizationDTO res3 = fetchUsersService.nextFetchUsersAuthorizationDTO();
        assertEquals(ReflectionTestUtils.getField(fetchUsersService, "nextFetchUserIndex"), 4);

        AuthorizationDTO res4 = fetchUsersService.nextFetchUsersAuthorizationDTO();
        assertEquals(ReflectionTestUtils.getField(fetchUsersService, "nextFetchUserIndex"), 1);

        assertSame(dto0, res0);
        assertSame(dto1, res1);
        assertSame(dto2, res2);
        assertSame(dto3, res3);
        assertSame(dto0, res4);
    }
}