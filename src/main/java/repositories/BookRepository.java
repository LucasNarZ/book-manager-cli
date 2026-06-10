package repositories;

import domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    public Book save(String title, String author);

    public Optional<Book> findById(long id);

    public List<Book> findAll();

    public void deleteById(long id);

    public boolean existsByTitleAndAuthor(String title, String author);

    public List<Book> findByTitle(String title);
}
