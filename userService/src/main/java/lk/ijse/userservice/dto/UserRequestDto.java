package lk.ijse.userservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String image;

}