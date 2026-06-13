package services;

import domain.Book;
import domain.Email;
import domain.Loan;
import domain.User;
import exceptions.BookNotFoundException;
import exceptions.LoanNotFoundException;
import exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.BookRepository;
import repositories.LoanRepository;
import repositories.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldBorrowBook() {
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        User user = new User(1L, "Lucas", new Email("lucas@email.com"));
        Book book = new Book(1L, "Clean Code", "Robert C. Martin");
        Loan loan = new Loan(1L, 1L, expirationDate);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.create(1L, 1L, expirationDate)).thenReturn(loan);

        Loan result = loanService.borrowBook(1L, 1L, expirationDate);

        assertEquals(loan, result);

        verify(userRepository).findById(1L);
        verify(bookRepository).findById(1L);
        verify(bookRepository).borrowBook(1L);
        verify(loanRepository).create(1L, 1L, expirationDate);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenBorrowingBookForNonExistingUser() {
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> loanService.borrowBook(1L, 1L, expirationDate)
        );

        verify(userRepository).findById(1L);
        verifyNoInteractions(bookRepository, loanRepository);
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenBorrowingNonExistingBook() {
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        User user = new User(1L, "Lucas", new Email("lucas@email.com"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> loanService.borrowBook(1L, 1L, expirationDate)
        );

        verify(userRepository).findById(1L);
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).borrowBook(anyLong());
        verifyNoInteractions(loanRepository);
    }

    @Test
    void shouldReturnBook() {
        when(loanRepository.deleteByBookId(1L)).thenReturn(true);

        loanService.returnBook(1L);

        verify(loanRepository).deleteByBookId(1L);
        verify(bookRepository).giveBookBack(1L);
    }

    @Test
    void shouldThrowLoanNotFoundExceptionWhenReturningBookWithoutLoan() {
        when(loanRepository.deleteByBookId(1L)).thenReturn(false);

        assertThrows(
                LoanNotFoundException.class,
                () -> loanService.returnBook(1L)
        );

        verify(loanRepository).deleteByBookId(1L);
        verify(bookRepository, never()).giveBookBack(anyLong());
    }
}
