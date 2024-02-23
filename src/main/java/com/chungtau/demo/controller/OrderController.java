package com.chungtau.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.model.order.CreateOrderInput;
import com.chungtau.demo.model.order.Order;
import com.chungtau.demo.model.orderDetail.CreateOrderDetailInput;
import com.chungtau.demo.model.orderDetail.OrderDetail;
import com.chungtau.demo.model.product.Product;
import com.chungtau.demo.model.user.User;
import com.chungtau.demo.repository.OrderRepository;
import com.chungtau.demo.repository.ProductRepository;
import com.chungtau.demo.repository.UserRepository;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @QueryMapping
    public Optional<Order> getOrderById(@Argument Long id) {
        return orderRepository.findById(id);
    }

    @QueryMapping
    public Iterable<Order> getOrders() {
        return orderRepository.findAll();
    }

    @SchemaMapping(typeName = "Order", field = "user")
    public User getUser(Order order) {
        Long userId = order.getUser().getId();
        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.orElse(null);
        } else {
            return null;
        }
    }

    @SchemaMapping(typeName = "Order", field = "orderDetails")
    public List<OrderDetail> getOrderDetails(Order order) {
        return order.getOrderDetails();
    }

    @MutationMapping
    public Order createOrder(@Argument CreateOrderInput input) {
        Order order = new Order();
        order.setOrderDate(input.getOrderDate());
        order.setTotalAmount(input.getTotalAmount());
        
        Optional<User> optionalUser = userRepository.findById(input.getUserId());
        if (optionalUser.isPresent()) {
            order.setUser(optionalUser.get());
        } else {
            throw new IllegalArgumentException("User not found for id: " + input.getUserId());
        }

        for (CreateOrderDetailInput orderDetailInput : input.getOrderDetails()) {
            order.addOrderDetail(createOrderDetail(order, orderDetailInput));
        }

        return orderRepository.save(order);
    }

    private OrderDetail createOrderDetail(Order order, CreateOrderDetailInput input) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setPrice(input.getPrice());
        orderDetail.setQuantity(input.getQuantity());
        Optional<Product> optionalProduct = productRepository.findById(input.getProductId());
        if (optionalProduct.isPresent()) {
            orderDetail.setProduct(optionalProduct.get());
        } else {
            throw new IllegalArgumentException("Product not found for id: " + input.getProductId());
        }
        return orderDetail;
    }
}
