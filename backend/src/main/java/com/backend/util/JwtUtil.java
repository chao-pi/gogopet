package com.backend.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 工具类，用于生成、解析和验证 JWT Token
 * 作用：
 * 使用密钥生成符合 JWT 标准的 Token；
 * 提供提取用户名、过期时间等功能；
 * 验证 Token 是否有效（包括签名和过期时间）；
 * 支持从 Token 中提取任意自定义字段。
 */
@Component
public class JwtUtil {

    // 从配置文件中注入密钥字符串（用于签名和验证）
    @Value("${jwt.secret}")
    private String secret;

    // Token 有效期（秒）
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成用于签名和验证的 HMAC 密钥
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 根据用户名生成 JWT Token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // 可添加自定义声明
        return createToken(claims, username);
    }

    /**
     * 构建 Token 字符串
     * - claims: 自定义声明
     * - subject: 一般设为用户名
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // 设置负载中的自定义声明
                .setSubject(subject) // 设置主题（如用户名）
                .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 使用密钥和算法签名
                .compact(); // 构建完整的 Token 字符串
    }

    /**
     * 校验 Token 是否有效（用户名一致且未过期）
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 校验 Token 是否有效（针对UserDetails对象）
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从 Token 中提取用户名（即 Subject 字段）
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 提取 Token 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 通用的声明提取方法，可以自定义提取任何字段
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 Token，获取完整的 Claims 对象（包含声明信息）
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 设置签名密钥用于验证
                .build()
                .parseClaimsJws(token) // 解析 JWT 字符串
                .getBody(); // 获取负载（Payload）
    }

    /**
     * 判断 Token 是否过期
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
