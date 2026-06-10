package ui;

import domain.Book;
import services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private final BookService bookService;

    public Menu(BookService bookService) {
        this.bookService = bookService;
    }

    public void begin() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Seja bem-vindo!");
        while (running) {
            System.out.println("O que vc quer fazer?");
            System.out.println("1. Adicionar livro");
            System.out.println("2. Ver todos os livros");
            System.out.println("3. Buscar livro por id");
            System.out.println("4. Excluir livro por id");
            System.out.println("5. Buscar livros por título");
            System.out.println("10. Sair");

            int resposta = scanner.nextInt();
            scanner.nextLine();

            switch (resposta) {
                case 1:
                    System.out.println("Digite o título:");
                    String title = scanner.nextLine();
                    System.out.println("Digite o autor:");
                    String author = scanner.nextLine();
                    Book createdBook = this.bookService.createBook(title, author);

                    System.out.println("Livro adicionado");
                    printBook(createdBook);
                    break;
                case 2:
                    List<Book> books = this.bookService.findAll();
                    printBooks(books);

                    break;
                case 3:
                    System.out.println("Digite o id do livro desajado:");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Optional<Book> bookOptional = this.bookService.findById(id);

                    if(bookOptional.isPresent()) {
                        Book book = bookOptional.get();
                        System.out.println("Livro encontrado:");
                        printBook(book);
                    }else {
                        System.out.println("Livro não encontrado");
                    }

                    break;
                case 4:
                    System.out.println("Digite o id do livro desajado:");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine();

                    this.bookService.deleteBook(idToDelete);

                    System.out.println("Livro excluído");
                    break;
                case 5:
                    System.out.println("Digite o título para buscar:");
                    String titleToSearch = scanner.nextLine();

                    List<Book> bookList = bookService.findByTitle(titleToSearch);

                    printBooks(bookList);

                case 10:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }

        scanner.close();
    }

    private void printBook(Book book) {
        System.out.println("id: " + book.getId() + ", titulo: " +  book.getTitle() + ", autor: " + book.getAuthor());
    }

    private void printBooks(List<Book> books) {
        for(Book book: books) {
            printBook(book);
        }
    }
}
