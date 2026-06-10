package exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("Livro não encontrado.");
    }
}
