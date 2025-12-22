package com.reviews.movie_review.controller;

import com.reviews.movie_review.service.AdminService;
import com.reviews.movie_review.service.request.MovieRequest;
import com.reviews.movie_review.service.response.MovieResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/movie/add")
    public ResponseEntity<MovieResponse> addMovie(@Valid @RequestBody MovieRequest movieRequest){
        MovieResponse response = adminService.addMovie(movieRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/movie/add/bulk")
    public ResponseEntity<List<MovieResponse>> addMoviesBulk(@Valid @RequestBody List<MovieRequest> movieRequests){
        List<MovieResponse> responses = adminService.addMoviesBulk(movieRequests);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @PutMapping("/movie/update/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id,
                                                     @Valid @RequestBody MovieRequest movieRequest){
        MovieResponse updatedMovie = adminService.updateMovie(id, movieRequest);
        if(updatedMovie != null) {
            return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/movie/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        boolean deleted = adminService.deleteMovie(id);
        if(deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}