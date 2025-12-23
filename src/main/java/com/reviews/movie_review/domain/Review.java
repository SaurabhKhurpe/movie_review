package com.reviews.movie_review.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reviews.movie_review.service.response.ReviewResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name="review_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Review {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    private String movieReview;

    private double rating;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    @JsonIgnore
    private Movie movie;

    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;

    public static ReviewResponse toReviewResponse(Review review){
        return ReviewResponse.builder()
                .review_id(review.getId())
                .review(review.getMovieReview())
                .rating(review.getRating())
                .build();
    }

    public static List<ReviewResponse> toReviewResponse(List<Review> reviewList){
        if(Objects.isNull(reviewList))
            return new ArrayList<>();
        else
            return reviewList.stream()
                    .filter(Objects::nonNull)  // ADD THIS FILTER
                    .map(Review::toReviewResponse)
                    .collect(Collectors.toList());
    }
}