package lk.ijse.productservice.service;

import lk.ijse.productservice.dto.ProductRequestDto;
import lk.ijse.productservice.dto.ProductResponseDto;
import lk.ijse.productservice.entity.Product;
import lk.ijse.productservice.exception.DataPersistException;
import lk.ijse.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    @Autowired
    private final ProductRepository productRepository;

    @Override
    public ProductResponseDto create(ProductRequestDto dto) {
        if (productRepository.existsByName(dto.getName())) {
            throw new DataPersistException("Product already exists : " + dto.getName());
        }

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(dto.getCategory())
                .brand(dto.getBrand())
                .image(dto.getImage())
                .build();

        Product saveProduct = productRepository.save(product);

        return ProductResponseDto.builder()
                .id(saveProduct.getId())
                .name(saveProduct.getName())
                .description(saveProduct.getDescription())
                .price(saveProduct.getPrice())
                .quantity(saveProduct.getQuantity())
                .category(saveProduct.getCategory())
                .brand(saveProduct.getBrand())
                .image(saveProduct.getImage())
                .build();
    }


    @Override
    public ProductResponseDto update(ProductRequestDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ProductResponseDto getById(Long id) {
        return null;
    }

    @Override
    public List<ProductResponseDto> getAll() {
        return List.of();
    }
}
