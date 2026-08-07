package lk.ijse.inventoryservice.service;

import lk.ijse.inventoryservice.dto.InventoryRequestDto;
import lk.ijse.inventoryservice.dto.InventoryResponseDto;
import lk.ijse.inventoryservice.entity.Inventory;
import lk.ijse.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private final InventoryRepository repository;

    @Override
    public InventoryResponseDto create(InventoryRequestDto dto) {
        Inventory inventory = Inventory.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .availableQuantity(dto.getAvailableQuantity())
                .reservedQuantity(dto.getReservedQuantity())
                .build();

        Inventory saved = repository.save(inventory);

        return map(saved);
    }

    @Override
    public InventoryResponseDto getByProductId(Long productId) {
            Inventory inventory = repository.findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException("Product stock not found"));
            return map(inventory);
    }

    @Override
    public List<InventoryResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();

    }

    @Override
    public void reduceStock(Long productId, Integer quantity) {

    }

    private InventoryResponseDto map(
            Inventory inventory){

        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .build();
    }
}
