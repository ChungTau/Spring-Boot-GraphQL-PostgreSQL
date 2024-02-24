package com.chungtau.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chungtau.demo.exception.EntityRuntimeException;
import com.chungtau.demo.model.role.Role;
import com.chungtau.demo.model.user.CreateUserInput;
import com.chungtau.demo.model.user.DeleteUserInput;
import com.chungtau.demo.model.user.UpdateUserInput;
import com.chungtau.demo.model.user.User;
import com.chungtau.demo.repository.RoleRepository;
import com.chungtau.demo.repository.UserRepository;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @QueryMapping
    public Optional<User> getUserById(@Argument Long id) {
        if (id != null) {
            return userRepository.findById(id);
        } else {
            return Optional.empty();
        }
    }
    

    @QueryMapping
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @SchemaMapping(typeName = "User", field = "role")
    public Role getRole(User user) {
        Integer roleId = user.getRole().getId();
        if (roleId != null) {
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            return optionalRole.orElse(null);
        } else {
            return null;
        }
    }

    @MutationMapping
    public User addUser(@Argument CreateUserInput input) {
        User user = new User();
        
        user.setLastName(input.getLastName());
        user.setFirstName(input.getFirstName());
        user.setEmail(input.getEmail());
        user.setCity(input.getCity());
        user.setStreet(input.getStreet());
    
        Integer roleId = input.getRoleId();
        if (roleId != null) {
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            if (optionalRole.isPresent()) {
                user.setRole(optionalRole.get());
                return userRepository.save(user);
            } else {
                throw EntityRuntimeException.notFound(Role.class.getName(), roleId);
            }
        } else {
            throw EntityRuntimeException.entityIdNotNull(Role.class.getName());
        }
    }
    

    @MutationMapping
    public User updateUser(@Argument UpdateUserInput input) {
        Long userId = input.getId();
        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setLastName(input.getLastName());
                user.setFirstName(input.getFirstName());
                user.setEmail(input.getEmail());
                user.setCity(input.getCity());
                user.setStreet(input.getStreet());
                return userRepository.save(user);
            } else {
                throw EntityRuntimeException.notFound(User.class.getName(), userId);
            }
        }else {
            throw EntityRuntimeException.entityIdNotNull(User.class.getName());
        }
    }

    @MutationMapping
    public boolean deleteUser(@Argument DeleteUserInput input) {
        Long userId = input.getId();
        if (userId != null) {
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return true;
            } else {
                throw EntityRuntimeException.notFound(User.class.getName(), userId);
            }
        }else {
            throw EntityRuntimeException.entityIdNotNull(User.class.getName());
        }
    }
}
