package domain;

public class Book {
    private final Long id;
    private String title;
    private String author;
    private BookStatus status;

    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void borrow() {
        if (status == BookStatus.BORROWED) {
            throw new IllegalStateException("Livro já está emprestado");
        }

        status = BookStatus.BORROWED;
    }

    public void giveBack() {
        status = BookStatus.AVAILABLE;
    }
}