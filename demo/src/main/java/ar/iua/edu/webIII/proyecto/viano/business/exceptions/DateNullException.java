package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class DateNullException extends Exception {
    public DateNullException() {
    }

    public DateNullException(String message) {
        super(message);
    }

    public DateNullException(Throwable cause) {
        super(cause);
    }

    public DateNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
