package com.chungtau.demo.model.category;

import lombok.Data;

@Data
public class UpdateCategoryInput {
    private Integer id;
    private String name;
    private String description;
}
