package lk.ijse.orderservice.service;

import lk.ijse.orderservice.dto.OrderRequestDto;
import lk.ijse.orderservice.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto create(OrderRequestDto dto);
    List<OrderResponseDto> getAll();
    OrderResponseDto getById(Long id);
    List<OrderResponseDto> getByUserId(Long userId);
    List<OrderResponseDto> getRecentByUserId(Long userId);
    OrderResponseDto updateStatus(Long id, String status);
    void delete(Long id);
}