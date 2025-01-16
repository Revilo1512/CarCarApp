package at.carcar.carcarbackend.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findGroupById(Long id);
    //Optional<Group> findGroupByCarId(@Param("carId")Long carId);
    @Query(value = "SELECT group_id FROM groups_cars  WHERE cars_id = :carId", nativeQuery = true)
    Optional<Long> findGroupByCarId(@Param("carId") Long carId);


}
