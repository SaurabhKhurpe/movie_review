package com.reviews.movie_review.repository;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    Movie findByTitle(String title);

    List<Movie> findByGenre(Genre genre);
}
