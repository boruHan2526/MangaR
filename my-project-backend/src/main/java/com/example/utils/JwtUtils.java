package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.entity.Const;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author boruOrris
 * @description: TODO
 * @date 2025/04/03
 */
@Component
public class JwtUtils {

    @Value("${spring.security.jwt.key}")
    String key;

    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //LogOut删除Token用（将Token放于Redis黑名单）

    public boolean invalidateJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);   // 定义了用来签名和验证 JWT 的加密算法
        JWTVerifier jwtverifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtverifier.verify(token);
            String id = jwt.getId(); // 注意这里取的是 withJWTId(UUID.randomUUID().toString()) 即UUID
            return deleteToken(id, jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private boolean deleteToken(String uuid, Date time){
        if (this.isInvalidToken(uuid))
            return false;
        Date now = new Date();
        long expire = Math.max(time.getTime() - now.getTime(), 0);   // 过期时间 - 当前时间
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS); // exipre 存储的时长，到达该时长后自动删除； TimeUnit.MILLISECONDS 它是 java.util.concurrent.TimeUnit 枚举类里的一个成员，代表 “毫秒” 这个单位。


        return true;
    }

    private boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid));
    }

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑



    private String convertToken(String headerToken) {   // 这个方法的作用是 从 HTTP 头的 Authorization 字符串中提取 JWT 令牌。 ※Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        if(headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

    public DecodedJWT resolveJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT verify = jwtVerifier.verify(token);  // jwtVerifier.verify(token) 解析 JWT，检查它是否 被正确签名。如果签名不匹配或 JWT 结构错误，会抛出 JWTVerificationException。
            if(this.isInvalidToken(verify.getId()))
                return null;
            Date expiresAt = verify.getExpiresAt(); // 检查 JWT 是否过期
            return new Date().after(expiresAt) ? null : verify; // 如果当前时间 已经 超过 JWT 过期时间，返回 null，表示令牌已失效。
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String createJWT(UserDetails details, int id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("name", username)
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                // stream()	把 List 转换成 Stream 流	方便链式操作数据
                // map(Function<T, R>)	把 A 类型转换成 B 类型	这里用于 GrantedAuthority -> String
                // toList()	把 Stream 结果转回 List	生成最终结果
                .withExpiresAt(expire)
                .withIssuedAt(new Date())  //设置 签发时间 以计算 过期时间
                .sign(algorithm);
    }

    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    public UserDetails toUser(DecodedJWT jwt){  // 把 JWT 解析成 Spring Security 的 UserDetails 对象，用于在 Spring Security 认证流程中获取用户信息。
        Map<String, Claim> claims = jwt.getClaims();    // jwt.getClaims() 获取 JWT 中的所有声明（Claim），返回一个 Map<String, Claim>。
                                                        // Claim 是 JWT 中的 自定义字段，比如 name（用户名）和 authorities（权限）。
        return User
                .withUsername(claims.get("name").asString())
                .password("******")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    public Integer toId(DecodedJWT jwt){    //  从 JWT 令牌中提取用户 ID，通常用于标识当前登录用户。
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

}
