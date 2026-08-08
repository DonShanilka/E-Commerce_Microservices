package lk.ijse.inventoryservice.controller;

import lk.ijse.inventoryservice.dto.InventoryRequestDto;
import lk.ijse.inventoryservice.dto.InventoryResponseDto;
import lk.ijse.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping
    public ResponseEntity<InventoryResponseDto> create(@RequestBody InventoryRequestDto dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDto>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto> getByProduct(@PathVariable Long productId){
        return ResponseEntity.ok(service.getByProductId(productId));
    }

    @PutMapping("/{productId}/reduce") public ResponseEntity<String> reduceStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
                service.reduceStock(productId, quantity);
                return ResponseEntity.ok( "Stock reduced successfully" );
    }
}
