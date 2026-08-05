package lk.ijse.productservice.service;

import lk.ijse.productservice.dto.ProductRequestDto;
import lk.ijse.productservice.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto update(ProductRequestDto dto);
    void delete(Long id);
    ProductResponseDto getById(Long id);
    List<ProductResponseDto> getAll();

}
