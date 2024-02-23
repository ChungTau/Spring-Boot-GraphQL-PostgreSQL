package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.review.Review;

@GraphQlRepository
public interface ReviewRepository extends CrudRepository<Review, Long>, QuerydslPredicateExecutor<Review> {

}
