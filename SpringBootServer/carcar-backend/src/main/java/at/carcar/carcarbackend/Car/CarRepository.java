package at.carcar.carcarbackend.Car;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Override
    Optional<Car> findById(Long id);
}
