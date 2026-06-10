package domain;

public class User {
    private long id;
    private String name;
    private Email email;

    public User(long id, String name, Email email) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
