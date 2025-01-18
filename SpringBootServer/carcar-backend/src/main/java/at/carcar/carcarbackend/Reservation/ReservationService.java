package at.carcar.carcarbackend.Reservation;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarService;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserService;
import at.carcar.carcarbackend.security.AuthorizationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public Reservation createReservation(Long carID, String startDateParam, String endDateParam) {

        Car car = carService.findCarById(carID).orElseThrow(() -> new IllegalStateException(
                "Car with: " + carID + " does not exist!"
        ));

        Date startDate = StringToDate(startDateParam);
        Date endDate = StringToDate(endDateParam);

        if (!isCarAvailable(car, startDate, endDate)) throw  new IllegalStateException("Car is not available in that time");


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

    private Date StringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse date: " + dateString);
        }
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
    public Reservation modifyReservation(Long reservationID, String startDateParam, String endDateParam) {
        Reservation reservation = reservationRepository.findById(reservationID).orElseThrow(() ->
                new IllegalStateException("Reservation with ID: " + reservationID + " does not exist!"));

        User authenticatedUser = userService.findUserById(authService.getAuthenticatedUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found!"));

        if (!reservation.getUser().equals(authenticatedUser)) {
            throw new IllegalStateException("You are not authorized to modify this reservation!");
        }

        Date startDate = StringToDate(startDateParam);
        Date endDate = StringToDate(endDateParam);

        Car car = reservation.getCar();

        // Check if the car is available for the new time
        List<Reservation> carReservations = reservationRepository.getReservationsByCar(car);
        carReservations.remove(reservation); // Exclude the current reservation
        if (!calcDates(startDate, endDate, carReservations)) {
            throw new IllegalStateException("The car is not available at the specified time.");
        }

        // Check if the user is available for the new time
        List<Reservation> userReservations = reservationRepository.getReservationsByUser(authenticatedUser);
        userReservations.remove(reservation); // Exclude the current reservation
        if (!calcDates(startDate, endDate, userReservations)) {
            throw new IllegalStateException("You are not available at the specified time.");
        }

        // Update the reservation
        reservation.setReservationStart(startDate);
        reservation.setReservationEnd(endDate);
        reservationRepository.save(reservation);

        return reservation;
    }

    @Transactional
    public void cancelReservation(Long reservationID) {
        Reservation reservation = reservationRepository.findById(reservationID).orElseThrow(() ->
                new IllegalStateException("Reservation with ID: " + reservationID + " does not exist!"));

        User authenticatedUser = userService.findUserById(authService.getAuthenticatedUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found!"));

        if (reservation.getUser().getId() != (authenticatedUser).getId()) {
            throw new IllegalStateException("You are not authorized to cancel this reservation!");
        }

        //reservation.getCar().removeReservation(reservation);
        reservationRepository.delete(reservation);
    }

    public List<Reservation> getReservationsByCar(Car cer) {
        return reservationRepository.getReservationsByCar(cer);
    }

    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public List<Reservation> getReservationByUser(Long authenticatedUserId) {
        User usr = userService.findUserById(authenticatedUserId).orElseThrow(() -> new IllegalStateException("User not found!"));
        return reservationRepository.getReservationsByUser(usr);
    }
}
