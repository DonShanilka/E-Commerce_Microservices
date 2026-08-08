package lk.ijse.cartservice.service;

import lk.ijse.cartservice.dto.CartRequestDto;
import lk.ijse.cartservice.dto.CartResponseDto;

public interface CartService {

    CartResponseDto addToCart(CartRequestDto dto);
    CartResponseDto getCart(Long userId);
    CartResponseDto updateQuantity(Long userId, Long productId, Integer quantity);
    void removeFromCart(Long userId, Long productId);
    void clearCart(Long userId);

}