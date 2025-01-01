package at.carcar.carcarbackend.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(path = "/trips")
public class TripController {

    private final TripService service;

    @Autowired
    public TripController(TripService service) {
        this.service = service;
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
    public ResponseEntity<Trip> getTripById(@PathVariable int tripID) {
        Optional<Trip> trip = service.findTripById(tripID);
        if (trip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(trip.get());
    }
}
