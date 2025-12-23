package com.reviews.movie_review.service;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.repository.MovieRepository;
import com.reviews.movie_review.service.request.MovieRequest;
import com.reviews.movie_review.service.response.MovieResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private MovieRepository movieRepository;
    @InjectMocks private AdminService adminService;

    @Test
    void addMovie_Success() {
        MovieRequest request = new MovieRequest("Inception", Genre.SCI_FI);
        Movie savedMovie = Movie.builder().id(1L).title("Inception").genre(Genre.SCI_FI).rating(0.0).build();
        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);
        MovieResponse response = adminService.addMovie(request);
        assertNotNull(response);
        assertEquals(1L, response.getMovie_id());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void addMoviesBulk_Success() {
        MovieRequest req1 = new MovieRequest("Movie 1", Genre.ACTION);
        MovieRequest req2 = new MovieRequest("Movie 2", Genre.COMEDY);
        List<MovieRequest> requests = Arrays.asList(req1, req2);
        Movie m1 = Movie.builder().id(1L).title("Movie 1").genre(Genre.ACTION).rating(0.0).build();
        Movie m2 = Movie.builder().id(2L).title("Movie 2").genre(Genre.COMEDY).rating(0.0).build();
        List<Movie> savedMovies = Arrays.asList(m1, m2);
        when(movieRepository.saveAll(anyList())).thenReturn(savedMovies);
        List<MovieResponse> responses = adminService.addMoviesBulk(requests);
        assertEquals(2, responses.size());
        verify(movieRepository, times(1)).saveAll(anyList());
    }

    @Test
    void addMoviesBulk_EmptyList() {
        List<MovieResponse> responses = adminService.addMoviesBulk(Collections.emptyList());
        assertTrue(responses.isEmpty());
    }

    @Test
    void updateMovie_Success() {
        Long movieId = 1L;
        MovieRequest updateRequest = new MovieRequest("Updated", Genre.THRILLER);
        Movie existingMovie = Movie.builder().id(movieId).title("Old").genre(Genre.ACTION).build();
        Movie updatedMovie = Movie.builder().id(movieId).title("Updated").genre(Genre.THRILLER).rating(8.5).build();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
        MovieResponse result = adminService.updateMovie(movieId, updateRequest);
        assertNotNull(result);
        assertEquals("Updated", result.getTitle());
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void updateMovie_NotFound() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        MovieRequest request = new MovieRequest("Test", Genre.ACTION);
        MovieResponse result = adminService.updateMovie(999L, request);
        assertNull(result);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_Success() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(1L);
        boolean result = adminService.deleteMovie(1L);
        assertTrue(result);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMovie_NotFound() {
        when(movieRepository.existsById(999L)).thenReturn(false);
        boolean result = adminService.deleteMovie(999L);
        assertFalse(result);
        verify(movieRepository, never()).deleteById(anyLong());
    }
}