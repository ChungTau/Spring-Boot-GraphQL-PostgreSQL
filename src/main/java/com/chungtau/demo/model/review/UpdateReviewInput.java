package com.chungtau.demo.model.review;

import lombok.Data;

@Data
public class UpdateReviewInput {
    private Long id;
    private Integer rating;
    private String comment;
}
