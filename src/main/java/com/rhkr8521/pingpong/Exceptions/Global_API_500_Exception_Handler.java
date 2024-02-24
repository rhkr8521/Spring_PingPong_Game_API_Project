package com.rhkr8521.pingpong.Exceptions;

import com.rhkr8521.pingpong.dto.APIResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Global_API_500_Exception_Handler {

    // 201 -> API_201_Exception
    @ExceptionHandler(API_201_Exception.class)
    public ResponseEntity<APIResponse> handleAPI201Exception(API_201_Exception ex) {
        return ResponseEntity.ok(APIResponse.badRequest());
    }

    // 서버 오류 -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity.ok(APIResponse.serverError());
    }
}
