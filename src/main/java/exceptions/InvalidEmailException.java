package exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Email inválido.");
    }
}
