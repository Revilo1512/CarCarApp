package at.carcar.carcarbackend.Group;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public Group() {
    }

    public Group(String name, User admin, List<User> users, List<Car> cars) {
        this.name = name;
        this.admin = admin;
        this.users = users;
        this.cars = cars;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("groupID")
    public long getId() {
        return id;
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

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
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

