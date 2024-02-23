package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.category.Category;

@GraphQlRepository
public interface CategoryRepository extends CrudRepository<Category, Integer>, QuerydslPredicateExecutor<Category> {

}