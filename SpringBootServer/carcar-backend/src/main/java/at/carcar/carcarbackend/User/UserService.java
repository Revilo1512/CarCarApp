package at.carcar.carcarbackend.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Validate user for login
    public User validateUser(String name, String password) {
        Optional<User> user = userRepository.findByName(name);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return null;
    }

    // Register user
    public User registerUser(User user) {

        if (userRepository.existsByName(user.getName()) || userRepository.existsByEmail(user.getEmail())) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = new User();
        return userRepository.save(newUser);
    }
}
