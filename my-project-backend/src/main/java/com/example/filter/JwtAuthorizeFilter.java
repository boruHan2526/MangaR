package com.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author boruOrris
 * @description: TODO
 * @date 2025/04/03
 */

@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils utils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");  // 读取请求头 Authorization 的值
        DecodedJWT jwt = utils.resolveJwt(authorization);   // 解析 Authorization 头，提取并验证 JWT 令牌。
        if (jwt != null ) {
            UserDetails user = utils.toUser(jwt);   // JWT 存在时，解析用户信息
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // UsernamePasswordAuthenticationToken 是 Spring Security 的用户身份认证对象。null 代表 不需要密码（因为 JWT 认证不需要密码）。
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // 设置额外的请求信息（如 IP 地址、Session ID）。WebAuthenticationDetails 主要存储两个信息：用户 IP 地址；用户 Session ID
            SecurityContextHolder.getContext().setAuthentication(authentication);   // 把用户认证信息存入 SecurityContext。让 SecurityContextHolder 持有当前用户的认证信息，这样整个应用程序可以识别该用户。
            request.setAttribute("id", utils.toId(jwt));    // 把用户 id 存入请求属性；把 id 存入 request，后面的控制器可以用：
        }
        //最后放行，继续下一个过滤器
        //可能各位小伙伴会好奇，要是没验证成功不是应该拦截吗？这个其实没有关系的
        //因为如果没有验证失败上面是不会给SecurityContext设置Authentication的，后面直接就被拦截掉了
        //而且有可能用户发起的是用户名密码登录请求，这种情况也要放行的，不然怎么登录，所以说直接放行就好
        filterChain.doFilter(request, response);
    }
}
