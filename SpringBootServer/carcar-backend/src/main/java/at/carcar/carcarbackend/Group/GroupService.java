package at.carcar.carcarbackend.Group;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Optional<Group> findGroupById(long id) {
        return groupRepository.findById(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}
