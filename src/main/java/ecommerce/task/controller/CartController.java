package ecommerce.task.controller;

import ecommerce.task.dto.UpdateCartItemRequest;
import ecommerce.task.model.Cart;
import ecommerce.task.service.CartService;
import jakarta.validation.Valid;
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

        return ResponseEntity.ok(
                cartService.getCart(
                        authentication.getName()
                )
        );
    }

    @PostMapping
    public ResponseEntity<Cart> addToCart(
            @RequestParam String productId,
            @RequestParam int quantity,
            Authentication authentication) {

        return ResponseEntity.ok(
                cartService.addToCart(
                        authentication.getName(),
                        productId,
                        quantity
                )
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable String productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        authentication.getName(),
                        productId,
                        request.getQuantity()
                )
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable String productId,
            Authentication authentication) {

        return ResponseEntity.ok(
                cartService.removeFromCart(
                        authentication.getName(),
                        productId
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(
            Authentication authentication) {

        cartService.clearCart(
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Cart cleared successfully"
        );
    }
}