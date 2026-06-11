package repositories;

import domain.Loan;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository {
    Loan create(long userId, long bookId, LocalDate expirationDate);

    boolean deleteByBookId(long bookId);

    List<Loan> findActiveLoans();

    List<Loan> findLoans();
}
