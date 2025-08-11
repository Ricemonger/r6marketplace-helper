package github.ricemonger.fast_sell_trade_manager.services;

import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.abstractions.UbiAccountEntryDatabaseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@SpringBootTest
class UbiAccountServiceTest {
    @Autowired
    private UbiAccountService ubiAccountService;
    @MockBean
    private UbiAccountEntryDatabaseService ubiAccountEntryDatabaseService;

    @Test
    public void getFastSellManagedUserById_should_return_db_service_result() {
        ManagedUser user = Mockito.mock(ManagedUser.class);

        when(ubiAccountEntryDatabaseService.getFastSellManagedUserById(1L, "email")).thenReturn(user);

        assertSame(user, ubiAccountService.getFastSellManagedUserById(1L, "email"));
    }

    @Test
    public void getOwnedItemsMedianPriceAndRarity_should_return_db_service_result() {
        List list = Mockito.mock(List.class);

        when(ubiAccountEntryDatabaseService.getOwnedItemsMedianPriceAndRarity("ubiProfileId")).thenReturn(list);

        assertSame(list, ubiAccountService.getOwnedItemsMedianPriceAndRarity("ubiProfileId"));
    }

    @Test
    public void getAllFetchAccountsAuthorizationDTOs_should_return_db_service_result() {
        List list = Mockito.mock(List.class);

        when(ubiAccountEntryDatabaseService.getAllFetchAccountsAuthorizationDTOs()).thenReturn(list);

        assertSame(list, ubiAccountService.getAllFetchAccountsAuthorizationDTOs());
    }
}