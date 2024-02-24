package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.cart.Cart;

@GraphQlRepository
public interface CartRepository extends CrudRepository<Cart, Long>, QuerydslPredicateExecutor<Cart> {

}