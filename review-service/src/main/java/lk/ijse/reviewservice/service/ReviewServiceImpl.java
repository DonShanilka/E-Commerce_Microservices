package lk.ijse.reviewservice.service;

import lk.ijse.reviewservice.dto.ReviewRequestDto;
import lk.ijse.reviewservice.dto.ReviewResponseDto;
import lk.ijse.reviewservice.entity.Review;
import lk.ijse.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponseDto create(ReviewRequestDto dto) {
        if (reviewRepository.existsByUserIdAndProductId(dto.getUserId(), dto.getProductId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This user has already reviewed this product");
        }
        LocalDateTime now = LocalDateTime.now();
        Review review = Review.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .rating(dto.getRating())
                .comment(dto.getComment().trim())
                .createdAt(now)
                .updatedAt(now)
                .build();
        return toDto(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getById(Long id) {
        return toDto(findReview(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAll() {
        return reviewRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public ReviewResponseDto update(Long id, ReviewRequestDto dto) {
        Review review = findReview(id);
        // Keep the original user/product identity stable; only rating/comment are editable here.
        if (!review.getUserId().equals(dto.getUserId()) || !review.getProductId().equals(dto.getProductId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User ID and product ID cannot be changed when updating a review");
        }
        review.setRating(dto.getRating());
        review.setComment(dto.getComment().trim());
        review.setUpdatedAt(LocalDateTime.now());
        return toDto(reviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        reviewRepository.delete(findReview(id));
    }

    private Review findReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found: " + id));
    }

    private ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .productId(review.getProductId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
