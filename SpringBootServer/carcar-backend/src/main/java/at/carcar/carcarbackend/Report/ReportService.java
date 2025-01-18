package at.carcar.carcarbackend.Report;
import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarService;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupService;
import at.carcar.carcarbackend.Trip.Trip;
import at.carcar.carcarbackend.Trip.TripService;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final CarService carService;
    private final TripService tripService;
    private final UserService userService;
    private final GroupService groupService;

    public ReportService(ReportRepository reportRepository, CarService carService, TripService tripService, UserService userService, GroupService groupService) {
        this.reportRepository = reportRepository;
        this.carService = carService;
        this.tripService = tripService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Transactional
    public MaintenanceReport createMaintenanceReport(long userId, long carId, Long tripId, String description, String maintenanceType, double cost) {
        Map<String, Object> result = checkParameters(carId, userId);
        Car car = (Car) result.get("car");
        User user = (User) result.get("user");
        Date currentDate = (Date) result.get("currentDate");

        Trip trip = validateTrip(tripId, car);

        MaintenanceReport report = new MaintenanceReport(user, currentDate, description, trip, maintenanceType, cost, car);
        return reportRepository.save(report);
    }

    @Transactional
    public DamageReport createDamageReport(long userId, long carId, Long tripId, String description, String damageDetails) {
        Map<String, Object> result = checkParameters(carId, userId);
        Car car = (Car) result.get("car");
        User user = (User) result.get("user");
        Date currentDate = (Date) result.get("currentDate");

        Trip trip = validateTrip(tripId, car);

        DamageReport report = new DamageReport(user, currentDate, description, trip, damageDetails, car);
        return reportRepository.save(report);
    }

    private Map<String, Object> checkParameters(long carId, long userId) {
        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new IllegalStateException("Car not found."));
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));
        Group grp = groupService.findGroupByCarId(carId)
                .orElseThrow(() -> new IllegalStateException("Car not in group."));

        if (!groupService.isUserInGroup(grp.getId(),user.getId())) {
            throw new IllegalStateException("User not in the same group as the car.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("car", car);
        result.put("user", user);
        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        result.put("currentDate", currentDate); // Store as a Date object
        return result;
    }

    private Trip validateTrip(Long tripId, Car car) {
        if (tripId == null) {
            return null;
        }

        Trip trip = tripService.findTripById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found."));
        if (!trip.getCar().equals(car)) {
            throw new IllegalStateException("Trip does not belong to the specified car.");
        }
        return trip;
    }



    @Transactional
    public void deleteReport(long userId, long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalStateException("Report not found."));
        if (report.getAuthor_user().getId() != userId) {
            throw new IllegalStateException("User not authorized to delete this report.");
        }
        reportRepository.delete(report);
    }

    public List<Report> getReportsByCar(long userId, long carId) {
        Car car = carService.findCarById(carId).orElseThrow(() -> new IllegalStateException("Car not found."));
        Group grp = groupService.findGroupByCarId(carId).orElseThrow(() -> new IllegalStateException("Car not in group."));

        if (grp.getAdmin().getId() != userId) {
            throw new IllegalStateException("User is not the author of this report.");
        }

        return reportRepository.findByCar(car);
    }

    public Report getReportById(long reportId, long userId) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));
        Report report = reportRepository.getReportById(reportId);
        if(report == null) {
            throw new IllegalStateException("Report does not exist.");
        }
        Group grp = groupService.findGroupByCarId(report.getCar().getId()).orElseThrow(() -> new IllegalStateException("Car not in group."));
        if (!groupService.isUserInGroup(grp.getId(),user.getId())) {
            throw new IllegalStateException("User not in the same group as the car.");
        }

        return report;
    }
}
