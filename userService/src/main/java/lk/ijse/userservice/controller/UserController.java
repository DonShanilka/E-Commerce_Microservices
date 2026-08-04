package lk.ijse.userservice.controller;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repository.UserRepository;
import lk.ijse.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Long id
    ){

        User user = userRepository.findById(id)
                .orElseThrow();


        return ResponseEntity
                .ok()
                .contentType(
                        MediaType.parseMediaType(
                                user.getImageType()
                        )
                )
                .body(user.getImage());

    }



    @PostMapping(
            path = "/createUser",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponseDto> createUser(

            @RequestPart("user")
            UserRequestDto dto,


            @RequestPart(
                    value = "image",
                    required = false
            )
            MultipartFile image

    ){

        return ResponseEntity.ok(
                userService.createUser(dto,image)
        );

    }

}
