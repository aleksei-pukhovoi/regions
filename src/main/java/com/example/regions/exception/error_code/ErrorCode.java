package com.example.regions.exception.error_code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    int getNumber();

    String getMessage();

    HttpStatus getHttpStatus();
}
