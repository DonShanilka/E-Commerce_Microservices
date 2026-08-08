package lk.ijse.cartservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    private Long productId;

    private String productName;

    private Double price;

    private Integer quantity;

    private Double subtotal;
}