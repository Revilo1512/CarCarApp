package at.carcar.carcarbackend.Group;
import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarRepository;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, CarRepository carRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public Optional<Group> findGroupById(long id) {
        return groupRepository.findById(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // Gruppe erstellen
    public Group createGroup(Long adminID, String groupName) {
        User user = userRepository.findUserById(adminID).orElseThrow(() -> new IllegalStateException(
                "User with ID: " + adminID + " does not exist!"));

        Group group;

        if (groupName != null && !groupName.isEmpty()) {
            group = new Group(groupName, user, List.of(), List.of());
            groupRepository.save(group);
        } else {
            throw new IllegalStateException("Invalid Group Name!");
        }
        return group;
    }

    // Gruppe löschen
    public void deleteGroup(Long groupID, long adminID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        groupRepository.deleteById(groupID);
    }

    public Group addCar(Long groupID, Long carID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        Car car = carRepository.findById(carID).orElseThrow(() -> new IllegalStateException(
                "Car with ID: " + carID + " does not exist! Create the car first!"));

        if (!group.addCar(car)) throw new IllegalStateException("Failed to add Car to Group!");

        groupRepository.save(group);

        return group;
    }

    public Group removeCar(Long groupID, Long carID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        Car car = carRepository.findById(carID).orElseThrow(() -> new IllegalStateException(
                "Car with ID: " + carID + " does not exist!"));

        if (!group.removeCar(car)) throw new IllegalStateException("Failed to remove Car from Group!");

        groupRepository.save(group);

        return group;
    }

    public Group addUser(Long groupID, Long userID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        User user = userRepository.findUserById(userID).orElseThrow(() -> new IllegalStateException(
                "User with ID: " + userID + " does not exist!"));

        if (!group.addUser(user)) throw new IllegalStateException("Failed to add User to Group!");

        groupRepository.save(group);

        return group;
    }

    public Group removeUser(Long groupID, Long userID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        User user = userRepository.findUserById(userID).orElseThrow(() -> new IllegalStateException(
                "User with ID: " + userID + " does not exist!"));

        if (!group.removeUser(user)) throw new IllegalStateException("Failed to remove User from Group!");

        groupRepository.save(group);

        return group;
    }

    public List<Group> findAllGroupsWithUser(Long userID) {
        List<Group> allGroups = groupRepository.findAll();
        List<Group> userInGroups = new ArrayList<>();

        for (Group g : allGroups) {
            if (g.getAdmin().getId() == userID) {
                userInGroups.add(g);
            } else {
                for (User u : g.getUsers()) {
                    if (u.getId() == userID) {
                        userInGroups.add(g);
                        break;
                    }
                }
            }
        }

        return userInGroups;
    }

    public void changeAdmin(Long groupID, Long userID) {
        // Fetch group and user
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() ->
                new IllegalStateException("Group with ID: " + groupID + " does not exist"));

        User user = userRepository.findUserById(userID).orElseThrow(() ->
                new IllegalStateException("User with ID: " + userID + " does not exist"));

        group.setAdmin(user);
        groupRepository.save(group);

    }


    public boolean isUserInGroup(Long groupID, Long userID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() ->
                new IllegalStateException("Group with ID: " + groupID + " does not exist"));
        for (User u : group.getUsers()) {
            if (u.getId() == userID) {
                return true;
            }
        }
        return false;
    }

    public Optional<Group> findGroupByCarId(long carId) {
        Long groupId = groupRepository.findGroupByCarId(carId).orElseThrow(() -> new IllegalStateException("Group not found for carId: " + carId));
        return groupRepository.findGroupById(groupId);
    }

}
