package lk.ijse.orderservice.messaging;

import lk.ijse.orderservice.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderCreated(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, event);
    }
}