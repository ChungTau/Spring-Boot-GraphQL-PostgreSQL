package com.chungtau.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.exception.EntityRuntimeException;
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
        if(id != null){
            return orderDetailRepository.findById(id);
        }else{
            return Optional.empty();
        }
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

        Long orderId = input.getOrderId();

        if(orderId != null){
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                orderDetail.setOrder(optionalOrder.get());
            } else {
                throw EntityRuntimeException.notFound(Order.class.getName(), orderId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Order.class.getName());
        }

        Long productId = input.getProductId();

        if(productId != null){
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (optionalProduct.isPresent()) {
                orderDetail.setProduct(optionalProduct.get());
                return orderDetailRepository.save(orderDetail);
            } else {
                throw EntityRuntimeException.notFound(Product.class.getName(), productId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(Product.class.getName());
        }
    }

    @MutationMapping
    public boolean deleteOrderDetail(@Argument DeleteOrderDetailInput input) {
        Long orderDetailId = input.getId();

        if(orderDetailId != null){
            if (orderDetailRepository.existsById(orderDetailId)) {
                orderDetailRepository.deleteById(orderDetailId);
                return true;
            } else {
                throw EntityRuntimeException.notFound(OrderDetail.class.getName(), orderDetailId);
            }
        }else{
            throw EntityRuntimeException.entityIdNotNull(OrderDetail.class.getName());
        }
    }
}
