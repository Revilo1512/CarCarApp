package at.carcar.carcarbackend.security;

import at.carcar.carcarbackend.Group.Group;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    // get UserID from session
    public Long getAuthenticatedUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    //check if authenticated user is same user as userID
    public boolean isSameUser(Long userID) {
        return getAuthenticatedUserId().equals(userID);
    }

    // check if User is admin of group
    public boolean isAdminOfGroup(Group group) {
        return getAuthenticatedUserId().equals(group.getAdmin().getId());
    }
}
