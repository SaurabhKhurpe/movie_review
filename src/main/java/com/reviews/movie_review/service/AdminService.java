package com.reviews.movie_review.service;

import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.repository.MovieRepository;
import com.reviews.movie_review.service.request.MovieRequest;
import com.reviews.movie_review.service.response.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final MovieRepository movieRepository;

    @Autowired
    public AdminService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponse addMovie(MovieRequest movieRequest) {
        Movie movie = movieRequest.toMovie();
        Movie savedMovie = movieRepository.save(movie);
        return savedMovie.toMovieResponse();
    }

    @Transactional
    public List<MovieResponse> addMoviesBulk(List<MovieRequest> movieRequests) {
        List<Movie> movies = movieRequests.stream()
                .map(MovieRequest::toMovie)
                .collect(Collectors.toList());

        List<Movie> savedMovies = movieRepository.saveAll(movies);

        return savedMovies.stream()
                .map(Movie::toMovieResponse)
                .collect(Collectors.toList());
    }

    public MovieResponse updateMovie(Long id, MovieRequest movieRequest) {
        Optional<Movie> existingMovie = movieRepository.findById(id);
        if(existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            movie.setTitle(movieRequest.getTitle());
            movie.setGenre(movieRequest.getGenre());
            Movie updatedMovie = movieRepository.save(movie);
            return updatedMovie.toMovieResponse();
        }
        return null;
    }

    @Transactional
    public boolean deleteMovie(Long id) {
        if(movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
