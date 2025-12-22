package com.reviews.movie_review.service.request;

import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.domain.Review;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @NotBlank(message = "Review text is required")
    @Size(min = 10, max = 500, message = "Review must be between 10 and 500 characters")
    private String movieReview;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating must be at most 10.0")
    private Double rating;

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    public Review toReview(){
        return Review.builder()
                .movieReview(movieReview)
                .rating(rating)
                .movie(Movie.builder()
                        .id(movieId)
                        .build())
                .build();
    }
}