package br.com.rodrigo.api.exception;

public class ViolocaoIntegridadeDadosException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ViolocaoIntegridadeDadosException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViolocaoIntegridadeDadosException(String message) {
        super(message);
    }
}