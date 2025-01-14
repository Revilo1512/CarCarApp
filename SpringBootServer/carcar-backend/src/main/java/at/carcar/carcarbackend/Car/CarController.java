package at.carcar.carcarbackend.Car;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupService;
import at.carcar.carcarbackend.Reservation.ReservationService;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "/cars")
public class CarController {

    private final CarService service;
    private final GroupService groupService;
    private final AuthorizationService authService;
    private final ReservationService resService;
    private final CarService carService;

    @Autowired
    public CarController(CarService service, GroupService gs, AuthorizationService authService, ReservationService resService, CarService carService) {
        this.service = service;
        this.groupService = gs;
        this.authService = authService;
        this.resService = resService;
        this.carService = carService;
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
    public ResponseEntity<?> getCarById(@PathVariable Long carID) {
        try {
            Car car = service.findCarById(carID)
                    .orElseThrow(() -> new IllegalStateException("Car with ID: " + carID + " not found!"));

            if (!service.isUserInSameGroupAsCar(authService.getAuthenticatedUserId(), carID)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this car.");
            }

            return ResponseEntity.ok(car);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Create Car
    @PostMapping("/createCar")
    public ResponseEntity<?> createCar(@RequestBody Car car) {

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

    @GetMapping("/getReservations")
    public ResponseEntity<?> getCarReservationsById(@RequestParam Long carId) {
        try{
            Optional<Car> car = service.findCarById(carId);
            if (car.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Optional<Group> groupOptional = groupService.findGroupByCarId(carId);
            if (groupOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group found for this car");
            }

            Group group = groupOptional.get();
            Car cer = car.get();
            if (!carService.isUserInSameGroupAsCar(authService.getAuthenticatedUserId(), carId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this car's reservations");
            }


            return ResponseEntity.ok(resService.getReservationsByCar(cer));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Delete car
    /*
    @DeleteMapping("/deleteCar")
    public ResponseEntity<?> deleteCar(@RequestParam Long carID) {
        service.deleteCar(carID);
        return ResponseEntity.status(HttpStatus.OK).body("Car successfully deleted!");
    }
    */
}
