package exceptions;

public class LoanAlreadyExpiredException extends RuntimeException {
    public LoanAlreadyExpiredException() {
        super("Data já vencida.");
    }
}
