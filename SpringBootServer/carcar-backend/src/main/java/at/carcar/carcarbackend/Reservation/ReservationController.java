package at.carcar.carcarbackend.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @PostMapping("/createReservation")
    public ResponseEntity<?> createReservation(@RequestParam Long carID, @RequestParam Date startTime, @RequestParam Date endTime) {
        Reservation res = service.createReservation(carID, startTime, endTime);
        return ResponseEntity.ok(res);
    }

    // Cancel Reservation
    @DeleteMapping("/cancelReservation")
    public ResponseEntity<?> cancelReservation(@RequestParam Long reservationID) {
        try {
            service.cancelReservation(reservationID);
            return ResponseEntity.ok("Reservation successfully cancelled.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Modify Reservation
    @PutMapping("/modifyReservation")
    public ResponseEntity<?> modifyReservation(
            @RequestParam Long reservationID,
            @RequestParam Date newStartTime,
            @RequestParam Date newEndTime) {
        try {
            Reservation updatedReservation = service.modifyReservation(reservationID, newStartTime, newEndTime);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
