package com.reviews.movie_review.controller;

import com.reviews.movie_review.service.ReviewService;
import com.reviews.movie_review.service.request.ReviewRequest;
import com.reviews.movie_review.service.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest){
        reviewService.addReview(reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Review added successfully");
    }

    @PostMapping("/add/bulk")
    public ResponseEntity<String> addReviewsBulk(@RequestBody List<ReviewRequest> reviewRequests){
        reviewService.addReviewsBulk(reviewRequests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewRequests.size() + " reviews added successfully");
    }

    @GetMapping
    public ResponseEntity<ReviewResponse> getReview(@RequestParam("Id") Long reviewId){
        ReviewResponse reviewResponse = reviewService.getReviewById(reviewId);
        if(reviewResponse != null) {
            return ResponseEntity.ok(reviewResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
