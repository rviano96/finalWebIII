package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class ListDoesNotExistException extends Exception {
    public ListDoesNotExistException() {
    }

    public ListDoesNotExistException(String message) {
        super(message);
    }

    public ListDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public ListDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
