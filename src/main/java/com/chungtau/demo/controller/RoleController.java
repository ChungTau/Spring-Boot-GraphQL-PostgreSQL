package com.chungtau.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.model.role.CreateRoleInput;
import com.chungtau.demo.model.role.DeleteRoleInput;
import com.chungtau.demo.model.role.Role;
import com.chungtau.demo.model.role.UpdateRoleInput;
import com.chungtau.demo.repository.RoleRepository;

@Controller
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @QueryMapping
    public Optional<Role> getRoleById(@Argument Integer id){
        return roleRepository.findById(id);
    }

    @QueryMapping
    public Iterable<Role> getRoles(){
        return roleRepository.findAll();
    }

    @MutationMapping
    public Role addRole(@Argument CreateRoleInput input) {
        Role role = new Role();
        role.setName(input.getName());
        return roleRepository.save(role);
    }

    @MutationMapping
    public Role updateRole(@Argument UpdateRoleInput input) {
        Optional<Role> optionalRole = roleRepository.findById(input.getId());
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.setName(input.getName());
            return roleRepository.save(role);
        } else {
            throw new RuntimeException("Role not found with id: " + input.getId());
        }
    }

    @MutationMapping
    public boolean deleteRole(@Argument DeleteRoleInput input) {
        Optional<Role> optionalRole = roleRepository.findById(input.getId());
        if (optionalRole.isPresent()) {
            roleRepository.deleteById(input.getId());
            return true;
        } else {
            throw new RuntimeException("Role not found with id: " + input.getId());
        }
    }
}
