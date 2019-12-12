package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class InvalidPriorityException extends Exception {
    public InvalidPriorityException() {
    }

    public InvalidPriorityException(String message) {
        super(message);
    }

    public InvalidPriorityException(Throwable cause) {
        super(cause);
    }

    public InvalidPriorityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPriorityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
