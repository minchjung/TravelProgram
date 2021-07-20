package com.github.homework.exception;

import com.github.homework.exception.ErrorResponse.ErrorResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 정의되지 않은 예외는 INTERNAL_SERVER_ERROR 처리한다
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("handleException", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다. HttpMessageConverter 에서 등록한 HttpMessageConverter
     * binding 못할경우 발생 주로 @RequestBody, @RequestParam 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error("handleException", e);
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
                ErrorResponseBuilder errorResponseBuilder = ErrorResponse.builder()
                    .field(error.getField())
                    .objectName(error.getObjectName())
                    .code(error.getCode())
                    .defaultMessage(error.getDefaultMessage());
                Object rejectedValue = error.getRejectedValue();
                if (rejectedValue != null) {
                    errorResponseBuilder.rejectedValue(rejectedValue.toString());
                }
                errorResponseList.add(errorResponseBuilder.build());
            }
        );

        return new ResponseEntity<>(Collections.unmodifiableList(errorResponseList), HttpStatus.BAD_REQUEST);
    }

}