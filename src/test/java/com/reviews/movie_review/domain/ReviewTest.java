package com.reviews.movie_review.domain;

import com.reviews.movie_review.service.response.ReviewResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void toReviewResponse_Single() {
        Review review = Review.builder()
                .id(5L)
                .movieReview("Loved it!")
                .rating(9.5)
                .build();

        ReviewResponse response = Review.toReviewResponse(review);

        assertEquals(5L, response.getReview_id());
        assertEquals("Loved it!", response.getReview());
        assertEquals(9.5, response.getRating());
    }

    @Test
    void toReviewResponse_List() {
        Review r1 = Review.builder().id(1L).movieReview("Good").rating(8.0).build();
        Review r2 = Review.builder().id(2L).movieReview("Bad").rating(3.0).build();
        List<Review> list = Arrays.asList(r1, r2);

        List<ReviewResponse> responses = Review.toReviewResponse(list);

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getReview_id());
        assertEquals(2L, responses.get(1).getReview_id());
    }

    @Test
    void toReviewResponse_NullList() {
        List<ReviewResponse> responses = Review.toReviewResponse((List<Review>) null);
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void toReviewResponse_ListWithNulls() {
        Review r1 = Review.builder().id(1L).movieReview("Good").rating(8.0).build();
        List<Review> listWithNull = Arrays.asList(r1, null);

        List<ReviewResponse> responses = Review.toReviewResponse(listWithNull);

        assertEquals(1, responses.size()); // Only one non-null element converted
        assertEquals(1L, responses.getFirst().getReview_id());
    }
}