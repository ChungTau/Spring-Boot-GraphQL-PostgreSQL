package com.chungtau.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.model.product.Product;
import com.chungtau.demo.model.review.CreateReviewInput;
import com.chungtau.demo.model.review.DeleteReviewInput;
import com.chungtau.demo.model.review.Review;
import com.chungtau.demo.model.review.UpdateReviewInput;
import com.chungtau.demo.model.user.User;
import com.chungtau.demo.repository.ProductRepository;
import com.chungtau.demo.repository.ReviewRepository;
import com.chungtau.demo.repository.UserRepository;

@Controller
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @QueryMapping
    public Optional<Review> getReviewById(@Argument Long id) {
        return reviewRepository.findById(id);
    }

    @QueryMapping
    public Iterable<Review> getReviews() {
        return reviewRepository.findAll();
    }

    @SchemaMapping(typeName = "Review", field = "user")
    public User getUser(Review review) {
        Long userId = review.getUser().getId();
        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.orElse(null);
        } else {
            return null;
        }
    }

    @SchemaMapping(typeName = "Review", field = "product")
    public Product getProduct(Review review) {
        Long productId = review.getProduct().getId();
        if (productId != null) {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            return optionalProduct.orElse(null);
        } else {
            return null;
        }
    }

    @MutationMapping
    public Review addReview(@Argument CreateReviewInput input) {
        Review review = new Review();
        
        review.setComment(input.getComment());
        review.setRating(input.getRating());

        Optional<User> optionalUser = userRepository.findById(input.getUserId());
        if (optionalUser.isPresent()) {
            review.setUser(optionalUser.get());
        } else {
            throw new IllegalArgumentException("User not found for id: " + input.getUserId());
        }

        Optional<Product> optionalProduct = productRepository.findById(input.getProductId());

        if (optionalProduct.isPresent()) {
            review.setProduct(optionalProduct.get());
            return reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Product not found for id: " + input.getProductId());
        }
    }

    @MutationMapping
    public Review updateReview(@Argument UpdateReviewInput input) {
        Optional<Review> optionalReview = reviewRepository.findById(input.getId());
        
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setComment(input.getComment());
            review.setRating(input.getRating());
            return reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Review not found for id: " + input.getId());
        }
    }

    @MutationMapping
    public boolean deleteReview(@Argument DeleteReviewInput input) {
        if (reviewRepository.existsById(input.getId())) {
            reviewRepository.deleteById(input.getId());
            return true;
        } else {
            throw new IllegalArgumentException("Review not found for id: " + input.getId());
        }
    }


}
