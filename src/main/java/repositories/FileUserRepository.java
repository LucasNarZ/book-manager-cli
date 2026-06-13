package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Email;
import domain.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements UserRepository {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File file = new File("src/main/java/repositories/data/users.json");
    private final List<User> userList;
    private long userIndex = 0;

    public FileUserRepository() {
        this.userList = loadUsers();
    }

    private List<User> loadUsers() {
        try{
            if (!file.exists()) {
                createEmptyFile();
                return new ArrayList<>();
            }

            List<FileUser> fileUsers = mapper.readValue(file, new TypeReference<List<FileUser>>() {});
            List<User> users = new ArrayList<>();

            for (FileUser fileUser : fileUsers) {
                users.add(new User(fileUser.id(), fileUser.name(), new Email(fileUser.email().value())));
            }

            return users;
        }catch (IOException e) {
            throw new RuntimeException("Error reading users file: " + e.getMessage());
        }
    }

    private void createEmptyFile() throws IOException {
        file.getParentFile().mkdirs();
        mapper.writeValue(file, new ArrayList<>());
    }

    private void saveUsers() {
        try{
            List<FileUser> fileUsers = userList.stream()
                    .map(user -> new FileUser(user.getId(), user.getName(), new FileEmail(user.getEmail().getValue())))
                    .toList();

            mapper.writeValue(file, fileUsers);
        }catch (IOException e) {
            throw new RuntimeException("Error writing users file: " + e.getMessage());
        }
    }

    public User save(String name, Email email){
        User user = new User(userIndex, name, email);
        userList.add(user);
        userIndex++;

        saveUsers();

        return user;
    }

    public Optional<User> findById(long id){
        return userList.stream().filter(user -> user.getId() == id).findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    public void deleteById(long id) {
        userList.removeIf(user -> user.getId() == id);
        saveUsers();
    }

    public boolean existsByEmail(Email email){
        return userList.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private record FileUser(long id, String name, FileEmail email) {
    }

    private record FileEmail(String value) {
    }
}
