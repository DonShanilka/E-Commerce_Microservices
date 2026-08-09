package lk.ijse.cartservice.service;

import lk.ijse.cartservice.dto.CartRequestDto;
import lk.ijse.cartservice.dto.CartResponseDto;


import lk.ijse.cartservice.dto.CartItemDto;
import lk.ijse.cartservice.dto.CartRequestDto;
import lk.ijse.cartservice.dto.CartResponseDto;
import lk.ijse.cartservice.entity.Cart;
import lk.ijse.cartservice.entity.CartItem;
import lk.ijse.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    @Override
    public CartResponseDto addToCart(CartRequestDto dto) {
        Cart cart = cartRepository.findByUserId(dto.getUserId());

        if (cart == null) {
            cart = Cart.builder()
                    .userId(dto.getUserId())
                    .items(new ArrayList<>())
                    .total(0.0)
                    .build();
        }

        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item ->
                        item.getProductId().equals(dto.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(
                    existingItem.getQuantity() + dto.getQuantity()
            );
            existingItem.setSubtotal(
                    existingItem.getPrice()
                            * existingItem.getQuantity()
            );
        } else {
            CartItem item = CartItem.builder()
                    .productId(dto.getProductId())
                    .productName(dto.getProductName())
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .subtotal(dto.getPrice() * dto.getQuantity())
                    .build();

            cart.getItems().add(item);
        }

        calculateTotal(cart);
        cartRepository.save(cart);

        return convertToResponse(cart);
    }

    @Override
    public CartResponseDto getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            return CartResponseDto.builder()
                    .userId(userId)
                    .items(new ArrayList<>())
                    .total(0.0)
                    .build();
        }

        return convertToResponse(cart);
    }

    @Override
    public CartResponseDto updateQuantity(Long userId, Long productId, Integer quantity) {

            Cart cart = cartRepository.findByUserId(userId);
            if (cart == null) {
                throw new RuntimeException("Cart not found");
            }

            CartItem item = cart.getItems()
                    .stream()
                    .filter(i -> i.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException("Product not found in cart"));

            item.setQuantity(quantity);
            item.setSubtotal(item.getPrice() * quantity);

            calculateTotal(cart);
            cartRepository.save(cart);

            return convertToResponse(cart);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {

    }

    @Override
    public void clearCart(Long userId) {

    }

    private void calculateTotal(Cart cart) {
        double total = cart.getItems()
                .stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        cart.setTotal(total);
    }

    private CartResponseDto convertToResponse(Cart cart) {

        List<CartItemDto> items = cart.getItems()
                .stream()
                .map(item -> CartItemDto.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        return CartResponseDto.builder()
                .userId(cart.getUserId())
                .items(items)
                .total(cart.getTotal())
                .build();
    }

}
