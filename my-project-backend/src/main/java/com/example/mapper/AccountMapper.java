package com.example.mapper;

import com.example.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    Account findAccountByNameOrEmail(@Param("username") String username, @Param("email") String email);

}
