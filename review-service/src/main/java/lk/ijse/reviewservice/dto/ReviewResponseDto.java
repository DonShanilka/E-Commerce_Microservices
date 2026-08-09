package lk.ijse.reviewservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;
    private Long userId;
    private Long productId;
    private Integer rating;
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}