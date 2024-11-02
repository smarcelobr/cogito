package br.nom.figueiredo.sergio.cogito.service;

public class CogitoServiceException extends RuntimeException {
    public CogitoServiceException() {
    }

    public CogitoServiceException(String message) {
        super(message);
    }

    public CogitoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CogitoServiceException(Throwable cause) {
        super(cause);
    }

    public CogitoServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
