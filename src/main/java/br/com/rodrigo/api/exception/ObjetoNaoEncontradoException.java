package br.com.rodrigo.api.exception;

public class ObjetoNaoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ObjetoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjetoNaoEncontradoException(String message) {
        super(message);
    }
}
