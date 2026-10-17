package lk.ijse.reviewservice.controller;

import jakarta.validation.Valid;
import lk.ijse.reviewservice.dto.ReviewRequestDto;
import lk.ijse.reviewservice.dto.ReviewResponseDto;
import lk.ijse.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    public record AiReview(Integer rating, String comment, java.time.LocalDateTime createdAt) { }
    private final ReviewService reviewService;
    @Value("${ai.internal-api-key:}")
    private String internalApiKey;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto create(@Valid @RequestBody ReviewRequestDto request) {
        return reviewService.create(request);
    }

    @GetMapping("/internal/ai/product/{productId}")
    public List<AiReview> getRecentReviewsForAi(@PathVariable Long productId,
            @RequestHeader(value = "X-AI-Internal-Key", required = false) String suppliedKey) {
        requireInternalKey(suppliedKey);
        return reviewService.getRecentByProductId(productId).stream()
                .map(review -> new AiReview(review.getRating(), review.getComment(), review.getCreatedAt())).toList();
    }

    private void requireInternalKey(String suppliedKey) {
        if (internalApiKey == null || internalApiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Internal AI data access is not configured.");
        }
        if (suppliedKey == null || !MessageDigest.isEqual(internalApiKey.getBytes(StandardCharsets.UTF_8), suppliedKey.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid service credential.");
        }
    }

    @GetMapping("/{id}")
    public ReviewResponseDto getById(@PathVariable Long id) {
        return reviewService.getById(id);
    }

    @GetMapping
    public List<ReviewResponseDto> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/product/{productId}")
    public List<ReviewResponseDto> getByProductId(@PathVariable Long productId) {
        return reviewService.getByProductId(productId);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponseDto> getByUserId(@PathVariable Long userId) {
        return reviewService.getByUserId(userId);
    }

    @PutMapping("/{id}")
    public ReviewResponseDto update(@PathVariable Long id,
                                    @Valid @RequestBody ReviewRequestDto request) {
        return reviewService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
