package ar.iua.edu.webIII.proyecto.viano.business.exceptions;

public class AssignNotAllowedException  extends Exception {
    public AssignNotAllowedException() {
    }

    public AssignNotAllowedException(String message) {
        super(message);
    }

    public AssignNotAllowedException(Throwable cause) {
        super(cause);
    }

    public AssignNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssignNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
