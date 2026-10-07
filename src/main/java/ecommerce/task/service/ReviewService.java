package ecommerce.task.service;

import ecommerce.task.dto.ReviewRequest;
import ecommerce.task.model.Product;
import ecommerce.task.model.Review;
import ecommerce.task.repository.ProductRepository;
import ecommerce.task.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ProductRepository productRepository) {

        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public List<Review> getReviews(String productId) {

        ensureProductExists(productId);
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public Review addReview(
            String productId,
            String userEmail,
            ReviewRequest request) {

        ensureProductExists(productId);

        if (reviewRepository
                .findByProductIdAndUserEmail(productId, userEmail)
                .isPresent()) {
            throw new RuntimeException(
                    "You have already reviewed this product"
            );
        }

        Review review = new Review(
                productId,
                userEmail,
                request.getRating(),
                request.getComment().trim(),
                LocalDateTime.now()
        );

        return reviewRepository.save(review);
    }

    public Review updateReview(
            String reviewId,
            String userEmail,
            ReviewRequest request) {

        Review review = getOwnedReview(reviewId, userEmail);

        review.setRating(request.getRating());
        review.setComment(request.getComment().trim());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public void deleteReview(
            String reviewId,
            String userEmail) {

        Review review = getOwnedReview(reviewId, userEmail);
        reviewRepository.delete(review);
    }

    private Review getOwnedReview(
            String reviewId,
            String userEmail) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if (!review.getUserEmail().equals(userEmail)) {
            throw new RuntimeException(
                    "You are not allowed to modify this review"
            );
        }

        return review;
    }

    private void ensureProductExists(String productId) {
        productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
    }
}
