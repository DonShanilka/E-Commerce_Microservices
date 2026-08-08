package lk.ijse.cartservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    private String productName;

    @NotNull
    private Double price;

    @NotNull
    @Min(1)
    private Integer quantity;
}