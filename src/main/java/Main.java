import repositories.BookRepository;
import repositories.InMemoryBookRepository;
import repositories.InMemoryLoanRepository;
import repositories.InMemoryUserRepository;
import repositories.LoanRepository;
import repositories.UserRepository;
import services.BookService;
import services.LoanService;
import services.UserService;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new InMemoryBookRepository();
        BookService bookService = new BookService(bookRepository);

        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        LoanRepository loanRepository = new InMemoryLoanRepository();
        LoanService loanService = new LoanService(loanRepository, bookRepository, userRepository);

        Menu menu = new Menu(bookService, userService, loanService);
        menu.begin();
    }
}
