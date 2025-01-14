package at.carcar.carcarbackend.Group;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/groups")
public class GroupController {

    private final GroupService service;
    private final AuthorizationService authService;

    @Autowired
    public GroupController(GroupService service, AuthorizationService aserv) {
        this.service = service;
        authService = aserv;
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
        try{
            // Maybe make necassery to be admin or user in group
            Group group = service.findGroupById(groupID).get();
            return ResponseEntity.ok(group);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Create Group
    @PostMapping("/createGroup")
    public ResponseEntity<?> addGroup(@RequestParam String groupName) {

        Group newGroup;
        try {
            newGroup = service.createGroup(authService.getAuthenticatedUserId(), groupName);
            if (newGroup != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newGroup); // Return the created Group
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group Creation failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(@RequestParam Long groupID) {
        try {

            Group group = service.findGroupById(groupID).get();

            if (!authService.isAdminOfGroup(group)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this group");
            }

            service.deleteGroup(groupID, groupID);
            return ResponseEntity.status(HttpStatus.OK).body("Group successfully deleted!");
        } catch (Exception e) {
            // Catch any unexpected errors and respond with an appropriate message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/addCar")
    public ResponseEntity<?> addCar(@RequestParam Long groupID, @RequestParam Long carID) {
        try {
            Group group = service.findGroupById(groupID).get();

            if ((!authService.isAdminOfGroup(group))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add cars to this group");
            }

            Group updatedGroup = service.addCar(groupID, carID);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Remove Car from Group
    @PutMapping("/removeCar")
    public ResponseEntity<?> removeCar(@RequestParam Long groupID, @RequestParam Long carID) {
        try {
            Group group = service.findGroupById(groupID).get();

            if ((!authService.isAdminOfGroup(group))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to remove cars from this group");
            }

            Group updatedGroup = service.removeCar(groupID, carID);
            return ResponseEntity.status(HttpStatus.OK).body(updatedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Add User to Group
    @PutMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestParam Long groupID, @RequestParam Long userID) {
        try {
            Group group = service.findGroupById(groupID).get();

            if (!authService.isAdminOfGroup(group)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add users to this group");
            }

            Group updatedGroup = service.addUser(groupID, userID);
            return ResponseEntity.status(HttpStatus.OK).body(updatedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Remove User from Group
    @PutMapping("/removeUser")
    public ResponseEntity<?> removeUser(@RequestParam Long groupID, @RequestParam Long userID) {
        try {
            Group group = service.findGroupById(groupID).get();
            if (!authService.isAdminOfGroup(group)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to remove users from this group");
            }

            Group updatedGroup = service.removeUser(groupID, userID);
            return ResponseEntity.status(HttpStatus.OK).body(updatedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/leaveGroup")
    public ResponseEntity<?> leaveGroup(@RequestParam Long groupID) {
        try {
            Long authenticatedUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Group group = service.findGroupById(groupID).get();

            if (authService.isAdminOfGroup(group)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin cannot leave the group. Please assign a new admin before leaving.");
            }

            service.removeUser(groupID, authenticatedUserId);
            return ResponseEntity.status(HttpStatus.OK).body("You have successfully left the group");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/changeAdmin")
    public ResponseEntity<?> changeGroupAdmin(@RequestParam Long groupID, @RequestParam Long userID) {
        try {
            Group group = service.findGroupById(groupID).get();
            Long authenticatedUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!authService.isAdminOfGroup(group)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to change admin of this group");
            }
            // Check if the target user exists in the group
            if (!service.isUserInGroup(groupID, userID)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found for user: ID=" + userID + " in group groupID=" + groupID);
            }
            service.changeAdmin(groupID, userID);
            return ResponseEntity.status(HttpStatus.OK).body("Admin changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    // Get All Groups of User
    @GetMapping("/groupsOfUser")
    public ResponseEntity<?> getAllGroupsOfUser(@RequestParam Long userID) {
        try {
            if (!authService.isSameUser(userID)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this user's groups");
            }

            List<Group> groups = service.findAllGroupsWithUser(userID);

            if (groups.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No groups found for user: ID=" + userID);
            }

            return ResponseEntity.status(HttpStatus.OK).body(groups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}


