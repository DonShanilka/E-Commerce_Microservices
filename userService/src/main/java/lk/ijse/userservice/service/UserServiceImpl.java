package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository userRepository;

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

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .role(dto.getRole())
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
        return List.of();
    }

    @Override
    public UserResponseDto getById(Long id) {
        return null;
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto dto, MultipartFile image) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
