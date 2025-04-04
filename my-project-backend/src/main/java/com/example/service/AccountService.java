package com.example.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountService extends UserDetailsService {

    // 只要继承了 UserDetailsService 接口并重写其 loadUserByUsername 方法，SpringSecurity就会自动改为基于DB认证【Spring Boot 自动装配】
    // ※但如果你项目里出现了多个 UserDetailsService 实现，或者你写了自定义的 AuthenticationProvider，你就必须手动配置它
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
