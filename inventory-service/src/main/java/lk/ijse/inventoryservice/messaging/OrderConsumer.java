package lk.ijse.inventoryservice.messaging;

import lk.ijse.inventoryservice.dto.OrderCreatedEvent;
import lk.ijse.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final InventoryService service;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void consume(OrderCreatedEvent event){
        service.reduceStock(
                event.getProductId(),
                event.getQuantity()
        );
    }


}