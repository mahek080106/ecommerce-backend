package ecommerce.task.service;

import ecommerce.task.model.Product;
import ecommerce.task.model.WishlistItem;
import ecommerce.task.repository.ProductRepository;
import ecommerce.task.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getWishlist(String userEmail) {

        return wishlistRepository.findByUserEmail(userEmail)
                .stream()
                .map(item -> productRepository.findById(item.getProductId())
                        .orElse(null))
                .filter(product -> product != null)
                .toList();
    }

    public Product addToWishlist(
            String userEmail,
            String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        if (wishlistRepository
                .findByUserEmailAndProductId(userEmail, productId)
                .isEmpty()) {

            wishlistRepository.save(
                    new WishlistItem(userEmail, productId)
            );
        }

        return product;
    }

    public void removeFromWishlist(
            String userEmail,
            String productId) {

        wishlistRepository.deleteByUserEmailAndProductId(
                userEmail,
                productId
        );
    }
}
