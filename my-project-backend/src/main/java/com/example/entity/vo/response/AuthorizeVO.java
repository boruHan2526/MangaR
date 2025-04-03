package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author boruOrris
 * @description: TODO
 * @date 2025/04/03
 */

@Data
public class AuthorizeVO {
    String username;
    String role;
    String token;
    Date expire;
}
