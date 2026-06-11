package repositories;

import domain.Book;
import exceptions.BookNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBookRepository implements BookRepository {
    private final List<Book> books = new ArrayList<>();
    private long bookIndex = 0;

    public Book save(String title, String author) {
        Book book = new Book(bookIndex, title, author);

        books.add(book);
        bookIndex++;

        return book;
    }

    public Optional<Book> findById(long id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public void deleteById(long id) {
        books.removeIf(book -> book.getId() == id);
    }

    public boolean existsByTitleAndAuthor(String title, String author) {
        return books.stream().anyMatch(book -> book.getTitle().equals(title) && book.getAuthor().equals(author));
    }

    public List<Book> findByTitle(String title) {
        return books.stream().filter(book -> book.getTitle().equals(title)).toList();
    }

    public void borrowBook(long bookId) throws BookNotFoundException {
        Optional<Book> bookToBorrow = getBookById(bookId);

        bookToBorrow.get().borrow();
    }

    public void giveBookBack(long bookId) {
        Optional<Book> bookToBorrow = getBookById(bookId);

        bookToBorrow.get().giveBack();
    }

    private Optional<Book> getBookById(long bookId) {
        Optional<Book> bookToBorrow = books.stream()
                .filter(book -> book.getId() == bookId)
                .findFirst();

        if (bookToBorrow.isEmpty()) {
            throw  new BookNotFoundException();
        }
        return bookToBorrow;
    }

}