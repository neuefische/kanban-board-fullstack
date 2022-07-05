package de.neuefische.muc.kanban.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createNewUser(UserCreationData userCreationData) {
        if (!Objects.equals(userCreationData.getPassword(), userCreationData.getPasswordRepeat())) {
            throw new IllegalArgumentException("passwords do not match");
        }

        if (userCreationData.getUsername() == null || userCreationData.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is blank");
        }

        User user = new User();
        user.setUsername(userCreationData.getUsername());
        user.setPassword(passwordEncoder.encode(userCreationData.getPassword()));

        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }
}
