package com.chungtau.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.model.order.Order;
import com.chungtau.demo.model.orderDetail.CreateOrderDetailInput;
import com.chungtau.demo.model.orderDetail.DeleteOrderDetailInput;
import com.chungtau.demo.model.orderDetail.OrderDetail;
import com.chungtau.demo.model.product.Product;
import com.chungtau.demo.repository.OrderDetailRepository;
import com.chungtau.demo.repository.OrderRepository;
import com.chungtau.demo.repository.ProductRepository;

@Controller
public class OrderDetailController {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @QueryMapping
    public Optional<OrderDetail> getOrderDetailById(@Argument Long id) {
        return orderDetailRepository.findById(id);
    }

    @QueryMapping
    public Iterable<OrderDetail> getOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @SchemaMapping(typeName = "OrderDetail", field = "order")
    public Order getOrder(OrderDetail orderDetail) {
        Long orderId = orderDetail.getOrder().getId();
        if (orderId != null) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            return optionalOrder.orElse(null);
        } else {
            return null;
        }
    }

    @SchemaMapping(typeName = "OrderDetail", field = "product")
    public Product getProduct(OrderDetail orderDetail) {
        Long productId = orderDetail.getProduct().getId();
        if (productId != null) {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            return optionalProduct.orElse(null);
        } else {
            return null;
        }
    }

    @MutationMapping
    public OrderDetail createOrderDetail(@Argument CreateOrderDetailInput input) {
        OrderDetail orderDetail = new OrderDetail();
       
        orderDetail.setPrice(input.getPrice());
        orderDetail.setQuantity(input.getQuantity());

        Optional<Order> optionalOrder = orderRepository.findById(input.getOrderId());
        if (optionalOrder.isPresent()) {
            orderDetail.setOrder(optionalOrder.get());
        } else {
            throw new IllegalArgumentException("Order not found for id: " + input.getOrderId());
        }

        Optional<Product> optionalProduct = productRepository.findById(input.getProductId());

        if (optionalProduct.isPresent()) {
            orderDetail.setProduct(optionalProduct.get());
            return orderDetailRepository.save(orderDetail);
        } else {
            throw new IllegalArgumentException("Product not found for id: " + input.getProductId());
        }
    }

    @MutationMapping
    public boolean deleteOrderDetail(@Argument DeleteOrderDetailInput input) {
        if (orderDetailRepository.existsById(input.getId())) {
            orderDetailRepository.deleteById(input.getId());
            return true;
        } else {
            throw new IllegalArgumentException("OrderDetail not found for id: " + input.getId());
        }
    }
}
