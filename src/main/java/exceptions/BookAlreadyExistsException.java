package exceptions;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super("Livro com essas informações já existe.");
    }
}
