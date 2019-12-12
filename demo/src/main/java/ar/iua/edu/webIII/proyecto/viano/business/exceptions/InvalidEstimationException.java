package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class InvalidEstimationException  extends Exception {
    public InvalidEstimationException() {
    }

    public InvalidEstimationException(String message) {
        super(message);
    }

    public InvalidEstimationException(Throwable cause) {
        super(cause);
    }

    public InvalidEstimationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEstimationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
