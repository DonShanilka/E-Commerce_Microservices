package lk.ijse.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequestDto {

    private Long productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;

}