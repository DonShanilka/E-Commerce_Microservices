package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.exception.DataPersistException;
import lk.ijse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserResponseDto map(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .image("/api/v1/users/" + user.getId() + "/image")
                .build();
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()
                || dto.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new DataPersistException("Password must be non-empty and at most 72 UTF-8 bytes.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            System.out.println(dto.getEmail() + " : User Email Already Exist");
            throw new DataPersistException(dto.getEmail() + " : User Email Already Exist");
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .role("CUSTOMER")
                .image(dto.getImage())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .role(savedUser.getRole())
                .image(savedUser.getImage())
                .build();

    }

    @Override
    public List<UserResponseDto> getAll() {

        List<User> users = userRepository.findAll();
            return users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .role(user.getRole())
                        .image(user.getImage())
                        .build())
                .toList();
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new DataPersistException("User ID " + id + " does not exist."));

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .image(user.getImage())
                .build();
    }

    @Override
    public UserResponseDto update(UserRequestDto dto) {

        User user = userRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new DataPersistException("User ID " + dto.getId() + " does not exist."));

        // Update first name
        if (dto.getFirstName() != null && !dto.getFirstName().trim().isEmpty()) {
            user.setFirstName(dto.getFirstName());
        }

        // Update last name
        if (dto.getLastName() != null && !dto.getLastName().trim().isEmpty()) {
            user.setLastName(dto.getLastName());
        }

        // Update email
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            if (!dto.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(dto.getEmail())) {
                throw new DataPersistException("Email already exists: " + dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }

        // Update phone
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            user.setPhone(dto.getPhone());
        }

        // Update image
        if (dto.getImage() != null && !dto.getImage().trim().isEmpty()) {
            user.setImage(dto.getImage());
        }
        // Password is Not Update

        User updatedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .email(updatedUser.getEmail())
                .phone(updatedUser.getPhone())
                .role(updatedUser.getRole())
                .image(updatedUser.getImage())
                .build();
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new DataPersistException("User ID " + id + " does not exist."));

        userRepository.delete(user);
    }
}
