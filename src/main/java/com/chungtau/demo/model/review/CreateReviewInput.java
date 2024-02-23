package com.chungtau.demo.model.review;

import lombok.Data;

@Data
public class CreateReviewInput {
    private Long userId;
    private Long productId;
    private Integer rating;
    private String comment;
}
