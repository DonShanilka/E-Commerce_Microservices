package lk.ijse.productservice.dto;

public record ProductSummaryDto(Long id, String name, String description, Double price,
                                Integer quantity, String category, String brand) { }
