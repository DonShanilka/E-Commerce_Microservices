package lk.ijse.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private Long productId;
    private Integer quantity;

}
