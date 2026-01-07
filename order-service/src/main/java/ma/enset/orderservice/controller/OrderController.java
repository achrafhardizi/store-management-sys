package ma.enset.orderservice.controller;

import ma.enset.orderservice.client.ProductRestClient;
import ma.enset.orderservice.entities.Order;
import ma.enset.orderservice.entities.OrderLineItem;
import ma.enset.orderservice.model.OrderRequest;
import ma.enset.orderservice.model.Product;
import ma.enset.orderservice.repository.OrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRestClient productRestClient;

    public OrderController(OrderRepository orderRepository, ProductRestClient productRestClient) {
        this.orderRepository = orderRepository;
        this.productRestClient = productRestClient;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public List<Order> getOrders(Authentication authentication) {
        // In a real app, filter by authentication.getName() (customerId)
        // For now, returning all for demo or filter if filtering logic existed in Repo
        return orderRepository.findAll().stream()
                .filter(o -> o.getCustomerId().equals(authentication.getName()))
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public Order getOrder(@PathVariable String id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public Order createOrder(@RequestBody OrderRequest orderRequest, Authentication authentication) {
        Product product = productRestClient.getProductById(orderRequest.getProductId());
        if (product.getQuantity() < orderRequest.getQuantity()) {
            throw new RuntimeException("Product not available in sufficient quantity");
        }

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .customerId(authentication.getName())
                .date(LocalDate.now())
                .status("CREATED")
                .totalAmount(product.getPrice() * orderRequest.getQuantity())
                .build();

        OrderLineItem orderLineItem = OrderLineItem.builder()
                .productId(product.getId())
                .price(product.getPrice())
                .quantity(orderRequest.getQuantity())
                .order(order)
                .build();

        order.setOrderLineItems(List.of(orderLineItem));

        return orderRepository.save(order);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Order> getAllOrdersAdmin() {
        return orderRepository.findAll();
    }
}