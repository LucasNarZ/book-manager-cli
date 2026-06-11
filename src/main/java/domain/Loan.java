package domain;

import exceptions.LoanAlreadyExpiredException;

import java.time.LocalDate;

public class Loan {
    private final long userId;
    private final long bookId;
    private boolean isActive;
    private LocalDate expirationDate;

    public Loan(long userId, long bookId, LocalDate expirationDate) throws LoanAlreadyExpiredException {
        this.userId = userId;
        this.bookId = bookId;

        if(LocalDate.now().isAfter(expirationDate)) {
            throw new LoanAlreadyExpiredException();
        }

    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(getExpirationDate());
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getBookId() {
        return bookId;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}