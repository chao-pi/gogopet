package com.backend.config;

import com.backend.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 作用：
 * 禁用了 CSRF 和 session（改用 JWT 模式）
 * 配置了允许跨域访问的规则
 * 设置了哪些接口不需要登录（如 /api/auth/**）
 * 添加了 JWT 认证过滤器，接管登录状态校验
 */
@Configuration // 声明这是一个配置类
@EnableWebSecurity // 启用 Spring Security 的 Web 安全支持
public class SecurityConfig {

    // 注入自定义的 JWT 认证过滤器（通过构造器注入，更推荐）
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    // 注入自定义的UserDetailsService
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, 
                         UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    // 配置安全过滤链（SecurityFilterChain）
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 开启 CORS 跨域支持，并指定配置源
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用 CSRF（通常在使用 JWT 时禁用，因为不依赖 Cookie）
                .csrf(AbstractHttpConfigurer::disable)
                // 使用无状态会话，不通过 session 保存用户状态（适合 JWT 模式）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置请求授权策略
                .authorizeHttpRequests(auth -> auth
                        // 放行认证接口、错误页面和上传文件
                        .requestMatchers("/api/auth/**", "/error", "/test", "/uploads/**", "/api/chat/**", "/ws/**").permitAll()
                        // 其他请求都需要认证
                        .anyRequest().authenticated()
                )
                // 将自定义的 JWT 认证过滤器添加到 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 构建并返回过滤链配置
        return http.build();
    }

    // 配置认证管理器，用于处理用户认证逻辑
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    // 配置密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 配置认证提供者
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 配置跨域请求支持
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的前端地址（开发时可设为前端 dev 服务器）
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // 允许的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(List.of("*"));
        // 允许携带 cookie（用于前后端协作时的认证）
        configuration.setAllowCredentials(true);

        // 注册配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
