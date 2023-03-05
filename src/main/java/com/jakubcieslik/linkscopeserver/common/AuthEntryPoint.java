package com.jakubcieslik.linkscopeserver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubcieslik.linkscopeserver.error.ErrorResDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
  private String message = Constants.UNAUTHORIZED;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException, ServletException {
    ErrorResDTO errorResDTO = new ErrorResDTO(message, httpStatus.value());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(httpStatus.value());

    OutputStream responseStream = response.getOutputStream();
    objectMapper.writeValue(responseStream, errorResDTO);
    responseStream.flush();
  }

  public void setHttpStatusError(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
