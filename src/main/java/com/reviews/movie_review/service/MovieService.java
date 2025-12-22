package com.reviews.movie_review.service;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import com.reviews.movie_review.repository.MovieRepository;
import com.reviews.movie_review.service.response.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieResponse findMovie(String title) {
        Movie movie = movieRepository.findByTitle(title);
        if (Objects.nonNull(movie)) {
            return movie.toMovieResponse();
        }
        // Return empty response instead of null
        return MovieResponse.builder()
                .movie_id(null)
                .title(null)
                .genre(null)
                .avg_rating(null)
                .reviews(new ArrayList<>())
                .build();
    }

    public List<MovieResponse> findMoviesByGenre(String genre) {
        try {
            // Convert genre string to uppercase for case-insensitive matching
            String genreUpper = genre.toUpperCase();
            if (Arrays.stream(Genre.values()).noneMatch(g -> g.toString().equals(genreUpper))) {
                return new ArrayList<>();
            }

            List<Movie> movieList = movieRepository.findByGenre(Genre.valueOf(genreUpper));
            if (!CollectionUtils.isEmpty(movieList)) {
                return movieList.stream()
                        .sorted(Comparator.comparing(Movie::getRating,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .limit(5)
                        .map(Movie::toMovieResponse)
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            // Invalid genre enum value
            return new ArrayList<>();
        }
    }
}