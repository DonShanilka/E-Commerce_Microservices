package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.AuthRequest;
import lk.ijse.userservice.dto.AuthResponse;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final long TOKEN_TTL_SECONDS = 900;
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final JwtEncoder jwtEncoder;

    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        User user = users.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(AuthService::invalidCredentials);
        String stored = user.getPassword();
        if (stored == null || request.password().getBytes(StandardCharsets.UTF_8).length > 72) throw invalidCredentials();
        boolean legacyPlaintext = stored != null && !stored.startsWith("$2a$") && !stored.startsWith("$2b$") && !stored.startsWith("$2y$");
        boolean matches = legacyPlaintext
                ? MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8), request.password().getBytes(StandardCharsets.UTF_8))
                : passwords.matches(request.password(), stored);
        if (!matches) throw invalidCredentials();
        if (legacyPlaintext) {
            user.setPassword(passwords.encode(request.password()));
            users.save(user);
        }

        Instant now = Instant.now();
        String role = user.getRole() == null || user.getRole().isBlank() ? "CUSTOMER" : user.getRole().toUpperCase(Locale.ROOT);
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("ecommerce-user-service")
                .issuedAt(now).expiresAt(now.plus(TOKEN_TTL_SECONDS, ChronoUnit.SECONDS))
                .subject(user.getId().toString()).claim("email", user.getEmail())
                .claim("roles", List.of(role)).build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        UserResponseDto profile = UserResponseDto.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName()).email(user.getEmail()).phone(user.getPhone()).role(role)
                .image("/api/v1/users/" + user.getId() + "/image").build();
        return new AuthResponse(token, "Bearer", TOKEN_TTL_SECONDS, profile);
    }

    private static ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
    }
}
