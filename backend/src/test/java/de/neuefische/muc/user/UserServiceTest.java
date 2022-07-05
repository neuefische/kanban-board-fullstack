package de.neuefische.muc.user;

import de.neuefische.muc.kanban.user.User;
import de.neuefische.muc.kanban.user.UserCreationData;
import de.neuefische.muc.kanban.user.UserRepository;
import de.neuefische.muc.kanban.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

    @Test
    void shouldCreateNewUser() {
        // Given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        UserCreationData userCreationData = new UserCreationData("testUser", "password", "password");
        UserService userService = new UserService(userRepository, passwordEncoder);

        // when
        userService.createNewUser(userCreationData);

        // then
        User expectedUser = new User();
        expectedUser.setUsername("testUser");
        expectedUser.setPassword("hashedPassword");

        Mockito.verify(userRepository).save(expectedUser);
    }

    @Test
    void shouldNotCreateNewUser_passwordsDoNotMatch() {
        // Given
        UserCreationData userCreationData = new UserCreationData("testUser", "password", "passw0rd");
        UserService userService = new UserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createNewUser(userCreationData))
                .withMessage("passwords do not match");
    }

    @Test
    void shouldNotCreateNewUser_usernameIsBlank() {
        // Given
        UserCreationData userCreationData = new UserCreationData(" ", "password", "password");
        UserService userService = new UserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createNewUser(userCreationData))
                .withMessage("username is blank");
    }

    @Test
    void shouldNotCreateNewUser_usernameIsNull() {
        // Given
        UserCreationData userCreationData = new UserCreationData(null, "password", "password");
        UserService userService = new UserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createNewUser(userCreationData))
                .withMessage("username is blank");
    }

}