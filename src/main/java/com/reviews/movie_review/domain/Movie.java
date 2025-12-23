package com.reviews.movie_review.domain;

import com.reviews.movie_review.service.response.MovieResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="movie_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Movie implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Genre is required")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Double rating;// a single entity which is average rating of all reviews for a movie

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public MovieResponse toMovieResponse(){
        List<Review> reviewsList = getReviews();
        return MovieResponse.builder()
                .movie_id(getId())
                .genre(getGenre())
                .title(getTitle())
                .avg_rating(getRating() != null ? getRating() : 0.0)
                .reviews(reviewsList != null ? Review.toReviewResponse(reviewsList) : new ArrayList<>())
                .build();
    }
}
