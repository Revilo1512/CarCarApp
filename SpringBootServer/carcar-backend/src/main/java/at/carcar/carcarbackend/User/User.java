package at.carcar.carcarbackend.User;

import at.carcar.carcarbackend.Group.Group;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToMany
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        // Implement login functionality
    }

    public Group createGroup(String name) {
        // Implement group creation functionality
        //return new Group();
        return null;
    }

    public void leaveGroup(int groupID) {
        // Implement functionality to leave a group
    }

    public void manageInvitation(int groupID) {
        // Implement functionality to manage group invitations
    }

    public void sendJoinRequest(int groupID) {
        // Implement functionality to send a join request to a group
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", groups=" + groups +
                '}';
    }
}
