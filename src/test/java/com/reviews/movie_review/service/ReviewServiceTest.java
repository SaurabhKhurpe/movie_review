package com.reviews.movie_review.service;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.domain.Review;
import com.reviews.movie_review.repository.MovieRepository;
import com.reviews.movie_review.repository.ReviewRepository;
import com.reviews.movie_review.service.request.ReviewRequest;
import com.reviews.movie_review.service.response.ReviewResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private MovieRepository movieRepository;
    @InjectMocks private ReviewService reviewService;

    @Test
    void addReview_SuccessAndUpdatesRating() {
        Long movieId = 1L;
        ReviewRequest request = new ReviewRequest("Great!", 9.0, movieId);
        Movie movie = Movie.builder().id(movieId).title("Test").genre(Genre.ACTION).rating(0.0).build();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());
        when(reviewRepository.getReviewAverage(movieId)).thenReturn(9.0);
        reviewService.addReview(request);
        verify(reviewRepository).save(any(Review.class));
        verify(reviewRepository).getReviewAverage(movieId);
        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository).save(movieCaptor.capture());
        assertEquals(9.0, movieCaptor.getValue().getRating());
    }

    @Test
    void addReview_MovieNotFound() {
        ReviewRequest request = new ReviewRequest("Great!", 9.0, 999L);
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        reviewService.addReview(request);
        verify(reviewRepository, never()).save(any());
        verify(reviewRepository, never()).getReviewAverage(anyLong());
    }

    @Test
    void addReviewsBulk_Success() {
        ReviewRequest req1 = new ReviewRequest("Good", 8.0, 1L);
        ReviewRequest req2 = new ReviewRequest("Great", 9.0, 1L);
        ReviewRequest req3 = new ReviewRequest("Ok", 7.0, 2L);
        List<ReviewRequest> requests = Arrays.asList(req1, req2, req3);
        Movie m1 = Movie.builder().id(1L).title("M1").build();
        Movie m2 = Movie.builder().id(2L).title("M2").build();

        when(movieRepository.findById(1L)).thenReturn(Optional.of(m1));
        when(movieRepository.findById(2L)).thenReturn(Optional.of(m2));
        when(reviewRepository.getReviewAverage(1L)).thenReturn(8.5);
        when(reviewRepository.getReviewAverage(2L)).thenReturn(7.0);

        reviewService.addReviewsBulk(requests);

        verify(reviewRepository, times(2)).saveAll(anyList());
        verify(movieRepository, times(2)).save(any(Movie.class));
    }

    @Test
    void addReviewsBulk_EmptyList() {
        reviewService.addReviewsBulk(Collections.emptyList());
        verify(movieRepository, never()).findById(anyLong());
        verify(reviewRepository, never()).saveAll(anyList());
    }

    @Test
    void getReviewById_Success() {
        Long reviewId = 1L;
        Review review = Review.builder().id(reviewId).movieReview("Excellent").rating(9.5).build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        ReviewResponse result = reviewService.getReviewById(reviewId);
        assertNotNull(result);
        assertEquals(reviewId, result.getReview_id());
    }

    @Test
    void getReviewById_NotFound() {
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());
        ReviewResponse result = reviewService.getReviewById(999L);
        assertNull(result);
    }

    @Test
    void addReview_AverageRatingIsNull() {
        Long movieId = 1L;
        ReviewRequest request = new ReviewRequest("Test", 5.0, movieId);
        Movie movie = Movie.builder().id(movieId).title("Test").build();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());
        when(reviewRepository.getReviewAverage(movieId)).thenReturn(null);
        reviewService.addReview(request);
        verify(movieRepository, never()).save(any(Movie.class));
    }
}