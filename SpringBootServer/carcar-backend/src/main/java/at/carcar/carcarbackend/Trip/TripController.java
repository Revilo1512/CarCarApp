package at.carcar.carcarbackend.Trip;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(path = "/trips")
public class TripController {

    private final TripService service;
    private final AuthorizationService authorizationService;

    @Autowired
    public TripController(TripService service, AuthorizationService authorizationService) {
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = service.getAllTrips();
        if (trips.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{tripID}")
    public ResponseEntity<?> getTripById(@PathVariable int tripID) {

        try {
            Trip trip = service.findTripById(tripID)
                    .orElseThrow(() -> new IllegalStateException("Car with ID: " + tripID + " not found!"));

            return ResponseEntity.ok(trip);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/createTrip")
    public ResponseEntity<?> createTrip(
            @RequestParam(required = false) Double distance,
            @RequestParam(required = false) Double fuelUsed,
            @RequestParam String startTimeParam,
            @RequestParam Long carID,
            @RequestParam(required = false) String endTimeParam) {
        try {
            Trip trip = service.createTripWithCustomTime(startTimeParam, carID, authorizationService.getAuthenticatedUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(trip);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/modifyTrip/{tripId}")
    public ResponseEntity<?> modifyTrip(
            @PathVariable long tripId,
            @RequestParam(required = false) Double distance,
            @RequestParam(required = false) Double fuelUsed,
            @RequestParam(required = false) String startTimeParam,
            @RequestParam(required = false) String endTimeParam) {
        try {
            Trip trp = service.findTripById(tripId)
                    .orElseThrow(() -> new IllegalStateException("Trip with ID: " + tripId + " not found!"));
            if(trp.getUser().getId() == authorizationService.getAuthenticatedUserId()){
                Trip trip = service.modifyTrip(tripId, distance,fuelUsed,startTimeParam,endTimeParam);
                return ResponseEntity.ok(trip);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to modify the trip!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /*@DeleteMapping("/{tripID}")
    public ResponseEntity<?> deleteTrip(@PathVariable long tripID) {
        try {
            service.deleteTripById(tripID);
            return ResponseEntity.ok("Trip deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }*/
}
