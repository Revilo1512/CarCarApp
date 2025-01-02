package at.carcar.carcarbackend.User;

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
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam String name, @RequestParam String password) {
        User user = service.validateUser(name, password);
        if (user != null) {
            return ResponseEntity.ok(user); // Return the user data
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        System.out.println("Received");

        try {
            newUser = service.registerUser(newUser);
            if (newUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newUser); // Return the created user
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed. Username or email already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
