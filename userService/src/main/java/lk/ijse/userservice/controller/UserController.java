package lk.ijse.userservice.controller;

import lk.ijse.userservice.dto.UserRequestDto;
import lk.ijse.userservice.dto.UserResponseDto;
import lk.ijse.userservice.exception.DataPersistException;
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
    public ResponseEntity<?> createUser(
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

//            if (response.equals(email)) {
//                System.out.println("Email Already Exists");
//                return ResponseEntity
//                        .status(HttpStatus.CONFLICT)
//                        .body("Email" + " already exists.");
//            }

            System.out.println("Response User : " + response);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUser(
            @RequestParam("id") Long id,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {

            String imageBase64 = null;
            if (image != null && !image.isEmpty()) {
                imageBase64 = AppUtil.imageBase64(image.getBytes());
            }

            UserRequestDto dto = UserRequestDto.builder()
                    .id(id)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .role(role)
                    .image(imageBase64)
                    .build();

            UserResponseDto response = userService.update(dto);

            return ResponseEntity.ok(response);
        } catch (DataPersistException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}
