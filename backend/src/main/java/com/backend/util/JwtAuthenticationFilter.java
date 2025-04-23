package com.backend.util;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义的 JWT 认证过滤器，在每次请求前验证 Token 并设置认证信息
 * 作用：
 * 拦截每个请求；
 * 如果是公共接口（如登录接口），直接放行；
 * 如果请求头中带有 JWT Token，解析并验证；
 * 验证成功后，设置 Spring Security 的认证上下文；
 * 验证失败返回 401 状态码和提示信息。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * 使用构造函数注入依赖，更符合 Spring 推荐实践
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 每次请求都会调用该方法，对请求中的 JWT Token 进行验证
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中获取 Authorization 字段
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 如果请求头中没有 Authorization 字段或者不是以 "Bearer " 开头，直接放行
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 提取 JWT Token
        jwt = authHeader.substring(7);
        
        try {
            // 从 Token 中提取用户名
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Token 解析失败，直接放行
            filterChain.doFilter(request, response);
            return;
        }

        // 如果已经设置了认证信息，直接放行
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 加载用户信息
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // 验证 Token 是否有效
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                // 设置认证详情
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 设置认证上下文
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 判断请求路径是否为公共路径
     */
    private boolean isPublicPath(String requestURI) {
        return requestURI.startsWith("/api/auth/") || requestURI.equals("/error");
    }
}
