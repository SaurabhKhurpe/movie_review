package com.reviews.movie_review.repository;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie;
    private Review testReview;

    @BeforeEach
    void setUp() {
        testMovie = Movie.builder()
                .title("Test Movie")
                .genre(Genre.ACTION)
                .rating(0.0)
                .build();

        testMovie = movieRepository.save(testMovie);

        testReview = Review.builder()
                .movieReview("Great movie!")
                .rating(9.0)
                .movie(testMovie)
                .build();
    }

    @Test
    void testSaveReview() {
        Review savedReview = reviewRepository.save(testReview);

        assertNotNull(savedReview.getId());
        assertEquals("Great movie!", savedReview.getMovieReview());
        assertEquals(9.0, savedReview.getRating());
        assertEquals(testMovie.getId(), savedReview.getMovie().getId());
    }

    @Test
    void testGetReviewAverage() {
        Review review1 = Review.builder().movieReview("Good").rating(8.0).movie(testMovie).build();
        Review review2 = Review.builder().movieReview("Excellent").rating(9.0).movie(testMovie).build();
        Review review3 = Review.builder().movieReview("Average").rating(7.0).movie(testMovie).build();

        reviewRepository.saveAll(List.of(review1, review2, review3));

        Double average = reviewRepository.getReviewAverage(testMovie.getId());

        assertNotNull(average);
        assertEquals(8.0, average, 0.01); // (8+9+7)/3 = 8.0
    }

    @Test
    void testGetReviewAverage_NoReviews() {
        Double average = reviewRepository.getReviewAverage(testMovie.getId());

        assertNull(average);
    }

    @Test
    void testFindAllReviews() {
        Review review1 = Review.builder().movieReview("Review 1").rating(8.0).movie(testMovie).build();
        Review review2 = Review.builder().movieReview("Review 2").rating(9.0).movie(testMovie).build();

        reviewRepository.saveAll(List.of(review1, review2));
        List<Review> allReviews = reviewRepository.findAll();

        assertEquals(2, allReviews.size());
    }

    @Test
    void testFindReviewById() {
        Review savedReview = reviewRepository.save(testReview);
        Long reviewId = savedReview.getId();

        assertTrue(reviewRepository.findById(reviewId).isPresent());
    }

    @Test
    void testDeleteReview() {
        Review savedReview = reviewRepository.save(testReview);
        Long reviewId = savedReview.getId();

        reviewRepository.deleteById(reviewId);

        assertFalse(reviewRepository.findById(reviewId).isPresent());
    }
}