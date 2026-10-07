package ecommerce.task.repository;

import ecommerce.task.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends MongoRepository<Review, String> {

    List<Review> findByProductIdOrderByCreatedAtDesc(String productId);

    Optional<Review> findByProductIdAndUserEmail(
            String productId,
            String userEmail
    );
}
