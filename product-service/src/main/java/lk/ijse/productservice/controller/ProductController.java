package lk.ijse.productservice.controller;

import lk.ijse.productservice.dto.ProductRequestDto;
import lk.ijse.productservice.dto.ProductResponseDto;
import lk.ijse.productservice.service.ProductService;
import lk.ijse.productservice.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createProduct(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Double price,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {

            String imageBase64 = null;
            if (image != null && !image.isEmpty()) {
                imageBase64 = AppUtil.imageBase64(image.getBytes());
            }

            ProductRequestDto dto = ProductRequestDto.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .quantity(quantity)
                    .category(category)
                    .brand(brand)
                    .image(imageBase64)
                    .build();

            ProductResponseDto response = productService.create(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(
            @RequestParam("id") Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {

            String imageBase64 = null;
            if (image != null && !image.isEmpty()) {
                imageBase64 = AppUtil.imageBase64(image.getBytes());
            }

            ProductRequestDto dto = ProductRequestDto.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .price(price)
                    .quantity(quantity)
                    .category(category)
                    .brand(brand)
                    .image(imageBase64)
                    .build();

            return ResponseEntity.ok(productService.update(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

}
