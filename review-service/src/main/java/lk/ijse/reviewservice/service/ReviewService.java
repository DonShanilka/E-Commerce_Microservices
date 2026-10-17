package lk.ijse.reviewservice.service;

import lk.ijse.reviewservice.dto.ReviewRequestDto;
import lk.ijse.reviewservice.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    ReviewResponseDto create(ReviewRequestDto dto);
    ReviewResponseDto getById(Long id);
    List<ReviewResponseDto> getAll();
    List<ReviewResponseDto> getByProductId(Long productId);
    List<ReviewResponseDto> getRecentByProductId(Long productId);
    List<ReviewResponseDto> getByUserId(Long userId);
    ReviewResponseDto update(Long id, ReviewRequestDto dto);
    void delete(Long id);
}