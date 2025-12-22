package com.reviews.movie_review.service.response;

import com.reviews.movie_review.domain.Genre;
import java.util.List;
import lombok.*;

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
