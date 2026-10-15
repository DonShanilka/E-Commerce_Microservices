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

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto create(@Valid @RequestBody ReviewRequestDto request) {
        return reviewService.create(request);
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
