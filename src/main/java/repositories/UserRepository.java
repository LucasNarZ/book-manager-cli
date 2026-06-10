package repositories;

import domain.Email;
import domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public User save(String name, Email email);

    public Optional<User> findById(long id);

    public List<User> findAll();

    public void deleteById(long id);

    public boolean existsByEmail(Email email);
}
