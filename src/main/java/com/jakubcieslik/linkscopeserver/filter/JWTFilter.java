package com.jakubcieslik.linkscopeserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTProvider jwtProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws IOException, ServletException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null) {
      String[] tokenParts = header.split(" ");

      if (tokenParts.length == 2 && "Bearer".equals(tokenParts[0])) {
        try {
          SecurityContextHolder.getContext().setAuthentication(jwtProvider.validateAccessToken(tokenParts[1]));
        } catch (RuntimeException e) {
          SecurityContextHolder.clearContext();
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
