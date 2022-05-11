package com.example.regions.exception.error_code;

import org.springframework.http.HttpStatus;

public enum RegionServiceErrorCode implements ErrorCode{

    REGION_NOT_EXIST("Region doesn't exist in database", HttpStatus.BAD_REQUEST),
    PAGE_OF_REGION_NOT_EXIST("The requested regions page does not exist. Please, change "
        + "your pagination settings", HttpStatus.BAD_REQUEST);

    private final int number = ordinal() + 1;

    private final String message;

    private final HttpStatus httpStatus;

    RegionServiceErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
