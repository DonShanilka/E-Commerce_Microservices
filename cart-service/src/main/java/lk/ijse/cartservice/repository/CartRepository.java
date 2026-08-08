package lk.ijse.cartservice.repository;

import lk.ijse.cartservice.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final RedisTemplate<String, Cart> redisTemplate;

    private static final String CART_PREFIX = "cart:";

    public void save(Cart cart) {
        String key = CART_PREFIX + cart.getUserId();
        redisTemplate.opsForValue().set(key, cart);
    }

    public Cart findByUserId(Long userId) {
        String key = CART_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteByUserId(Long userId) {
        String key = CART_PREFIX + userId;
        redisTemplate.delete(key);
    }
}