package com.example.service.impl;

import com.example.entity.dto.Account;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{

    @Resource
    AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String verifyInfo) throws UsernameNotFoundException {

        Account account;

        account = this.findAccountByNameOrEmail(verifyInfo);

        if (account == null)
            throw new UsernameNotFoundException("用户名或邮箱不存在");

        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();

    }

    public Account findAccountByNameOrEmail(String verifyInfo) {

        // 判断 verifyInfo 是否包含 '@' 符号
        if (verifyInfo.contains("@")) {
            // 假设是电子邮件
            return accountMapper.findAccountByNameOrEmail(null, verifyInfo); // 第二个参数是电子邮件
        } else {
            // 假设是用户名
            return accountMapper.findAccountByNameOrEmail(verifyInfo, null); // 第一个参数是用户名
        }

    }

}
