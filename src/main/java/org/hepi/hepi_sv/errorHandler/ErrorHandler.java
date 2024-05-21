package org.hepi.hepi_sv.errorHandler;

public class ErrorHandler extends RuntimeException {
    public ErrorHandler(String message) {
        super(message);
    }
}
