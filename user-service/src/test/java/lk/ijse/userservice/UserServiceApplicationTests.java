package lk.ijse.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceApplicationTests {
    private static final KeyPair TEST_KEYS = createTestKeys();

    @DynamicPropertySource
    static void jwtKeys(DynamicPropertyRegistry properties) {
        properties.add("security.jwt.private-key-base64", () -> Base64.getEncoder().encodeToString(TEST_KEYS.getPrivate().getEncoded()));
        properties.add("security.jwt.public-key-base64", () -> Base64.getEncoder().encodeToString(TEST_KEYS.getPublic().getEncoded()));
    }

    private static KeyPair createTestKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Test
    void contextLoads() {
    }
}
