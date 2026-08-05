package lk.ijse.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private Long id;

    @NotBlank
    private String name;
    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    private String category;
    private String brand;
    private String image;
}