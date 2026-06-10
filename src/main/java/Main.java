import repositories.BookRepository;
import repositories.InMemoryBookRepository;
import repositories.InMemoryUserRepository;
import repositories.UserRepository;
import services.BookService;
import services.UserService;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new InMemoryBookRepository();
        BookService bookService = new BookService(bookRepository);

        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        Menu menu = new Menu(bookService, userService);
        menu.begin();
    }
}