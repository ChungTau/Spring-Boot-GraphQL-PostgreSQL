package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.product.Product;

@GraphQlRepository
public interface ProductRepository extends CrudRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

}
