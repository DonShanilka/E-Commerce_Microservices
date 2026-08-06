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
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new DataPersistException("Product not found : " + dto.getId()));

        if (dto.getName() != null)
            product.setName(dto.getName());

        if (dto.getDescription() != null)
            product.setDescription(dto.getDescription());

        if (dto.getPrice() != null)
            product.setPrice(dto.getPrice());

        if (dto.getQuantity() != null)
            product.setQuantity(dto.getQuantity());

        if (dto.getCategory() != null)
            product.setCategory(dto.getCategory());

        if (dto.getBrand() != null)
            product.setBrand(dto.getBrand());

        if (dto.getImage() != null)
            product.setImage(dto.getImage());

        Product updateProduct = productRepository.save(product);

        return ProductResponseDto.builder()
                .id(updateProduct.getId())
                .name(updateProduct.getName())
                .description(updateProduct.getDescription())
                .price(updateProduct.getPrice())
                .quantity(updateProduct.getQuantity())
                .category(updateProduct.getCategory())
                .brand(updateProduct.getBrand())
                .image(updateProduct.getImage())
                .build();
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new DataPersistException("Product not found : " + id));

        productRepository.delete(product);
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
