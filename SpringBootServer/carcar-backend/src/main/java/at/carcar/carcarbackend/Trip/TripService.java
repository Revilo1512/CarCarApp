package at.carcar.carcarbackend.Trip;
import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarService;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupService;
import at.carcar.carcarbackend.Reservation.Reservation;
import at.carcar.carcarbackend.Reservation.ReservationService;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserService;
import at.carcar.carcarbackend.security.AuthorizationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class TripService {
    private final TripRepository tripRepository;
    private final UserService userService;
    private final ReservationService reservationService;
    private final AuthorizationService authserv;
    private final CarService carService;
    private final GroupService groupService;

    public TripService(TripRepository tripRepository, UserService userService, ReservationService reservationService, AuthorizationService authserv, CarService carService, GroupService groupService) {
        this.tripRepository = tripRepository;
        this.userService = userService;
        this.reservationService = reservationService;
        this.authserv = authserv;
        this.carService = carService;
        this.groupService = groupService;
    }

    public Optional<Trip> findTripById(long id) {
        return tripRepository.findById(id);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public void deleteTripById(long id) {
        if (!tripRepository.existsById(id)) {
            throw new IllegalStateException("Trip with ID " + id + " does not exist");
        }

        tripRepository.deleteById(id);
    }

    private Optional<Date> StringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            return Optional.ofNullable(formatter.parse(dateString));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Trip createTripWithCustomTime(String startTimeParam, Long carID, Long userID) {
        User user = userService.findUserById(userID).orElseThrow(() ->
                new IllegalStateException("User with ID " + userID + " does not exist"));

        Car car = carService.findCarById(carID).orElseThrow(() ->
                new IllegalStateException("Car with ID " + carID + " does not exist"));

        Group grp = groupService.findGroupByCarId(carID).orElseThrow(() ->
                new IllegalStateException("Group with Car" + carID + " does not exist"));

        if(!groupService.isUserInGroup(carID, userID)) throw new IllegalStateException("Not authorized to add trip");

        Trip trip = new Trip(StringToDate(startTimeParam).get(),car,user);

        tripRepository.save(trip);
        return trip;
    }

    @Transactional
    public Trip modifyTrip(long tripID, Double distance, Double fuelUsed, String startTimeParam, String endTimeParam) {
        Trip trip = tripRepository.findById(tripID).orElseThrow(() ->
                new IllegalStateException("Trip with ID " + tripID + " does not exist"));

        // Update distance if provided
        if (distance != null) {
            trip.setDistance(distance);
        }

        // Update fuelUsed if provided
        if (fuelUsed != null) {
            trip.setFuelUsed(fuelUsed);
        }

        // Update startTime if provided
        if (startTimeParam != null) {
            Date newStartTime = StringToDate(startTimeParam).orElseThrow(() ->
                    new IllegalStateException("Invalid start time format"));
            trip.setStartTime(newStartTime);
        }

        if (endTimeParam != null) {
            Date newEndTime = StringToDate(endTimeParam).orElseThrow(() ->
                    new IllegalStateException("Invalid end time format"));
            trip.setEndTime(newEndTime);
        }

        // Ensure startTime is before endTime if both are updated
        if (trip.getStartTime() != null && trip.getEndTime() != null && !trip.getEndTime().after(trip.getStartTime())) {
            throw new IllegalStateException("End time must be after start time");
        }

        return tripRepository.save(trip);
    }

    public String getCarStatistics(Long carId) {
        List<Trip> trips = tripRepository.findByCarId(carId);

        if (trips.isEmpty()) {
            throw new IllegalStateException("No trips found for car with ID: " + carId);
        }

        double totalDistance = trips.stream().mapToDouble(Trip::getDistance).sum();
        double totalFuelUsed = trips.stream().mapToDouble(Trip::getFuelUsed).sum();
        int totalTrips = trips.size();

        return "totalDistance: " + totalDistance+ " " + " totalFuelUsed: " + totalFuelUsed+ " " + " totalTrips: " + totalTrips + " ";
    }
}
