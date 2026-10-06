package ecommerce.task.controller;

import ecommerce.task.model.Cart;
import ecommerce.task.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                cartService.getCart(userEmail)
        );
    }

    @PostMapping
    public ResponseEntity<Cart> addToCart(
            @RequestParam String productId,
            @RequestParam int quantity,
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                cartService.addToCart(
                        userEmail,
                        productId,
                        quantity
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(
            Authentication authentication) {

        String userEmail = authentication.getName();

        cartService.clearCart(userEmail);

        return ResponseEntity.ok(
                "Cart cleared successfully"
        );
    }
}