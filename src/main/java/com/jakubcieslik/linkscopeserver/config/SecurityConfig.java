package com.jakubcieslik.linkscopeserver.config;

import com.jakubcieslik.linkscopeserver.common.AuthEntryPoint;
import com.jakubcieslik.linkscopeserver.filter.JWTFilter;
import com.jakubcieslik.linkscopeserver.filter.JWTProvider;
import com.jakubcieslik.linkscopeserver.filter.RateLimiterFilter;
import com.jakubcieslik.linkscopeserver.filter.RateLimiterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthEntryPoint authEntryPoint;
  private final JWTProvider authProvider;
  private final RateLimiterProvider rateLimiterProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .headers()
        .frameOptions().sameOrigin()
        .contentSecurityPolicy("default-src 'self'; base-uri 'self'; font-src 'self'; script-src 'self'")
        .and()
        .httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(15552000) //problem
        .and()
        .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER);

    httpSecurity
        .exceptionHandling().authenticationEntryPoint(authEntryPoint)
        .and()
        .addFilterBefore(new JWTFilter(authProvider), BasicAuthenticationFilter.class)
        .addFilterBefore(new RateLimiterFilter(rateLimiterProvider), JWTFilter.class)
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/auth/refreshToken", "/auth/logout", "/profile/getProfile/*").permitAll()
            .anyRequest().authenticated()
        );

    return httpSecurity.build();
  }
}
