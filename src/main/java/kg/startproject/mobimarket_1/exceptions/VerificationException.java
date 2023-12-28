package kg.startproject.mobimarket_1.exceptions;

public class VerificationException extends RuntimeException {

    public VerificationException() {
        super();
    }

    public VerificationException(String message) {
        super(message);
    }

    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationException(Throwable cause) {
        super(cause);
    }
}
