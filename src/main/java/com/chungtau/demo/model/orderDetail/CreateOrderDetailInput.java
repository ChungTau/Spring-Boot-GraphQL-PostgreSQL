package com.chungtau.demo.model.orderDetail;

import lombok.Data;

@Data
public class CreateOrderDetailInput {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Float price;
}
