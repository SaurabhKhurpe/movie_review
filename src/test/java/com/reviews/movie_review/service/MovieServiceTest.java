package com.reviews.movie_review.service;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.repository.MovieRepository;
import com.reviews.movie_review.service.response.MovieResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock private MovieRepository movieRepository;
    @InjectMocks private MovieService movieService;

    @Test
    void findMovie_Success() {
        String title = "Inception";
        Movie movie = Movie.builder().id(1L).title(title).genre(Genre.SCI_FI).rating(8.8).build();
        when(movieRepository.findByTitle(title)).thenReturn(movie);
        MovieResponse result = movieService.findMovie(title);
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(8.8, result.getAvg_rating());
    }

    @Test
    void findMovie_NotFound() {
        when(movieRepository.findByTitle("Unknown")).thenReturn(null);
        MovieResponse result = movieService.findMovie("Unknown");
        assertNotNull(result);
        assertNull(result.getMovie_id());
    }

    @Test
    void findMovie_NullInput() {
        MovieResponse result = movieService.findMovie(null);
        assertNotNull(result);
        assertNull(result.getTitle());
    }

    @Test
    void findMoviesByGenre_SuccessTop5() {
        Genre genre = Genre.ACTION;
        List<Movie> movies = Arrays.asList(
                Movie.builder().id(1L).title("A").genre(genre).rating(9.0).build(),
                Movie.builder().id(2L).title("B").genre(genre).rating(8.5).build(),
                Movie.builder().id(3L).title("C").genre(genre).rating(8.0).build(),
                Movie.builder().id(4L).title("D").genre(genre).rating(7.5).build(),
                Movie.builder().id(5L).title("E").genre(genre).rating(7.0).build(),
                Movie.builder().id(6L).title("F").genre(genre).rating(6.5).build()
        );
        when(movieRepository.findByGenre(genre)).thenReturn(movies);
        List<MovieResponse> results = movieService.findMoviesByGenre("ACTION");
        assertEquals(5, results.size());
        assertEquals(9.0, results.get(0).getAvg_rating());
        assertEquals(7.0, results.get(4).getAvg_rating());
    }

    @Test
    void findMoviesByGenre_EmptyResult() {
        when(movieRepository.findByGenre(Genre.DRAMA)).thenReturn(Collections.emptyList());
        List<MovieResponse> results = movieService.findMoviesByGenre("DRAMA");
        assertTrue(results.isEmpty());
    }

    @Test
    void findMoviesByGenre_InvalidGenre() {
        List<MovieResponse> results = movieService.findMoviesByGenre("INVALID_GENRE");
        assertTrue(results.isEmpty());
        verify(movieRepository, never()).findByGenre(any());
    }

    @Test
    void findMoviesByGenre_NullInput() {
        List<MovieResponse> results = movieService.findMoviesByGenre(null);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}