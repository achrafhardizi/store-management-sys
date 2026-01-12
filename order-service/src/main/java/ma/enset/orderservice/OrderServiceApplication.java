package ma.enset.orderservice;

import ma.enset.orderservice.entities.Order;
import ma.enset.orderservice.entities.OrderLineItem;
import ma.enset.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(OrderRepository orderRepository){
        return args -> {
            if(orderRepository.count() == 0) {
                // Order 1
                Order order1 = Order.builder()
                        .id(UUID.randomUUID().toString())
                        .customerId("user1") // Matches Keycloak user
                        .date(LocalDate.now())
                        .status("CONFIRMED")
                        .totalAmount(1200)
                        .build();

                OrderLineItem item1 = OrderLineItem.builder()
                        .productId("product-1-id") // Dummy ID
                        .price(1200)
                        .quantity(1)
                        .order(order1)
                        .build();

                order1.setOrderLineItems(List.of(item1));
                orderRepository.save(order1);

                // Order 2
                Order order2 = Order.builder()
                        .id(UUID.randomUUID().toString())
                        .customerId("user1")
                        .date(LocalDate.now().minusDays(1))
                        .status("PENDING")
                        .totalAmount(500)
                        .build();

                OrderLineItem item2 = OrderLineItem.builder()
                        .productId("product-2-id")
                        .price(250)
                        .quantity(2)
                        .order(order2)
                        .build();

                order2.setOrderLineItems(List.of(item2));
                orderRepository.save(order2);
            }
        };
    }

}
