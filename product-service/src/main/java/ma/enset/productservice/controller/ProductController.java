package ma.enset.productservice.controller;

import ma.enset.productservice.entities.Product;
import ma.enset.productservice.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Product getProductById(@PathVariable String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product saveProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        if(product.getName() != null) p.setName(product.getName());
        if(product.getPrice() != 0) p.setPrice(product.getPrice());
        if(product.getQuantity() != 0) p.setQuantity(product.getQuantity());
        return productRepository.save(p);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id);
    }
}