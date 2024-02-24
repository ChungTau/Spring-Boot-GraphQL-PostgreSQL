package com.chungtau.demo.model.cart;

import java.util.List;

import lombok.Data;

@Data
public class CreateCartInput {
    private Long userId;
    private List<Long> productIds;
}
