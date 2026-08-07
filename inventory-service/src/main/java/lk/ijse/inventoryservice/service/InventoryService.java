package lk.ijse.inventoryservice.service;

import lk.ijse.inventoryservice.dto.InventoryRequestDto;
import lk.ijse.inventoryservice.dto.InventoryResponseDto;

import java.util.List;

public interface InventoryService {

    InventoryResponseDto create(InventoryRequestDto dto);
    InventoryResponseDto getByProductId(Long productId);
    List<InventoryResponseDto> getAll();
    void reduceStock(Long productId,Integer quantity);

}
