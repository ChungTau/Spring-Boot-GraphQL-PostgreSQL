package com.chungtau.demo.model.user;

import lombok.Data;

@Data
public class UpdateUserInput {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
}
