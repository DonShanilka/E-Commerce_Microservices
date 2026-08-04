package lk.ijse.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    private String role;
    private String imageName;
    private String imageType;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void created(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updated(){
        updatedAt = LocalDateTime.now();
    }

}