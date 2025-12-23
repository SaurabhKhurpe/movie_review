package com.reviews.movie_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviews.movie_review.service.ReviewService;
import com.reviews.movie_review.service.request.ReviewRequest;
import com.reviews.movie_review.service.response.ReviewResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewService reviewService;

    @Test
    void addReview_Success() throws Exception {
        ReviewRequest validRequest = new ReviewRequest("A great movie indeed! It was fantastic.", 9.5, 1L);

        mockMvc.perform(post("/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Review added successfully"));

        verify(reviewService, times(1)).addReview(any(ReviewRequest.class));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 11.0})
    void addReview_InvalidRating(double invalidRating) throws Exception {
        ReviewRequest invalidRequest = new ReviewRequest("This is a valid review text that meets the 10 character minimum requirement.", invalidRating, 1L);

        mockMvc.perform(post("/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).addReview(any());
    }

    @Test
    void addReviewsBulk_Success() throws Exception {
        ReviewRequest req = new ReviewRequest("Good movie, I enjoyed it very much! Really great.", 8.0, 1L);

        mockMvc.perform(post("/review/add/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(req))))
                .andExpect(status().isCreated())
                .andExpect(content().string("1 reviews added successfully"));

        verify(reviewService, times(1)).addReviewsBulk(anyList());
    }

    @Test
    void getReviewById_Success() throws Exception {
        ReviewResponse response = ReviewResponse.builder()
                .review_id(1L)
                .review("Great!")
                .rating(9.0)
                .build();

        when(reviewService.getReviewById(1L)).thenReturn(response);

        mockMvc.perform(get("/review")
                        .param("Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.review_id").value(1))
                .andExpect(jsonPath("$.review").value("Great!"));

        verify(reviewService, times(1)).getReviewById(1L);
    }

    @Test
    void getReviewById_NotFound() throws Exception {
        when(reviewService.getReviewById(999L)).thenReturn(null);

        mockMvc.perform(get("/review")
                        .param("Id", "999"))
                .andExpect(status().isNotFound());

        verify(reviewService, times(1)).getReviewById(999L);
    }

    @Test
    void getReviewById_MissingParameter() throws Exception {
        mockMvc.perform(get("/review"))
                .andExpect(status().isBadRequest());

        verify(reviewService, never()).getReviewById(anyLong());
    }
}