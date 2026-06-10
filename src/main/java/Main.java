import repositories.BookRepository;
import repositories.InMemoryBookRepository;
import services.BookService;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new InMemoryBookRepository();
        BookService bookService = new BookService(bookRepository);

        Menu menu = new Menu(bookService);
        menu.begin();
    }
}