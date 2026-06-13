package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Book;
import domain.BookStatus;
import exceptions.BookNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileBookRepository implements BookRepository {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("src/main/java/repositories/data/books.json");
    private final List<Book> books;
    private long bookIndex = 0;

    public FileBookRepository() {
        this.books = loadBooks();
    }

    private List<Book> loadBooks() {
        try{
            if (!file.exists()) {
                createEmptyFile();
                return new ArrayList<>();
            }

            List<FileBook> fileBooks = mapper.readValue(file, new TypeReference<List<FileBook>>() {});
            List<Book> loadedBooks = new ArrayList<>();

            for (FileBook fileBook : fileBooks) {
                Book book = new Book(fileBook.id(), fileBook.title(), fileBook.author());

                if (fileBook.status() == BookStatus.BORROWED) {
                    book.borrow();
                }

                loadedBooks.add(book);
            }

            return loadedBooks;
        }catch (IOException e) {
            throw new RuntimeException("Error reading users file: " + e.getMessage());
        }
    }

    private void createEmptyFile() throws IOException {
        file.getParentFile().mkdirs();
        mapper.writeValue(file, new ArrayList<>());
    }

    private void saveBooks() {
        try{
            List<FileBook> fileBooks = books.stream()
                    .map(book -> new FileBook(book.getId(), book.getTitle(), book.getAuthor(), book.getStatus()))
                    .toList();

            mapper.writeValue(file, fileBooks);
        }catch (IOException e) {
            throw new RuntimeException("Error writing users file: " + e.getMessage());
        }
    }

    public Book save(String title, String author) {
        Book book = new Book(bookIndex, title, author);

        books.add(book);
        bookIndex++;

        saveBooks();

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
        saveBooks();
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

        saveBooks();
    }

    public void giveBookBack(long bookId) {
        Optional<Book> bookToBorrow = getBookById(bookId);

        bookToBorrow.get().giveBack();

        saveBooks();
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

    private record FileBook(Long id, String title, String author, BookStatus status) {
    }
}
