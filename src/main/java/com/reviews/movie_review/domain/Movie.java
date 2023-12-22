package com.reviews.movie_review.domain;

import com.reviews.movie_review.service.response.MovieResponse;
import lombok.*;


import javax.persistence.*;
//import jakarta.persistence.*;
import java.io.Serializable;
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

    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Double rating;// a single entity which is average rating of all reviews for a movie

    @OneToMany(mappedBy="movie")
    private List<Review> reviews;

    public  MovieResponse toMovieResponse(){
        return MovieResponse.builder()
                .movie_id(getId())
                .genre(getGenre())
                .title(getTitle())
                .avg_rating(getRating())
                .reviews(Review.toReviewResponse(getReviews())).build();
    }



}
