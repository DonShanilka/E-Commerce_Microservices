package lk.ijse.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long productId;

    private String productName;

    private Double price;

    private Integer quantity;

    private Double subtotal;
}