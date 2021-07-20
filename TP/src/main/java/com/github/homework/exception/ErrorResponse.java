package com.github.homework.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String field;
    private String objectName;
    private String code;
    private String defaultMessage;
    private String rejectedValue;

    @Builder
    public ErrorResponse(String field, String objectName, String code, String defaultMessage,
        String rejectedValue) {
        this.field = field;
        this.objectName = objectName;
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.rejectedValue = rejectedValue;
    }
}
