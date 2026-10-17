package lk.ijse.userservice.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, UserResponseDto user) { }
