package at.carcar.carcarbackend.User;

import at.carcar.carcarbackend.security.AuthorizationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;
    private final AuthorizationService authService;
    @Autowired //dependency injection shit wir generieren den Userservice magically oder so.
    public UserController(UserService service, AuthorizationService aserv){
        this.service=service;
        authService = aserv;
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
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            User user = service.validateUser(email, password); // Ensure password is hashed and validated securely
            if (user != null) {
                session.setAttribute("userId", user.getId());

                // Set the authentication in SecurityContext
                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return ResponseEntity.ok(user); // Return user data
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = service.registerUser(user);

            if (newUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newUser); // Return the created user
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed. Username or email may already exist.");
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
                                        @RequestParam(required = false) String password) {
        try {

            if (!authService.isSameUser(userID)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot modify user");
            }

            // Authorization passed, modifying the user
            User newUser = service.modifyUser(userID, name, email, password);

            if (newUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Modification failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
