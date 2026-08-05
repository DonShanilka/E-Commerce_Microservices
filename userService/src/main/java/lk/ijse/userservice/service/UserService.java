package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);
    List<UserResponseDto> getAll();
    UserResponseDto getById(Long id);
    UserResponseDto update(Long id, UserRequestDto dto, MultipartFile image);
    void delete(Long id);

}
