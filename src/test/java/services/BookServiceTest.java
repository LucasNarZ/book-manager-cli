package services;

import domain.Book;
import exceptions.BookAlreadyExistsException;
import exceptions.BookNotFoundException;
import exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldCreateBook() throws ValidationException, BookAlreadyExistsException {
        Book book = new Book(1L, "Clean Code", "Robert C. Martin");

        when(bookRepository.existsByTitleAndAuthor("Clean Code", "Robert C. Martin"))
                .thenReturn(false);

        when(bookRepository.save("Clean Code", "Robert C. Martin"))
                .thenReturn(book);

        Book createdBook = bookService.createBook("Clean Code", "Robert C. Martin");

        assertEquals("Clean Code", createdBook.getTitle());
        assertEquals("Robert C. Martin", createdBook.getAuthor());

        verify(bookRepository).existsByTitleAndAuthor("Clean Code", "Robert C. Martin");
        verify(bookRepository).save("Clean Code", "Robert C. Martin");
    }

    @Test
    void shouldThrowValidationExceptionWhenTitleIsEmpty() {
        assertThrows(
                ValidationException.class,
                () -> bookService.createBook("", "Robert C. Martin")
        );

        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldThrowValidationExceptionWhenTitleIsBlank() {
        assertThrows(
                ValidationException.class,
                () -> bookService.createBook("   ", "Robert C. Martin")
        );

        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldThrowValidationExceptionWhenTitleIsNull() {
        assertThrows(
                ValidationException.class,
                () -> bookService.createBook(null, "Robert C. Martin")
        );

        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldThrowValidationExceptionWhenAuthorIsEmpty() {
        assertThrows(
                ValidationException.class,
                () -> bookService.createBook("Clean Code", "")
        );

        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldThrowValidationExceptionWhenAuthorIsBlank() {
        assertThrows(
                ValidationException.class,
                () -> bookService.createBook("Clean Code", "   ")
        );

        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldThrowValidationExceptionWhenAuthorIsNull() {
        assertThrows(
                ValidationException.class,
                () -> bookService.createBook("Clean Code", null)
        );

        verifyNoInteractions(bookRepository);
    }

    @Test
    void shouldThrowBookAlreadyExistsException() {
        when(bookRepository.existsByTitleAndAuthor("Clean Code", "Robert C. Martin"))
                .thenReturn(true);

        assertThrows(
                BookAlreadyExistsException.class,
                () -> bookService.createBook("Clean Code", "Robert C. Martin")
        );

        verify(bookRepository).existsByTitleAndAuthor("Clean Code", "Robert C. Martin");
        verify(bookRepository, never()).save(anyString(), anyString());
    }

    @Test
    void shouldFindBookById() {
        Book book = new Book(1L, "Clean Code", "Robert C. Martin");

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(1L, foundBook.get().getId());
        assertEquals("Clean Code", foundBook.get().getTitle());
        assertEquals("Robert C. Martin", foundBook.get().getAuthor());

        verify(bookRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenBookDoesNotExist() {
        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.findById(999L);

        assertTrue(foundBook.isEmpty());

        verify(bookRepository).findById(999L);
    }

    @Test
    void shouldFindAllBooks() {
        Book cleanCode = new Book(1L, "Clean Code", "Robert C. Martin");
        Book theHobbit = new Book(2L, "The Hobbit", "J.R.R. Tolkien");

        when(bookRepository.findAll())
                .thenReturn(List.of(cleanCode, theHobbit));

        List<Book> books = bookService.findAll();

        assertEquals(2, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
        assertEquals("The Hobbit", books.get(1).getTitle());

        verify(bookRepository).findAll();
    }

    @Test
    void shouldDeleteBook() throws BookNotFoundException {
        Book book = new Book(1L, "Clean Code", "Robert C. Martin");

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository).findById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenDeletingBookThatDoesNotExist() {
        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(999L)
        );

        verify(bookRepository).findById(999L);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldFindByTitle() {
        Book cleanCode1 = new Book(1L, "Clean Code", "Robert C. Martin");
        Book cleanCode2 = new Book(2L, "Clean Code", "Robert C. Bob");

        when(bookRepository.findByTitle("Clean Code"))
                .thenReturn(List.of(cleanCode1, cleanCode2));

        List<Book> books = bookService.findByTitle("Clean Code");

        assertEquals(2, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
        assertEquals("Clean Code", books.get(1).getTitle());

        verify(bookRepository).findByTitle("Clean Code");
    }

    @Test
    void shouldReturnEmptyListIfNoBooksFound() {
        when(bookRepository.findByTitle("Code"))
                .thenReturn(List.of());

        List<Book> books = bookService.findByTitle("Code");

        assertTrue(books.isEmpty());

        verify(bookRepository).findByTitle("Code");
    }
}