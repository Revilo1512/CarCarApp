package at.carcar.carcarbackend.Car;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupService;
import at.carcar.carcarbackend.Reservation.Reservation;
import at.carcar.carcarbackend.User.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class CarService {
    private final CarRepository carRepository;
    private final GroupService groupService;

    public CarService(CarRepository carRepository, GroupService groupService) {
        this.carRepository = carRepository;
        this.groupService = groupService;
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

    public boolean isUserInSameGroupAsCar(Long userId, Long carId) {
        // Find the group that contains the car
        Group group = groupService.findGroupByCarId(carId)
                .orElseThrow(() -> new IllegalStateException("Group containing car with ID: " + carId + " not found!"));

        // Check if the user is in the group
        return groupService.isUserInGroup(group.getId(), userId);
    }

}
