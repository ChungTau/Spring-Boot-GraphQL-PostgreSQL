package com.chungtau.demo.model.user;

import lombok.Data;

@Data
public class CreateUserInput {
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private Integer roleId;
}
