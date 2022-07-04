package de.neuefische.muc.user;

import de.neuefische.muc.kanban.user.User;
import de.neuefische.muc.kanban.user.UserCreationDTO;
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

        UserCreationDTO userCreationDTO = new UserCreationDTO("testUser", "password", "password");
        UserService userService = new UserService(userRepository, passwordEncoder);

        // when
        userService.createNewUser(userCreationDTO);

        // then
        User expectedUser = new User();
        expectedUser.setUsername("testUser");
        expectedUser.setPassword("hashedPassword");

        Mockito.verify(userRepository).save(expectedUser);
    }

    @Test
    void shouldNotCreateNewUser_passwordsDoNotMatch() {
        // Given
        UserCreationDTO userCreationDTO = new UserCreationDTO("testUser", "password", "passw0rd");
        UserService userService = new UserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createNewUser(userCreationDTO))
                .withMessage("passwords do not match");
    }

    @Test
    void shouldNotCreateNewUser_usernameIsBlank() {
        // Given
        UserCreationDTO userCreationDTO = new UserCreationDTO(" ", "password", "password");
        UserService userService = new UserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createNewUser(userCreationDTO))
                .withMessage("username is blank");
    }

    @Test
    void shouldNotCreateNewUser_usernameIsNull() {
        // Given
        UserCreationDTO userCreationDTO = new UserCreationDTO(null, "password", "password");
        UserService userService = new UserService(null, null);

        // when
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.createNewUser(userCreationDTO))
                .withMessage("username is blank");
    }

}