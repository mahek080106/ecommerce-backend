package ecommerce.task.controller;

import ecommerce.task.dto.ReviewRequest;
import ecommerce.task.model.Review;
import ecommerce.task.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/products/{productId}/reviews")
    public List<Review> getReviews(
            @PathVariable String productId) {

        return reviewService.getReviews(productId);
    }

    @PostMapping("/products/{productId}/reviews")
    public Review addReview(
            @PathVariable String productId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {

        return reviewService.addReview(
                productId,
                authentication.getName(),
                request
        );
    }
    @PostMapping("/reviews/{productId}")
public Review addReviewAlternative(
        @PathVariable String productId,
        @Valid @RequestBody ReviewRequest request,
        Authentication authentication) {

    return reviewService.addReview(
            productId,
            authentication.getName(),
            request
    );
}

    @PutMapping("/reviews/{reviewId}")
    public Review updateReview(
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {

        return reviewService.updateReview(
                reviewId,
                authentication.getName(),
                request
        );
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(
            @PathVariable String reviewId,
            Authentication authentication) {

        reviewService.deleteReview(
                reviewId,
                authentication.getName()
        );
    }
}
