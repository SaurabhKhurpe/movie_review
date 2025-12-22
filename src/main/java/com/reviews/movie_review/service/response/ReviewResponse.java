package com.reviews.movie_review.service.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private Long review_id;
    private String review;
    private Double rating;
}
