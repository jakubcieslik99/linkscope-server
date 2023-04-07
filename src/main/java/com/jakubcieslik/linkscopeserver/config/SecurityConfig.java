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
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthEntryPoint authEntryPoint;
  private final JWTProvider authProvider;
  private final RateLimiterProvider rateLimiterProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.headers().contentSecurityPolicy("default-src 'self'; base-uri 'self'; font-src 'self' https: data:; form-action 'self'; frame-ancestors 'self'; img-src 'self' data:; object-src 'none'; script-src 'self'; script-src-attr 'none'; style-src 'self' https: 'unsafe-inline'; upgrade-insecure-requests");
    httpSecurity.headers().referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER);
    httpSecurity.headers().frameOptions().sameOrigin();

    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Strict-Transport-Security", "max-age=15552000; includeSubDomains"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Origin-Agent-Cluster", "?1"));

    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy", "require-corp"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy", "same-origin"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy", "same-site"));

    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("X-Permitted-Cross-Domain-Policies", "none"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("X-Content-Type-Options", "nosniff"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("X-DNS-Prefetch-Control", "off"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("X-Download-Options", "noopen"));
    httpSecurity.headers().addHeaderWriter(new StaticHeadersWriter("X-XSS-Protection", "0"));

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
