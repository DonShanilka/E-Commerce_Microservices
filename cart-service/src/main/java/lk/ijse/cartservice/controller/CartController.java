package lk.ijse.cartservice.controller;

import jakarta.validation.Valid;
import lk.ijse.cartservice.dto.CartRequestDto;
import lk.ijse.cartservice.dto.CartResponseDto;
import lk.ijse.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDto> addToCart(@Valid @RequestBody CartRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addToCart(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(
                cartService.getCart(userId)
        );
    }

    @PutMapping("/{userId}/{productId}")
    public ResponseEntity<CartResponseDto> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        userId,
                        productId,
                        quantity
                )
        );
    }

}
