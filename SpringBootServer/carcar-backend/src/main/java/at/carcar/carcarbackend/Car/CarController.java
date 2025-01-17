package at.carcar.carcarbackend.Car;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupService;
import at.carcar.carcarbackend.Reservation.ReservationService;
import at.carcar.carcarbackend.Trip.TripService;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/cars")
public class CarController {

    private final CarService service;
    private final GroupService groupService;
    private final AuthorizationService authService;
    private final ReservationService resService;
    private final TripService tripService;

    @Autowired
    public CarController(CarService service, GroupService gs, AuthorizationService authService, ReservationService resService,  TripService tripService) {
        this.service = service;
        this.groupService = gs;
        this.authService = authService;
        this.resService = resService;
        this.tripService = tripService;
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
    @NonNull
    public ResponseEntity<?> getCarById(@PathVariable Long carID) {
        try {
            Car car = service.findCarById(carID)
                    .orElseThrow(() -> new IllegalStateException("Car with ID: " + carID + " not found!"));

            ResponseEntity<String> response = getStringResponseEntity(carID);
            if (response != null) return response;


            return ResponseEntity.ok(car);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Create Car
    @PostMapping("/createCar")
    @NonNull
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
    @NonNull
    public ResponseEntity<?> getCarReservationsById(@RequestParam Long carId) {
        try{
            Optional<Car> car = service.findCarById(carId);
            if (car.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CarID invalid");
            }
            ResponseEntity<String> response = getStringResponseEntity(carId);
            if (response != null) return response;
            Car cer = car.get();

            return ResponseEntity.ok(resService.getReservationsByCar(cer));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getStatistics")
    @NonNull
    public ResponseEntity<?> getCarStatistics(@RequestParam Long carId) {
        try{
            Optional<Car> car = service.findCarById(carId);
            if (car.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CarID invalid");
            }
            String s = tripService.getCarStatistics(carId);

            return ResponseEntity.ok(s);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private ResponseEntity<String> getStringResponseEntity(Long carId) {
        Optional<Group> groupOptional = groupService.findGroupByCarId(carId);
        if (groupOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group containing car with ID: " + carId + " not found!");
        }

        Group group = groupOptional.get();
        if (!groupService.isUserInGroup(group.getId(), authService.getAuthenticatedUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this car.");
        }
        return null;
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
