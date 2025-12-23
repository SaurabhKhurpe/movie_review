package com.reviews.movie_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.service.AdminService;
import com.reviews.movie_review.service.request.MovieRequest;
import com.reviews.movie_review.service.response.MovieResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminService adminService;

    @Test
    void addMovie_Success() throws Exception {
        MovieRequest request = new MovieRequest("Inception", Genre.SCI_FI);
        MovieResponse response = MovieResponse.builder().movie_id(1L).title("Inception").genre(Genre.SCI_FI).avg_rating(0.0).build();

        when(adminService.addMovie(any(MovieRequest.class))).thenReturn(response);

        mockMvc.perform(post("/admin/movie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movie_id", is(1)))
                .andExpect(jsonPath("$.title", is("Inception")));
    }

    @Test
    void addMovie_ValidationFails() throws Exception {
        MovieRequest invalidRequest = new MovieRequest(null, null);
        mockMvc.perform(post("/admin/movie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        verify(adminService, never()).addMovie(any());
    }

    @Test
    void addMoviesBulk_Success() throws Exception {
        MovieRequest req = new MovieRequest("Movie", Genre.ACTION);
        MovieResponse resp = MovieResponse.builder().movie_id(1L).title("Movie").genre(Genre.ACTION).build();

        when(adminService.addMoviesBulk(any())).thenReturn(Collections.singletonList(resp));

        mockMvc.perform(post("/admin/movie/add/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(req))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].title", is("Movie")));
    }

    @Test
    void updateMovie_Success() throws Exception {
        MovieRequest request = new MovieRequest("Updated", Genre.THRILLER);
        MovieResponse response = MovieResponse.builder().movie_id(1L).title("Updated").genre(Genre.THRILLER).build();

        when(adminService.updateMovie(eq(1L), any(MovieRequest.class))).thenReturn(response);

        mockMvc.perform(put("/admin/movie/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated")));
    }

    @Test
    void updateMovie_NotFound() throws Exception {
        when(adminService.updateMovie(eq(999L), any(MovieRequest.class))).thenReturn(null);
        MovieRequest request = new MovieRequest("Test", Genre.ACTION);

        mockMvc.perform(put("/admin/movie/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMovie_Success() throws Exception {
        when(adminService.deleteMovie(1L)).thenReturn(true);
        mockMvc.perform(delete("/admin/movie/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMovie_NotFound() throws Exception {
        when(adminService.deleteMovie(999L)).thenReturn(false);
        mockMvc.perform(delete("/admin/movie/delete/999"))
                .andExpect(status().isNotFound());
    }
}