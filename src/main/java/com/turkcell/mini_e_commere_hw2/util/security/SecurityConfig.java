package com.turkcell.mini_e_commere_hw2.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((req) -> req
                    .requestMatchers("/api/v1/products").authenticated()
                    .requestMatchers("/api/v1/users/register").permitAll()
                    .requestMatchers("/api/v1/users/login").permitAll()
                    .requestMatchers("/api/v1/carts/**").authenticated()
                    .requestMatchers("/api/v1/orders/**").authenticated()
                    .requestMatchers("/api/v1/categories/**").permitAll()
                    .requestMatchers("/api/v1/sub-categories/**").permitAll()
                    .requestMatchers("/api/v1/products/**").permitAll()
                    .requestMatchers("/api/v1/users/**").permitAll()
                    .requestMatchers("/v3/api-docs/**",
                                   "/swagger-ui/**",
                                   "/swagger-ui.html",
                                   "/webjars/**").permitAll()
                    .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
