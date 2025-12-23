package com.reviews.movie_review.domain;

import com.reviews.movie_review.service.response.MovieResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void toMovieResponse_WithReviews() {
        Review r1 = Review.builder().id(1L).movieReview("R1").rating(8.0).build();
        Review r2 = Review.builder().id(2L).movieReview("R2").rating(9.0).build();

        Movie movie = Movie.builder()
                .id(10L)
                .title("Test Movie")
                .genre(Genre.ACTION)
                .rating(8.5)
                .reviews(Arrays.asList(r1, r2))
                .build();

        MovieResponse response = movie.toMovieResponse();

        assertEquals(10L, response.getMovie_id());
        assertEquals("Test Movie", response.getTitle());
        assertEquals(Genre.ACTION, response.getGenre());
        assertEquals(8.5, response.getAvg_rating());
        assertEquals(2, response.getReviews().size());
    }

    @Test
    void toMovieResponse_NullReviewsAndRating() {
        Movie movie = Movie.builder()
                .id(1L)
                .title("Movie")
                .genre(Genre.COMEDY)
                .rating(null)
                .reviews(null)
                .build();

        MovieResponse response = movie.toMovieResponse();

        assertNotNull(response.getReviews());
        assertTrue(response.getReviews().isEmpty());
        assertEquals(0.0, response.getAvg_rating());
    }
}