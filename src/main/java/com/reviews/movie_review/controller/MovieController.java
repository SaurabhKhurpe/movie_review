package com.reviews.movie_review.controller;

import com.reviews.movie_review.service.MovieService;
import com.reviews.movie_review.service.response.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getMovie(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre) {

        if (title != null && genre != null) {
            return ResponseEntity.badRequest().body("Please provide either 'title' or 'genre', not both");
        }

        if (title != null) {
            MovieResponse movie = movieService.findMovie(title);
            if (movie != null && movie.getMovie_id() != null) {
                return ResponseEntity.ok(movie);
            }
            return ResponseEntity.notFound().build();
        }

        if (genre != null) {
            List<MovieResponse> movies = movieService.findMoviesByGenre(genre);
            return ResponseEntity.ok(movies);
        }

        return ResponseEntity.badRequest().body("Either 'title' or 'genre' parameter is required");
    }
}
