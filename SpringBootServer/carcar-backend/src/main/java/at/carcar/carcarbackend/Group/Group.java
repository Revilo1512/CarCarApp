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
//    @ManyToMany(mappedBy = "groups")
//    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "groups_users",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @OneToMany
    private List<Car> cars;
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_user_id")
    private User admin;

    //private List<User> pendingRequests;

    public Group(long groupID, String name, User admin, List<User> users, List<Car> cars) {
        this.id = groupID;
        this.name = name;
        this.admin = admin;
        this.users = users;
        this.cars = cars;
    }

    public Group(String name, User admin, List<User> users, List<Car> cars) {
        this.name = name;
        this.admin = admin;
        this.users = users;
        this.cars = cars;
    }

    public Group() {

    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Car> getCars() {
        return cars;
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

    public boolean addCar(Car car) {
        return cars.add(car);
    }

    public boolean removeCar(Car car) {
        return cars.remove(car);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", users=" + users +
                ", cars=" + cars +
                ", name='" + name + '\'' +
                ", admin=" + admin +
                '}';
    }
}

