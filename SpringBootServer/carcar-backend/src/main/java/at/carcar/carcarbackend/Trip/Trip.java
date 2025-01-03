package at.carcar.carcarbackend.Trip;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;
import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    private Date startTime;
    private Date endTime;
    private double distance;
    private double fuelUsed;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Trip() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Trip(int tripID, Date startTime, Car car, User user) {
        this.id = tripID;
        this.startTime = startTime;
        this.car = car;
        this.user = user;
    }
    public Trip(Date startTime, Date endTime, double distance, double fuelUsed, Car car, User user) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.fuelUsed = fuelUsed;
        this.car = car;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public User getUser() {
        return user;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFuelUsed() {
        return fuelUsed;
    }

    public void setFuelUsed(double fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public Trip initiateTrip(User user) {
        // Implement functionality to initiate a trip
        //return new Trip();
        return null;
    }
}
