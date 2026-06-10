package services;

import domain.Email;
import domain.User;
import exceptions.InvalidEmailException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import exceptions.ValidationException;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email) throws ValidationException, UserAlreadyExistsException, InvalidEmailException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Nome é obrigatório.");
        }

        Email userEmail = new Email(email);

        if (userRepository.existsByEmail(userEmail)) {
            throw new UserAlreadyExistsException();
        }

        return userRepository.save(name, userEmail);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUser(long id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException();
        }

        userRepository.deleteById(id);
    }
}