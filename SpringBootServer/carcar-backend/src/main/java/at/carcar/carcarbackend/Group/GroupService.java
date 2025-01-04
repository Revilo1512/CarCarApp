package at.carcar.carcarbackend.Group;
import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarRepository;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserRepository;
import org.springframework.stereotype.Service;

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

    public Group addCar(Long groupID, Long carID) {
        Group group = groupRepository.findGroupById(groupID).orElseThrow(() -> new IllegalStateException(
                "Group with ID: " + groupID + " does not exist!"));

        Car car = carRepository.findById(carID).orElseThrow(() -> new IllegalStateException(
                "Car with ID: " + carID + " does not exist!"));

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

        Group newGroup = new Group(groupID, group.getName(), group.getAdmin(), group.getUsers(), group.getCars());

        groupRepository.save(group);
//        System.out.println(group);
//        groupRepository.deleteById(groupID);
//        System.out.println("Eins");
//        System.out.println(newGroup);
//        groupRepository.save(newGroup);

        return group;
    }
}
