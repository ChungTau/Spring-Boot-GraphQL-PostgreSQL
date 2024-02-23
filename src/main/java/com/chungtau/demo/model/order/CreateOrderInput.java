package com.chungtau.demo.model.order;

import java.util.List;

import com.chungtau.demo.model.orderDetail.CreateOrderDetailInput;

import lombok.Data;

@Data
public class CreateOrderInput {
    private Long userId;
    private String orderDate;
    private Float totalAmount;
    private List<CreateOrderDetailInput> orderDetails;
}
