package com.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Account {
    Integer id;
    String username;
    String password;
    String email;
    String role;
    Date registerTime;
}
