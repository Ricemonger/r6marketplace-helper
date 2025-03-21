package github.ricemonger.marketplace.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorizationServiceTest {
    @MockBean
    private AesPasswordEncoder aesPasswordEncoder;

    @SpyBean
    private AuthorizationService authorizationService;

    @Test
    void encode_password() {
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(aesPasswordEncoder.encode(password)).thenReturn(encodedPassword);

        String actualEncodedPassword = authorizationService.encodePassword(password);

        assertEquals(encodedPassword, actualEncodedPassword);
    }
}
