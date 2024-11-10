package org.hepi.hepi_sv.auth.exception;

import org.hepi.hepi_sv.common.errorHandler.CustomErrorCode;
import org.hepi.hepi_sv.common.errorHandler.CustomException;

public class TokenException extends CustomException {

    public TokenException(CustomErrorCode errorCode) {
        super(errorCode);
    }
}
