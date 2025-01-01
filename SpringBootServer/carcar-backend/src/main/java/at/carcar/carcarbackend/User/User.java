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

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public User() {

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

    public List<Integer> getInvites() {
        return new ArrayList<>();
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

}
