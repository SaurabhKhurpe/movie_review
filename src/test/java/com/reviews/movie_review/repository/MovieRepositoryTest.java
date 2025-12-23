package com.reviews.movie_review.repository;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = Movie.builder()
                .title("Test Movie")
                .genre(Genre.ACTION)
                .rating(8.5)
                .build();
    }

    @Test
    void testSaveMovie() {
        Movie savedMovie = movieRepository.save(testMovie);

        assertNotNull(savedMovie.getId());
        assertEquals("Test Movie", savedMovie.getTitle());
        assertEquals(Genre.ACTION, savedMovie.getGenre());
        assertEquals(8.5, savedMovie.getRating());
    }

    @Test
    void testFindById() {
        Movie savedMovie = movieRepository.save(testMovie);
        Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getId());

        assertTrue(foundMovie.isPresent());
        assertEquals(savedMovie.getId(), foundMovie.get().getId());
    }

    @Test
    void testFindByTitle() {
        movieRepository.save(testMovie);
        Movie foundMovie = movieRepository.findByTitle("Test Movie");

        assertNotNull(foundMovie);
        assertEquals("Test Movie", foundMovie.getTitle());
    }

    @Test
    void testFindByTitle_NotFound() {
        Movie foundMovie = movieRepository.findByTitle("NonExistent");

        assertNull(foundMovie);
    }

    @Test
    void testFindByGenre() {
        Movie movie1 = Movie.builder().title("Action Movie 1").genre(Genre.ACTION).rating(8.0).build();
        Movie movie2 = Movie.builder().title("Action Movie 2").genre(Genre.ACTION).rating(7.5).build();
        Movie movie3 = Movie.builder().title("Comedy Movie").genre(Genre.COMEDY).rating(6.0).build();

        movieRepository.saveAll(List.of(movie1, movie2, movie3));

        List<Movie> actionMovies = movieRepository.findByGenre(Genre.ACTION);

        assertEquals(2, actionMovies.size());
        assertThat(actionMovies).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("Action Movie 1", "Action Movie 2");
    }

    @Test
    void testFindAll() {
        Movie movie1 = Movie.builder().title("Movie 1").genre(Genre.ACTION).rating(8.0).build();
        Movie movie2 = Movie.builder().title("Movie 2").genre(Genre.COMEDY).rating(7.0).build();

        movieRepository.saveAll(List.of(movie1, movie2));
        List<Movie> allMovies = movieRepository.findAll();

        assertEquals(2, allMovies.size());
    }

    @Test
    void testDeleteMovie() {
        Movie savedMovie = movieRepository.save(testMovie);
        Long movieId = savedMovie.getId();

        assertTrue(movieRepository.findById(movieId).isPresent());

        movieRepository.deleteById(movieId);

        assertFalse(movieRepository.findById(movieId).isPresent());
    }
}