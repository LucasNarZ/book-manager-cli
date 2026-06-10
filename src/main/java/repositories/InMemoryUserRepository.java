package repositories;

import domain.Email;
import domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final ArrayList<User> userList = new ArrayList<>();
    private long userIndex = 0;


    public User save(String name, Email email){
        User user = new User(userIndex, name, email);
        userList.add(user);
        userIndex++;

        return user;

    }

    public Optional<User> findById(long id){
        return userList.stream().filter(user -> user.getId() == id).findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    public void deleteById(long id) {
        userList.remove(id);
    }

    public boolean existsByEmail(Email email){
        return userList.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
