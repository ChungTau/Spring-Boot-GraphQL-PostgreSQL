package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.user.User;

@GraphQlRepository
public interface UserRepository extends CrudRepository<User, Long>, QuerydslPredicateExecutor<User> {

}