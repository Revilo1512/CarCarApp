package at.carcar.carcarbackend.User;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    public User validateUser(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);

        System.out.println(email);
        System.out.println(password);
        System.out.println("Is User present? " + user.isPresent());
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return null;
    }

    // Register user
    public User registerUser(User user) {

        if (userRepository.existsUserByEmail(user.getEmail())) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = new User(user.getName(), user.getEmail(), hashedPassword);

        return userRepository.save(newUser);
    }

    @Transactional
    public User modifyUser(long userID, String name, String email, String password) {
        User user = userRepository.findUserById(userID).orElseThrow(() -> new IllegalStateException(
                "student with ID: " + userID + " does not exist!"
        ));

        if (name != null && !name.isEmpty() && !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(user.getEmail(), email)) {
            if (userRepository.findUserByEmail(email).isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return user;
    }
}
