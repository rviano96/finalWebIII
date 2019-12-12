package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class InvalidTaskException extends Exception {
    public InvalidTaskException() {
    }

    public InvalidTaskException(String message) {
        super(message);
    }

    public InvalidTaskException(Throwable cause) {
        super(cause);
    }

    public InvalidTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
