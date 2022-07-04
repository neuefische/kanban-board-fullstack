package de.neuefische.muc.kanban.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createNewUser(UserCreationDTO userCreationDTO) {
        if (!Objects.equals(userCreationDTO.getPassword(), userCreationDTO.getPasswordRepeat())) {
            throw new IllegalArgumentException("passwords do not match");
        }

        if (userCreationDTO.getUsername() == null || userCreationDTO.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is blank");
        }

        User user = new User();
        user.setUsername(userCreationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

        userRepository.save(user);
    }
}
