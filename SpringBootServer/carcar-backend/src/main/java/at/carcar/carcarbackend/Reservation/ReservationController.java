package at.carcar.carcarbackend.Reservation;

import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupService;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {

    private final ReservationService service;
    private final AuthorizationService authService;
    private final GroupService groupService;




    @Autowired
    public ReservationController(ReservationService service, AuthorizationService authService, GroupService groupService) {
        this.service = service;
        this.authService = authService;
        this.groupService = groupService;
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
    @NonNull
    public ResponseEntity<?> createReservation(@RequestParam Long carID, @RequestParam String startTime, @RequestParam String endTime) {
        try{
            Group grp = groupService.findGroupByCarId(carID).get();
            System.out.println("Group: " + grp.getName());
            if(groupService.isUserInGroup(grp.getId(),authService.getAuthenticatedUserId())){
                Reservation res = service.createReservation(carID, startTime, endTime);
                System.out.println("Groups: " + grp.getName());
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not authorized to make this reservation");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Cancel Reservation
    @DeleteMapping("/cancelReservation")
    @NonNull
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
    @NonNull
    public ResponseEntity<?> modifyReservation(@RequestParam Long reservationID, @RequestParam String startTime, @RequestParam String endTime) {
        try {
            Reservation updatedReservation = service.modifyReservation(reservationID, startTime, endTime);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
