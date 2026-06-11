package services;

import domain.Book;
import domain.Loan;
import domain.User;
import exceptions.LoanNotFoundException;
import exceptions.UserNotFoundException;
import repositories.BookRepository;
import repositories.LoanRepository;
import repositories.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Loan borrowBook(long userId, long bookId, LocalDate expirationDate) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<Book> book = bookRepository.findById(bookId);


        bookRepository.borrowBook(bookId);

        Loan loan = loanRepository.create(userId, bookId, expirationDate);

        return loan;
    }

    public void returnBook(long bookId) throws LoanNotFoundException {
        boolean deleted = loanRepository.deleteByBookId(bookId);

        if (!deleted) {
            throw new LoanNotFoundException();
        }
        bookRepository.giveBookBack(bookId);
    }
}
