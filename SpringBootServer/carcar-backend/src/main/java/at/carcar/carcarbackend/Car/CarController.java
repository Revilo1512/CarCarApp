package at.carcar.carcarbackend.Car;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/cars")
public class CarController {

    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = service.getAllCars();
        if (cars.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{carID}")
    public ResponseEntity<Car> getCarById(@PathVariable int carID) {
        Optional<Car> car = service.findCarById(carID);
        if (car.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(car.get());
    }

    // Create Car
    @PostMapping("/createCar")
    public ResponseEntity<?> addCar(@RequestBody Car car) {

        Car newCar;
        try {
            newCar = service.createCar(car);
            if (newCar != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newCar); // Return the created car
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Creation of Car failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Delete Group
    @DeleteMapping("/deleteCar")
    public ResponseEntity<?> deleteCar(@RequestParam Long carID) {
        service.deleteCar(carID);
        return ResponseEntity.status(HttpStatus.OK).body("Car successfully deleted!");
    }
}
