package ui;

import domain.Book;
import domain.Loan;
import domain.User;
import services.BookService;
import services.LoanService;
import services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    public Menu(BookService bookService, UserService userService, LoanService loanService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
    }

    public void begin() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Seja bem-vindo!");

        while (running) {
            System.out.println();
            System.out.println("O que vc quer fazer?");
            System.out.println("1. Adicionar livro");
            System.out.println("2. Ver todos os livros");
            System.out.println("3. Buscar livro por id");
            System.out.println("4. Excluir livro por id");
            System.out.println("5. Buscar livros por título");
            System.out.println("6. Adicionar usuário");
            System.out.println("7. Ver todos os usuários");
            System.out.println("8. Buscar usuário por id");
            System.out.println("9. Excluir usuário por id");
            System.out.println("10. Emprestar livro");
            System.out.println("11. Devolver livro");
            System.out.println("12. Sair");

            int resposta = Integer.parseInt(scanner.nextLine());

            try {
                switch (resposta) {
                    case 1:
                        System.out.println("Digite o título:");
                        String title = scanner.nextLine();

                        System.out.println("Digite o autor:");
                        String author = scanner.nextLine();

                        Book createdBook = bookService.createBook(title, author);

                        System.out.println("Livro adicionado");
                        printBook(createdBook);
                        break;

                    case 2:
                        List<Book> books = bookService.findAll();
                        printBooks(books);
                        break;

                    case 3:
                        System.out.println("Digite o id do livro desejado:");
                        long bookId = Long.parseLong(scanner.nextLine());

                        Optional<Book> bookOptional = bookService.findById(bookId);

                        if (bookOptional.isPresent()) {
                            System.out.println("Livro encontrado:");
                            printBook(bookOptional.get());
                        } else {
                            System.out.println("Livro não encontrado");
                        }

                        break;

                    case 4:
                        System.out.println("Digite o id do livro desejado:");
                        long bookIdToDelete = Long.parseLong(scanner.nextLine());

                        bookService.deleteBook(bookIdToDelete);

                        System.out.println("Livro excluído");
                        break;

                    case 5:
                        System.out.println("Digite o título para buscar:");
                        String titleToSearch = scanner.nextLine();

                        List<Book> bookList = bookService.findByTitle(titleToSearch);

                        printBooks(bookList);
                        break;

                    case 6:
                        System.out.println("Digite o nome:");
                        String name = scanner.nextLine();

                        System.out.println("Digite o email:");
                        String email = scanner.nextLine();

                        User createdUser = userService.createUser(name, email);

                        System.out.println("Usuário adicionado");
                        printUser(createdUser);
                        break;

                    case 7:
                        List<User> users = userService.findAll();
                        printUsers(users);
                        break;

                    case 8:
                        System.out.println("Digite o id do usuário desejado:");
                        long userId = Long.parseLong(scanner.nextLine());

                        Optional<User> userOptional = userService.findById(userId);

                        if (userOptional.isPresent()) {
                            System.out.println("Usuário encontrado:");
                            printUser(userOptional.get());
                        } else {
                            System.out.println("Usuário não encontrado");
                        }

                        break;

                    case 9:
                        System.out.println("Digite o id do usuário desejado:");
                        long userIdToDelete = Long.parseLong(scanner.nextLine());

                        userService.deleteUser(userIdToDelete);

                        System.out.println("Usuário excluído");
                        break;

                    case 10:
                        System.out.println("Digite o id do usuário:");
                        long userIdToBorrow = Long.parseLong(scanner.nextLine());

                        System.out.println("Digite o id do livro:");
                        long bookIdToBorrow = Long.parseLong(scanner.nextLine());

                        System.out.println("Digite a data de vencimento (AAAA-MM-DD):");
                        LocalDate expirationDate = LocalDate.parse(scanner.nextLine());

                        Loan loan = loanService.borrowBook(userIdToBorrow, bookIdToBorrow, expirationDate);

                        System.out.println("Livro emprestado");
                        printLoan(loan);
                        break;

                    case 11:
                        System.out.println("Digite o id do livro:");
                        long bookIdToReturn = Long.parseLong(scanner.nextLine());

                        loanService.returnBook(bookIdToReturn);

                        System.out.println("Livro devolvido");
                        break;

                    case 12:
                        running = false;
                        break;

                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } catch (Exception exception) {
                System.out.println("Erro: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    private void printBook(Book book) {
        System.out.println(
                "id: %d, titulo: %s, autor: %s".formatted(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor()
                )
        );
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        for (Book book : books) {
            printBook(book);
        }
    }

    private void printUser(User user) {
        System.out.println(
                "id: %d, nome: %s, email: %s".formatted(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )
        );
    }

    private void printUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário encontrado.");
            return;
        }

        for (User user : users) {
            printUser(user);
        }
    }

    private void printLoan(Loan loan) {
        System.out.println(
                "id do usuário: %d, id do livro: %d".formatted(
                        loan.getUserId(),
                        loan.getBookId()
                )
        );
    }
}
