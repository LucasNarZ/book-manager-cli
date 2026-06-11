package exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException() {
        super("Emprestimo não encontrado.");
    }
}
