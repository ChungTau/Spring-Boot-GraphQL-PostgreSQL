package com.chungtau.demo.model.cart;

import java.util.List;

import lombok.Data;

@Data
public class UpdateCartInput {
    private Long id;
    private List<Long> productIds;
}
