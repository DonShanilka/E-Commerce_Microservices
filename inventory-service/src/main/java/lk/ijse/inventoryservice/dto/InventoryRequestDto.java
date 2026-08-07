package lk.ijse.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequestDto {

    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;

}