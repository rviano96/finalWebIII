package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class InvalidSprintException extends Exception {
    public InvalidSprintException() {
    }

    public InvalidSprintException(String message) {
        super(message);
    }

    public InvalidSprintException(Throwable cause) {
        super(cause);
    }

    public InvalidSprintException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSprintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
