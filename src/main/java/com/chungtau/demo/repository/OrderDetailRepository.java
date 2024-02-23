package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.orderDetail.OrderDetail;

@GraphQlRepository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long>, QuerydslPredicateExecutor<OrderDetail> {

}