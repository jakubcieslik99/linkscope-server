package com.jakubcieslik.linkscopeserver.error;

import com.jakubcieslik.linkscopeserver.common.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorHandler {

  @ExceptionHandler(value = {AppError.class})
  @ResponseBody
  public ResponseEntity<ErrorResDTO> handleAppError(AppError e) {
    return ResponseEntity.status(e.getHttpStatus()).body(
        new ErrorResDTO(e.getMessage(), e.getHttpStatus().value())
    );
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseBody
  public ResponseEntity<ErrorResDTO> handleArgumentNotValidException(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
        new ErrorResDTO(Constants.UNPROCESSABLE_ENTITY, HttpStatus.UNPROCESSABLE_ENTITY.value())
    );
  }

  @ExceptionHandler(value = {HttpMessageNotReadableException.class})
  @ResponseBody
  public ResponseEntity<ErrorResDTO> handleMessageNotReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResDTO(Constants.BAD_REQUEST, HttpStatus.BAD_REQUEST.value())
    );
  }

  @ExceptionHandler(value = {Exception.class})
  @ResponseBody
  public ResponseEntity<ErrorResDTO> handleRuntimeException(Exception e) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResDTO(Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value())
    );
  }
}
