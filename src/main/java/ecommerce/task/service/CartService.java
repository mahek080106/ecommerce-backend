package ecommerce.task.service;

import ecommerce.task.model.Cart;
import ecommerce.task.model.CartItem;
import ecommerce.task.model.Product;
import ecommerce.task.repository.CartRepository;
import ecommerce.task.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getCart(String email) {

        return cartRepository
                .findByUserEmail(email)
                .orElseGet(() ->
                        cartRepository.save(
                                new Cart(email)
                        ));
    }

    public Cart addToCart(
            String email,
            String productId,
            int quantity) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        if (quantity > product.getStock()) {
            throw new RuntimeException(
                    "Not enough stock"
            );
        }

        Cart cart = getCart(email);

        for (CartItem item : cart.getItems()) {

            if (item.getProductId().equals(productId)) {

                int newQuantity =
                        item.getQuantity() + quantity;

                if (newQuantity > product.getStock()) {
                    throw new RuntimeException(
                            "Not enough stock"
                    );
                }

                item.setQuantity(newQuantity);

                return cartRepository.save(cart);
            }
        }

        cart.getItems().add(
                new CartItem(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        quantity
                )
        );

        return cartRepository.save(cart);
    }

    public Cart updateQuantity(
            String email,
            String productId,
            int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));

        if (quantity > product.getStock()) {
            throw new RuntimeException(
                    "Not enough stock"
            );
        }

        Cart cart = getCart(email);

        for (CartItem item : cart.getItems()) {

            if (item.getProductId().equals(productId)) {

                item.setQuantity(quantity);

                return cartRepository.save(cart);
            }
        }

        throw new RuntimeException(
                "Product is not present in cart"
        );
    }

    public Cart removeFromCart(
            String email,
            String productId) {

        Cart cart = getCart(email);

        boolean removed =
                cart.getItems().removeIf(
                        item ->
                                item.getProductId()
                                        .equals(productId)
                );

        if (!removed) {
            throw new RuntimeException(
                    "Product is not present in cart"
            );
        }

        return cartRepository.save(cart);
    }

    public Cart clearCart(String email) {

        Cart cart = getCart(email);

        cart.getItems().clear();

        return cartRepository.save(cart);
    }
}