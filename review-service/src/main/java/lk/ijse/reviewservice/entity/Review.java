package lk.ijse.reviewservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long productId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 1000)
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}