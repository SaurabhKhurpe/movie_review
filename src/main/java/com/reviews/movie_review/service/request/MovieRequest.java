package com.reviews.movie_review.service.request;

import com.reviews.movie_review.domain.Genre;
import com.reviews.movie_review.domain.Movie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Genre is required")
    private Genre genre;

    public Movie toMovie(){
        return Movie.builder()
                .title(title)
                .genre(genre)
                .rating(0.0)
                .build();
    }
}