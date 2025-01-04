package at.carcar.carcarbackend.Group;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public Optional<Group> findGroupById(long id) {
        return groupRepository.findById(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // Gruppe erstellen
    public Group createGroup(Long userID, String groupName) {
        User user = userRepository.findUserById(userID).orElseThrow(() -> new IllegalStateException(
                "User with ID: " + userID + " does not exist!"));

        Group group;

        if (groupName != null && groupName.length() > 0) {
            group = new Group(groupName, user, List.of(), List.of());
            groupRepository.save(group);
        } else {
            throw new IllegalStateException("Invalid Group Name!");
        }
        return group;
    }

    // Gruppe löschen
    public void deleteGroup(Long groupID) {
        groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        groupRepository.deleteById(groupID);
    }
}
