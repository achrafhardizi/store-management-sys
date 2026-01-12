package ma.enset.productservice;

import ma.enset.productservice.entities.Product;
import ma.enset.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {
            if(productRepository.count() == 0){
                productRepository.save(Product.builder().id(UUID.randomUUID().toString()).name("Laptop").price(12000).quantity(10).build());
                productRepository.save(Product.builder().id(UUID.randomUUID().toString()).name("Smartphone").price(8000).quantity(20).build());
                productRepository.save(Product.builder().id(UUID.randomUUID().toString()).name("Printer").price(3000).quantity(5).build());
            }
        };
    }

}
