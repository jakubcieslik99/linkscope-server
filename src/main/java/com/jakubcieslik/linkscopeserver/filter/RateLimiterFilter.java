package com.jakubcieslik.linkscopeserver.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubcieslik.linkscopeserver.common.Constants;
import com.jakubcieslik.linkscopeserver.error.ErrorResDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {

  private final RateLimiterProvider rateLimiterProvider;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws IOException, ServletException {

    if (rateLimiterProvider.checkRate(request.getRemoteAddr())) {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.setContentType("application/json");
      response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResDTO(Constants.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS.value())));
      response.getWriter().flush();
    } else filterChain.doFilter(request, response);
  }
}
