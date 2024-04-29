package github.ricemonger.marketplace.graphs.database.neo4j.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class AesPasswordEncoderTests {

    @Autowired
    private AesPasswordEncoder AESPasswordEncoder;

    @Test
    public void encryptionShouldReturnEncodedPassword() {
        assertNotEquals(AESPasswordEncoder.getEncodedPassword("password"), "password");
    }

    @Test
    public void decryptionShouldReturnOriginalPassword() {
        String password = "password";
        String encodedPassword = AESPasswordEncoder.getEncodedPassword(password);

        assertEquals(AESPasswordEncoder.getDecodedPassword(encodedPassword), password);
    }
}