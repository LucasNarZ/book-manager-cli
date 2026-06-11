package repositories;

import domain.Loan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InMemoryLoanRepository implements LoanRepository {
    private final List<Loan> loanList = new ArrayList<>();

    public Loan create(long userId, long bookId, LocalDate expirationDate) {
        Loan loan = new Loan(userId, bookId, expirationDate);
        loanList.add(loan);

        return loan;
    }

    public boolean deleteByBookId(long bookId) {
        return loanList.removeIf(loan -> loan.getBookId() == bookId);
    }

    public List<Loan> findActiveLoans() {
        return loanList.stream().filter(Loan::isActive).toList();
    }

    public List<Loan> findLoans() {
        return loanList;
    }
}
