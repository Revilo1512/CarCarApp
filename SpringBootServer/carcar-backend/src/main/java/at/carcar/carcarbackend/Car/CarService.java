package at.carcar.carcarbackend.Car;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Optional<Car> findCarById(long id) {
        return carRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}
