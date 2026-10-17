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
import java.time.LocalDate;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import lk.ijse.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    public record AiOrderItem(Long productId, String productName) { }

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    @Value("${ai.internal-api-key:}")
    private String internalApiKey;


    @GetMapping("/internal/ai/users/{userId}/orders")
    public ResponseEntity<List<AiOrderItem>> getOrdersForAi(@PathVariable Long userId,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey) {
        requireInternalKey(suppliedKey);
        List<AiOrderItem> items = orderService.getRecentByUserId(userId).stream()
                .filter(order -> order.getItems() != null)
                .flatMap(order -> order.getItems().stream())
                .map(item -> new AiOrderItem(item.getProductId(), item.getProductName()))
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/internal/ai/products/{productId}/daily-sales")
    public ResponseEntity<List<Map<String, Object>>> getDailySalesForAi(@PathVariable Long productId,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey) {
        requireInternalKey(suppliedKey);
        LocalDate fromDate = LocalDate.now().minusDays(179);
        List<Map<String, Object>> result = orderRepository.findDailySalesForProduct(productId, fromDate).stream()
                .map(row -> Map.<String, Object>of("date", row[0].toString(), "unitsSold", ((Number) row[1]).doubleValue()))
                .toList();
        return ResponseEntity.ok(result);
    }

    private void requireInternalKey(String suppliedKey) {
        if (internalApiKey == null || internalApiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Internal AI data access is not configured.");
        }
        if (suppliedKey == null || !MessageDigest.isEqual(internalApiKey.getBytes(StandardCharsets.UTF_8), suppliedKey.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid service credential.");
        }
    }

    private void requireUserAssertion(Long userId, String authenticatedUser, String expiry, String signature) {
        try {
            long expires = Long.parseLong(expiry);
            long now = System.currentTimeMillis() / 1000;
            if (userId == null || !userId.toString().equals(authenticatedUser) || expires < now || expires > now + 300) throw new IllegalArgumentException();
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec(internalApiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] expected = mac.doFinal((authenticatedUser + "\n" + expires).getBytes(StandardCharsets.UTF_8));
            if (!MessageDigest.isEqual(expected, java.util.Base64.getUrlDecoder().decode(signature))) throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A valid user authorization assertion is required.");
        }
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto>create(@Valid @RequestBody OrderRequestDto dto,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey,
            @RequestHeader(value = "X-Authenticated-User", required = false) String authenticatedUser,
            @RequestHeader(value = "X-User-Assertion-Expiry", required = false) String assertionExpiry,
            @RequestHeader(value = "X-User-Assertion", required = false) String assertion) {
        requireInternalKey(suppliedKey);
        requireUserAssertion(dto.getUserId(), authenticatedUser, assertionExpiry, assertion);
        OrderResponseDto response = orderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll(@RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey) {
        requireInternalKey(suppliedKey);
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long id,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey,
            @RequestHeader(value = "X-Authenticated-User", required = false) String authenticatedUser,
            @RequestHeader(value = "X-User-Assertion-Expiry", required = false) String assertionExpiry,
            @RequestHeader(value = "X-User-Assertion", required = false) String assertion) {
        requireInternalKey(suppliedKey);
        OrderResponseDto order = orderService.getById(id);
        requireUserAssertion(order.getUserId(), authenticatedUser, assertionExpiry, assertion);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getByUserId(@PathVariable Long userId,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey,
            @RequestHeader(value = "X-Authenticated-User", required = false) String authenticatedUser,
            @RequestHeader(value = "X-User-Assertion-Expiry", required = false) String assertionExpiry,
            @RequestHeader(value = "X-User-Assertion", required = false) String assertion) {
        requireInternalKey(suppliedKey);
        requireUserAssertion(userId, authenticatedUser, assertionExpiry, assertion);
        return ResponseEntity.ok(orderService.getByUserId(userId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateStatus(@PathVariable Long id, @RequestParam String status,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey) {
        requireInternalKey(suppliedKey);
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey) {
        requireInternalKey(suppliedKey);
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
