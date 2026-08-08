package lk.ijse.inventoryservice.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_QUEUE = "product.created.queue";
    public static final String ORDER_QUEUE = "order.created.queue";


    @Bean
    public Queue productQueue() {
        return new Queue(PRODUCT_QUEUE, true);
    }


    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }


    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}