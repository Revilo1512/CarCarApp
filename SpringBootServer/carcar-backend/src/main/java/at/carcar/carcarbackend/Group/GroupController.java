package at.carcar.carcarbackend.Group;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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
    @NonNull
    public ResponseEntity<?> getGroupById(@PathVariable int groupID) {
        try{
            System.out.println("adas");
            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));
            System.out.println("adas");
            if(service.isUserInGroup(group.getId(),authService.getAuthenticatedUserId())){
                return ResponseEntity.ok(group);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this group");

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Create Group
    @PostMapping("/createGroup")
    @NonNull
    public ResponseEntity<?> addGroup(@RequestParam String groupName) {
        Group newGroup;
        System.out.println("adas");
        try {
            System.out.println("adas");

            newGroup = service.createGroup(authService.getAuthenticatedUserId(), groupName);
            System.out.println("adas");

            if (newGroup != null) {
                System.out.println("adasasdsadsadasd");

                return ResponseEntity.status(HttpStatus.CREATED).body(newGroup); // Return the created Group
            } else {
                System.out.println("ahghozifrtzop");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group Creation failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteGroup")
    @NonNull
    public ResponseEntity<?> deleteGroup(@RequestParam Long groupID) {
        try {

            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));

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
    @NonNull
    public ResponseEntity<?> addCar(@RequestParam Long groupID, @RequestParam Long carID) {
        try {
            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));

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
    @NonNull
    @PutMapping("/removeCar")
    public ResponseEntity<?> removeCar(@RequestParam Long groupID, @RequestParam Long carID) {
        try {
            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));

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
    @NonNull
    public ResponseEntity<?> addUser(@RequestParam Long groupID, @RequestParam Long userID) {
        try {
            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));

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
    @NonNull
    public ResponseEntity<?> removeUser(@RequestParam Long groupID, @RequestParam Long userID) {
        try {
            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));
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
    @NonNull
    public ResponseEntity<?> leaveGroup(@RequestParam Long groupID) {
        try {
            Long authenticatedUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));

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
    @NonNull
    public ResponseEntity<?> changeGroupAdmin(@RequestParam Long groupID, @RequestParam Long userID) {
        try {
            Group group = service.findGroupById(groupID).orElseThrow(() -> new IllegalStateException("Group not found."));
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
    @NonNull
    public ResponseEntity<?> getAllGroupsOfUser() {
        try {
            long userID = authService.getAuthenticatedUserId();

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


