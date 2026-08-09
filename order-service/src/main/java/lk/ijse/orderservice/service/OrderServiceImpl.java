package lk.ijse.orderservice.service;

import lk.ijse.orderservice.dto.OrderItemRequestDto;
import lk.ijse.orderservice.dto.OrderItemResponseDto;
import lk.ijse.orderservice.dto.OrderRequestDto;
import lk.ijse.orderservice.dto.OrderResponseDto;
import lk.ijse.orderservice.entity.Order;
import lk.ijse.orderservice.entity.OrderItem;
import lk.ijse.orderservice.entity.OrderStatus;
import lk.ijse.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    @Override public OrderResponseDto create(OrderRequestDto dto) {
        Order order = Order.builder()
                .userId(dto.getUserId())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        if (order.getItems() == null) {
            order.setItems(new java.util.ArrayList<>());
        }

        for (OrderItemRequestDto itemDto : dto.getItems()) {

            BigDecimal price = BigDecimal.ZERO;
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            OrderItem item = OrderItem.builder()
                    .productId(itemDto.getProductId())
                    .quantity(itemDto.getQuantity())
                    .price(price)
                    .subtotal(subtotal)
                    .order(order)
                    .build();
            order.getItems().add(item);
        }

        BigDecimal total = order.getItems()
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponseDto getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order ID " + id + " does not exist"
                        )
                );
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponseDto> getByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponseDto updateStatus(Long id, String status) {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Order ID " + id + " does not exist"
                            )
                    );
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            return mapToResponse(orderRepository.save(order));
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException(
                    "Order ID " + id + " does not exist"
            );
        }
        orderRepository.deleteById(id);
    }

    private OrderResponseDto mapToResponse(Order order) {
        List<OrderItemResponseDto> items = order.getItems()
                .stream()
                .map(item -> OrderItemResponseDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build()
                )
                .toList();

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
