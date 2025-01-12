package at.carcar.carcarbackend.Car;

import at.carcar.carcarbackend.Report.Report;
import at.carcar.carcarbackend.Reservation.Reservation;
import at.carcar.carcarbackend.Trip.Trip;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")

public class Car {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    private String carName;
    private String brand;
    private String model;
    private boolean availabilityStatus;
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Trip> trips = new ArrayList<>();
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Reservation> reservations;


    public Car(String carName, String brand, String model, boolean availabilityStatus) {
        this.carName = carName;
        this.brand = brand;
        this.model = model;
        this.availabilityStatus = availabilityStatus;
    }

    public Car() {

    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public void changeStatus(boolean newStatus) {
        // Implement functionality to change the car's availability status
    }

    public Report createReport(String reportType, String details) {
        // Implement functionality to create a report
        //return new Report();
        return null;
    }

    public void changeReport(int reportID, String details) {
        // Implement functionality to change a report
    }

    public void generateStatistics() {
        // Implement functionality to generate statistics
    }


    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("carID")
    public Long getId() {
        return id;
    }

    public void addTrip(Trip trip) {
        this.trips.add(trip);
    }
    public void addReport(Report rep) {
        this.reports.add(rep);
    }
}
