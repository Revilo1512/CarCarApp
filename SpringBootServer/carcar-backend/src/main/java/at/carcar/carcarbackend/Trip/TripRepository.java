package at.carcar.carcarbackend.Trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findById(long id);

    List<Trip> findByCarId(Long carId);
}
