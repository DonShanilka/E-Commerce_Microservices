package lk.ijse.userservice.controller;

import jakarta.validation.Valid;
import lk.ijse.userservice.dto.AuthRequest;
import lk.ijse.userservice.dto.AuthResponse;
import lk.ijse.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }
}
