package com.enrsolidr.energyanalysis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Already paid this year")
public class AlreadyPaidException extends Exception {
    public AlreadyPaidException() {
    }

    public AlreadyPaidException(String message) {
        super(message);
    }

    public AlreadyPaidException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyPaidException(Throwable cause) {
        super(cause);
    }

    public AlreadyPaidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
