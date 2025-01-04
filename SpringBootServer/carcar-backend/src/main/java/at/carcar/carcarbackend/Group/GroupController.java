package at.carcar.carcarbackend.Group;
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
    public ResponseEntity<Group> getGroupById(@PathVariable int groupID) {
        Optional<Group> group = service.findGroupById(groupID);
        if (group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(group.get());
    }

    // Create Group
    @PostMapping("/createGroup/{userID}")
    public ResponseEntity<?> addGroup(@PathVariable Long userID, @RequestParam String groupName) {

        Group newGroup;
        try {
            newGroup = service.createGroup(userID, groupName);
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
    @DeleteMapping("/deleteGroup/{groupID}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupID) {
        service.deleteGroup(groupID);
        return ResponseEntity.status(HttpStatus.OK).body("Group successfully deleted!");
    }

    // Add Car to Group
    
}

