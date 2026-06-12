package services;

import domain.Email;
import domain.User;
import exceptions.InvalidEmailException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        String name = "Lucas";
        String email = "lucas@email.com";
        Email userEmail = new Email(email);
        User user = new User(1L, name, userEmail);

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);
        when(userRepository.save(name, userEmail)).thenReturn(user);

        User result = userService.createUser(name, email);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository).save(name, userEmail);
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsNull() {
        assertThrows(
                ValidationException.class,
                () -> userService.createUser(null, "lucas@email.com")
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowValidationExceptionWhenNameIsBlank() {
        assertThrows(
                ValidationException.class,
                () -> userService.createUser("   ", "lucas@email.com")
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailAlreadyExists() throws Exception {
        String email = "lucas@email.com";
        Email userEmail = new Email(email);

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser("Lucas", email)
        );

        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository, never()).save(anyString(), any(Email.class));
    }

    @Test
    void shouldThrowInvalidEmailExceptionWhenEmailIsInvalid() {
        assertThrows(
                InvalidEmailException.class,
                () -> userService.createUser("Lucas", "invalid-email")
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldFindUserById() throws Exception {
        User user = new User(1L, "Lucas", new Email("lucas@email.com"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        User user1 = new User(1L, "Lucas", new Email("lucas@email.com"));
        User user2 = new User(2L, "Ana", new Email("ana@email.com"));
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals(users, result);

        verify(userRepository).findAll();
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        User user = new User(1L, "Lucas", new Email("lucas@email.com"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenDeletingNonExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(1L)
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}