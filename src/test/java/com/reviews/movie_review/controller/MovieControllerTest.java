package com.reviews.movie_review.controller;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.service.MovieService;
import com.reviews.movie_review.service.response.MovieResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @Test
    void getMovieByTitle_Success() throws Exception {
        MovieResponse response = MovieResponse.builder().movie_id(1L).title("Inception").genre(Genre.SCI_FI).avg_rating(8.8).build();
        when(movieService.findMovie("Inception")).thenReturn(response);

        mockMvc.perform(get("/movie")
                        .param("title", "Inception")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Inception")));
    }

    @Test
    void getMovieByGenre_Success() throws Exception {
        MovieResponse m1 = MovieResponse.builder().movie_id(1L).title("A").genre(Genre.ACTION).avg_rating(9.0).build();
        MovieResponse m2 = MovieResponse.builder().movie_id(2L).title("B").genre(Genre.ACTION).avg_rating(8.5).build();
        when(movieService.findMoviesByGenre("ACTION")).thenReturn(Arrays.asList(m1, m2));

        mockMvc.perform(get("/movie")
                        .param("genre", "ACTION")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("A")));
    }

    @Test
    void getMovieByGenre_EmptyResult() throws Exception {
        when(movieService.findMoviesByGenre("DRAMA")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/movie")
                        .param("genre", "DRAMA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getMovie_BothParametersError() throws Exception {
        mockMvc.perform(get("/movie")
                        .param("title", "Inception")
                        .param("genre", "SCI_FI"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please provide either 'title' or 'genre', not both"));
        verify(movieService, never()).findMovie(any());
    }

    @Test
    void getMovie_NoParametersError() throws Exception {
        mockMvc.perform(get("/movie"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Either 'title' or 'genre' parameter is required"));
    }
}