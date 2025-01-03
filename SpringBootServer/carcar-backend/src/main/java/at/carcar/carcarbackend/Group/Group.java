package at.carcar.carcarbackend.Group;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "groups")

public class Group {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    @ManyToMany(mappedBy = "groups")
    private List<User> users;
    @OneToMany
    private List<Car> cars;
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_user_id")
    private User admin;

    //private List<User> pendingRequests;

    public Group(int groupID, String name, User admin) {
        this.id = groupID;
        this.name = name;
        this.admin = admin;
    }

    public Group() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public void deleteGroup() {
        // Implement group deletion functionality
    }

    public void inviteUser(int userID) {
        // Implement functionality to invite a user to the group
    }

    public void manageRequest(User user) {
        // Implement functionality to manage user requests
    }

    public void removeUser(User user) {
        // Implement functionality to remove a user from the group
    }

    public void addCar() {
        // Implement functionality to add a car to the group
    }

    public void removeCar() {
        // Implement functionality to remove a car from the group
    }


}

