package github.ricemonger.users_ubi_accs_reauthorizer.authorization;

import github.ricemonger.utils.exceptions.server.UbiCredentialsInnerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

@Component
public class AesPasswordEncoder {

    private final static String ALGORITHM = "AES";

    @Value("${auth.user_service.password.encryption.key}")
    private String encryptionKey;

    public String decode(String encodedPassword) {
        try {
            Key aesKey = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encodedPassword)));
        } catch (GeneralSecurityException | NullPointerException e) {
            throw new UbiCredentialsInnerException(e);
        }
    }
}
