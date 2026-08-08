package lk.ijse.inventoryservice.messaging;

import lk.ijse.inventoryservice.dto.ProductCreatedEvent;
import lk.ijse.inventoryservice.dto.InventoryRequestDto;
import lk.ijse.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConsumer {

    private final InventoryService service;

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_QUEUE)
    public void consume(ProductCreatedEvent event) {

        System.out.println(
                "Received ProductCreatedEvent: productId="
                        + event.getProductId()
                        + ", quantity="
                        + event.getQuantity()
        );

        service.create(
                InventoryRequestDto.builder()
                        .productId(event.getProductId())
                        .quantity(event.getQuantity())
                        .availableQuantity(event.getQuantity())
                        .reservedQuantity(0)
                        .build()
        );
    }
}