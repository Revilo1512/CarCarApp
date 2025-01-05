package at.carcar.carcarbackend.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {

    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        List<Reservation> reservations = service.getAllReservations();

        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Reservations found!");
        }
        return ResponseEntity.ok(reservations);
    }
}
