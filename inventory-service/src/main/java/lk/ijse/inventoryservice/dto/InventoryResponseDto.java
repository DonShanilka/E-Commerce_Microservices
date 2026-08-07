package lk.ijse.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

    private Long id;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;

}