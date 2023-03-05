package com.jakubcieslik.linkscopeserver.error;

import org.springframework.http.HttpStatus;

public class AppError extends RuntimeException {

  private final HttpStatus httpStatus;

  public AppError(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
