package at.carcar.carcarbackend.Car;
import at.carcar.carcarbackend.User.User;
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

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long carID) {
        carRepository.findById(carID).orElseThrow(() -> new IllegalStateException(
                "Car with ID: " + carID + " does not exist!"));
        carRepository.deleteById(carID);
    }
}
