package at.carcar.carcarbackend.Group;
import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/groups")
public class GroupController {

    private final GroupService service;

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = service.getAllGroups();
        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{groupID}")
    public ResponseEntity<?> getGroupById(@PathVariable int groupID) {
        Optional<Group> group = service.findGroupById(groupID);
        if (group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(group.get());
    }

    // Create Group
    @PostMapping("/createGroup")
    public ResponseEntity<?> addGroup(@RequestParam Long adminID, @RequestParam String groupName) {

        Group newGroup;
        try {
            newGroup = service.createGroup(adminID, groupName);
            if (newGroup != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newGroup); // Return the created Group
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group Creation failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Delete Group
    @DeleteMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(@RequestParam Long groupID) {
        service.deleteGroup(groupID);
        return ResponseEntity.status(HttpStatus.OK).body("Group successfully deleted!");
    }

    // Add Car to Group
    @PutMapping("/addCar")
    public ResponseEntity<?> addCar(@RequestParam Long groupID, @RequestParam Long carID) {
        Group group;

        try {
            group = service.addCar(groupID, carID);

            if (group != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(group);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add Car");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Remove Car from Group
    @PutMapping("/removeCar")
    public ResponseEntity<?> removeCar(@RequestParam Long groupID, @RequestParam Long carID) {
        Group group;

        try {
            group = service.removeCar(groupID, carID);

            if (group != null) {
                return ResponseEntity.status(HttpStatus.OK).body(group);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to remove Car");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Add User to Group
    @PutMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestParam Long groupID, @RequestParam Long userID) {
        Group group;

        try {
            group = service.addUser(groupID, userID);

            if (group != null) {
                return ResponseEntity.status(HttpStatus.OK).body(group);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add User to Group!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Remove User from Group
    @PutMapping("/removeUser")
    public ResponseEntity<?> removeUser(@RequestParam Long groupID, @RequestParam Long userID) {
        Group group;

        try {
            group = service.removeUser(groupID, userID);

            if (group != null) {
                return ResponseEntity.status(HttpStatus.OK).body(group);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to remove User from Group!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}

