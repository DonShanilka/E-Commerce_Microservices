package lk.ijse.inventoryservice.service;

import jakarta.transaction.Transactional;
import lk.ijse.inventoryservice.dto.InventoryRequestDto;
import lk.ijse.inventoryservice.dto.InventoryResponseDto;
import lk.ijse.inventoryservice.entity.Inventory;
import lk.ijse.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository repository;

    @Override
    public InventoryResponseDto create(InventoryRequestDto dto) {
        Inventory existing = repository.findByProductId(dto.getProductId())
                .orElse(null);

        if (existing != null) {
            throw new RuntimeException(
                    "Inventory already exists for product: " + dto.getProductId()
            );
        }

        Inventory inventory = Inventory.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .availableQuantity(
                        dto.getAvailableQuantity() != null
                                ? dto.getAvailableQuantity()
                                : dto.getQuantity()
                )
                .reservedQuantity(
                        dto.getReservedQuantity() != null
                                ? dto.getReservedQuantity()
                                : 0
                )
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
    @Transactional
    public void reduceStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product stock not found: " + productId
                        )
                );

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException(
                    "Insufficient stock for product: " + productId
            );
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        repository.save(inventory);
    }

    private InventoryResponseDto map(Inventory inventory){
        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .build();
    }
}
