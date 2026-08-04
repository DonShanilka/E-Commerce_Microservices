package lk.ijse.ecommerce_microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ECommerceMicroservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceMicroservicesApplication.class, args);
    }

}