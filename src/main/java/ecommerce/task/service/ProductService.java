package ecommerce.task.service;

import ecommerce.task.model.Product;
import ecommerce.task.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
    }

    public Product updateProduct(
            String id,
            Product updatedProduct) {

        Product existing = getProductById(id);

        existing.setName(updatedProduct.getName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setPrice(updatedProduct.getPrice());
        existing.setOldPrice(updatedProduct.getOldPrice());
        existing.setRating(updatedProduct.getRating());
        existing.setReviews(updatedProduct.getReviews());
        existing.setImage(updatedProduct.getImage());
        existing.setDescription(updatedProduct.getDescription());
        existing.setStock(updatedProduct.getStock());

        return productRepository.save(existing);
    }

    public void deleteProduct(String id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String name) {

        return productRepository
                .findByNameContainingIgnoreCase(name);
    }

    public Page<Product> getProductsWithFilters(
            int page,
            int size,
            String sortBy,
            String direction,
            String category) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        if (category != null && !category.isBlank()) {
            return productRepository
                    .findByCategoryIgnoreCase(
                            category,
                            pageable
                    );
        }

        return productRepository.findAll(pageable);
    }
}
