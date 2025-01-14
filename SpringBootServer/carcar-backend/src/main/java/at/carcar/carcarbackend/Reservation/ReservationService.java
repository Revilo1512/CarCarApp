package at.carcar.carcarbackend.Reservation;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarService;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserService;
import at.carcar.carcarbackend.security.AuthorizationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CarService carService;
    private final AuthorizationService authService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, CarService cs, AuthorizationService aserv, UserService us) {
        this.reservationRepository = reservationRepository;
        carService = cs;
        authService = aserv;
        userService = us;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(Long carID, Date startDate, Date endDate) {

        Car car = carService.findCarById(carID).orElseThrow(() -> new IllegalStateException(
                "Car with: " + carID + " does not exist!"
        ));

        if (isCarAvailable(car, startDate, endDate)) throw  new IllegalStateException("Car is not available in that time");


        User user = userService.findUserById(authService.getAuthenticatedUserId()).orElseThrow(() -> new IllegalStateException(
                "User  does not exist!"
        ));
        if (!isUserAvailable(user, startDate, endDate)) throw  new IllegalStateException("User is not available in that time");

        // Reservierung erstellen
        Reservation reservation = new Reservation(startDate,endDate,car,user);
        reservationRepository.save(reservation);
        //car.addReservation(reservation);
        return reservation;
    }

    public boolean isCarAvailable(Car car, Date startDate, Date endDate) {
        List<Reservation> reservations = reservationRepository.getReservationsByCar(car);
        return calcDates(startDate, endDate, reservations);
    }
    public boolean isUserAvailable(User user, Date startDate, Date endDate) {
        List<Reservation> reservations = reservationRepository.getReservationsByUser(user);
        return calcDates(startDate, endDate, reservations);
    }

    private boolean calcDates(Date startDate, Date endDate, List<Reservation> reservations) {
        for (Reservation res : reservations) {
            if ((res.getReservationStart().before(endDate) && res.getReservationEnd().after(startDate)) ||  // Overlaps with the provided interval
                    (res.getReservationStart().before(startDate) && res.getReservationEnd().after(startDate)) ||  // Starts before the provided interval and ends after the start
                    (res.getReservationStart().before(endDate) && res.getReservationEnd().after(endDate)) ||  // Starts before the provided interval and ends after the end
                    (res.getReservationStart().equals(startDate) || res.getReservationEnd().equals(endDate)) ||  // Exact start or end date matches
                    (res.getReservationStart().after(startDate) && res.getReservationEnd().before(endDate))) {  // Completely contained within the provided interval
                return false;
            }
        }
        return true;
    }


    @Transactional
    public Reservation modifyReservation(Long reservationID, Date newStartDate, Date newEndDate) {
        Reservation reservation = reservationRepository.findById(reservationID).orElseThrow(() ->
                new IllegalStateException("Reservation with ID: " + reservationID + " does not exist!"));

        User authenticatedUser = userService.findUserById(authService.getAuthenticatedUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found!"));

        if (!reservation.getUser().equals(authenticatedUser)) {
            throw new IllegalStateException("You are not authorized to modify this reservation!");
        }

        Car car = reservation.getCar();

        // Check if the car is available for the new time
        List<Reservation> carReservations = reservationRepository.getReservationsByCar(car);
        carReservations.remove(reservation); // Exclude the current reservation
        if (!calcDates(newStartDate, newEndDate, carReservations)) {
            throw new IllegalStateException("The car is not available at the specified time.");
        }

        // Check if the user is available for the new time
        List<Reservation> userReservations = reservationRepository.getReservationsByUser(authenticatedUser);
        userReservations.remove(reservation); // Exclude the current reservation
        if (!calcDates(newStartDate, newEndDate, userReservations)) {
            throw new IllegalStateException("You are not available at the specified time.");
        }

        // Update the reservation
        reservation.setReservationStart(newStartDate);
        reservation.setReservationEnd(newEndDate);
        reservationRepository.save(reservation);

        return reservation;
    }

    @Transactional
    public void cancelReservation(Long reservationID) {
        Reservation reservation = reservationRepository.findById(reservationID).orElseThrow(() ->
                new IllegalStateException("Reservation with ID: " + reservationID + " does not exist!"));

        User authenticatedUser = userService.findUserById(authService.getAuthenticatedUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found!"));

        if (!reservation.getUser().equals(authenticatedUser)) {
            throw new IllegalStateException("You are not authorized to cancel this reservation!");
        }

        //reservation.getCar().removeReservation(reservation);
        reservationRepository.delete(reservation);
    }

    public List<Reservation> getReservationsByCar(Car cer) {
        return reservationRepository.getReservationsByCar(cer);
    }
}
