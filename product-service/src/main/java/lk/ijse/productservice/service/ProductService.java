package lk.ijse.productservice.service;

import lk.ijse.productservice.dto.ProductRequestDto;
import lk.ijse.productservice.dto.ProductResponseDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lk.ijse.productservice.dto.ProductSummaryDto;

public interface ProductService {

    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto update(ProductRequestDto dto);
    void delete(Long id);
    ProductResponseDto getById(Long id);
    List<ProductResponseDto> getAll();
    Page<ProductSummaryDto> search(String query, String category, String brand, Pageable pageable);

}
