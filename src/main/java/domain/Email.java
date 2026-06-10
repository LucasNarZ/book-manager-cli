package domain;

public class Email {
    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }

        if (!isValid(value)) {
            throw new IllegalArgumentException("Email inválido.");
        }

        this.value = value.trim().toLowerCase();
    }

    public String getValue() {
        return value;
    }

    private boolean isValid(String value) {
        return value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Email email)) {
            return false;
        }

        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}