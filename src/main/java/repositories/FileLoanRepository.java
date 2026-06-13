package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Loan;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileLoanRepository implements LoanRepository {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final File file = new File("src/main/java/repositories/data/loans.json");
    private final List<Loan> loanList;

    public FileLoanRepository() {
        this.loanList = loadLoans();
    }

    private List<Loan> loadLoans() {
        try{
            if (!file.exists()) {
                createEmptyFile();
                return new ArrayList<>();
            }

            List<FileLoan> fileLoans = mapper.readValue(file, new TypeReference<List<FileLoan>>() {});
            List<Loan> loans = new ArrayList<>();

            for (FileLoan fileLoan : fileLoans) {
                loans.add(new Loan(fileLoan.userId(), fileLoan.bookId(), fileLoan.expirationDate()));
            }

            return loans;
        }catch (IOException e) {
            throw new RuntimeException("Error reading loans file: " + e.getMessage());
        }
    }

    private void createEmptyFile() throws IOException {
        file.getParentFile().mkdirs();
        mapper.writeValue(file, new ArrayList<>());
    }

    private void saveLoans() {
        try{
            List<FileLoan> fileLoans = loanList.stream()
                    .map(loan -> new FileLoan(loan.getUserId(), loan.getBookId(), loan.getExpirationDate(), loan.isExpired(), loan.isActive()))
                    .toList();

            mapper.writeValue(file, fileLoans);
        }catch (IOException e) {
            throw new RuntimeException("Error writing loans file: " + e.getMessage());
        }
    }

    public Loan create(long userId, long bookId, LocalDate expirationDate) {
        Loan loan = new Loan(userId, bookId, expirationDate);
        loanList.add(loan);

        saveLoans();

        return loan;
    }

    public boolean deleteByBookId(long bookId) {
        boolean deleted = loanList.removeIf(loan -> loan.getBookId() == bookId);
        saveLoans();

        return deleted;
    }

    public List<Loan> findActiveLoans() {
        return loanList.stream().filter(Loan::isActive).toList();
    }

    public List<Loan> findLoans() {
        return loanList;
    }

    private record FileLoan(long userId, long bookId, LocalDate expirationDate, boolean expired, boolean active) {
    }
}
