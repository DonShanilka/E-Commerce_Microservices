package lk.ijse.userservice.controller;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.service.UserService;
import lk.ijse.userservice.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> createUser(
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("phone") String phone,
            @RequestPart("role") String role,
            @RequestPart("image") MultipartFile image
    ){
        try {

            byte[] imageBytes = image.getBytes();
            String imageBase64 = AppUtil.imageBase64(imageBytes);

            UserRequestDto dto = UserRequestDto.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .password(password)
                            .phone(phone)
                            .role(role)
                            .image(imageBase64)
//                            .imageName(image.getOriginalFilename())
//                            .imageType(image.getContentType())
                            .build();

            UserResponseDto response = userService.createUser(dto);
            System.out.println("Response User : " + response);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
