package lk.ijse.inventoryservice.controller;

import lk.ijse.inventoryservice.dto.OrderCreatedEvent;
import lk.ijse.inventoryservice.dto.ProductCreatedEvent;
import lk.ijse.inventoryservice.messaging.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class RabbitTestController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/product")
    public String sendProduct(@RequestBody ProductCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_QUEUE,
                event
        );

        return "Product Event Sent";
    }

    @PostMapping("/order")
    public String sendOrder(@RequestBody OrderCreatedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_QUEUE,
                event
        );

        return "Order Event Sent";
    }
}