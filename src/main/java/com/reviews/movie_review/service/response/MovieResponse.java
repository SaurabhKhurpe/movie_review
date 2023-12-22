package com.reviews.movie_review.service.response;


import com.reviews.movie_review.domain.Genre;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {

    private Long movie_id;
    private String title;
    private Genre genre;
    private Double avg_rating;
    private List<ReviewResponse> reviews;


}
