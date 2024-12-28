package at.carcar.carcarbackend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController {


    private final UserService service;
    @Autowired //dependency injection shit wir generieren den Userservice magically oder so
    public UserController(UserService service){
        this.service=service;
    }

    // go to localhost:8080 / api/user
    @GetMapping
    public List<User> getUsers(){
        return List.of(new User(1,"a","as"));
    }

}
