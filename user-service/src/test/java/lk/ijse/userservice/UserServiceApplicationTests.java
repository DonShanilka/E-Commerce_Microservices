package lk.ijse.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Base64;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceApplicationTests {
    private static final String TEST_SECRET = Base64.getEncoder().encodeToString(new byte[32]);

    @DynamicPropertySource
    static void jwtSecret(DynamicPropertyRegistry properties) {
        properties.add("security.jwt.secret-base64", () -> TEST_SECRET);
    }

    @Test
    void contextLoads() {
    }
}
