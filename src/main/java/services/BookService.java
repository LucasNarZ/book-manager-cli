package services;

import domain.Book;
import exceptions.BookAlreadyExistsException;
import exceptions.BookNotFoundException;
import exceptions.ValidationException;
import repositories.BookRepository;

import java.util.List;
import java.util.Optional;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(String title, String author) throws ValidationException, BookAlreadyExistsException {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Título é obrigatório.");
        }

        if (author == null || author.isBlank()) {
            throw new ValidationException("Autor é obrigatório.");
        }

        if(this.bookRepository.existsByTitleAndAuthor(title, author)) {
            throw new BookAlreadyExistsException();
        }

        return bookRepository.save(title, author);
    }

    public Optional<Book> findById(long id) {
        return this.bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void deleteBook(long id) throws BookNotFoundException {
        if(this.bookRepository.findById(id).isEmpty()) {
            throw new BookNotFoundException();
        }

        this.bookRepository.deleteById(id);
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}
