package com.example.regions.exception;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(value = ServiceException.class)
  public ResponseEntity<ServiceException> serviceExceptionHandler(ServiceException exception) {
    return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(exception);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ServiceException> generalHandler(Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(
        error -> errors.put(error.getField(), error.getDefaultMessage())
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> methodConstraintViolationExceptionHandler(
      ConstraintViolationException e) {
    Map<String, String> errors = new HashMap<>();
    e.getConstraintViolations().forEach(
        error -> {
          errors.put("message", error.getMessage());
          errors.put("path", (error.getPropertyPath()).toString());
        }
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(value = {ServletException.class})
  public ResponseEntity<ServiceException> clientErrorHandler(ServletException exception) {
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
  }

  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ServiceException> methodArgumentHandler(
      MethodArgumentTypeMismatchException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }
}
