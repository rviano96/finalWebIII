package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class ListAlreadyExistsException extends Exception {
    public ListAlreadyExistsException() {
    }

    public ListAlreadyExistsException(String message) {
        super(message);
    }

    public ListAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ListAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
