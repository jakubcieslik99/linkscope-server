package com.jakubcieslik.linkscopeserver.config;

import com.jakubcieslik.linkscopeserver.common.AuthEntryPoint;
import com.jakubcieslik.linkscopeserver.filter.JWTFilter;
import com.jakubcieslik.linkscopeserver.filter.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthEntryPoint authEntryPoint;
  private final JWTProvider authProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .exceptionHandling().authenticationEntryPoint(authEntryPoint)
        .and()
        .addFilterBefore(new JWTFilter(authProvider), BasicAuthenticationFilter.class)
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/auth/refresh", "/auth/logout").permitAll()
            .anyRequest().authenticated()
        );

    return httpSecurity.build();
  }
}
