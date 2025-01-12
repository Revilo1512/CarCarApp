package at.carcar.carcarbackend.User;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;
    @Autowired //dependency injection shit wir generieren den Userservice magically oder so.
    public UserController(UserService service){
        this.service=service;
    }

    // go to localhost:8080/user
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> user = service.getAllUsers();
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userID}")
    public ResponseEntity<User> getUsersById(@PathVariable long userID){
        Optional<User> user = service.findUserById(userID);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user.get());
    }
    // Login
    @GetMapping("/login")
    public ResponseEntity<?> getUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = service.validateUser(email, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(user); // Return the user data
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpSession session) {

        User newUser;
        try {
            newUser = service.registerUser(user);
            if (newUser != null) {
                session.setAttribute("userId", user.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(newUser); // Return the created user
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed. Username or email already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Modify
    @PutMapping("/modifyUser/{userID}")
    public ResponseEntity<?> modifyUser(@PathVariable long userID,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String password,
                                        HttpSession session) {
        User newUser;
        try {
            // Ensure session userId is not null and matches the userID in the request
            Object sessionUserId = session.getAttribute("userId");

            if (sessionUserId == null) {
                throw new IllegalArgumentException("User is not logged in");
            }

            // Compare the session userId (which is likely a String or Long) to the userID from the path
            if (Long.parseLong(sessionUserId.toString()) != userID) {
                throw new IllegalArgumentException("Cannot modify user");
            }

            // If authorization passed, proceed with modifying the user
            newUser = service.modifyUser(userID, name, email, password);

            if (newUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Modification failed");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
