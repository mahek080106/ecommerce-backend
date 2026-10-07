package ecommerce.task.controller;

import ecommerce.task.model.Product;
import ecommerce.task.service.WishlistService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<Product> getMyWishlist(Authentication authentication) {
        return wishlistService.getWishlist(authentication.getName());
    }

    @PostMapping("/{productId}")
    public Product addToWishlist(
            @PathVariable String productId,
            Authentication authentication) {

        return wishlistService.addToWishlist(
                authentication.getName(),
                productId
        );
    }

    @DeleteMapping("/{productId}")
    public void removeFromWishlist(
            @PathVariable String productId,
            Authentication authentication) {

        wishlistService.removeFromWishlist(
                authentication.getName(),
                productId
        );
    }
}
