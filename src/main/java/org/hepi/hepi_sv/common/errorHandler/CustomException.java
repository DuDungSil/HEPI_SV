package org.hepi.hepi_sv.common.errorHandler;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final CustomErrorCode errorCode;
    private final String message;

    public CustomException(CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomException(CustomErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}