package at.carcar.carcarbackend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
