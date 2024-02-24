package com.chungtau.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.exception.EntityRuntimeException;
import com.chungtau.demo.model.order.CreateOrderInput;
import com.chungtau.demo.model.order.DeleteOrderInput;
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
        if(id != null){
            return orderRepository.findById(id);
        }else{
            return Optional.empty();
        }
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
        
        Long userId = input.getUserId();
        if(userId != null){
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                order.setUser(optionalUser.get());
            } else {
                throw EntityRuntimeException.notFound(User.class.getName(), userId);
            }

            for (CreateOrderDetailInput orderDetailInput : input.getOrderDetails()) {
                order.addOrderDetail(createOrderDetail(order, orderDetailInput));
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(User.class.getName());
        }

        return orderRepository.save(order);
    }

    @MutationMapping
    public Boolean deleteOrder(@Argument DeleteOrderInput input) {
        Long orderId = input.getId();

        if(orderId != null){
            if (orderRepository.existsById(orderId)) {
                orderRepository.deleteById(orderId);
                return true;
            } else {
                throw EntityRuntimeException.notFound(Order.class.getName(), orderId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Order.class.getName());
        }
    }

    private OrderDetail createOrderDetail(Order order, CreateOrderDetailInput input) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setPrice(input.getPrice());
        orderDetail.setQuantity(input.getQuantity());
        Long productId = input.getProductId();

        if(productId != null){
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                orderDetail.setProduct(optionalProduct.get());
            } else {
                throw EntityRuntimeException.notFound(Product.class.getName(), productId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Product.class.getName());
        }
        return orderDetail;
    }
}
