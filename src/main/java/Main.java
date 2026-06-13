import repositories.*;
import services.BookService;
import services.LoanService;
import services.UserService;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new FileBookRepository();
        BookService bookService = new BookService(bookRepository);

        UserRepository userRepository = new FileUserRepository();
        UserService userService = new UserService(userRepository);

        LoanRepository loanRepository = new FileLoanRepository();
        LoanService loanService = new LoanService(loanRepository, bookRepository, userRepository);

        Menu menu = new Menu(bookService, userService, loanService);
        menu.begin();
    }
}
