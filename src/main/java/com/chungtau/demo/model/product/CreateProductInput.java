package com.chungtau.demo.model.product;

import lombok.Data;

@Data
public class CreateProductInput {
    private String name;
    private String description;
    private Float price;
    private Integer categoryId;
}
