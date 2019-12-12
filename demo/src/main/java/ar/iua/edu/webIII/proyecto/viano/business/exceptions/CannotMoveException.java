package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class CannotMoveException extends Exception {
    public CannotMoveException() {
    }

    public CannotMoveException(String message) {
        super(message);
    }

    public CannotMoveException(Throwable cause) {
        super(cause);
    }

    public CannotMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotMoveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
