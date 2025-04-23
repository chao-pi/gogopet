package com.backend.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

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

        // 判断请求是否为公开路径，如果是则直接放行
        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 若没有 token 或格式不正确，返回 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未提供有效的认证令牌");
            return;
        }

        try {
            // 提取 token 并解析用户名
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            // 若用户名有效，且当前未认证，则进行手动认证
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 加载用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 验证 token 是否有效
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    // 构造认证信息并加入安全上下文
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // 放行请求
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 若 token 无效或解析异常，返回 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("无效的认证令牌");
        }
    }

    /**
     * 判断是否为无需认证的公共路径
     */
    private boolean isPublicPath(String requestURI) {
        return requestURI.startsWith("/api/auth/") || requestURI.equals("/error");
    }
}
