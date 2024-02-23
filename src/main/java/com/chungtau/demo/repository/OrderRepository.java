package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.order.Order;

@GraphQlRepository
public interface OrderRepository extends CrudRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

}
