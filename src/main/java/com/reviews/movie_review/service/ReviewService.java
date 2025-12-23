package com.reviews.movie_review.service;

import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.domain.Review;
import com.reviews.movie_review.repository.MovieRepository;
import com.reviews.movie_review.repository.ReviewRepository;
import com.reviews.movie_review.service.request.ReviewRequest;
import com.reviews.movie_review.service.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public void addReview(ReviewRequest reviewRequest) {
        Optional<Movie> movieOptional = movieRepository.findById(reviewRequest.getMovieId());
        if(movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            Review review = reviewRequest.toReview();
            review.setMovie(movie);
            reviewRepository.save(review);
            updateMovieRating(movie.getId());
        }
    }

    @Transactional
    public void addReviewsBulk(List<ReviewRequest> reviewRequests) {
        Map<Long, List<ReviewRequest>> reviewsByMovie = reviewRequests.stream()
                .collect(Collectors.groupingBy(ReviewRequest::getMovieId));

        reviewsByMovie.forEach((movieId, requests) -> {
            Optional<Movie> movieOptional = movieRepository.findById(movieId);
            if(movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                List<Review> reviews = requests.stream()
                        .map(req -> Review.builder()
                                .movieReview(req.getMovieReview())
                                .rating(req.getRating())
                                .movie(movie)
                                .build())
                        .collect(Collectors.toList());

                reviewRepository.saveAll(reviews);
                updateMovieRating(movieId);
            }
        });
    }

    private void updateMovieRating(Long movieId) {
        Double average = reviewRepository.getReviewAverage(movieId);
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if(movieOptional.isPresent() && average != null) {
            Movie movie = movieOptional.get();
            movie.setRating(Math.round(average * 10.0) / 10.0); // Round to 1 decimal
            movieRepository.save(movie);
        }
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(Review::toReviewResponse).orElse(null);
    }
}
