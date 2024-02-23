package com.chungtau.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.chungtau.demo.model.role.Role;

@GraphQlRepository
public interface RoleRepository extends CrudRepository<Role, Integer>, QuerydslPredicateExecutor<Role> {

}