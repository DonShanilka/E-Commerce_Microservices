package lk.ijse.orderservice.controller;

import jakarta.validation.Valid;
import lk.ijse.orderservice.dto.OrderRequestDto;
import lk.ijse.orderservice.dto.OrderResponseDto;
import lk.ijse.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto>create(@Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto response = orderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getByUserId(userId));
    }

}
