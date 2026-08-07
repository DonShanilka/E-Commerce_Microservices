package lk.ijse.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {

    private Long productId;
    private Integer quantity;

}
