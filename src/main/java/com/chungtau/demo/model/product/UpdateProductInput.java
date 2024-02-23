package com.chungtau.demo.model.product;

import lombok.Data;

@Data
public class UpdateProductInput {
    private Long id;
    private String name;
    private String description;
    private Float price;
    private Integer categoryId;
}