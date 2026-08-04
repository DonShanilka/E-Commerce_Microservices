package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

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
    public UserResponseDto createUser(
            UserRequestDto dto,
            MultipartFile image
    ) {

        try {

            User user = User.builder()
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .email(dto.getEmail())
                    .password(dto.getPassword())
                    .phone(dto.getPhone())
                    .role(dto.getRole())
                    .build();


            // Store image as bytea
            if(image != null && !image.isEmpty()) {

                user.setImage(image.getBytes());
                user.setImageName(image.getOriginalFilename());
                user.setImageType(image.getContentType());

            }


            User savedUser = repository.save(user);


            return map(savedUser);


        } catch(Exception e) {

            throw new RuntimeException(
                    "User creation failed",
                    e
            );

        }

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
