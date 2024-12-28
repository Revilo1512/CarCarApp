package at.carcar.carcarbackend.User;

import at.carcar.carcarbackend.Group.Group;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;

    private List<Integer> invites;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getInvites() {
        return invites;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
